package com.anliantest.project.mapper;

import com.anliantest.project.entity.ContractSampleTerm;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.anliantest.project.entity.ContractSample;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author zhanghao
 * @date 2023-10-16
 * @desc : 合同范本基础信息表
 */
@Mapper
public interface ContractSampleMapper extends BaseMapper<ContractSample> {
    @Select(" SELECT cs.*,ct.name contractTypeName,d.dept_name companyName from contract_sample cs " +
            " left join  `anliantest-data`.data_contract_type ct on cs.contract_type_id =ct.id " +
            "left join `anliantest-system`.sys_dept d on d.dept_id=cs.company_id " +
            "${ew.customSqlSegment} " )
    List<ContractSample> listPage(@Param(Constants.WRAPPER) QueryWrapper wrapper);

    @Select(" SELECT st.id,st.contract_sample_id,st.contract_term_id,ty.`name`,ct.content,ct.associated_fields,ct.content_type,st.must " +
            " FROM contract_sample_term st  " +
            " left join contract_term  ct on st.contract_term_id=ct.id " +
            " left join contract_term_type ty on ct.term_type_id=ty.id " +
            " where st.contract_sample_id=#{id } " )
    List<ContractSampleTerm> previewList(Long id);
}
