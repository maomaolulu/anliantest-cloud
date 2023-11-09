package com.anliantest.data.service;

import com.anliantest.data.domain.dto.SubstanceTestMethodDto;
import com.anliantest.data.entity.SubstanceTestMethodEntity;
import com.baomidou.mybatisplus.extension.service.IService;


import java.util.List;

/**
 * @author gy
 * @date 2023-06-06
 */
public interface SubstanceTestMethodService extends IService<SubstanceTestMethodEntity> {
    /**
     * 检测方法分页列表
     * @param substanceTestMethodDto
     * @return
     */
    List<SubstanceTestMethodDto> pageList(SubstanceTestMethodDto substanceTestMethodDto);

    /**
     * 检测方法详情
     * @param id
     * @return
     */
    SubstanceTestMethodDto info(Long id);
}
