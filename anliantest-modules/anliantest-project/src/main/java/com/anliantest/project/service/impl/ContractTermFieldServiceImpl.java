package com.anliantest.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.anliantest.project.entity.ContractTermField;
import com.anliantest.project.mapper.ContractTermFieldMapper;
import com.anliantest.project.service.ContractTermFieldService;
import org.springframework.stereotype.Service;

/**
 * @author zhanghao
 * @date 2023-10-16
 * @desc : 合同条款与字段关联表
 */
@Service
public class ContractTermFieldServiceImpl extends ServiceImpl<ContractTermFieldMapper, ContractTermField> implements ContractTermFieldService {

}