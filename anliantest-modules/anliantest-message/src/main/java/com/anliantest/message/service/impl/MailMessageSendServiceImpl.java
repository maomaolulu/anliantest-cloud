package com.anliantest.message.service.impl;

import com.anliantest.message.api.domain.MailMessageSend;
import com.anliantest.message.service.IMailMessageSendService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description
 * @Date 2023/7/20 14:02
 * @Author maoly
 **/
@Service
public class MailMessageSendServiceImpl implements IMailMessageSendService {

    @Value("${spring.mail.username}")
    private String sendEmailAccount;

    @Resource
    private JavaMailSender javaMailSender;

    @Override
    public Boolean sendSimpleMail(MailMessageSend message) {
        Boolean flag = true;
        try{
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setSubject(message.getSubject());
            simpleMailMessage.setText(message.getText());
            simpleMailMessage.setFrom(sendEmailAccount);
            List<String> toList = message.getToList();
            String[] to = toList.toArray(new String[toList.size()]);
            simpleMailMessage.setTo(to);
            javaMailSender.send(simpleMailMessage);
        }catch (Exception e){
            flag = false;
        }
        return flag;
    }
}
