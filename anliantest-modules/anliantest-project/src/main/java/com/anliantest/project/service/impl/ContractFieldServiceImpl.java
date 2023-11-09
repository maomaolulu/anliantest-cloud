package com.anliantest.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.anliantest.project.entity.ContractField;
import com.anliantest.project.mapper.ContractFieldMapper;
import com.anliantest.project.service.ContractFieldService;
import org.springframework.stereotype.Service;

/**
 * @author zhanghao
 * @date 2023-10-16
 * @desc : 合同条款字段库
 */
@Service
public class ContractFieldServiceImpl extends ServiceImpl<ContractFieldMapper, ContractField> implements ContractFieldService {

}