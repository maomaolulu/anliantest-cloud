package com.anliantest.data.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.anliantest.common.core.constant.HttpStatus;
import com.anliantest.common.core.constant.MinioConstants;
import com.anliantest.common.core.constant.SecurityConstants;
import com.anliantest.common.core.domain.R;
import com.anliantest.common.core.enums.DeleteFlag;
import com.anliantest.common.core.enums.Numbers;
import com.anliantest.common.core.utils.DateUtils;
import com.anliantest.common.core.utils.StringUtils;
import com.anliantest.common.security.utils.SecurityUtils;
import com.anliantest.data.domain.dto.EquipVerificationRecordDto;
import com.anliantest.data.domain.vo.EquipVerificationRecordVo;
import com.anliantest.data.entity.EquipVerificationRecord;
import com.anliantest.data.entity.EquipWarehouseRecord;
import com.anliantest.data.mapper.EquipVerificationRecordMapper;
import com.anliantest.data.service.EquipVerificationRecordService;
import com.anliantest.data.service.EquipWarehouseRecordService;
import com.anliantest.file.api.RemoteSysAttachmentService;
import com.anliantest.file.api.domain.SysAttachment;
import com.anliantest.file.api.domain.dto.MinioDto;
import com.anliantest.message.api.RemoteMailMessageSendService;
import com.anliantest.message.api.domain.MailMessageSend;
import com.anliantest.system.api.RemoteUserService;
import com.anliantest.system.api.domain.SysUser;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ZhuYiCheng
 * @date 2023/8/7 9:04
 */
@Service
public class EquipVerificationRecordServiceImpl extends ServiceImpl<EquipVerificationRecordMapper, EquipVerificationRecord> implements EquipVerificationRecordService {

    private final RemoteSysAttachmentService remoteSysAttachmentService;
    private final EquipWarehouseRecordService warehouseRecordService;
    private final RemoteMailMessageSendService remoteMailMessageSendService;
    private final RemoteUserService remoteUserService;

    @Autowired
    public EquipVerificationRecordServiceImpl(RemoteSysAttachmentService remoteSysAttachmentService, EquipWarehouseRecordService warehouseRecordService
            , RemoteMailMessageSendService remoteMailMessageSendService, RemoteUserService remoteUserService) {
        this.remoteSysAttachmentService = remoteSysAttachmentService;
        this.warehouseRecordService = warehouseRecordService;
        this.remoteMailMessageSendService = remoteMailMessageSendService;
        this.remoteUserService = remoteUserService;
    }


    /**
     * 设备送检
     */
    @Override
    @Transactional
    public R<String> equipSubmission(EquipVerificationRecord verificationRecord) {

        Date nowDate = DateUtils.getNowDate();
        String usernameCn = SecurityUtils.getUsernameCn();
        Long equipId = verificationRecord.getEquipId();
        if (equipId != null) {
            //修改设备状态、检定状态
            EquipWarehouseRecord equipWarehouseRecord = new EquipWarehouseRecord();
            equipWarehouseRecord.setId(verificationRecord.getEquipId());
            equipWarehouseRecord.setStatus(8);
            equipWarehouseRecord.setVerificationStatus(2);
            boolean b = warehouseRecordService.updateById(equipWarehouseRecord);
            //添加检定记录
            verificationRecord.setCreateBy(usernameCn);
            verificationRecord.setCreateTime(nowDate);
            verificationRecord.setUpdateBy(usernameCn);
            verificationRecord.setUpdateTime(nowDate);
            verificationRecord.setDelFlag(DeleteFlag.NO.ordinal());
            boolean b1 = this.save(verificationRecord);

            return b && b1 ? R.ok("送检成功") : R.fail("送检失败");
        } else {
            return R.fail("送检失败");
        }

    }


