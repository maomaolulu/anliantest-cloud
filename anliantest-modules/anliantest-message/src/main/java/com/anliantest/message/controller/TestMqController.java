package com.anliantest.message.controller;

import com.anliantest.common.core.constant.SecurityConstants;
import com.anliantest.common.core.domain.R;
import com.anliantest.common.core.enums.rocketmq.MessageCodeEnum;
import com.anliantest.rocketmq.api.RemoteRocketMqSendService;
import com.anliantest.rocketmq.api.domain.RocketMqSend;
import com.anliantest.websocket.api.RemoteWebSocketService;
import com.anliantest.websocket.api.domain.WebSocketPush;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Date 2023/8/2 10:38
 * @Author maoly
 **/
@RestController
@RequestMapping("/temq")
public class TestMqController {

    @Autowired
    private RemoteRocketMqSendService remoteRocketMqSendService;

    @Autowired
    private RemoteWebSocketService remoteWebSocketService;


    @GetMapping("/send")
    public R list() {
        String topic = MessageCodeEnum.ORDER_MESSAGE.getCode();
        String tag = MessageCodeEnum.ORDER_TIMEOUT_TAG.getCode();
        String key = "mq-key";
        String value = "测试的消息内容，看看谁能收得到？";
        remoteRocketMqSendService.sendSynchronizeMessage(RocketMqSend.builder().topic(topic).key(key).tag(tag).messageText(value).build(),
                SecurityConstants.INNER);
        return R.ok("消息发送成功");

    }

    /**
     * websocket调用API测试
     * @return
     */
    @GetMapping("/websocket")
    public R websocket() {
        WebSocketPush webSocketPush = new WebSocketPush();
        webSocketPush.setMessage("张三，你好啊，收得到吗？");
        List<Long> toUserIds = new ArrayList<>();
        toUserIds.add(914L);
        toUserIds.add(804L);
        webSocketPush.setToUserIds(toUserIds);
        remoteWebSocketService.pushWebSocket(webSocketPush,SecurityConstants.INNER);
        return R.ok("消息发送成功");

    }
}
