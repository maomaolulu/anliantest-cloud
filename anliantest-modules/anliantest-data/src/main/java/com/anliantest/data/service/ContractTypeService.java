package com.anliantest.data.service;

import com.anliantest.data.entity.ContractType;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author zhanghao
 * @date 2023-07-28
 * @desc : 合同类型 */
public interface ContractTypeService extends IService<ContractType> {
    /**
     * 合同分类分页列表
     */
    List<ContractType> typeListPage(ContractType contractType);

    /**
     * 二级合同类型下拉框
     */
    List<ContractType> dropDownContractType(String name);


}
