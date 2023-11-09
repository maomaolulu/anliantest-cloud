package com.anliantest.data.mapper;

import com.anliantest.data.entity.EquipPurchaseRecord;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author yrb
 * @Date 2023/8/1 16:28
 * @Version 1.0
 * @Description 仪器设备采购记录dao层
 */
@Mapper
public interface EquipPurchaseRecordMapper extends BaseMapper<EquipPurchaseRecord> {
    @Select(" select t1.*,t2.dept_name as company_name,t3.dept_name from `anliantest-data`.equip_purchase_record t1 " +
            " left join `anliantest-system`.sys_dept t2 on t1.company_id = t2.dept_id " +
            " left join `anliantest-system`.sys_dept t3 on t1.dept_id = t3.dept_id " +
            " ${ew.customSqlSegment} ")
    List<EquipPurchaseRecord> selectInfo(@Param(Constants.WRAPPER) QueryWrapper wrapper);
}
