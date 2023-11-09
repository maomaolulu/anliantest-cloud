package com.anliantest.project.service.impl;

import cn.hutool.core.exceptions.StatefulException;
import com.anliantest.common.core.enums.DeleteFlag;
import com.anliantest.common.core.utils.ObjectUtils;
import com.anliantest.common.core.utils.StringUtils;
import com.anliantest.common.security.utils.SecurityUtils;
import com.anliantest.project.domain.dto.CustomCustomerDto;
import com.anliantest.project.domain.dto.RoleAndCompanyDto;
import com.anliantest.project.domain.vo.CompanyAndContactVo;
import com.anliantest.project.domain.vo.CompanyPublicVo;
import com.anliantest.project.domain.vo.CustomCustomerVo;
import com.anliantest.project.entity.CustomAdvanceTaskEntity;
import com.anliantest.project.entity.CustomContacterEntity;
import com.anliantest.project.entity.CustomCustomerEntity;
import com.anliantest.project.mapper.CustomCustomerMapper;
import com.anliantest.project.service.CustomAdvanceTaskService;
import com.anliantest.project.service.CustomContacterService;
import com.anliantest.project.service.CustomCustomerService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gy
 * @date 2023-08-18 17:00
 */
@Service
public class CustomCustomerServiceImpl extends ServiceImpl<CustomCustomerMapper, CustomCustomerEntity> implements CustomCustomerService {

    @Autowired
    private CustomContacterService customContacterService;

    @Autowired
    private CustomAdvanceTaskService customAdvanceTaskService;


