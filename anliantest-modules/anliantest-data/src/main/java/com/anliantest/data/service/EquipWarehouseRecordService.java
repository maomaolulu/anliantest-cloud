package com.anliantest.data.service;

import com.anliantest.data.domain.dto.EquipWarehouseRecordDto;
import com.anliantest.data.entity.EquipWarehouseRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Author yrb
 * @Date 2023/8/1 16:33
 * @Version 1.0
 * @Description 仪器设备入库记录服务层
 */
public interface EquipWarehouseRecordService extends IService<EquipWarehouseRecord> {
    /**
     * 设备入库
     *
     * @return result
     */
    int add(EquipWarehouseRecord equipWarehouseRecord);

    /**
     * 获取入库列表
     *
     * @param equipWarehouseRecord 入库信息
     * @return 入库列表
     */
    List<EquipWarehouseRecord> list(EquipWarehouseRecord equipWarehouseRecord);

    /**
     * 设备清单
     *
     * @param equipWarehouseRecordDto 设备清单
     * @return 设备清单
     */
    List<EquipWarehouseRecordDto> equipWarehouseRecordList(EquipWarehouseRecordDto equipWarehouseRecordDto);

    /**
     * 获取应检定设备列表
     *
     */
    List<EquipWarehouseRecord> getVerificationList(EquipWarehouseRecord equipWarehouseRecord);

    /**
     * 设备详情
     */
    EquipWarehouseRecord getInfo(Long id);

    /**
     * 校验设备编号唯一性
     *
     * @param equipCode 设备编号
     * @return 数量
     */
    int checkEuipCodeUnique(String equipCode);

    /**
     * 更新标签打印状态
     *
     * @param equipCode 设备编号
     * @return result
     */
    int updatePrintLabel(String equipCode);

    /**
     * 同步修改OA仪器设备信息（新增）
     *
     * @param equipWarehouseRecord 仪器设备信息
     * @return result
     */
    int synOaEquipInfo(EquipWarehouseRecord equipWarehouseRecord);

    /**
     * 同步修改OA仪器设备信息（修改）
     *
     * @param equipWarehouseRecord 仪器设备信息
     * @return result
     */
    int updateEquipInfo(EquipWarehouseRecord equipWarehouseRecord);
}
