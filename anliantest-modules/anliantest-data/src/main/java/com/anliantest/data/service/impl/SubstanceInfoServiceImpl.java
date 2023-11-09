package com.anliantest.data.service.impl;


import com.anliantest.common.core.enums.DeleteFlag;
import com.anliantest.common.core.utils.DateUtils;
import com.anliantest.common.core.utils.pageUtil;
import com.anliantest.common.security.utils.SecurityUtils;
import com.anliantest.data.entity.SubstanceInfoEntity;
import com.anliantest.data.entity.SubstanceTestMethodEntity;
import com.anliantest.data.mapper.SubstanceInfoMapper;
import com.anliantest.data.service.SubstanceInfoService;
import com.anliantest.data.service.SubstanceTestMethodService;
import com.anliantest.data.domain.vo.SubstanceInfoVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


/**
 * @Description
 * @Date 2023/6/6 9:15
 * @Author maoly
 **/
@Service
public class SubstanceInfoServiceImpl extends ServiceImpl<SubstanceInfoMapper, SubstanceInfoEntity> implements SubstanceInfoService {

    private final SubstanceTestMethodService substanceTestMethodService;

    @Autowired
    public SubstanceInfoServiceImpl(SubstanceTestMethodService substanceTestMethodService){
        this.substanceTestMethodService = substanceTestMethodService;
    }

    /**
     * 新增危害因素
     *
     */
    @Override
    public Boolean addSubstanceInfo(SubstanceInfoEntity substanceInfo) {
        Date nowDate = DateUtils.getNowDate();
        String usernameCn = SecurityUtils.getUsernameCn();
        substanceInfo.setCreateBy(usernameCn);
        substanceInfo.setUpdateBy(usernameCn);
        substanceInfo.setCreateTime(nowDate);
        substanceInfo.setUpdateTime(nowDate);

        return this.save(substanceInfo);
    }

    /**
     * 获取危害因素列表
     *
     */
    @Override
    public List<SubstanceInfoEntity> getSubstanceInfoList(SubstanceInfoVo substanceInfoVo) {
        String substanceNameOrOtherName = substanceInfoVo.getSubstanceNameOrOtherName();
        String casCode = substanceInfoVo.getCasCode();
        Integer substanceType = substanceInfoVo.getSubstanceType();

        pageUtil.startPage();
        return this.list(new QueryWrapper<SubstanceInfoEntity>()
                .like(substanceNameOrOtherName != null, "substance_name", substanceNameOrOtherName)
                .or().like(substanceNameOrOtherName != null, "substance_other_name", substanceNameOrOtherName)
                .like(casCode != null, "cas_code", casCode)
                .eq(substanceType != null, "substance_type", substanceType)
                .eq("delete_flag", DeleteFlag.NO.ordinal())
                .orderByDesc("update_time"));
    }


    /**
     * 获取危害因素详情
     *
     */
    @Override
    public SubstanceInfoEntity getSubstanceInfo(Long id) {

        return this.getById(id);
    }

    /**
     * 修改危害因素信息
     *
     */
    @Override
    public Boolean updateSubstanceInfo(SubstanceInfoEntity substanceInfo) {

        Date nowDate = DateUtils.getNowDate();
        String usernameCn = SecurityUtils.getUsernameCn();
        substanceInfo.setUpdateBy(usernameCn);
        substanceInfo.setUpdateTime(nowDate);
        return this.updateById(substanceInfo);
    }

    /**
     * 逻辑删除危害因素信息
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteSubstanceInfo(Long id) {

        // TODO 逻辑删除物质检测方法,若关联资质则不可删除
        //逻辑删除危害因素相关的检测方法
        SubstanceTestMethodEntity method = new SubstanceTestMethodEntity();
        method.setDeleteFlag(DeleteFlag.YES.ordinal());
        substanceTestMethodService.update(method,new QueryWrapper<SubstanceTestMethodEntity>().eq("substance_info_id",id));

        return this.update(new UpdateWrapper<SubstanceInfoEntity>()
                .eq("id", id)
                .set("delete_flag", DeleteFlag.YES.ordinal()));
    }
}
