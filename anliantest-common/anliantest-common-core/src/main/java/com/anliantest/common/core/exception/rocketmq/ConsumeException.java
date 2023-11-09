package com.anliantest.common.core.exception.rocketmq;

/**
 * @author 影子
 */
public class ConsumeException extends RuntimeException{
    private static final long serialVersionUID = 4093867789628938836L;

    public ConsumeException(String message) {
        super(message);
    }

    public ConsumeException(Throwable cause) {
        super(cause);
    }

    public ConsumeException(String message, Throwable cause) {
        super(message, cause);
    }
}
