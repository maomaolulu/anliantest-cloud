package com.anliantest.rocketmq.config;

/**
 * @author yz
 */
public class MessageConfig {
    private Class<?> messageClass;
    private boolean orderlyMessage;

    public Class<?> getMessageClass() {
        return messageClass;
    }

    public void setMessageClass(Class<?> messageClass) {
        this.messageClass = messageClass;
    }

    public boolean isOrderlyMessage() {
        return orderlyMessage;
    }

    public void setOrderlyMessage(boolean orderlyMessage) {
        this.orderlyMessage = orderlyMessage;
    }


}
