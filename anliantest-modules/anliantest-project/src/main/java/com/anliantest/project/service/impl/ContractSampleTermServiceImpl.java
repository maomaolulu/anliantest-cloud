package com.anliantest.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.anliantest.project.entity.ContractSampleTerm;
import com.anliantest.project.mapper.ContractSampleTermMapper;
import com.anliantest.project.service.ContractSampleTermService;
import org.springframework.stereotype.Service;

/**
 * @author zhanghao
 * @date 2023-10-16
 * @desc : 合同范本与合同条款关联表
 */
@Service
public class ContractSampleTermServiceImpl extends ServiceImpl<ContractSampleTermMapper, ContractSampleTerm> implements ContractSampleTermService {

}