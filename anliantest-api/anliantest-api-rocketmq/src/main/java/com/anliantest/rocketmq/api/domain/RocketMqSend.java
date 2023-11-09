package com.anliantest.rocketmq.api.domain;

import lombok.*;

import java.io.Serializable;

/**
 * @Description mq消息入参封装
 * @Date 2023/8/2 10:12
 * @Author maoly
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RocketMqSend implements Serializable {
    private static final long serialVersionUID = -7726198365529766075L;

    /**
     * 主题
     */
    private String topic;
    /**
     * 标签
     */
    private String tag;
    /**
     * 自定义的key，根据业务来定
     */
    private String key;
    /**
     * 消息内容
     */
    private String messageText;
}
