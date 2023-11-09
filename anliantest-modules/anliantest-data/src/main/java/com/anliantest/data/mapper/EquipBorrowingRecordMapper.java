package com.anliantest.data.mapper;

import com.anliantest.data.entity.EquipBorrowingRecord;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * @author zhanghao
 * @date 2023-08-04
 * @desc : 设备借用记录
 */
@Mapper
public interface EquipBorrowingRecordMapper extends BaseMapper<EquipBorrowingRecord> {

    @Select(" select DISTINCT equip_id from equip_borrowing_record  " +
            "where type=0 and ( (start_time> #{startTime } and start_time<#{endTime })  " +
            "or (actual_end_time> #{startTime } and actual_end_time<#{endTime })  " +
            "or (start_time=#{startTime } and actual_end_time=#{endTime }) ) ")
    List<Long> equipIdList(@Param("startTime")Date startTime,@Param("endTime") Date endTime);

    @Select(" select wr.equip_code,wr.goods_name,wr.company_id internalCompanyId,br.* from equip_borrowing_record  br\n" +
            "left join equip_warehouse_record wr on  wr.id=br.equip_id " +
            "${ew.customSqlSegment}  ")
    List<EquipBorrowingRecord> listPage(@Param(Constants.WRAPPER) QueryWrapper wrapper);
    @Select(" select wr.equip_code,wr.goods_name,wr.company_name,br.* from equip_borrowing_record  br\n" +
            "left join equip_external wr on  wr.id=br.equip_id " +
            "${ew.customSqlSegment}  ")
    List<EquipBorrowingRecord> externalListPage(@Param(Constants.WRAPPER) QueryWrapper wrapper);
}
