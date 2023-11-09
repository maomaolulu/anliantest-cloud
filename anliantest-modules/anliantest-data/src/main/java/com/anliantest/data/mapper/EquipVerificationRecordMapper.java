package com.anliantest.data.mapper;

import com.anliantest.data.domain.vo.EquipVerificationRecordVo;
import com.anliantest.data.entity.EquipVerificationRecord;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author ZhuYiCheng
 * @date 2023/8/7 9:01
 */
@Mapper
public interface EquipVerificationRecordMapper extends BaseMapper<EquipVerificationRecord> {


    @Select("SELECT\n" +
            "\tv.id,\n" +
            "\tv.equip_id,\n" +
            "\tv.inspection_time,\n" +
            "\tv.return_time,\n" +
            "\tv.verification_completion_time,\n" +
            "\tv.useful_time,\n" +
            "\tv.verification_unit,\n" +
            "\tv.certificate_code,\n" +
            "\tv.calibrator,\n" +
            "\tv.correction_factor,\n" +
//            "\tv.attachment_id,\n" +
            "\tv.verification_fee,\n" +
            "\tv.create_by,\n" +
            "\tv.create_time,\n" +
            "\tv.update_by,\n" +
            "\tv.update_time,\n" +
            "\tv.del_flag,\n" +
            "\tw.equip_code,\n" +
            "\tw.goods_name,\n" +
            "\tw.model \n" +
            "FROM\n" +
            "\tequip_verification_record AS v \n" +
            "\tLEFT JOIN equip_warehouse_record AS w ON v.equip_id = w.id \n" +
            "${ew.customSqlSegment}")
    List<EquipVerificationRecordVo> getList(@Param(Constants.WRAPPER) QueryWrapper queryWrapper);

    @Select("SELECT\n" +
            "\tv.id,\n" +
            "\tv.equip_id,\n" +
            "\tv.inspection_time,\n" +
            "\tv.return_time,\n" +
            "\tv.verification_completion_time,\n" +
            "\tv.useful_time,\n" +
            "\tv.verification_unit,\n" +
            "\tv.certificate_code,\n" +
            "\tv.calibrator,\n" +
            "\tv.correction_factor,\n" +
//            "\tv.attachment_id,\n" +
            "\tv.verification_fee,\n" +
            "\tv.create_by,\n" +
            "\tv.create_time,\n" +
            "\tv.update_by,\n" +
            "\tv.update_time,\n" +
            "\tv.del_flag,\n" +
            "\tw.equip_code,\n" +
            "\tw.goods_name,\n" +
            "\tw.model \n" +
            "FROM\n" +
            "\tequip_verification_record AS v \n" +
            "\tLEFT JOIN equip_warehouse_record AS w ON v.equip_id = w.id \n" +
            "where v.id = #{id}")
    EquipVerificationRecordVo getInfo(@Param("id") Long id);
}
