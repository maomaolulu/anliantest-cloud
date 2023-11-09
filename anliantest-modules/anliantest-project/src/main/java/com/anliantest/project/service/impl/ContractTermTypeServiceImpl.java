package com.anliantest.project.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.anliantest.common.core.enums.DeleteFlag;
import com.anliantest.common.core.utils.StringUtils;
import com.anliantest.common.security.utils.SecurityUtils;
import com.anliantest.project.entity.ContractTerm;
import com.anliantest.project.entity.ContractTermType;
import com.anliantest.project.mapper.ContractTermTypeMapper;
import com.anliantest.project.service.ContractTermService;
import com.anliantest.project.service.ContractTermTypeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ZhuYiCheng
 * @date 2023/10/16 9:39
 */
@Service
public class ContractTermTypeServiceImpl extends ServiceImpl<ContractTermTypeMapper, ContractTermType> implements ContractTermTypeService {

    private final ContractTermService contractTermService;

    @Autowired
    public ContractTermTypeServiceImpl(ContractTermService contractTermService){
        this.contractTermService = contractTermService;
    }

    /**
     * 校验唯一性，所属同公司的条款类型名称唯一
     */
    @Override
    public Boolean checkName(ContractTermType contractTermType) {
        String newName = contractTermType.getName();

        List<ContractTermType> list = this.list(new QueryWrapper<ContractTermType>().eq(contractTermType.getCompanyId() != null, "company_id", contractTermType.getCompanyId())
                .eq(StringUtils.isNotBlank(newName), "name", newName)
                .eq("del_flag", DeleteFlag.NO.ordinal()));
        return CollectionUtil.isEmpty(list);
    }

    /**
     * 新增条款类型
     */
    @Override
    public Boolean add(ContractTermType contractTermType) {

        contractTermType.setCreateBy(SecurityUtils.getUsernameCn());
        contractTermType.setUpdateBy(SecurityUtils.getUsernameCn());
        contractTermType.setCreateTime(new Date());
        contractTermType.setUpdateTime(new Date());

        return this.save(contractTermType);
    }


    /**
     * 合同条款类型列表查询
     */
    @Override
    public Map<Long, List<ContractTermType>> getList() {

        //数据权限==>角色判断
        Set<String> roles = SecurityUtils.getLoginUser().getRoles();
        QueryWrapper<ContractTermType> queryWrapper = new QueryWrapper<>();
        Long companyId = SecurityUtils.getLoginUser().getSysUser().getCompanyId();
        if(!roles.contains("contractCenterGroup")&&!SecurityUtils.isAdmin(SecurityUtils.getUserId())){
            queryWrapper.eq("company_id",companyId);
        }

        List<ContractTermType> list = this.list(queryWrapper.eq("del_flag", DeleteFlag.NO.ordinal())
                .orderByAsc("order_num"));
        return list.stream().collect(Collectors.groupingBy(ContractTermType::getCompanyId));
    }


    /**
     * 编辑合同条款类型
     */
    @Override
    public Boolean updateTermType(ContractTermType contractTermType) {

        contractTermType.setUpdateTime(new Date());
        contractTermType.setUpdateBy(SecurityUtils.getUsernameCn());
        return this.updateById(contractTermType);
    }


    /**
     * 校验是否存在关联条款
     */
    @Override
    public Boolean check(Long id) {

        List<ContractTerm> list = contractTermService.list(new QueryWrapper<ContractTerm>()
                .eq(id != null, "term_type_id", id)
                .eq("del_flag", DeleteFlag.NO.ordinal()));

        return CollectionUtil.isEmpty(list);
    }

    /**
     * 删除合同条款类型
     */
    @Override
    public Boolean removeTermType(Long id) {

        return this.update(new UpdateWrapper<ContractTermType>()
                .eq(id != null, "id", id)
                .set("update_time", new Date())
                .set("update_by", SecurityUtils.getUsernameCn())
                .set("del_flag", DeleteFlag.YES.ordinal()));
    }
}