    /**
     * 设备退检
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<String> equipReturn(EquipVerificationRecord verificationRecord) {
        Date nowDate = DateUtils.getNowDate();
        String usernameCn = SecurityUtils.getUsernameCn();
        Long equipId = verificationRecord.getEquipId();
        if (equipId != null) {
            //修改设备状态、检定状态
            EquipWarehouseRecord equipWarehouseRecord = warehouseRecordService.getById(equipId);
            //退检后根据证书有效期判断设备是否可用
            Date usefulTime = equipWarehouseRecord.getUsefulTime();
            long nowDate1 = nowDate.getTime();
            long usefulTime1 = usefulTime.getTime();
            if (usefulTime1 > nowDate1) {
                //证书未过期,在用
                equipWarehouseRecord.setStatus(1);
            } else {
                //证书已过期,停用
                equipWarehouseRecord.setStatus(8);
            }
            //待送检
            equipWarehouseRecord.setVerificationStatus(1);
            boolean b = warehouseRecordService.updateById(equipWarehouseRecord);
            //修改检定记录
            EquipVerificationRecord verificationRecord1 = this.list(new QueryWrapper<EquipVerificationRecord>()
                    .eq("equip_id", equipId)
                    .eq("del_flag", DeleteFlag.NO.ordinal())).get(0);
            verificationRecord1.setReturnTime(verificationRecord.getReturnTime());
            verificationRecord1.setUpdateTime(nowDate);
            verificationRecord1.setUpdateBy(usernameCn);
            boolean b1 = this.updateById(verificationRecord1);
            return b && b1 ? R.ok("退检成功") : R.fail("退检失败");
        } else {
            return R.fail("退检失败");
        }
    }


    /**
     * 检定记录列表查询（查看证书）
     */
    @Override
    public List<EquipVerificationRecordVo> getList(EquipVerificationRecordDto verificationRecordDto) {

        //获取检定记录列表
        List<EquipVerificationRecordVo> verificationRecordVoList = baseMapper.getList(new QueryWrapper<>()
                .like(verificationRecordDto.getEquipCode() != null, "w.equip_code", verificationRecordDto.getEquipCode())
                .like(verificationRecordDto.getGoodsName() != null, "w.goods_name", verificationRecordDto.getGoodsName())
                .like(verificationRecordDto.getCertificateCode() != null, "v.certificate_code", verificationRecordDto.getCertificateCode())
                .eq("v.del_flag", DeleteFlag.NO.ordinal())
                .orderByAsc("v.certificate_code")
                .orderByDesc("v.useful_time"));

        //获取检定记录id列表
        List<EquipVerificationRecordVo> verificationRecordVoList1 = verificationRecordVoList.stream().filter(v ->
                StringUtils.isNotBlank(v.getCertificateCode())
        ).collect(Collectors.toList());

        //收集id
        List<Long> idList = new ArrayList<>();
        for (EquipVerificationRecordVo equipVerificationRecordVo : verificationRecordVoList1) {
            idList.add(equipVerificationRecordVo.getId());
        }
        //根据父级id获取附件列表map
        MinioDto minioDto = new MinioDto();
        minioDto.setBucketName(MinioConstants.EQUIP_BUCKET_NAME);
        minioDto.setIdList(idList);
        R<Map<Long, List<SysAttachment>>> r = remoteSysAttachmentService.getSysAttachmentMap(minioDto, SecurityConstants.INNER);
        if (r.getCode() != HttpStatus.SUCCESS) {
            return verificationRecordVoList;
        }
        Map<Long, List<SysAttachment>> sysAttachmentMap = r.getData();

        //根据检定记录id=附件p_id，为检定记录分配附件
        verificationRecordVoList.stream().filter(v ->
                StringUtils.isNotBlank(v.getCertificateCode())).forEach(v -> v.setSysAttachmentList(sysAttachmentMap.get(v.getId())));

        return verificationRecordVoList;
    }


    /**
     * 证书录入
     */
    @Override
    public R<String> certificateEntry(EquipVerificationRecord verificationRecord) {

        Date nowDate = DateUtils.getNowDate();
        String usernameCn = SecurityUtils.getUsernameCn();
        //获取该设备最新一条检定记录的id
        Long equipId = verificationRecord.getEquipId();
        if (equipId == null) {
            System.out.println("未传设备id");
            return R.fail("录入失败");
        }
        Integer entryType = verificationRecord.getEntryType();
        Long id;
        EquipWarehouseRecord equipWarehouseRecord = new EquipWarehouseRecord();
        if (entryType == Numbers.ZERO.ordinal()) {
            EquipVerificationRecord equipVerificationRecord = this.list(new QueryWrapper<EquipVerificationRecord>()
                    .eq("equip_id", equipId)
                    .eq("del_flag", DeleteFlag.NO.ordinal())
                    .orderByDesc("create_time")).get(0);
            id = equipVerificationRecord.getId();
            verificationRecord.setId(id);

        } else {
            id = verificationRecord.getId();
            if (id == null) {
                System.out.println("未传检定记录id");
                return R.fail("录入失败");
            }
        }

        List<SysAttachment> sysAttachmentList = verificationRecord.getSysAttachmentList();
        if (CollUtil.isEmpty(sysAttachmentList)) {
            System.out.println("未传附件信息");
            return R.fail("录入失败，未传附件信息");
        }
        for (SysAttachment sysAttachment : sysAttachmentList) {
            sysAttachment.setPId(id);
            sysAttachment.setTempId(MinioConstants.FOREVER);
            sysAttachment.setUpdatedBy(usernameCn);
            sysAttachment.setUpdatedTime(nowDate);
        }
        //将附件信息临时转有效
        MinioDto minioDto = new MinioDto();
        minioDto.setSysAttachmentList(sysAttachmentList);
        Boolean b2 = remoteSysAttachmentService.transformByIds(minioDto, SecurityConstants.INNER);
        if (!b2) {
            System.out.println("临时转有效失败");
            return R.fail("录入失败");
        }
        //更新设备表证书有效期
        equipWarehouseRecord.setId(equipId);
        equipWarehouseRecord.setUsefulTime(verificationRecord.getUsefulTime());
        equipWarehouseRecord.setVerificationStatus(Numbers.ZERO.ordinal());
        equipWarehouseRecord.setStatus(Numbers.FIRST.ordinal());
        equipWarehouseRecord.setVerifyState(Numbers.ZERO.ordinal());
        equipWarehouseRecord.setVerifyDate(null);
        equipWarehouseRecord.setUpdateBy(usernameCn);
        equipWarehouseRecord.setUpdateTime(nowDate);
        boolean b = warehouseRecordService.updateById(equipWarehouseRecord);
        boolean b1 = this.updateById(verificationRecord);
        return b && b1 ? R.ok("录入成功") : R.fail("录入失败");
    }


