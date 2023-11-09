package com.anliantest.websocket.api;

import com.anliantest.common.core.constant.SecurityConstants;
import com.anliantest.common.core.constant.ServiceNameConstants;
import com.anliantest.common.core.web.domain.AjaxResult;
import com.anliantest.websocket.api.domain.WebSocketPush;
import com.anliantest.websocket.api.factory.RemoteWebSocketFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.validation.Valid;

/**
 * @Description
 * @Date 2023/8/2 9:50
 * @Author maoly
 **/
@FeignClient(contextId = "remoteWebSocketService", value = ServiceNameConstants.WEBSOCKET_SERVICE, fallbackFactory = RemoteWebSocketFallbackFactory.class)
public interface RemoteWebSocketService {


    /**
     * 发送websocket消息
     * @param webSocketPush
     * @param source
     * @return
     */
    @PostMapping("/websocket/push")
    public AjaxResult pushWebSocket(@RequestBody WebSocketPush webSocketPush, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

}
