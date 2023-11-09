package com.anliantest.data.controller;

import com.anliantest.common.core.domain.R;
import com.anliantest.common.core.web.controller.BaseController;
import com.anliantest.common.core.web.page.TableDataInfo;
import com.anliantest.common.log.annotation.Log;
import com.anliantest.common.log.enums.BusinessType;
import com.anliantest.common.security.annotation.InnerAuth;
import com.anliantest.data.domain.dto.EquipVerificationRecordDto;
import com.anliantest.data.domain.vo.EquipVerificationRecordVo;
import com.anliantest.data.entity.EquipVerificationRecord;
import com.anliantest.data.service.EquipVerificationRecordService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ZhuYiCheng
 * @date 2023/8/7 14:10
 */
@RestController
@Api("检定记录")
@RequestMapping("/verification")
public class EquipVerificationRecordController extends BaseController {

    private final EquipVerificationRecordService verificationRecordService;

    @Autowired
    public EquipVerificationRecordController(EquipVerificationRecordService verificationRecordService){
        this.verificationRecordService = verificationRecordService;
    }

    /**
     * 设备送检
     *
     */
    @Log(title = "设备送检", businessType = BusinessType.UPDATE)
    @PostMapping("/submission/verification")
    public R<String> equipSubmission(@RequestBody EquipVerificationRecord verificationRecord) {

        return verificationRecordService.equipSubmission(verificationRecord);
    }


    /**
     * 设备退检
     *
     */
    @Log(title = "设备退检", businessType = BusinessType.UPDATE)
    @PostMapping("/return/verification")
    public R<String> equipReturn(@RequestBody EquipVerificationRecord verificationRecord) {

        return verificationRecordService.equipReturn(verificationRecord);
    }

    /**
     * 检定记录列表查询
     *
     */
    @GetMapping("/list")
    public TableDataInfo getList(EquipVerificationRecordDto verificationRecordDto) {
        startPage();
        List<EquipVerificationRecordVo> list = verificationRecordService.getList(verificationRecordDto);
        return getDataTable(list);
    }


    /**
     * 证书录入
     *
     */
    @PostMapping("/certificateEntry")
    public R<String> certificateEntry(@RequestBody EquipVerificationRecord verificationRecord) {

        return verificationRecordService.certificateEntry(verificationRecord);
    }


    /**
     * 获取检定记录详情
     *
     */
    @GetMapping("/info")
    public R<Object> getInfo(Long id) {
        EquipVerificationRecordVo verificationRecordVo = verificationRecordService.getInfo(id);
        return R.ok(verificationRecordVo);
    }


    /**
     * 编辑检定记录
     *
     */
    @PostMapping("/update")
    public R<String> updateVerification(@RequestBody EquipVerificationRecord verificationRecord) {

        return verificationRecordService.updateVerification(verificationRecord);
    }


    /**
     * 定时更新设备状态和检定状态,每天凌晨1:00执行
     */
    @InnerAuth
    @GetMapping("/refresh")
    public void refreshVerificationStatus(){
        verificationRecordService.refreshVerificationStatus();
    }


    /**
     *  设备送检消息提醒：有效期前7天，每天上午9：00均要发送邮件提示设备管理员
     */
    @InnerAuth
    @GetMapping("/send")
    public void sendReminderMessage(){
        verificationRecordService.sendReminderMessage();
    }



}
