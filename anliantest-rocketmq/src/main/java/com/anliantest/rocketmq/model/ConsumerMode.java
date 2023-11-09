package com.anliantest.rocketmq.model;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Configuration
@Component
public class ConsumerMode {
    @Value("${suning.rocketmq.namesrvAddr}")
    private String namesrvAddr;
    @Value("${suning.rocketmq.conumer.groupName}")
    private String groupName ;
    @Value("${suning.rocketmq.conumer.consumeThreadMin}")
    private int consumeThreadMin;
    @Value("${suning.rocketmq.conumer.consumeThreadMax}")
    private int consumeThreadMax;
    @Value("${suning.rocketmq.conumer.consumeMessageBatchMaxSize}")
    private int consumeMessageBatchMaxSize;
}
