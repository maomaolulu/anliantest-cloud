package com.anliantest.data.service;

import com.anliantest.data.entity.EquipCategory;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Author yrb
 * @Date 2023/8/14 18:05
 * @Version 1.0
 * @Description 仪器类型服务
 */
public interface EquipCategoryService extends IService<EquipCategory> {
    /**
     * 添加仪器类型
     *
     * @param equipCategory 类型信息
     * @return result
     */
    int add(EquipCategory equipCategory);

    /**
     * 编辑仪器类型
     *
     * @param equipCategory 类型信息
     * @return result
     */
    int edit(EquipCategory equipCategory);

    /**
     * 删除仪器类型
     *
     * @param id 类型信息
     * @return result
     */
    int delete(Long id);

    /**
     * 获取仪器类型一级类目下的子类下拉列表（单个一级类目）
     *
     * @param equipCategory 类型信息
     * @return 集合
     */
    List<EquipCategory> getDropdownList(EquipCategory equipCategory);

    /**
     * 获取仪器类型列表
     *
     * @param equipCategory 类型信息
     * @return 集合
     */
    List<EquipCategory> getList(EquipCategory equipCategory);
}
