package com.anliantest.project.mapper;

import com.anliantest.project.domain.vo.CustomAdvanceVo;
import com.anliantest.project.entity.CustomAdvanceTaskEntity;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * @Author yrb
 * @Date 2023/8/21 11:42
 * @Version 1.0
 * @Description 客户跟进任务
 */
@Mapper
public interface CustomAdvanceTaskMapper extends BaseMapper<CustomAdvanceTaskEntity> {

    /**
     * 查询跟进任务列表
     *
     * @param wrapper 查询条件
     * @return result
     */
    @Select("SELECT cat.id task_id, cc.id customer_id, cat.task_code, IF(cc.if_new_company = 0 && cc.customer_order != '',cc.customer_order,cc.customer_company) customer_order, cc.enterprise_name, cc.province, cc.city, cc.district, u.nick_name advance_name, cat.advance_first_time, cat.advance_last_time, cat.business_status, cat.advance_result, cc.registered_address, ccs.contacter_name, ccs.mobile_phone, cc.if_new_company \n" +
            "FROM custom_advance_task cat \n" +
            "LEFT JOIN custom_customer cc ON cat.custom_id = cc.id\n" +
            "LEFT JOIN custom_contacters ccs ON cat.custom_id = ccs.customer_id AND ccs.if_default = 1\n" +
            "LEFT JOIN `anliantest-system`.sys_user u ON u.user_id = cat.advance_id " +
            "${ew.customSqlSegment}")
    List<CustomAdvanceVo> listTasks(@Param(Constants.WRAPPER) QueryWrapper wrapper);

    /**
     * 查询需要释放到公海的客户
     */
    @Select("SELECT tc.id FROM custom_customer cc " +
            "LEFT JOIN (SELECT custom_id,MIN(advance_first_time) advance_first_time FROM custom_advance_task GROUP BY custom_id HAVING advance_first_time is not NULL) cat1 ON cat1.custom_id = tc.id\n" +
            "LEFT JOIN (SELECT custom_id,MAX(advance_last_time) advance_last_time FROM custom_advance_task GROUP BY custom_id HAVING advance_last_time is not NULL) cat2 ON cat2.custom_id = tc.id\n" +
            "${ew.customSqlSegment}")
    List<Long> selectToBeOpenData(@Param(Constants.WRAPPER) QueryWrapper wrapper);
}
