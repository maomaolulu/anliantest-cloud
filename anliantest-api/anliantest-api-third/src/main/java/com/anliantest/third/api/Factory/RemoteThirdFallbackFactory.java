package com.anliantest.third.api.Factory;

import com.anliantest.third.api.RemoteThirdService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author yrb
 * @Date 2023/7/27 15:25
 * @Version 1.0
 * @Description 第三方服务调用降级
 */
@Slf4j
@Component
public class RemoteThirdFallbackFactory implements FallbackFactory<RemoteThirdService> {
    /**
     * @param cause
     * @return
     */
    @Override
    public RemoteThirdService create(Throwable cause) {
        log.error(cause.getMessage());
        return new RemoteThirdService() {
            @Override
            public Map<String, String> findUserInfo(Map<String, String> param) {
                return null;
            }
        };
    }
}
