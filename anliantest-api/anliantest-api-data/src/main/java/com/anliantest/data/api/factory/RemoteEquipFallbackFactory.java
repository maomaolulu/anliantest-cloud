package com.anliantest.data.api.factory;

import com.anliantest.data.api.RemoteEquipService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author ZhuYiCheng
 * @date 2023/8/10 15:54
 */
@Component
public class RemoteEquipFallbackFactory implements FallbackFactory<RemoteEquipService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteEquipFallbackFactory.class);


    @Override
    public RemoteEquipService create(Throwable throwable) {
        log.error("设备相关服务调用失败:{}", throwable.getMessage());
        return new RemoteEquipService() {
            @Override
            public void refreshVerificationStatus(String source) {
                log.error("refresh equip status fail");
            }

            @Override
            public void sendReminderMessage(String source) {
                log.error("Send Reminder Message fail");
            }
        };
    }
}
