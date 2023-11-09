package com.anliantest.websocket.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
/**
 * @Description 消息响应体
 * @Date 2023/8/14 13:33
 * @Author maoly
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WebsocketResponseBody implements Serializable {
    private static final long serialVersionUID = -6641203009467269194L;
    /**
     * 消息主题
     */
    private String topic;

    /**
     * 业务类型
     */
    private String bussinessType;

    /**
     * 消息发送人
     */
    private Long sendUserId;

    /**
     * 消息内容
     */
    private String text;


}
