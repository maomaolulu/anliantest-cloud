package com.anliantest.data.service.impl;

import cn.hutool.core.util.StrUtil;
import com.anliantest.common.core.constant.SecurityConstants;
import com.anliantest.data.entity.EquipPurchaseRecord;
import com.anliantest.data.mapper.EquipPurchaseRecordMapper;
import com.anliantest.data.service.EquipPurchaseRecordService;
import com.anliantest.system.api.RemoteUserService;
import com.anliantest.system.api.domain.SysDept;
import com.anliantest.system.api.domain.SysUser;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author yrb
 * @Date 2023/8/1 15:03
 * @Version 1.0
 * @Description 仪器设备采购记录实现层
 */
@Service
public class EquipPurchaseRecordServiceImpl extends ServiceImpl<EquipPurchaseRecordMapper, EquipPurchaseRecord> implements EquipPurchaseRecordService {
    private final RemoteUserService remoteUserService;
    private final EquipPurchaseRecordMapper equipPurchaseRecordMapper;

    public EquipPurchaseRecordServiceImpl(RemoteUserService remoteUserService,
                                          EquipPurchaseRecordMapper equipPurchaseRecordMapper) {
        this.remoteUserService = remoteUserService;
        this.equipPurchaseRecordMapper = equipPurchaseRecordMapper;
    }

    /**
     * OA采购信息入库
     *
     * @param equipPurchaseRecord 采购信息
     * @return result
     */
    @Override
    public boolean add(EquipPurchaseRecord equipPurchaseRecord) {
        // OA用户ID关联运营2.0用户 获取部门id和公司id
        SysUser sysUser = remoteUserService.selectUserByOaUserId(equipPurchaseRecord.getUserId(),SecurityConstants.INNER);
        if (sysUser != null && sysUser.getUserId() != null) {
            SysDept sysDept = remoteUserService.getInfoByDeptId(sysUser.getDeptId(), SecurityConstants.INNER);
            equipPurchaseRecord.setDeptId(sysDept.getDeptId());
            equipPurchaseRecord.setCompanyId(Long.valueOf(sysUser.getCompanyKey()));
        }
        equipPurchaseRecord.setRemainAmount(equipPurchaseRecord.getActualAmount());
        equipPurchaseRecord.setCreateTime(new Date());
        return baseMapper.insert(equipPurchaseRecord) > 0;
    }

    /**
     * 获取采购信息列表
     *
     * @param equipPurchaseRecord 采购信息
     * @return 采购列表
     */
    @Override
    public List<EquipPurchaseRecord> list(EquipPurchaseRecord equipPurchaseRecord) {
        QueryWrapper<EquipPurchaseRecord> queryWrapper = new QueryWrapper<>();
        String purchaseCode = equipPurchaseRecord.getPurchaseCode();
        String applier = equipPurchaseRecord.getApplier();
        queryWrapper.orderByDesc("t1.arrived_time");
        // 申请编号
        queryWrapper.like(StrUtil.isNotBlank(purchaseCode), "t1.purchase_code", purchaseCode);
        // 申请人
        queryWrapper.like(StrUtil.isNotBlank(applier), "t1.applier", applier);
        // 获取未完全入库的记录
        queryWrapper.eq("t1.fully_stored", 0);
        // 公司
        queryWrapper.like(StrUtil.isNotBlank(equipPurchaseRecord.getCompanyName()), "t2.dept_name", equipPurchaseRecord.getCompanyName());
        // 部门
        queryWrapper.like(StrUtil.isNotBlank(equipPurchaseRecord.getDeptName()), "t3.dept_name", equipPurchaseRecord.getDeptName());
        return equipPurchaseRecordMapper.selectInfo(queryWrapper);
    }
}
