package com.anliantest.rocketmq.api;

import com.anliantest.common.core.constant.SecurityConstants;
import com.anliantest.common.core.constant.ServiceNameConstants;
import com.anliantest.common.core.domain.R;
import com.anliantest.rocketmq.api.domain.RocketMqSend;
import com.anliantest.rocketmq.api.factory.RemoteRocketMqSendFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @Description
 * @Date 2023/8/2 9:50
 * @Author maoly
 **/
@FeignClient(contextId = "remoteRocketMqSendService", value = ServiceNameConstants.ROCKETMQ_SERVICE, fallbackFactory = RemoteRocketMqSendFallbackFactory.class)
public interface RemoteRocketMqSendService {


    /**
     * 发送同步消息
     * @param rocketMqSend mq消息入参封装
     * @param source 请求来源
     * @return
     */
    @PostMapping("/rocketmqsend/sendSynchronizeMessage")
    public R sendSynchronizeMessage(@RequestBody RocketMqSend rocketMqSend, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

}
