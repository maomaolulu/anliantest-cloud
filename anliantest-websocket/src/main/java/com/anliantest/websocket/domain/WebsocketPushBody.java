package com.anliantest.websocket.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 消息请求体
 * @Date 2023/8/14 13:31
 * @Author maoly
 **/
@Data
public class WebsocketPushBody implements Serializable {
    private static final long serialVersionUID = 369730341414595357L;

    /**
     * 消息主题
     */
    private String topic;

    /**
     * 业务类型
     */
    private String bussinessType;

    /**
     * 消息接收人userId
     */
    private List<Long> toUserIdList;

    /**
     * 消息内容
     */
    private String text;
}
