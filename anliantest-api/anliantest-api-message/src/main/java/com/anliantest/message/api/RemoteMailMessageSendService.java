package com.anliantest.message.api;

import com.anliantest.common.core.constant.SecurityConstants;
import com.anliantest.common.core.constant.ServiceNameConstants;
import com.anliantest.common.core.domain.R;
import com.anliantest.message.api.domain.MailMessageSend;
import com.anliantest.message.api.factory.RemoteMailMessageSendFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @Description
 * @Date 2023/7/20 15:38
 * @Author maoly
 **/
@FeignClient(contextId = "remoteMailMessageSendService", value = ServiceNameConstants.MESSAGE_SERVICE, fallbackFactory = RemoteMailMessageSendFallbackFactory.class)
public interface RemoteMailMessageSendService {

    /**
     * 发送邮件消息
     * @param message 邮件消息对象
     * @param source 请求来源
     * @return true 发送成功 false 发送失败
     */
    @PostMapping("/mail/send")
    public R<Boolean> sendMail(@RequestBody MailMessageSend message, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
