package com.anliantest.project.service;

import com.anliantest.project.domain.dto.ContractTermDto;
import com.anliantest.project.entity.ContractTerm;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author zhanghao
 * @date 2023-10-16
 * @desc : 合同条款表
 */
public interface ContractTermService extends IService<ContractTerm> {

    /**
     * 新增条款
     * @param contractTermDto 条款以及关联字段信息
     * @return result
     */
    Boolean add(ContractTermDto contractTermDto);

    /**
     * 条款列表查询
     * @param contractTermDto 查询条件
     * @return 查询结果
     */
    List<ContractTerm> getList(ContractTermDto contractTermDto);

    /**
     * 根据条款类型查询条款分页
     * @param contractTerm  查询条件
     * @return 查询结果
     */
    List<ContractTerm> listByTermTypeId(ContractTerm contractTerm);

    /**
     * 条款详情查询
     * @param id 条款id
     * @return 查询结果
     */
    ContractTerm getInfo(Long id);

    /**
     * 编辑条款
     * @param contractTermDto 新条款信息
     * @return result
     */
    Boolean updateTerm(ContractTermDto contractTermDto);

    /**
     * 编辑条款状态，停用/启用
     * @param contractTermDto 条款id以及条款状态
     * @return result
     */
    Boolean updateStatus(ContractTermDto contractTermDto);

    /**
     * 校验是否存在关联合同范本
     * @param id 条款id
     * @return result
     */
    Boolean check(Long id);

    /**
     * 删除条款
     * @param id 条款id
     * @return result
     */
    Boolean removeTerm(Long id);


}
