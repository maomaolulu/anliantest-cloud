package com.anliantest.data.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import com.anliantest.common.core.utils.DateUtils;
import com.anliantest.common.security.utils.SecurityUtils;
import com.anliantest.data.domain.vo.EquipMaintainVo;
import com.anliantest.data.domain.vo.PeriodCheckVo;
import com.anliantest.data.entity.EquipMaintainRecord;
import com.anliantest.data.entity.EquipWarehouseRecord;
import com.anliantest.data.mapper.EquipMaintainRecordMapper;
import com.anliantest.data.mapper.EquipWarehouseRecordMapper;
import com.anliantest.data.service.EquipMaintainRecordService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: liyongqiang
 * @create: 2023-08-07 10:54
 */
@Service
public class EquipMaintainRecordServiceImpl extends ServiceImpl<EquipMaintainRecordMapper, EquipMaintainRecord> implements EquipMaintainRecordService {

    @Resource
    private EquipWarehouseRecordMapper equipWarehouseRecordMapper;

    /**
     * 设备维修-列表
     */
    @Override
    public List<EquipMaintainVo> selectEquipMaintainList(EquipMaintainVo equipMaintainVo) {
        QueryWrapper<EquipMaintainVo> wrapper = new QueryWrapper<>();
        wrapper.like(CharSequenceUtil.isNotBlank(equipMaintainVo.getEquipCode()), "ewr.equip_code", equipMaintainVo.getEquipCode());
        wrapper.like(CharSequenceUtil.isNotBlank(equipMaintainVo.getGoodsName()), "ewr.goods_name", equipMaintainVo.getGoodsName());
        wrapper.eq(ObjectUtil.isNotNull(equipMaintainVo.getRepairStatus()), "emr.repair_status", equipMaintainVo.getRepairStatus());
        wrapper.eq("emr.del_flag", 0);
        wrapper.orderByDesc("emr.breakdown_time");
        return baseMapper.selectEquipMaintainList(wrapper);
    }

    /**
     * 在用设备列表
     */
    @Override
    public List<EquipWarehouseRecord> selectEquipUseList() {
        return equipWarehouseRecordMapper.selectList(new LambdaQueryWrapper<EquipWarehouseRecord>()
                .select(EquipWarehouseRecord::getId, EquipWarehouseRecord::getEquipCode, EquipWarehouseRecord::getGoodsName, EquipWarehouseRecord::getModel, EquipWarehouseRecord::getChargeDeptId)
                .eq(EquipWarehouseRecord::getStatus, 1));
    }

    /**
     * 新增-维修记录
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertEquipMaintainRecord(EquipMaintainRecord maintainRecord) {
        maintainRecord.setCreateBy(SecurityUtils.getUsernameCn());
        maintainRecord.setCreateTime(DateUtils.getNowDate());
        baseMapper.insert(maintainRecord);
        equipWarehouseRecordMapper.updateStatusById(4, SecurityUtils.getUsernameCn(), DateUtil.dateSecond(), maintainRecord.getEquipId());
        return 1;
    }

    /**
     * 删除-维修记录
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteEquipMaintainRecordById(Long id, Long equipId) {
        baseMapper.deleteRepairRecordById(id, SecurityUtils.getUsernameCn(), DateUtil.dateSecond());
        equipWarehouseRecordMapper.updateStatusById(1, SecurityUtils.getUsernameCn(), DateUtil.dateSecond(), equipId);
        return 1;
    }

    /**
     * 编辑-维修记录
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateEquipMaintainRecord(EquipMaintainVo equipMaintainVo) {
        if (!equipMaintainVo.getEquipId().equals(equipMaintainVo.getAfterEquipId())) {
            equipWarehouseRecordMapper.updateStatusById(1, SecurityUtils.getUsernameCn(), DateUtil.dateSecond(), equipMaintainVo.getEquipId());
            equipWarehouseRecordMapper.updateStatusById(4, SecurityUtils.getUsernameCn(), DateUtil.dateSecond(), equipMaintainVo.getAfterEquipId());
        }
        return baseMapper.updateEquipMaintainRecordById(equipMaintainVo.getAfterEquipId(), DateUtils.parseDate(equipMaintainVo.getBreakdownTime()), SecurityUtils.getUsernameCn(), DateUtil.dateSecond(), equipMaintainVo.getId());
    }

    /**
     * 设备-完成维修
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int completeRepair(EquipMaintainVo equipMaintainVo) {
        EquipMaintainRecord maintainRecord = new EquipMaintainRecord();
        maintainRecord.setId(equipMaintainVo.getId());
        maintainRecord.setEquipId(equipMaintainVo.getEquipId());
        maintainRecord.setRepairStatus(1);
        maintainRecord.setCompleteTime(DateUtils.parseDate(equipMaintainVo.getCompleteTime()));
        maintainRecord.setRepairCost(equipMaintainVo.getRepairCost());
        maintainRecord.setRemark(equipMaintainVo.getRemark());
        maintainRecord.setUpdateBy(SecurityUtils.getUsernameCn());
        maintainRecord.setUpdateTime(DateUtil.dateSecond());
        baseMapper.updateById(maintainRecord);

        equipWarehouseRecordMapper.updateStatusById(1, SecurityUtils.getUsernameCn(), DateUtil.dateSecond(), equipMaintainVo.getEquipId());
        return 1;
    }

    /**
     * 设备维修详情
     */
    @Override
    public EquipMaintainVo selectInfoById(Long id) {
        return baseMapper.selectInfoById(id);
    }


    /**
     * 期间核查列表
     */
    @Override
    public List<PeriodCheckVo> selectPeriodCheckList(PeriodCheckVo periodCheckVo) {
        QueryWrapper<PeriodCheckVo> wrapper = new QueryWrapper<>();
        if (CharSequenceUtil.isNotBlank(periodCheckVo.getEquipCode())) {
            wrapper.apply("ewr.equip_code = {0}", periodCheckVo.getEquipCode());
        }
        if (CharSequenceUtil.isNotBlank(periodCheckVo.getGoodsName())) {
            wrapper.apply("ewr.goods_name = {0}", periodCheckVo.getGoodsName());
        }
        if (ObjectUtil.isNotNull(periodCheckVo.getVerifyState())) {
            wrapper.apply("ewr.verify_state = {0}", periodCheckVo.getVerifyState());
        }
        wrapper.ne("ewr.status", 6);
        return baseMapper.selectPeriodCheckList(wrapper);
    }

    /**
     * 已核查
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int haveChecked(Long equipId) {
        EquipWarehouseRecord equipWarehouseRecord = new EquipWarehouseRecord();
        equipWarehouseRecord.setId(equipId);
        equipWarehouseRecord.setVerifyState(1);
        equipWarehouseRecord.setVerifyDate(DateUtils.getNowDate());
        equipWarehouseRecord.setUpdateBy(SecurityUtils.getUsernameCn());
        equipWarehouseRecord.setUpdateTime(DateUtil.dateSecond());
        return equipWarehouseRecordMapper.updateById(equipWarehouseRecord);
    }


}
