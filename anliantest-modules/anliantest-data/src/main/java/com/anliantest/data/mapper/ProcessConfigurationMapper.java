package com.anliantest.data.mapper;

import com.anliantest.data.domain.dto.ProcessConfigurationDto;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.anliantest.data.entity.ProcessConfiguration;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author zhanghao
 * @date 2023-07-17
 * @desc : 流程配置表
 */
@Mapper
public interface ProcessConfigurationMapper extends BaseMapper<ProcessConfiguration> {

    @Select(" SELECT pc.id,pc.type,pc.subtype,pc.role_ids,pt.id as  project_type_id,pt.company_id,d.dept_name companyName,pt.`code`, " +
            "pt.`name` from date_process_configuration pc " +
            "left join data_project_type pt on pc.project_type_id=pt.id " +
            "left join `anliantest-system`.sys_dept d on d.dept_id=pt.company_id " +
            "${ew.customSqlSegment} " )
    List<ProcessConfigurationDto> processConfigurationDtoListPage(@Param(Constants.WRAPPER) QueryWrapper wrapper);
}
