package com.anliantest.data.api;

import com.anliantest.common.core.constant.SecurityConstants;
import com.anliantest.common.core.constant.ServiceNameConstants;
import com.anliantest.data.api.factory.RemoteEquipFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @author ZhuYiCheng
 * @date 2023/8/10 15:48
 */
@FeignClient(contextId = "remoteEquipService", value = ServiceNameConstants.DATA_SERVICE, fallbackFactory = RemoteEquipFallbackFactory.class)
public interface RemoteEquipService {

    /**
     * 定时更新设备状态和检定状态,每天凌晨1:00执行
     */
    @GetMapping("/verification/refresh")
    void refreshVerificationStatus(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     *  设备送检消息提醒：有效期前7天，每天上午9：00均要发送邮件提示设备管理员
     */
    @GetMapping("/verification/send")
    public void sendReminderMessage(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
