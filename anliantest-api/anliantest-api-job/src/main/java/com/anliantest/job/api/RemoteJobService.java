package com.anliantest.job.api;

import com.anliantest.common.core.constant.SecurityConstants;
import com.anliantest.common.core.constant.ServiceNameConstants;
import com.anliantest.common.core.domain.R;
import com.anliantest.job.api.domain.SysJobApi;
import com.anliantest.job.api.factory.RemoteJobFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @author zhanghao
 * @date 2023-09-04
 */
@FeignClient(contextId = "remoteJobService", value = ServiceNameConstants.JOB_SERVICE, fallbackFactory = RemoteJobFallbackFactory.class)
public interface RemoteJobService {

    /**
     * 定时任务导出
     *
     * @param sysJobApi
     * @param source
     * @return
     */
    @PostMapping("/job/export")
    Long export(@RequestBody SysJobApi sysJobApi, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);


}
