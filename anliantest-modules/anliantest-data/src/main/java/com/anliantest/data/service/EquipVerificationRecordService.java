package com.anliantest.data.service;

import com.anliantest.common.core.domain.R;
import com.anliantest.data.domain.dto.EquipVerificationRecordDto;
import com.anliantest.data.domain.vo.EquipVerificationRecordVo;
import com.anliantest.data.entity.EquipVerificationRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author ZhuYiCheng
 * @date 2023/8/7 9:02
 */
public interface EquipVerificationRecordService extends IService<EquipVerificationRecord> {


    /**
     * 设备送检
     * @param verificationRecord 送检设备信息
     * @return result
     */
    R<String> equipSubmission(EquipVerificationRecord verificationRecord);

    /**
     * 设备退检
     * @param verificationRecord 退检设备信息
     * @return result
     */
    R<String> equipReturn(EquipVerificationRecord verificationRecord);

    /**
     * 检定记录列表查询（查看证书）
     * @param verificationRecordDto 列表查询参数
     * @return 查询结果
     */
    List<EquipVerificationRecordVo> getList(EquipVerificationRecordDto verificationRecordDto);

    /**
     * 证书录入
     * @param verificationRecord 设备以及证书信息
     * @return result
     */
    R<String> certificateEntry(EquipVerificationRecord verificationRecord);

    /**
     * 获取检定记录详情
     * @param id 检定记录id
     * @return 查询结果
     */
    EquipVerificationRecordVo getInfo(Long id);

    /**
     * 编辑检定记录
     * @param verificationRecord 新检定信息
     * @return result
     */
    R<String> updateVerification(EquipVerificationRecord verificationRecord);


    /**
     * 定时更新设备状态和检定状态,每天凌晨1:00执行
     */
    void refreshVerificationStatus();

    /**
     *  设备送检消息提醒：有效期前7天，每天上午9：00均要发送邮件提示设备管理员
     */
    void sendReminderMessage();


}
