package com.anliantest.websocket.api.domain;

import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;
import java.util.List;

/**
 * @Description
 * @Date 2023/8/15 14:33
 * @Author maoly
 **/
@Data
public class WebSocketPush implements Serializable {
    private static final long serialVersionUID = 3041280921770792551L;
    /**
     * 消息接收人userId
     */
    private List<Long> toUserIds;
    /**
     * 消息内容,可根据业务自定义
     */
    private String message;
}
