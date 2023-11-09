package com.anliantest.message.service;

import com.anliantest.message.api.domain.MailMessageSend;

/**
 * @Description
 * @Date 2023/7/20 14:01
 * @Author maoly
 **/
public interface IMailMessageSendService {

    /**
     * 邮件发送
     * @param message
     * @return
     */
    Boolean sendSimpleMail(MailMessageSend message);
}
