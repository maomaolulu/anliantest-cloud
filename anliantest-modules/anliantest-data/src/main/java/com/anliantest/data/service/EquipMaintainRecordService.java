package com.anliantest.data.service;

import com.anliantest.data.domain.vo.EquipMaintainVo;
import com.anliantest.data.domain.vo.PeriodCheckVo;
import com.anliantest.data.entity.EquipMaintainRecord;
import com.anliantest.data.entity.EquipWarehouseRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author: liyongqiang
 * @create: 2023-08-07 10:54
 */
public interface EquipMaintainRecordService extends IService<EquipMaintainRecord> {

    /**
     * 设备维修-列表
     * @param equipMaintainVo 查询条件
     * @return 结果
     */
    List<EquipMaintainVo> selectEquipMaintainList(EquipMaintainVo equipMaintainVo);

    /**
     * 新增-维修记录
     * @param maintainRecord 设备维修信息
     * @return rows
     */
    int insertEquipMaintainRecord(EquipMaintainRecord maintainRecord);

    /**
     * 在用设备列表
     * @return 结果
     */
    List<EquipWarehouseRecord> selectEquipUseList();

    /**
     * 删除-维修记录
     * @param id 将要删除的维修记录id
     * @param equipId 将要更新的设备清单表id
     * @return rows
     */
    int deleteEquipMaintainRecordById(Long id, Long equipId);

    /**
     * 编辑-维修记录
     * @param equipMaintainVo 维修vo
     * @return rows
     */
    int updateEquipMaintainRecord(EquipMaintainVo equipMaintainVo);

    /**
     * 设备-完成维修
     * @param equipMaintainVo 维修vo
     * @return rows
     */
    int completeRepair(EquipMaintainVo equipMaintainVo);

    /**
     * 设备维修详情
     * @param id id
     * @return 结果
     */
    EquipMaintainVo selectInfoById(Long id);

    /**
     * 期间核查列表
     * @param periodCheckVo 查询条件
     * @return 结果
     */
    List<PeriodCheckVo> selectPeriodCheckList(PeriodCheckVo periodCheckVo);

    /**
     * 已核查
     * @param equipId 设备id
     * @return rows
     */
    int haveChecked(Long equipId);
}
