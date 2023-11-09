package com.anliantest.project.mapper;

import com.anliantest.project.domain.dto.RoleAndCompanyDto;
import com.anliantest.project.domain.vo.CompanyAndContactVo;
import com.anliantest.project.domain.vo.CompanyPublicVo;
import com.anliantest.project.domain.vo.CustomCustomerVo;
import com.anliantest.project.entity.CustomCustomerEntity;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author gy
 * @date 2023-08-18 16:57
 */
@Mapper
public interface CustomCustomerMapper extends BaseMapper<CustomCustomerEntity> {

    /**
     * 查询客户管理分页数据
     */
    @Select("SELECT cc.id customer_id, cat.id task_id, IF(cc.if_new_company = 0 && cc.customer_order != '',cc.customer_order,cc.customer_company) customer_order, cc.enterprise_name, cc.province, cc.city, cc.district, cat.business_status, cc.customer_status FROM custom_customer cc\n" +
            "LEFT JOIN (SELECT id, custom_id, business_status FROM custom_advance_task WHERE id IN(SELECT MAX(id) FROM custom_advance_task GROUP BY custom_id)) cat ON cat.custom_id = cc.id" +
            "  ${ew.customSqlSegment} ")
    List<CustomCustomerVo> listWithPage(@Param(Constants.WRAPPER) QueryWrapper wrapper);

    /**
     * 查询某个客户的企业信息
     */
    @Select("SELECT  IF(cc.if_new_company = 0 && cc.customer_order != '',cc.customer_order,cc.customer_company) customer_order, cc.customer_status, cc.enterprise_name, cc.credit_code, cc.province, cc.city, cc.district, cc.registered_address, cc.inspection_address, cc.legal_person, cc.industry_category,cc.staff_size, cc.risk_class,cc.product_info, cat.business_status, cc.clue_from FROM custom_customer cc\n" +
            "LEFT JOIN (\n" +
            "SELECT custom_id,business_status FROM custom_advance_task WHERE id IN(SELECT MAX(id) FROM custom_advance_task GROUP BY custom_id)) cat ON cat.custom_id = cc.id\n" +
            "WHERE cc.id = #{customerId}")
    CompanyAndContactVo getCompanyAndContact(Long customerId);

    /**
     * 查询某个
     */
    @Select("SELECT sr.role_key,sdd.dict_label FROM `anliantest-system`.sys_role sr\n" +
            "LEFT JOIN `anliantest-system`.sys_user_role sur ON sur.role_id = sr.role_id\n" +
            "LEFT JOIN `anliantest-system`.sys_user su ON su.user_id = sur.user_id\n" +
            "LEFT JOIN `anliantest-system`.sys_dict_data sdd ON sdd.dict_value = su.company_key AND sdd.dict_type = 'sys_ehs_company'\n" +
            "WHERE sur.user_id =  #{userId}")
    List<RoleAndCompanyDto> getRoleAndCompany(@Param("userId") Long userId);

    /**
     * 客户公海列表
     * @param wrapper 查询条件
     * @return list
     */
    @Select("SELECT cc.id, cc.customer_company, cc.enterprise_name, cc.province, cc.city, cc.district,  '2023-01-01' contract_last, cc.customer_status \n" +
            "FROM custom_customer cc\n" +
            "LEFT JOIN (SELECT custom_id, AVG(IF(business_status = 5 OR business_status = 0,10,-1)) new_status FROM custom_advance_task GROUP BY custom_id) cat ON cat.custom_id = cc.id  " +
            "${ew.customSqlSegment}")
    List<CompanyPublicVo> selectPublicCompany(@Param(Constants.WRAPPER) QueryWrapper wrapper);
}
