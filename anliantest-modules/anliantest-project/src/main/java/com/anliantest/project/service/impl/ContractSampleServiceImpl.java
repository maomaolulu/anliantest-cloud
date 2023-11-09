package com.anliantest.project.service.impl;

import cn.hutool.core.util.StrUtil;
import com.anliantest.common.datascope.util.DataScopeUtil;
import com.anliantest.common.security.utils.SecurityUtils;
import com.anliantest.project.entity.ContractSample;
import com.anliantest.project.entity.ContractSampleTerm;
import com.anliantest.project.mapper.ContractSampleMapper;
import com.anliantest.project.service.ContractSampleService;
import com.anliantest.project.service.ContractSampleTermService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * @author zhanghao
 * @date 2023-10-16
 * @desc : 合同范本基础信息表
 */
@Service
public class ContractSampleServiceImpl extends ServiceImpl<ContractSampleMapper, ContractSample> implements ContractSampleService {

    private final ContractSampleTermService contractSampleTermService;
    @Autowired
    public ContractSampleServiceImpl(ContractSampleTermService contractSampleTermService) {
        this.contractSampleTermService = contractSampleTermService;
    }

    @Override
    public List<ContractSample> listPage(ContractSample contractSample) {
        Set<String> roles = SecurityUtils.getLoginUser().getRoles();
        Long companyId = SecurityUtils.getLoginUser().getSysUser().getCompanyId();

        QueryWrapper<ContractSample> wrapper = new QueryWrapper<>();
        //数据管理员-集团
        if(!roles.contains("contractCenterGroup")&&!SecurityUtils.isAdmin(SecurityUtils.getUserId())){
            wrapper.eq("cs.company_id",companyId);
        }

        //所属公司
        wrapper.eq(contractSample.getCompanyId()!=null,"cs.company_id",contractSample.getCompanyId());
        //合同类型
        wrapper.eq(contractSample.getContractTypeId()!=null,"cs.contract_type_id",contractSample.getContractTypeId());
        //合同范本名称
        wrapper.like(StrUtil.isNotBlank(contractSample.getName()),"cs.name",contractSample.getName());
        //状态
        wrapper.eq(contractSample.getStatus()!=null,"cs.company_id",contractSample.getStatus());
        wrapper.eq("cs.del_flag",0);
        wrapper.orderByDesc("cs.id");



        return baseMapper.listPage(wrapper);
    }

    @Override
    public List<ContractSampleTerm> previewList(Long id) {

        List<ContractSampleTerm> contractSampleTerms = baseMapper.previewList(id);

        return contractSampleTerms;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ContractSample saveContractSample(ContractSample contractSample) {
        this.save(contractSample);

        List<ContractSampleTerm> contractSampleTerms = contractSample.getContractSampleTerms();
        int sort=1;
        for (ContractSampleTerm contractSampleTerm : contractSampleTerms) {
            contractSampleTerm.setId(0L);
            contractSampleTerm.setContractSampleId(contractSample.getId());
            contractSampleTerm.setSort(sort);
            sort++;
        }
        contractSampleTermService.saveBatch(contractSampleTerms);

        return contractSample;
    }

    @Override
    @Transactional
    public void removeContractSample(ContractSample contractSample) {
        this.removeById(contractSample.getId());
        contractSampleTermService.remove(new QueryWrapper<ContractSampleTerm>().eq("contract_sample_id",contractSample.getId()));
    }
}
