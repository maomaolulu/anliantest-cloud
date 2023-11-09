package com.anliantest.data.service;

import com.anliantest.data.entity.SubstanceStateLimitEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author gy
 * @date 2023-06-06
 */
public interface SubstanceStateLimitService extends IService<SubstanceStateLimitEntity> {

    /**
     * 新增危害因素限值
     * @param stateLimit 危害因素限值信息
     * @return result
     */
    Boolean addLimit(SubstanceStateLimitEntity stateLimit);


    /**
     * 获取危害因素限值
     * @param substanceInfoId 危害因素id
     * @return 查询结果
     */
    List<SubstanceStateLimitEntity> getListBySubstanceInfoId(Long substanceInfoId);


    /**
     * 修改危害因素限值
     * @param stateLimit 新危害因素限值信息
     * @return result
     */
    Boolean updateLimit(SubstanceStateLimitEntity stateLimit);

    /**
     * 逻辑删除危害因素限值
     * @param id 限值id
     * @return result
     */
    Boolean deleteLimit(Long id);


}
