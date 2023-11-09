package com.anliantest.common.security.service;

import com.anliantest.common.core.constant.CacheConstants;
import com.anliantest.common.core.constant.SecurityConstants;
import com.anliantest.common.core.utils.JwtUtils;
import com.anliantest.common.core.utils.ServletUtils;
import com.anliantest.common.core.utils.StringUtils;
import com.anliantest.common.core.utils.ip.IpUtils;
import com.anliantest.common.core.utils.uuid.IdUtils;
import com.anliantest.common.redis.service.RedisService;
import com.anliantest.common.security.utils.SecurityUtils;
import com.anliantest.system.api.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * token验证处理
 *
 * @author ruoyi
 */
@Component
public class TokenService
{
    @Autowired
    private RedisService redisService;

    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private final static long expireTime = CacheConstants.EXPIRATION;

    private final static String ACCESS_TOKEN = CacheConstants.LOGIN_TOKEN_KEY;
    private final static String LOGIN_USER_ID_KEY = CacheConstants.LOGIN_USER_ID_KEY;

    private final static Long MILLIS_MINUTE_TEN = CacheConstants.REFRESH_TIME * MILLIS_MINUTE;

    /**
     * 创建令牌
     */
    public Map<String, Object> createToken(LoginUser loginUser)
    {
        String type = loginUser.getType();

        String token = IdUtils.fastUUID();
        Long userId = loginUser.getSysUser().getUserId();
        String userName = loginUser.getSysUser().getUserName();
        String nickName = loginUser.getSysUser().getNickName();
        //生成token pc wx app
        loginUser.setToken(token);
        loginUser.setUserid(userId);
        loginUser.setUsername(userName);
        loginUser.setIpaddr(IpUtils.getIpAddr());

        //登录redis存储token
        saveToken(loginUser);


        // Jwt存储信息
        Map<String, Object> claimsMap = new HashMap<String, Object>();
        claimsMap.put(SecurityConstants.USER_KEY, token);
        claimsMap.put(SecurityConstants.DETAILS_USER_ID, userId);
        claimsMap.put(SecurityConstants.DETAILS_USERNAME, userName);
        claimsMap.put(SecurityConstants.CN_USERNAME, nickName);

        // 接口返回信息
        Map<String, Object> rspMap = new HashMap<String, Object>();
        rspMap.put("access_token", JwtUtils.createToken(claimsMap));
        rspMap.put("expires_in", expireTime);
        return rspMap;
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser()
    {
        return getLoginUser(ServletUtils.getRequest());
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser(HttpServletRequest request)
    {
        // 获取请求携带的令牌
        String token = SecurityUtils.getToken(request);
        return getLoginUser(token);
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser(String token)
    {
        LoginUser user = null;
        try
        {
            if (StringUtils.isNotEmpty(token))
            {
                String userkey = JwtUtils.getUserKey(token);
                user = redisService.getCacheObject(getTokenKey(userkey));
                return user;
            }
        }
        catch (Exception e)
        {
        }
        return user;
    }

    /**
     * 设置用户身份信息
     */
    public void setLoginUser(LoginUser loginUser)
    {
        if (StringUtils.isNotNull(loginUser) && StringUtils.isNotEmpty(loginUser.getToken()))
        {
            refreshToken(loginUser);
        }
    }

    /**
     * 删除用户缓存信息
     */
    public void delLoginUser(String token)
    {
        if (StringUtils.isNotEmpty(token))
        {
            String userkey = JwtUtils.getUserKey(token);
            redisService.deleteObject(getTokenKey(userkey));
        }
    }

    /**
     * 验证令牌有效期，相差不足120分钟，自动刷新缓存
     *
     * @param loginUser
     */
    public void verifyToken(LoginUser loginUser)
    {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= MILLIS_MINUTE_TEN)
        {
            refreshToken(loginUser);
        }
    }

    /**
     * 刷新令牌有效期
     *
     * @param loginUser 登录信息
     */
    public void refreshToken(LoginUser loginUser)
    {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);

        String token = loginUser.getToken();
        // 根据uuid将loginUser缓存
        String userKey = getTokenKey(token);
        String userIdKey = getUserIdKey(loginUser.getType(), loginUser.getUserid());
        redisService.setCacheObject(userIdKey, userKey, expireTime, TimeUnit.MINUTES);
        redisService.setCacheObject(userKey, loginUser, expireTime, TimeUnit.MINUTES);
    }


    /**
     * 登录redis存储token
     *
     * @param loginUser 登录信息
     */
    public void saveToken(LoginUser loginUser)
    {
        String type = loginUser.getType();
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);

        // 根据uuid将loginUser缓存
        String userKey = getTokenKey(loginUser.getToken());
        String userIdKey = getUserIdKey(type, loginUser.getUserid());
        //根据类型（pc wx app）和用户id 查询之前token  如果存在 踢出之前token\
        expireToken(loginUser);

        //token
        redisService.setCacheObject(userIdKey, userKey, expireTime, TimeUnit.MINUTES);
        redisService.setCacheObject(userKey, loginUser, expireTime, TimeUnit.MINUTES);
    }
    public void expireToken(LoginUser loginUser)
    {
        String type = loginUser.getType();
        String userIdKey = getUserIdKey(type, loginUser.getUserid());
        String token = (String)redisService.getCacheObject(userIdKey);
        if (StringUtils.isNotBlank(token))
        {
            redisService.deleteObject(type + loginUser.getUserid());
            redisService.deleteObject(token);
        }
    }
    private String getTokenKey(String token)
    {
        return ACCESS_TOKEN + token;
    }
    private String getUserIdKey(String type,Long userId)
    {
        return LOGIN_USER_ID_KEY +type+ userId;
    }
}
