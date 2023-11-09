package com.anliantest.file.mapper;

import com.anliantest.file.api.domain.ProgressManage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author zhanghao
 * @date 2023-08-29
 * @desc : 进度管理
 */
@Mapper
public interface ProgressManageMapper extends BaseMapper<ProgressManage> {

    @Select(" SELECT pr.*,u.nick_name   FROM `progress_manage` pr \n" +
            "left join sys_user  u on pr.create_by_id=u.user_id " +
            " ${ew.customSqlSegment} " )
    List<ProgressManage> listPage(@Param(Constants.WRAPPER) QueryWrapper wrapper);
}
