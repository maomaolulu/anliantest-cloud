package com.anliantest.data.service;

import com.anliantest.data.domain.dto.EquipWarehouseRecordDto;
import com.anliantest.data.entity.EquipWarehouseRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.anliantest.data.entity.EquipBorrowingRecord;

import java.util.List;

/**
 * @author zhanghao
 * @date 2023-08-04
 * @desc : 设备借用记录 */
public interface EquipBorrowingRecordService extends IService<EquipBorrowingRecord> {
    /**
     * 设备借出列表
     * @param equipWarehouseRecordDto
     * @return
     */
    List<EquipWarehouseRecord>  lendListPage(EquipWarehouseRecordDto equipWarehouseRecordDto);
    /**
     * 设备借用记录分页
     * @param equipBorrowingRecord
     * @return
     */
    List<EquipBorrowingRecord>  listPage(EquipBorrowingRecord equipBorrowingRecord);
    /**
     * 外部设备借入记录分页
     * @param equipBorrowingRecord
     * @return
     */
    List<EquipBorrowingRecord>  externalListPage(EquipBorrowingRecord equipBorrowingRecord);

}
