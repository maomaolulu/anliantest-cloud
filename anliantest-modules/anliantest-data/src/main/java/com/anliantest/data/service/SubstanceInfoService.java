package com.anliantest.data.service;


import com.anliantest.data.domain.vo.SubstanceInfoVo;
import com.anliantest.data.entity.SubstanceInfoEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SubstanceInfoService extends IService<SubstanceInfoEntity> {

    /**
     * 新增危害因素
     * @param substanceInfo 危害因素信息
     * @return result
     */
    Boolean addSubstanceInfo(SubstanceInfoEntity substanceInfo);

    /**
     * 获取危害因素列表
     * @param substanceInfoVo 列表查询参数
     * @return 查询结果
     */
    List<SubstanceInfoEntity> getSubstanceInfoList(SubstanceInfoVo substanceInfoVo);

    /**
     * 获取危害因素详情
     * @param id 危害因素id
     * @return 查询结果
     */
    SubstanceInfoEntity getSubstanceInfo(Long id);

    /**
     * 修改危害因素信息
     * @param substanceInfo 新危害因素信息
     * @return result
     */
    Boolean updateSubstanceInfo(SubstanceInfoEntity substanceInfo);

    /**
     * 逻辑删除危害因素信息
     * @param id 危害因素id
     * @return result
     */
    Boolean deleteSubstanceInfo(Long id);


}
