package com.anliantest.data.service.impl;

import cn.hutool.core.util.StrUtil;
import com.anliantest.common.datascope.util.DataScopeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.anliantest.data.entity.ProjectType;
import com.anliantest.data.mapper.ProjectTypeMapper;
import com.anliantest.data.service.ProjectTypeService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhanghao
 * @date 2023-07-17
 * @desc : 项目类型管理表
 */
@Service
public class ProjectTypeServiceImpl extends ServiceImpl<ProjectTypeMapper, ProjectType> implements ProjectTypeService {
    /**
     * 项目类型管理分页列表
     * @param projectType
     * @return
     */
    @Override
    public List<ProjectType> projectTypeList(ProjectType projectType) {

        String d = DataScopeUtil.getScopeSql("d" , "");
        QueryWrapper<ProjectType> wrapper = new QueryWrapper<ProjectType>()
                //所属公司
                .eq(projectType.getCompanyId() != null, "pt.company_id" , projectType.getCompanyId())
                //类型编号
                .like(StrUtil.isNotBlank(projectType.getCode()), "pt.code" , projectType.getCode())
                //类型名称
                .like(StrUtil.isNotBlank(projectType.getName()), "pt.name" , projectType.getName())
                //状态
                .eq(projectType.getStatus() != null, "pt.status" , projectType.getStatus()
                );
        wrapper.apply(StrUtil.isNotBlank(d),d);

        return baseMapper.projectTypeList(wrapper);
    }
}
