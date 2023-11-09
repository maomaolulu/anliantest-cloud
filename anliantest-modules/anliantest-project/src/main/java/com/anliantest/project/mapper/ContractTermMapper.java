package com.anliantest.project.mapper;

import com.anliantest.project.entity.ContractField;
import com.anliantest.project.entity.ContractTerm;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author zhanghao
 * @date 2023-10-16
 * @desc : 合同条款表
 */
@Mapper
public interface ContractTermMapper extends BaseMapper<ContractTerm> {


    @Select("SELECT\n" +
            "\tt.*,\n" +
            "\td.dept_name AS companyName,\n" +
            "\ttt.NAME AS termType \n" +
            "FROM\n" +
            "\tcontract_term t\n" +
            "\tLEFT JOIN contract_term_type tt ON t.term_type_id = tt.id\n" +
            "\tLEFT JOIN `anliantest-system`.sys_dept d ON d.dept_id = t.company_id \n" +
            "\t ${ew.customSqlSegment}")
    List<ContractTerm> getList(@Param(Constants.WRAPPER) QueryWrapper<ContractTerm> queryWrapper);


    @Select("SELECT \n" +
            "\tf.id,\n" +
            "\tf.english_name,\n" +
            "\tf.NAME ,\n" +
            "\ttf.field_describe \n" +
            "FROM\n" +
            "\tcontract_term_field AS tf\n" +
            "\tLEFT JOIN contract_term AS t ON t.id = tf.contract_term_id \n" +
            "\tLEFT JOIN contract_field AS f ON tf.contract_field_id = f.id \n" +
            "\t ${ew.customSqlSegment}")
    List<ContractField> getInfo(@Param(Constants.WRAPPER) QueryWrapper<ContractTerm> queryWrapper);
}
