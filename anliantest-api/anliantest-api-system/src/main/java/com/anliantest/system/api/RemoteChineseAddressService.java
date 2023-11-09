package com.anliantest.system.api;

import com.anliantest.common.core.constant.SecurityConstants;
import com.anliantest.common.core.constant.ServiceNameConstants;
import com.anliantest.common.core.domain.R;
import com.anliantest.system.api.factory.RemoteChineseAddressFallbackFactory;
import com.anliantest.system.api.model.AmapRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @Description
 * @Date 2023/8/25 15:23
 * @Author maoly
 **/
@FeignClient(contextId = "remoteChineseAddressService", name = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteChineseAddressFallbackFactory.class)
public interface RemoteChineseAddressService {

    @GetMapping("/address/saveChineseAddress/{param}")
    public R saveChineseAddress(@PathVariable("param") String param, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
