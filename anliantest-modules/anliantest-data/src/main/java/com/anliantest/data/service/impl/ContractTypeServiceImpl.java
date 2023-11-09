package com.anliantest.data.service.impl;

import cn.hutool.core.util.StrUtil;
import com.anliantest.common.datascope.util.DataScopeUtil;
import com.anliantest.common.security.utils.SecurityUtils;
import com.anliantest.data.entity.ContractType;
import com.anliantest.data.mapper.ContractTypeMapper;
import com.anliantest.data.service.ContractTypeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @author zhanghao
 * @date 2023-07-28
 * @desc : 合同类型
 */
@Service
public class ContractTypeServiceImpl extends ServiceImpl<ContractTypeMapper, ContractType> implements ContractTypeService {

    /**
     * 合同分类分页列表
     * @param contractType
     * @return
     */
    @Override
    public List<ContractType> typeListPage(ContractType contractType) {
        String d = DataScopeUtil.getScopeSql("d" , "");
        QueryWrapper<ContractType> wrapper = new QueryWrapper<>();
        wrapper.eq("co.pid", contractType.getPid());
        //公司id
        wrapper.eq(contractType.getCompanyId()!=null,"co.company_id",contractType.getCompanyId());
        //事业部id
        wrapper.eq(contractType.getBusinessUnitId()!=null,"co.business_unit_id",contractType.getBusinessUnitId());
        //一二及合同名称
        wrapper.like(StrUtil.isNotBlank(contractType.getName()),"co.name",contractType.getName());
        //编号
        wrapper.like(StrUtil.isNotBlank(contractType.getCode()),"co.code",contractType.getCode());
        //状态
        wrapper.eq(contractType.getStatus()!=null,"co.status",contractType.getStatus());
        //数据权限
        wrapper.apply(StrUtil.isNotBlank(d),d);
        List<ContractType> contractTypes = baseMapper.oneTypeListPage(wrapper);

        return contractTypes;

    }

    @Override
    public List<ContractType> dropDownContractType(String name) {
        Set<String> roles = SecurityUtils.getLoginUser().getRoles();
        Long companyId = SecurityUtils.getLoginUser().getSysUser().getCompanyId();

        QueryWrapper<ContractType> wrapper = new QueryWrapper<>();
        //数据管理员-集团
        if(!roles.contains("contractCenterGroup")&&!SecurityUtils.isAdmin(SecurityUtils.getUserId())){
            wrapper.eq("company_id",companyId);
        }

        //二级合同类型下拉
        wrapper.ne("pid", 0);
        //二及合同名称
        wrapper.like(StrUtil.isNotBlank(name),"name",name);

        List<ContractType> list = this.list(wrapper);

        return list;
    }

}
