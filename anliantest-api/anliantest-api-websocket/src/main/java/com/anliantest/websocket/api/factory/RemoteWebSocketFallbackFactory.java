package com.anliantest.websocket.api.factory;

import com.anliantest.common.core.web.domain.AjaxResult;
import com.anliantest.websocket.api.RemoteWebSocketService;
import com.anliantest.websocket.api.domain.WebSocketPush;
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
public class RemoteWebSocketFallbackFactory implements FallbackFactory<RemoteWebSocketService> {

    @Override
    public RemoteWebSocketService create(Throwable throwable) {
        log.error("rocketmq消息调用失败:{}", throwable.getMessage());
        return new RemoteWebSocketService()
        {
            @Override
            public AjaxResult pushWebSocket(WebSocketPush webSocketPush, String source) {
                return AjaxResult.error("消息发送失败:" + throwable.getMessage());
            }
        };
    }
}
