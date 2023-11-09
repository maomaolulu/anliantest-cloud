package com.anliantest.data.api.factory;

import com.anliantest.data.api.RemoteSubstanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author gy
 * @Description 物质库相关服务兜底方案
 * @date 2023-07-03 11:31
 */
@Component
public class RemoteSubstanceFallbackFactory implements FallbackFactory<RemoteSubstanceService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteSubstanceFallbackFactory.class);

    @Override
    public RemoteSubstanceService create(Throwable throwable)
    {
        log.error("物质库相关服务调用失败:{}", throwable.getMessage());
        return new RemoteSubstanceService()
        {
            @Override
            public void refreshStatus(String source){
                log.error("refresh status fail");
            }
        };

    }
}
