package com.anliantest.websocket.constant;

/**
 * @Description websocket消息主题
 * @Date 2023/8/14 13:23
 * @Author maoly
 **/
public class TopicConstants {

    /**
     * 测试websocket连接是否正常
     */
    public static final String PING_TOPIC = "ping";

    /**
     * 系统消息
     */
    public static final String SYSTEM_TOPIC = "system";

    /**
     * 群发消息
     */
    public static final String GROUP_TOPIC = "group";

    /**
     * 后端api调用发送的消息
     */
    public static final String API_TOPIC = "api";
}
