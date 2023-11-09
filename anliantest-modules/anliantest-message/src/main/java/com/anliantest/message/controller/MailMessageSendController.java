package com.anliantest.message.controller;

import com.anliantest.common.core.domain.R;
import com.anliantest.message.api.domain.MailMessageSend;
import com.anliantest.message.service.IMailMessageSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @Date 2023/7/20 13:55
 * @Author maoly
 **/
@RestController
@RequestMapping("/mail")
public class MailMessageSendController {

    @Autowired
    private IMailMessageSendService mailMessageSendService;

    @PostMapping("/send")
    public R<Boolean> sendMail(@RequestBody MailMessageSend message){
       return R.ok(mailMessageSendService.sendSimpleMail(message));
    }
}