    /**
     * 获取检定记录详情
     */
    @Override
    public EquipVerificationRecordVo getInfo(Long id) {

        EquipVerificationRecordVo equipVerificationRecordVo = baseMapper.getInfo(id);
        if (StringUtils.isEmpty(equipVerificationRecordVo.getCertificateCode())) {
            return equipVerificationRecordVo;
        }
        //获取证书附件列表
        List<Long> idList = new ArrayList<>();
        idList.add(equipVerificationRecordVo.getId());

        //根据父级id获取附件列表map
        MinioDto minioDto = new MinioDto();
        minioDto.setBucketName(MinioConstants.EQUIP_BUCKET_NAME);
        minioDto.setIdList(idList);
        R<Map<Long, List<SysAttachment>>> r = remoteSysAttachmentService.getSysAttachmentMap(minioDto, SecurityConstants.INNER);
        if (r.getCode() != HttpStatus.SUCCESS) {
            return equipVerificationRecordVo;
        }
        Map<Long, List<SysAttachment>> sysAttachmentMap = r.getData();

        equipVerificationRecordVo.setSysAttachmentList(sysAttachmentMap.get(equipVerificationRecordVo.getId()));

        return equipVerificationRecordVo;
    }


    /**
     * 编辑检定记录
     */
    @Override
    public R<String> updateVerification(EquipVerificationRecord verificationRecord) {

        Date nowDate = DateUtils.getNowDate();
        String usernameCn = SecurityUtils.getUsernameCn();
        Long id = verificationRecord.getId();
        if (id == null) {
            System.out.println("未传检定记录id");
            return R.fail("编辑失败");
        }

        List<SysAttachment> sysAttachmentList = verificationRecord.getSysAttachmentList();
        if (CollUtil.isEmpty(sysAttachmentList)) {
            System.out.println("未传附件信息");
            return R.fail("编辑失败，未上传证书附件");
        }

        //根据父级id,新附件列表更新附件信息
        MinioDto minioDto = new MinioDto();
        minioDto.setId(id);
        minioDto.setSysAttachmentList(sysAttachmentList);
        R<String> r = remoteSysAttachmentService.updateByPid(minioDto, SecurityConstants.INNER);
        if (r.getCode() != HttpStatus.SUCCESS) {
            System.out.println("根据父级id,新附件列表更新附件信息");
            return R.fail("编辑失败");
        }
        //更新检定记录信息
        verificationRecord.setUpdateBy(usernameCn);
        verificationRecord.setUpdateTime(nowDate);
        boolean b = this.updateById(verificationRecord);
        //更新设备表证书有效期
        EquipWarehouseRecord equipWarehouseRecord = new EquipWarehouseRecord();
        equipWarehouseRecord.setId(verificationRecord.getEquipId());
        equipWarehouseRecord.setUsefulTime(verificationRecord.getUsefulTime());
        equipWarehouseRecord.setUpdateBy(usernameCn);
        equipWarehouseRecord.setUpdateTime(nowDate);
        boolean b1 = warehouseRecordService.updateById(equipWarehouseRecord);
        return b && b1 ? R.ok("编辑成功") : R.fail("编辑失败");
    }


