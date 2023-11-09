package com.anliantest.project.api;

import com.anliantest.common.core.constant.SecurityConstants;
import com.anliantest.common.core.constant.ServiceNameConstants;
import com.anliantest.project.api.factory.CleanCompanyFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @Description
 * @Date 2023/10/31 15:38
 * @Author gy
 **/
@FeignClient(contextId = "CleanCompanyService", value = ServiceNameConstants.PROJECT_SERVICE, fallbackFactory = CleanCompanyFallbackFactory.class)
public interface CleanCompanyService {


    /**
     * 定时释放客户至客户公海
     */
    @PostMapping("/advance/releaseCompanyJob")
    public void cleanCompany(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
