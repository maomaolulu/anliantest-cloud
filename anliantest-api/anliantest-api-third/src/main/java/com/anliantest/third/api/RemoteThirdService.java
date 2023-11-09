package com.anliantest.third.api;

import com.anliantest.common.core.constant.ServiceNameConstants;
import com.anliantest.third.api.Factory.RemoteThirdFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

/**
 * @Author yrb
 * @Date 2023/7/27 15:19
 * @Version 1.0
 * @Description 第三方系统调用
 */
@FeignClient(contextId = "remoteThirdService", value = ServiceNameConstants.THIRD_SERVICE, fallbackFactory = RemoteThirdFallbackFactory.class)
public interface RemoteThirdService {
    /**
     * 获取OA用户id
     * @param param 邮箱
     * @return 用户ID
     */
    @PostMapping("/oa/findUserInfo")
    Map<String, String> findUserInfo(Map<String, String> param);
}
