package com.anliantest.system.api.factory;

import com.anliantest.common.core.domain.R;
import com.anliantest.system.api.RemoteChineseAddressService;
import com.anliantest.system.api.RemoteConfigService;
import com.anliantest.system.api.domain.SysConfig;
import com.anliantest.system.api.model.AmapRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RemoteChineseAddressFallbackFactory implements FallbackFactory<RemoteChineseAddressService> {
    @Override
    public RemoteChineseAddressService create(Throwable throwable) {
        log.error(throwable.getMessage());
        return new RemoteChineseAddressService() {
            @Override
            public R saveChineseAddress(String param, String source) {
                return R.fail("调用失败:" + throwable.getMessage());
            }
        };
    }
}
