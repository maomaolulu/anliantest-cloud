package com.anliantest.websocket.controller;

import com.alibaba.fastjson2.JSONObject;
import com.anliantest.common.core.web.domain.AjaxResult;
import com.anliantest.websocket.api.domain.WebSocketPush;
import com.anliantest.websocket.constant.TopicConstants;
import com.anliantest.websocket.domain.WebsocketResponseBody;
import com.anliantest.websocket.server.WebSocketServer;
import org.springframework.web.bind.annotation.*;


/**
 * @Description
 * @Date 2023/8/15 13:06
 * @Author maoly
 **/
@RestController
@RequestMapping("/websocket")
public class WebSocketController {

    /**
     * websocket消息发送
     * @param webSocketPush
     * @return
     */
    @PostMapping("/push")
    public AjaxResult push(@RequestBody WebSocketPush webSocketPush) {
        WebSocketServer.sendApi(JSONObject.toJSONString(WebsocketResponseBody.builder()
                        .topic(TopicConstants.API_TOPIC)
                        .text(webSocketPush.getMessage()).build()),
                webSocketPush.getToUserIds());
        return AjaxResult.success();

    }

}
