package com.anliantest.data.mapper;

import com.anliantest.data.domain.dto.ProcessConfigurationDto;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.anliantest.data.entity.ProjectType;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author zhanghao
 * @date 2023-07-17
 * @desc : 项目类型管理表
 */
@Mapper
public interface ProjectTypeMapper extends BaseMapper<ProjectType> {

    @Select(" SELECT pt.*,d.dept_name companyName from data_project_type pt " +
            "left join `anliantest-system`.sys_dept d on d.dept_id=pt.company_id " +
            "${ew.customSqlSegment} " )
    List<ProjectType> projectTypeList(@Param(Constants.WRAPPER) QueryWrapper wrapper);
}
