package com.anliantest.project.service;

import com.anliantest.project.entity.ContractTermType;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @author ZhuYiCheng
 * @date 2023/10/16 9:38
 */

public interface ContractTermTypeService extends IService<ContractTermType> {

    /**
     * 校验唯一性，所属同公司的条款类型名称唯一
     * @param contractTermType 条款类型信息
     * @return result
     */
    Boolean checkName(ContractTermType contractTermType);

    /**
     * 新增条款类型
     * @param contractTermType 条款类型信息
     * @return result
     */
    Boolean add(ContractTermType contractTermType);

    /**
     * 合同条款类型列表查询
     * @return 查询结果
     */
    Map<Long, List<ContractTermType>> getList();

    /**
     * 编辑合同条款类型
     * @param contractTermType 编辑后条款类型信息
     * @return result
     */
    Boolean updateTermType(ContractTermType contractTermType);

    /**
     * 校验是否存在关联条款
     * @param id 条款类型id
     * @return result
     */
    Boolean check(Long id);

    /**
     * 删除合同条款类型
     * @param id 条款类型id
     * @return result
     */
    Boolean removeTermType(Long id);


}
