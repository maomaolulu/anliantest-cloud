package com.anliantest.data.mapper;

import com.anliantest.data.domain.dto.SubstanceTestMethodDto;
import com.anliantest.data.entity.SubstanceTestMethodEntity;
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
public interface SubstanceTestMethodMapper extends BaseMapper<SubstanceTestMethodEntity> {

    @Select(" SELECT sm.id,sm.substance_test_law_id,sm.substance_info_id,sm.test_number,sm.test_name,sm.is_direct_reading,\n" +
            "sm.is_area_sample,sm.is_personal_sample,sm.fixed_sample_traffic,sm.fixed_sample_duration,sm.fixed_sample_note,\n" +
            "sm.fixed_sample_equipment,sm.fixed_collector,sm.personal_sample_traffic,sm.personal_sample_duration,\n" +
            "sm.personal_sample_note,sm.personal_sample_equipment,sm.personal_collector,sm.blank_sample,sm.save_method,\n" +
            "sm.save_term,sm.save_requirement,sm.absorbent_solution,sm.detection_limit,sm.limit_of_detection,\n" +
            "sm.standard_sampling_volume,sm.explanation_of_detection_limit,sm.calculation,sm.decimal_places,su.substance_name,\n" +
            "st.test_category,st.test_standards,st.test_standards_name,st.implementation_date ,st.abolition_date,st.`status` \n" +
            "FROM `substance_test_method`   sm\n" +
            "left join  substance_test_law st on sm.substance_test_law_id=st.id\n" +
            "left join  substance_info  su on  sm.substance_info_id =su.id  " +
            " ${ew.customSqlSegment} ")
    List<SubstanceTestMethodDto> substanceTestMethodList(@Param(Constants.WRAPPER) QueryWrapper wrapper);

    /**
     * 详情
     * @param id
     * @return
     */
    @Select(" SELECT sm.id,sm.substance_test_law_id,sm.substance_info_id,sm.test_number,sm.test_name,sm.is_direct_reading,\n" +
            "sm.is_area_sample,sm.is_personal_sample,sm.fixed_sample_traffic,sm.fixed_sample_duration,sm.fixed_sample_note,\n" +
            "sm.fixed_sample_equipment,sm.fixed_collector,sm.personal_sample_traffic,sm.personal_sample_duration,\n" +
            "sm.personal_sample_note,sm.personal_sample_equipment,sm.personal_collector,sm.blank_sample,sm.save_method,\n" +
            "sm.save_term,sm.save_requirement,sm.absorbent_solution,sm.detection_limit,sm.limit_of_detection,\n" +
            "sm.standard_sampling_volume,sm.explanation_of_detection_limit,sm.calculation,sm.decimal_places,su.substance_name,\n" +
            "st.test_category,st.test_standards,st.test_standards_name,st.implementation_date ,st.abolition_date,st.`status` \n" +
            "FROM `substance_test_method`   sm\n" +
            "left join  substance_test_law st on sm.substance_test_law_id=st.id\n" +
            "left join  substance_info  su on  sm.substance_info_id =su.id  " +
            " where sm.id=#{id } ")
    SubstanceTestMethodDto info(Long id);

}
