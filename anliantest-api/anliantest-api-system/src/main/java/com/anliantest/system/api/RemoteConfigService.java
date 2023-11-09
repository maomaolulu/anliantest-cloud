package com.anliantest.system.api;

import com.anliantest.common.core.constant.ServiceNameConstants;
import com.anliantest.system.api.domain.SysConfig;
import com.anliantest.system.api.factory.RemoteConfigFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 用户 Feign服务层
 *
 * @author zmr
 * @date 2019-05-20
 */
@FeignClient(contextId = "remoteConfigService", name = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteConfigFallbackFactory.class)
public interface RemoteConfigService {

    @GetMapping("/config/url")
    SysConfig findConfigUrl();
}
