package com.anliantest.data.mapper;

import com.anliantest.data.entity.ContractType;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author zhanghao
 * @date 2023-07-28
 * @desc : 合同类型
 */
@Mapper
public interface ContractTypeMapper extends BaseMapper<ContractType> {

    @Select(" select co.* from `anliantest-data`.data_contract_type co " +
            " left join `anliantest-system`.sys_dept d on co.company_id=d.dept_id" +
            "  ${ew.customSqlSegment} ")
    List<ContractType> oneTypeListPage(@Param(Constants.WRAPPER) QueryWrapper wrapper);

}
