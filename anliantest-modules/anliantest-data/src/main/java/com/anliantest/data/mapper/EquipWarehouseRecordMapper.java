package com.anliantest.data.mapper;

import com.anliantest.data.domain.dto.EquipWarehouseRecordDto;
import com.anliantest.data.entity.EquipWarehouseRecord;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

/**
 * @Author yrb
 * @Date 2023/8/1 16:35
 * @Version 1.0
 * @Description 仪器设备入库记录dao层
 */
@Mapper
public interface EquipWarehouseRecordMapper extends BaseMapper<EquipWarehouseRecord> {

    @Select(" SELECT wr.id,wr.goods_name,wr.equip_code,wr.model,wr.equip_type,wr.company_id, " +
            "wr.charge_dept_id,wr.charge_id, wr.print_label,wr.create_time,wr.create_name, " +
            "u.nick_name chargeName  " +
            "FROM `anliantest-data`.`equip_warehouse_record` wr " +
            "left join `anliantest-system`.sys_user u on u.user_id=wr.charge_id " +
            "left join `anliantest-system`.sys_dept d on d.dept_id=wr.company_id " +
            " ${ew.customSqlSegment} ")
    List<EquipWarehouseRecordDto> equipWarehouseRecordList(@Param(Constants.WRAPPER) QueryWrapper wrapper);

    @Select(" select t1.* from `anliantest-data`.equip_warehouse_record t1 " +
            " left join `anliantest-system`.sys_dept t2 on t1.company_id = t2.dept_id "+
            " ${ew.customSqlSegment} ")
    List<EquipWarehouseRecord> getInfo(@Param(Constants.WRAPPER) QueryWrapper wrapper);
    /**
     * 根据id更新设备清单表
     * @param status 设备状态
     * @param updateBy 更新者
     * @param updateTime 更新时间
     * @param id 主键id
     * @return row
     */
    @Update("update equip_warehouse_record set `status` = #{status} , update_by = #{updateBy} , update_time = #{updateTime} where id = #{id}; ")
    int updateStatusById(@Param("status") Integer status, @Param("updateBy") String updateBy, @Param("updateTime")Date updateTime, @Param("id") Long id);

    /**
     * 校验设备编号唯一性
     *
     * @param equipCode 设备编号
     * @return 数量
     */
    @Select("select count(*) from equip_warehouse_record where equip_code = #{equipCode} ")
    int checkEuipCodeUnique(@Param("equipCode") String equipCode);

    /**
     * 校验标签编号唯一性
     *
     * @param labelCode 打印条码
     * @return 数量
     */
    @Select("select count(*) from equip_warehouse_record where label_code = #{labelCode} ")
    int checkLabelCodeUnique(@Param("labelCode") String labelCode);

    /**
     * 更新标签打印状态
     *
     * @param equipCode 设备编号
     * @return result
     */
    @Update("update equip_warehouse_record set print_label =1 where equip_code = #{equipCode} ")
    int updatePrintLabel(@Param("equipCode") String equipCode);
}
