package com.anliantest.system.service;

import com.anliantest.system.api.model.AmapData;
import com.anliantest.system.domain.ChineseAddress;
import com.anliantest.system.domain.vo.ChineseAddressVo;

import java.util.List;

/**
 * @Author maoly
 * @Date 2023/8/28 9:11
 */
public interface IChineseAddressService {

    /**
     * 全国地址数据保存
     * @param amapData
     */
    void saveChineseAddress(AmapData amapData);

    /**
     * 查询地区列表
     * @param regionParentId 地区名称
     */
    List<ChineseAddressVo> getIdAndName(String regionParentId);

    /**
     * 查询树形结构地区数据
     * @param regionParentId 父级id
     */
    List<ChineseAddress> getRegions(String regionParentId);
}
