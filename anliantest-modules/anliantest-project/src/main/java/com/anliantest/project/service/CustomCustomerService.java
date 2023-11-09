package com.anliantest.project.service;

import com.anliantest.project.domain.dto.CustomCustomerDto;
import com.anliantest.project.domain.dto.RoleAndCompanyDto;
import com.anliantest.project.domain.vo.CompanyAndContactVo;
import com.anliantest.project.domain.vo.CompanyPublicVo;
import com.anliantest.project.domain.vo.CustomCustomerVo;
import com.anliantest.project.entity.CustomCustomerEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author gy
 * @date 2023-08-18 16:59
 */
public interface CustomCustomerService extends IService<CustomCustomerEntity> {

    /**
     * 客户管理分页查询
     * @param dto 查询条件
     * @return List<CustomCustomerVo>
     */
    List<CustomCustomerVo> listWithPage(CustomCustomerDto dto);

    /**
     * 新增客户以及联系人
     */
    Boolean newCustomer(CustomCustomerDto dto);

    /**
     * 修改客户以及联系人
     */
    Boolean updateCustomer(CustomCustomerDto dto);

    /**
     * 修改客户以及联系人
     */
    Boolean checkIfRelate(Long customerId);

    /**
     * 删除客户(支持批量删除)
     */
    void deleteCustomer(Long customerId);

    /**
     * 查询某个客户的企业信息和联系人
     */
    CompanyAndContactVo getCompanyAndContact(Long customerId);

    List<RoleAndCompanyDto> getRoleAndCompany(Long userId);

    /**
     * 客户公海列表
     */
    List<CompanyPublicVo> selectPublicCompany(CustomCustomerDto dto, String companyOrder);
}
