package com.anliantest.message.api.factory;

import com.anliantest.common.core.domain.R;
import com.anliantest.message.api.RemoteMailMessageSendService;
import com.anliantest.message.api.domain.MailMessageSend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 邮件消息服务降级处理
 *
 * @author ruoyi
 */
@Component
public class RemoteMailMessageSendFallbackFactory implements FallbackFactory<RemoteMailMessageSendService>
{
    private static final Logger log = LoggerFactory.getLogger(RemoteMailMessageSendFallbackFactory.class);

    @Override
    public RemoteMailMessageSendService create(Throwable throwable)
    {
        log.error("邮件消息调用失败:{}", throwable.getMessage());
        return new RemoteMailMessageSendService()
        {
            @Override
            public R sendMail(MailMessageSend message, String source) {
                return R.fail("邮件发送失败:" + throwable.getMessage());
            }
        };
    }
}