    @Override
    public List<CustomCustomerVo> listWithPage(CustomCustomerDto dto) {
        QueryWrapper<Object> queryWrapper = new QueryWrapper<>();
        checkRoles(queryWrapper);
        if (dto.getBusinessStatus() != null){
            if (dto.getBusinessStatus() == 0){
                queryWrapper.and(q -> q.eq("cat.business_status", 0).or().isNull("cat.business_status"));
            }else {
                queryWrapper.eq("cat.business_status", dto.getBusinessStatus());
            }
        }
        queryWrapper.eq(StringUtils.isNotBlank(dto.getCustomerOrder()), "IF(cc.if_new_company = 0 && cc.customer_order != '',cc.customer_order,cc.customer_company)", dto.getCustomerOrder());
        queryWrapper.eq(StringUtils.isNotBlank(dto.getEnterpriseName()), "cc.enterprise_name", dto.getEnterpriseName());
        queryWrapper.eq(StringUtils.isNotBlank(dto.getProvince()), "cc.province", dto.getProvince());
        queryWrapper.eq(StringUtils.isNotBlank(dto.getCity()), "cc.city", dto.getCity());
        queryWrapper.eq(StringUtils.isNotBlank(dto.getDistrict()), "cc.district", dto.getDistrict());
        queryWrapper.eq(dto.getCustomerStatus() != null, "cc.customer_status", dto.getCustomerStatus());
        // TODO: 2023/8/21 合同表加入后添加相关属性
//        queryWrapper.ge(dto.getAdvanceTimeStart() != null, "合同.签订时间", dto.getAdvanceTimeStart());
//        queryWrapper.le(dto.getAdvanceTimeEnd() != null, "合同.签订时间", dto.getAdvanceTimeEnd());
        queryWrapper.orderByDesc("cc.customer_status");
//        queryWrapper.orderByAsc("合同.签订时间");
        queryWrapper.eq("cc.delete_flag", DeleteFlag.NO.ordinal());
        return baseMapper.listWithPage(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean newCustomer(CustomCustomerDto dto) {
        CustomCustomerEntity entity = ObjectUtils.transformObj(dto, CustomCustomerEntity.class);
        entity.setCustomerOrder(SecurityUtils.getUsernameCn());
        boolean a = this.save(entity);
        List<CustomContacterEntity> contacts = dto.getContacts();
        contacts.forEach(customContacterEntity -> customContacterEntity.setCustomerId(entity.getId()));
        if (createTask(entity.getId(), dto.getBusinessPeopleId()) == 0) {
            throw new StatefulException("创建任务失败");
        }
        return a && customContacterService.saveBatch(contacts);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateCustomer(CustomCustomerDto dto) {
        CustomCustomerEntity entity = ObjectUtils.transformObj(dto, CustomCustomerEntity.class);
        List<CustomContacterEntity> contacts = dto.getContacts();
        contacts.forEach(customContacterEntity -> customContacterEntity.setCustomerId(entity.getId()));
        customContacterService.remove(new QueryWrapper<CustomContacterEntity>().eq("customer_id", entity.getId()));
        return this.updateById(entity) && customContacterService.saveBatch(contacts);
    }

    @Override
    public Boolean checkIfRelate(Long customerId) {
        List<CustomAdvanceTaskEntity> list1 = customAdvanceTaskService.list(new QueryWrapper<CustomAdvanceTaskEntity>().eq("custom_id", customerId));
        // TODO: 2023/8/21 检查是否有对应合同 以条件或加入到判断中去
        return list1.size() > 0;
    }

    @Override
    public void deleteCustomer(Long customerId) {
        CustomCustomerEntity entity = new CustomCustomerEntity();
        entity.setDeleteFlag(DeleteFlag.YES.ordinal());
        this.update(entity, new QueryWrapper<CustomCustomerEntity>().eq("id", customerId));
    }

    @Override
    public CompanyAndContactVo getCompanyAndContact(Long customerId) {
        CompanyAndContactVo companyAndContact = baseMapper.getCompanyAndContact(customerId);
        companyAndContact.setContacts(customContacterService.list(new QueryWrapper<CustomContacterEntity>().eq("customer_id", customerId)));
        return companyAndContact;
    }

    /**
     * 创建任务
     *
     * @param customId     客户ID
     * @param businessorId 业务员ID
     * @return 结果
     */
    private int createTask(Long customId, Long businessorId) {
        CustomAdvanceTaskEntity customAdvanceTaskEntity = new CustomAdvanceTaskEntity();
        customAdvanceTaskEntity.setCustomId(customId);
        if (businessorId == null) {
            // 待分配
            customAdvanceTaskEntity.setBusinessStatus(0);
        } else {
            // 待跟进
            customAdvanceTaskEntity.setAdvanceId(businessorId);
            customAdvanceTaskEntity.setBusinessStatus(1);
        }
        return customAdvanceTaskService.add(customAdvanceTaskEntity);
    }

    /**
     * 根据角色分流
     */
    private void checkRoles(QueryWrapper<Object> queryWrapper) {
        List<RoleAndCompanyDto> racs = getRoleAndCompany(SecurityUtils.getUserId());
        String salesMan = "company_salesman", director = "company_director", manager = "company_manager", company = "", admin = "admin", administrators = "administrators";
        List<String> roles = new ArrayList<>();
        if (racs.size() > 0) {
            company = racs.get(0).getDictLabel();
        }
        for (RoleAndCompanyDto rac : racs) {
            roles.add(rac.getRoleKey());
        }

        if (roles.contains(admin) || roles.contains(administrators)){
            return;
        }

        if (roles.contains(salesMan)) {
            String finalCompany = company;
            queryWrapper.and(i -> i.eq("IF(cc.if_new_company = 0 && cc.customer_order != '',cc.customer_order,cc.customer_company)", SecurityUtils.getUsernameCn())
                    .or().eq("IF(cc.if_new_company = 0 && cc.customer_order != '',cc.customer_order,cc.customer_company)", finalCompany));
        } else if (roles.contains(manager)) {
            queryWrapper.eq("cc.customer_company", company);
        }
    }

    /**
     * 根据角色分流
     */
    @Override
    public List<RoleAndCompanyDto> getRoleAndCompany(Long userId){
        return baseMapper.getRoleAndCompany(userId);
    }

    @Override
    public List<CompanyPublicVo> selectPublicCompany(CustomCustomerDto dto, String companyOrder) {
        // TODO: 2023/11/1 合同模块开发后填充最近合作日期字段
        QueryWrapper<Object> query = new QueryWrapper<>();
        query.eq(StringUtils.isNotBlank(companyOrder), "cc.customer_company",companyOrder);
        query.eq("cc.customer_status", 1);
        query.eq("cc.if_new_company", 1);
        query.and(a -> a.eq("cat.new_status", 10).or().isNull("cat.new_status"));
        query.like(StringUtils.isNotBlank(dto.getCustomerCompany()), "cc.customer_company", dto.getCustomerCompany());
        query.like(StringUtils.isNotBlank(dto.getEnterpriseName()), "cc.enterprise_name", dto.getEnterpriseName());
        query.eq(StringUtils.isNotBlank(dto.getProvince()), "cc.province", dto.getProvince());
        query.eq(StringUtils.isNotBlank(dto.getCity()), "cc.city", dto.getCity());
        query.eq(StringUtils.isNotBlank(dto.getDistrict()), "cc.district", dto.getDistrict());
        query.ge(dto.getAdvanceTimeStart() != null, "contract_last", dto.getAdvanceTimeStart());
        query.le(dto.getAdvanceTimeEnd() != null, "contract_last", dto.getAdvanceTimeEnd());
        return baseMapper.selectPublicCompany(query);
    }
}
