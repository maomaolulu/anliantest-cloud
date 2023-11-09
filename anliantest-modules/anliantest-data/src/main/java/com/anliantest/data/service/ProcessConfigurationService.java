package com.anliantest.data.service;

import com.anliantest.data.domain.dto.ProcessConfigurationDto;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.anliantest.data.entity.ProcessConfiguration;

import java.util.List;

/**
 * @author zhanghao
 * @date 2023-07-17
 * @desc : 流程配置表 */
public interface ProcessConfigurationService extends IService<ProcessConfiguration> {

    /**
     *  流程配置分页列表
     * @param processConfigurationDto
     * @return
     */
     List<ProcessConfigurationDto> processConfigurationList(ProcessConfigurationDto processConfigurationDto);
}
