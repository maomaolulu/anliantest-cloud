package com.anliantest.data.service.impl;

import cn.hutool.core.util.StrUtil;
import com.anliantest.data.domain.dto.SubstanceTestMethodDto;
import com.anliantest.data.entity.SubstanceTestMethodEntity;
import com.anliantest.data.mapper.SubstanceTestMethodMapper;
import com.anliantest.data.service.SubstanceTestMethodService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author gy
 * @date 2023-06-06
 */
@Service
public class SubstanceTestMethodServiceImpl extends ServiceImpl<SubstanceTestMethodMapper, SubstanceTestMethodEntity> implements SubstanceTestMethodService {

    /**
     * 检测方法分页列表
     * @param substanceTestMethodDto
     * @return
     */
    @Override
    public List<SubstanceTestMethodDto> pageList(SubstanceTestMethodDto substanceTestMethodDto) {


        List<SubstanceTestMethodDto> list = baseMapper.substanceTestMethodList(new QueryWrapper<Object>()
                //危害名称
                .like(StrUtil.isNotBlank(substanceTestMethodDto.getSubstanceName()), "su.substance_name", substanceTestMethodDto.getSubstanceName())
                //检测对象/类别
                .eq(substanceTestMethodDto.getTestCategory()!=null, "st.test_category", substanceTestMethodDto.getSubstanceName())
                //标准号
                .like(StrUtil.isNotBlank(substanceTestMethodDto.getTestStandards()), "st.test_standards", substanceTestMethodDto.getSubstanceName())
                //标准名称
                .like(StrUtil.isNotBlank(substanceTestMethodDto.getTestStandardsName()), "st.test_standards_name", substanceTestMethodDto.getSubstanceName())
                //状态 (1现行，0废止，2发布)',
                .eq(substanceTestMethodDto.getStatus()!=null, "st.`status`", substanceTestMethodDto.getStatus())
                .orderByDesc("id")
        );

        return list;
    }

    /**
     * 检测方法详情
     * @param id
     * @return
     */
    @Override
    public SubstanceTestMethodDto info(Long id) {

        return baseMapper.info(id);
    }
}
