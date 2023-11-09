package com.anliantest.data.service;

import com.anliantest.data.entity.EquipPurchaseRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Author yrb
 * @Date 2023/8/1 15:02
 * @Version 1.0
 * @Description 仪器设备采购记录服务层
 */
public interface EquipPurchaseRecordService extends IService<EquipPurchaseRecord>{
    /**
     * OA采购信息入库
     *
     * @param equipPurchaseRecord 采购信息
     * @return result
     */
    boolean add(EquipPurchaseRecord equipPurchaseRecord);

    /**
     * 获取采购信息列表
     *
     * @param equipPurchaseRecord 采购信息
     * @return 采购列表
     */
    List<EquipPurchaseRecord> list(EquipPurchaseRecord equipPurchaseRecord);
}
