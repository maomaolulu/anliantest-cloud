package com.anliantest.data.api;

import com.anliantest.common.core.constant.SecurityConstants;
import com.anliantest.common.core.constant.ServiceNameConstants;
import com.anliantest.data.api.factory.RemoteSubstanceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @author gy
 * @Description 物质库相关服务
 * @date 2023-07-03 11:15
 */
@FeignClient(contextId = "remoteSubstanceService", value = ServiceNameConstants.DATA_SERVICE, fallbackFactory = RemoteSubstanceFallbackFactory.class)
public interface RemoteSubstanceService {
    /**
     * 刷新状态
     */
    @PostMapping(value = "/substancetestlaw/refreshStatus")
    public void refreshStatus(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
