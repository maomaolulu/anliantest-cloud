package com.anliantest.third.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.anliantest.system.api.RemoteConfigService;
import com.anliantest.system.api.domain.SysConfig;
import com.anliantest.third.constant.OaConstants;
import com.anliantest.third.service.OaService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author yrb
 * @Date 2023/7/26 14:11
 * @Version 1.0
 * @Description OA系统服务层接口实现
 */
@Service
public class OaServiceImpl implements OaService {

    @Autowired
    private RemoteConfigService remoteConfigService;

    /**
     * 获取OA用户信息
     *
     * @param param 邮箱
     * @return result
     */
    @Override
    public Map<String, String> getUserInfo(Map<String, String> param) {
        // 判断是测试环境还是正式环境
        Map<String, String> result = Maps.newHashMap();
        SysConfig configUrl = remoteConfigService.findConfigUrl();
        String urlPre;
        if ("test".equals(configUrl.getConfigValue())) {
            urlPre = OaConstants.URL_PRE_TEST;
        } else {
            urlPre = OaConstants.URL_PRE_ONLINE;
        }
        // 获取token
        String tokenUrl = urlPre + "/auth/token";
        param.put("password", OaConstants.SECTET);
        String response = HttpUtil.createPost(tokenUrl).body(JSONObject.toJSONString(param)).execute().body();
        Map<String, String> userInfo = JSONObject.parseObject(response, Map.class);
        // 获取userId
        if (userInfo.get("userId") != null) {
            result.put("userId", userInfo.get("userId"));
        }
        return result;
    }
}
