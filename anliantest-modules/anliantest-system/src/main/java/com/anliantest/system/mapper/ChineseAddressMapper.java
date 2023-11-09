package com.anliantest.system.mapper;

import com.anliantest.system.domain.ChineseAddress;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author maoly
 * @Date 2023/8/28 9:41
 */
@Component
public interface ChineseAddressMapper {

    /**
     * 批量新增地址数据
     *
     * @param chineseAddressList 用户角色列表
     * @return 结果
     */
    public int batchSaveChineseAddress(List<ChineseAddress> chineseAddressList);

    /**
     * 全表删除
     */
    void deleteChineseAddress();

    /**
     * 查询数量
     * @return
     */
    int selectCount();

    /**
     * 查询地区列表
     * @param regionName 地区名称
     * @return 地区列表
     */
    List<ChineseAddress> getList(String regionName);

    /**
     * 查询地区列表
     * @param regionParentId 上一级地区编码
     * @return 地区列表
     */
    List<ChineseAddress> getRegions(String regionParentId);

}
