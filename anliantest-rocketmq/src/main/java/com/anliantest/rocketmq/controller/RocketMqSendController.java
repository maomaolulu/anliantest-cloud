package com.anliantest.rocketmq.controller;

import com.anliantest.common.core.domain.R;
import com.anliantest.rocketmq.api.domain.RocketMqSend;
import com.anliantest.rocketmq.producer.MessageProducer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @Date 2023/8/2 9:57
 * @Author maoly
 **/
@RestController
@RequestMapping("/rocketmqsend")
public class RocketMqSendController {

    private final MessageProducer messageProducer = new MessageProducer();

    @PostMapping ("/sendSynchronizeMessage")
    public R sendSynchronizeMessage(@RequestBody RocketMqSend rocketMqSend) {
        messageProducer.sendSynchronizeMessage(rocketMqSend.getTopic(),
                rocketMqSend.getTag(),
                rocketMqSend.getKey(),
                rocketMqSend.getMessageText());
        return R.ok();

    }
}
