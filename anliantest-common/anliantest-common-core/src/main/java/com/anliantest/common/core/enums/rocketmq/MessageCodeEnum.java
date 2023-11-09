package com.anliantest.common.core.enums.rocketmq;


import lombok.Getter;

@Getter
public enum MessageCodeEnum {
    /**
     * 消息模块主题
     */
    MESSAGE_TOPIC("elink-message","消息服务模块topic名称"),
    /**
     * 系统消息
     */
    NOTE_MESSAGE("system-message","系统消息服务模块topic名称"),
    /**
     * 用户消息
     */
    USER_MESSAGE("user-message","用户消息服务模块topic名称"),

    /**
     * 订单消息
     */
    ORDER_MESSAGE("order-message","订单消息服务模块topic名称"),

    /**
     * 平台编号
     */
    USER_MESSAGE_TAG("user_message_tag","用户消息推送"),
    NOTE_MESSAGE_TAG("system_message_tag","系统消息推送"),
    ORDER_MESSAGE_TAG("order_message_tag","订单消息推送"),

    /**
     * 订单处理编号
     */
    //订单超时处理
    ORDER_TIMEOUT_TAG("order_timeout_tag","订单超时处理"),

    /**
     * 第三方API消息
     */
    THIRD_MESSAGE("third-message","第三方消息模块名称"),

    /**
     * third消息编号
     */
    THIRD_ADDRESS_MESSAGE_TAG("third-address-message-tag","地址数据消息"),
    /**
     * 进度管理消息
     */
    PROGRESS_MANAGE("progress_manage","进度管理消息"),
    /**
     * 定时任务导出
     */
    SYS_JOB_EXPORT("sys_job_export","定时任务导出");


    private final String code;
    private final String msg;

    MessageCodeEnum(String code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public static String valuesOfType(String code) {
        String value = "";
        for (MessageCodeEnum e : MessageCodeEnum.values()) {
            if (code.equals(e.code)) {
                value = e.msg;
            }

        }
        return value;
    }


}
