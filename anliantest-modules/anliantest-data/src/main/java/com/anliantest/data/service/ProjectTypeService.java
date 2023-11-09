package com.anliantest.data.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.anliantest.data.entity.ProjectType;

import java.util.List;

/**
 * @author zhanghao
 * @date 2023-07-17
 * @desc : 项目类型管理表 */
public interface ProjectTypeService extends IService<ProjectType> {
    /**
     * 项目类型管理分页列表
     * @param projectType
     * @return
     */
    List<ProjectType> projectTypeList(ProjectType projectType);

}
