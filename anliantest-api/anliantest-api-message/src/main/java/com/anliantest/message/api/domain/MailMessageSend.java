package com.anliantest.message.api.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description
 * @Date 2023/7/20 17:19
 * @Author maoly
 **/
@Data
public class MailMessageSend implements Serializable {
    private static final long serialVersionUID = 8598173093338356486L;

    /**
     * 邮件接收人
     */
    private List<String> toList;
    /**
     * 主题
     */
    private String subject;
    /**
     * 邮件内容
     */
    private String text;
}
