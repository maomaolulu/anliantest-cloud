package com.anliantest.job.task;

import com.anliantest.common.core.constant.SecurityConstants;
import com.anliantest.data.api.RemoteEquipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author ZhuYiCheng
 * @date 2023/8/10 16:16
 */
@Component("equipTask")
public class EquipTask {

    @Autowired
    private RemoteEquipService remoteEquipService;


    /**
     * 定时更新设备状态和检定状态,每天凌晨1:00执行
     */
    public void refreshVerificationStatus(){
        remoteEquipService.refreshVerificationStatus(SecurityConstants.INNER);
    }

    /**
     * 设备送检消息提醒：有效期前7天，每天上午9：00均要发送邮件提示设备管理员
     */
    public void sendReminderMessage(){
        remoteEquipService.sendReminderMessage(SecurityConstants.INNER);
    }
}