    /**
     * 定时更新设备状态和检定状态,每天凌晨1:00执行
     */
    @Override
    public void refreshVerificationStatus() {
        Date now = DateUtils.getNowDate();
        long nowTime = now.getTime();
        long standardTime = nowTime + 2592000000L;
        Date standard = new Date(standardTime);
        String username = "admin";
        List<String> calibrationList = new ArrayList<>();
        calibrationList.add("检定");
        calibrationList.add("校准");
        //检定状态需要改为待送检状态的list，现在时间 < 证书过期时间 < 现在时间-30天，检定状态改为待送检
        List<EquipWarehouseRecord> warehouseRecordList = warehouseRecordService.list(new QueryWrapper<EquipWarehouseRecord>()
                .ne("status", 6)
                .eq("verification_status", 0)
                .gt("useful_time", now)
                .lt("useful_time", standard)
                .in("calibration", calibrationList));
        for (EquipWarehouseRecord equipWarehouseRecord : warehouseRecordList) {
            equipWarehouseRecord.setVerificationStatus(1);
            equipWarehouseRecord.setUpdateTime(now);
            equipWarehouseRecord.setUpdateBy(username);
        }

        //设备状态需要改为停用状态的list
        List<EquipWarehouseRecord> warehouseRecordList1 = warehouseRecordService.list(new QueryWrapper<EquipWarehouseRecord>()
                .ne("status", 6)
                .eq("verification_status", 1)
                .lt("useful_time", now)
                .in("calibration", calibrationList));
        for (EquipWarehouseRecord equipWarehouseRecord : warehouseRecordList1) {
            //现在时间 > 证书过期时间, 设备状态改为停用
            equipWarehouseRecord.setStatus(8);
            equipWarehouseRecord.setUpdateTime(now);
            equipWarehouseRecord.setUpdateBy(username);
        }

        warehouseRecordList.addAll(warehouseRecordList1);
        //更新设备的状态
        warehouseRecordService.updateBatchById(warehouseRecordList);
    }


    /**
     * 设备送检消息提醒：有效期前7天，每天上午9：00均要发送邮件提示设备管理员
     */
    @Override
    public void sendReminderMessage() {
        Date now = DateUtils.getNowDate();
        long nowTime = now.getTime();
        long standardTime = nowTime + 604800000L;
        Date standard = new Date(standardTime);
        List<String> calibrationList = new ArrayList<>();
        calibrationList.add("检定");
        calibrationList.add("校准");

        //现在时间 < 证书过期时间 < 现在时间+7天,获取需要邮件提醒的设备列表
        List<EquipWarehouseRecord> warehouseRecordList = warehouseRecordService.list(new QueryWrapper<EquipWarehouseRecord>()
                .ne("status", 6)
                .eq("verification_status", 1)
                .lt("useful_time", standard)
                .gt("useful_time", now)
                .in("calibration", calibrationList));


        if (CollectionUtil.isNotEmpty(warehouseRecordList)) {
            //给符合条件的设备的责任人发邮件提醒
            MailMessageSend mailMessageSend = new MailMessageSend();
            mailMessageSend.setSubject("设备送检提醒");
            //将列表根据责任人分组
            Map<Long, List<EquipWarehouseRecord>> listMap = warehouseRecordList.stream().collect(Collectors.groupingBy(EquipWarehouseRecord::getChargeId));
            for (Long userId : listMap.keySet()) {
                SysUser chargeUser = remoteUserService.getInfoById(userId, SecurityConstants.INNER);
                String email = chargeUser.getEmail();
                StringBuilder informationBuilder = new StringBuilder("您好：\r\n" +
                        "\r\n你所负责的设备中，存在需要送检的设备，具体所需送检设备的信息如下：\r\n");
                for (EquipWarehouseRecord equipWarehouseRecord : listMap.get(userId)) {
                    String equipCode = equipWarehouseRecord.getEquipCode();
                    String goodsName = equipWarehouseRecord.getGoodsName();
                    String s = "\r\n设备编号为 [ " + equipCode + " ] 的 " + goodsName + "，\r\n";
                    informationBuilder.append(s);
                }
                String information = informationBuilder.toString();
                information += "\r\n以上设备证书七天内将会过期，请及时将以上设备送检，并在集团运营系统-设备中心-检定校验页面记录送检操作。";
                mailMessageSend.setText(information);
                List<String> toList = new ArrayList<>();
                toList.add(email);
                mailMessageSend.setToList(toList);
                remoteMailMessageSendService.sendMail(mailMessageSend, SecurityConstants.INNER);
            }
        }
    }


}
