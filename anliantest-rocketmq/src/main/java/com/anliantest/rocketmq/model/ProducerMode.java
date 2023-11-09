package com.anliantest.rocketmq.model;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * 生产者初始化
 */
@RefreshScope
@Data
@Configuration
public class ProducerMode {
    @Value("${suning.rocketmq.producer.groupName}")
    private String groupName;
    @Value("${suning.rocketmq.namesrvAddr}")
    private String namesrvAddr;
    @Value("${suning.rocketmq.producer.maxMessageSize}")
    private Integer maxMessageSize;
    @Value("${suning.rocketmq.producer.sendMsgTimeout}")
    private Integer sendMsgTimeout;
    @Value("${suning.rocketmq.producer.retryTimesWhenSendFailed}")
    private Integer retryTimesWhenSendFailed;
}
