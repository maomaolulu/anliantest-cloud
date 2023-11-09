package com.anliantest.data.mapper;

import com.anliantest.data.entity.SubstanceStateLimitEntity;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author gy
 * @date 2023-06-06
 */
@Mapper
public interface SubstanceStateLimitMapper extends BaseMapper<SubstanceStateLimitEntity> {

    @Select("SELECT\n" +
            "\tss.id,\n" +
            "\tss.substance_info_id,\n" +
            "\tsi.substance_type,\n" +
            "\tss.condition_note,\n" +
            "\tss.mac,\n" +
            "\tss.pc_twa,\n" +
            "\tss.pc_stel,\n" +
            "\tss.adverse_reactions,\n" +
            "\tss.remark,\n" +
            "\tss.noise_time_frequency,\n" +
            "\tss.noise_peak_value,\n" +
            "\tss.contact_time_rate,\n" +
            "\tss.labor_intensity_one,\n" +
            "\tss.labor_intensity_two,\n" +
            "\tss.labor_intensity_three,\n" +
            "\tss.labor_intensity_four,\n" +
            "\tss.irradiance,\n" +
            "\tss.exposure,\n" +
            "\tss.daily_dose,\n" +
            "\tss.average_power_density,\n" +
            "\tss.non_average_power_density,\n" +
            "\tss.short_power_density,\n" +
            "\tss.contact_time,\n" +
            "\tss.acceleration,\n" +
            "\tss.frequency,\n" +
            "\tss.electric_field_intensity,\n" +
            "\tss.magnetic_field_intensity,\n" +
            "\tss.power_density,\n" +
            "\tss.laser_wavelength,\n" +
            "\tss.laser_exposure_time,\n" +
            "\tss.laser_exposure_site,\n" +
            "\tss.laser_exposure,\n" +
            "\tss.laser_irradiance \n" +
            "FROM\n" +
            "\tsubstance_state_limit ss\n" +
            "\tLEFT JOIN substance_info si ON ss.substance_info_id = si.id \n" +
            "${ew.customSqlSegment}")
    List<SubstanceStateLimitEntity> getListBySubstanceInfoId(@Param(Constants.WRAPPER) QueryWrapper<SubstanceStateLimitEntity> queryWrapper);
}
