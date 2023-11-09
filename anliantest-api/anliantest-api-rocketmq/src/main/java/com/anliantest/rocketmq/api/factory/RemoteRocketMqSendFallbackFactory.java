package com.anliantest.rocketmq.api.factory;

import com.anliantest.common.core.domain.R;
import com.anliantest.rocketmq.api.RemoteRocketMqSendService;
import com.anliantest.rocketmq.api.domain.RocketMqSend;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Date 2023/8/2 9:53
 * @Author maoly
 **/
@Component
@Slf4j
public class RemoteRocketMqSendFallbackFactory implements FallbackFactory<RemoteRocketMqSendService> {

    @Override
    public RemoteRocketMqSendService create(Throwable throwable) {
        log.error("rocketmq消息调用失败:{}", throwable.getMessage());
        return new RemoteRocketMqSendService()
        {
            @Override
            public R sendSynchronizeMessage(RocketMqSend rocketMqSend, String source) {
                return R.fail("消息发送失败:" + throwable.getMessage());
            }
        };
    }
}
