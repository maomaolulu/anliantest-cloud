package com.anliantest.data.service.impl;

import com.anliantest.common.core.enums.DeleteFlag;
import com.anliantest.common.core.utils.DateUtils;
import com.anliantest.common.security.utils.SecurityUtils;
import com.anliantest.data.entity.SubstanceStateLimitEntity;
import com.anliantest.data.mapper.SubstanceStateLimitMapper;
import com.anliantest.data.service.SubstanceStateLimitService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author gy
 * @date 2023-06-06
 */
@Service
public class SubstanceStateLimitServiceImpl extends ServiceImpl<SubstanceStateLimitMapper, SubstanceStateLimitEntity> implements SubstanceStateLimitService {

    /**
     * 新增危害因素限值
     *
     */
    @Override
    public Boolean addLimit(SubstanceStateLimitEntity stateLimit) {

        Date nowDate = DateUtils.getNowDate();
        String usernameCn = SecurityUtils.getUsernameCn();
        stateLimit.setCreateBy(usernameCn);
        stateLimit.setUpdateBy(usernameCn);
        stateLimit.setCreateTime(nowDate);
        stateLimit.setUpdateTime(nowDate);

        return this.save(stateLimit);
    }


    /**
     * 获取危害因素限值
     *
     */
    @Override
    public List<SubstanceStateLimitEntity> getListBySubstanceInfoId(Long substanceInfoId) {
        QueryWrapper<SubstanceStateLimitEntity> queryWrapper = new QueryWrapper<SubstanceStateLimitEntity>()
                .eq(substanceInfoId != null, "ss.substance_info_id", substanceInfoId)
                .eq("ss.delete_flag", DeleteFlag.NO.ordinal());

        return baseMapper.getListBySubstanceInfoId(queryWrapper);
    }


    /**
     * 修改危害因素限值
     *
     */
    @Override
    public Boolean updateLimit(SubstanceStateLimitEntity stateLimit) {
        Date nowDate = DateUtils.getNowDate();
        String usernameCn = SecurityUtils.getUsernameCn();
        stateLimit.setUpdateBy(usernameCn);
        stateLimit.setUpdateTime(nowDate);
        return this.updateById(stateLimit);
    }


    /**
     * 逻辑删除危害因素限值
     *
     */
    @Override
    public Boolean deleteLimit(Long id) {

        return this.update(new UpdateWrapper<SubstanceStateLimitEntity>()
                .eq("id", id)
                .set("delete_flag", DeleteFlag.YES.ordinal()));
    }

}
