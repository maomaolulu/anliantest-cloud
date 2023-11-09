package com.anliantest.system.api.factory;

import com.anliantest.system.api.RemoteConfigService;
import com.anliantest.system.api.domain.SysConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RemoteConfigFallbackFactory implements FallbackFactory<RemoteConfigService> {
    @Override
    public RemoteConfigService create(Throwable throwable) {
        log.error(throwable.getMessage());
        return new RemoteConfigService() {
            /**
             * @return
             */
            @Override
            public SysConfig findConfigUrl() {
                return null;
            }
        };
    }
}
