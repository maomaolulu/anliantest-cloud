package com.anliantest.job.api.factory;

import com.anliantest.job.api.RemoteJobService;
import com.anliantest.job.api.domain.SysJobApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author zhanghao
 * @date 2023-09-04
 */
@Component
public class RemoteJobFallbackFactory implements FallbackFactory<RemoteJobService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteJobFallbackFactory.class);

    @Override
    public RemoteJobService create(Throwable throwable) {

        log.error("定时任务服务调用失败:{}", throwable.getMessage());
        return new RemoteJobService() {
            @Override
            public Long export(SysJobApi sysJobApi, String source) {
                log.error("调用定时任务导出失败");
                return null;
            }


        };
    }
}
