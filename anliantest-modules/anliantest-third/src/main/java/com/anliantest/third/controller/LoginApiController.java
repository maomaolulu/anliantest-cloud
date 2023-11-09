package com.anliantest.third.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.shaded.org.checkerframework.checker.units.qual.C;
import com.anliantest.common.core.domain.R;
import com.anliantest.common.core.web.controller.BaseController;
import com.anliantest.common.log.annotation.Log;
import com.anliantest.common.log.enums.BusinessType;
import com.anliantest.common.security.utils.SecurityUtils;
import com.anliantest.system.api.RemoteConfigService;
import com.anliantest.system.api.domain.SysConfig;
import com.anliantest.third.constant.Constants;
import com.anliantest.third.constant.OaConstants;
import com.anliantest.third.service.OaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 内部系统登录获取tokenApi
 * @Author zh
 * @Date 2023/11/03 14:07
 * @Version 1.0
 * @Description 内部系统登录获取tokenApi
 */
@Slf4j
@RestController
@RequestMapping("/loginApi")
public class LoginApiController extends BaseController {
    private final RemoteConfigService remoteConfigService;
    public LoginApiController(RemoteConfigService remoteConfigService) {
        this.remoteConfigService = remoteConfigService;
    }


    /**
     * 获取运营OAToken
     * @return
     */
    @Log(title = "获取运营OAToken", businessType = BusinessType.OTHER)
    @PostMapping(value = "/jumpOa")
    public R jumpOa() {
        String oaId = SecurityUtils.getLoginUser().getSysUser().getOaUserId();
        if (oaId == null) {
            return R.fail("还没绑定云管家平台账号，请联系管理员绑定。");
        }
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId", oaId);
        JSONObject josmmap = JSONUtil.parseObj(paramMap);
        String msg = null;
        try {
            SysConfig configUrl = remoteConfigService.findConfigUrl();
            String urlPre;
            if ("test".equals(configUrl.getConfigValue())) {
                urlPre = Constants.LOGIN_OA_TEST;
            } else {
                urlPre = Constants.LOGIN_OA_ONLINE;
            }
             msg = HttpUtil.post(urlPre, josmmap.toString());
        } catch (Exception e) {
            log.error("跳转oa系统获取token" + msg);
            return R.fail("未知异常，请联系管理员");
        }
        R r = JSON.parseObject(msg, R.class);

        return r;
    }

    /**
     * 获取运营1.0Token
     * @return
     */
    @Log(title = "获取运营1.0Token", businessType = BusinessType.OTHER)
    @PostMapping(value = "/jumpOperate")
    public R jumpOperate() {
        String email = SecurityUtils.getLoginUser().getSysUser().getEmail();
        if (StrUtil.isBlank(email)) {
            return R.fail("无邮箱，请联系管理员。");
        }
        HashMap<String, Object> paramMap = new HashMap<>();
            paramMap.put("email", email);
            paramMap.put("secretKey", Constants.SECRETKEY);
            paramMap.put("timeStamp", System.currentTimeMillis());
            paramMap.put("isWant", false);
        JSONObject josmmap = JSONUtil.parseObj(paramMap);
        String msg = null;
        try {
            SysConfig configUrl = remoteConfigService.findConfigUrl();
            String urlPre;
            if ("test".equals(configUrl.getConfigValue())) {
                urlPre = Constants.LOGIN_OPERATE_TEST;
            } else {
                urlPre = Constants.LOGIN_OPERATE_ONLINE;
            }
             msg = HttpUtil.post(urlPre, josmmap.toString());
        } catch (Exception e) {
            log.error("跳转运营1.0系统获取token" + msg);
            return R.fail("未知异常，请联系管理员");
        }
        R r = JSON.parseObject(msg, R.class);

        return r;
    }



}
