package com.anliantest.project.service;

import com.anliantest.project.entity.ContractSample;
import com.anliantest.project.entity.ContractSampleTerm;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author zhanghao
 * @date 2023-10-16
 * @desc : 合同范本基础信息表 */
public interface ContractSampleService extends IService<ContractSample> {
    /**
     * 合同范本分页
     * @param contractSample
     * @return
     */
    List<ContractSample> listPage(ContractSample contractSample);
    /**
     * 合同预览分页
     * @param id
     * @return
     */
    List<ContractSampleTerm> previewList(Long id);

    /**
     * 新增合同范本
     * @param contractSample
     * @return
     */
    ContractSample saveContractSample(ContractSample contractSample);

    /**
     * 删除合同范本
     * @param contractSample
     * @return
     */
    void removeContractSample(ContractSample contractSample);
}
