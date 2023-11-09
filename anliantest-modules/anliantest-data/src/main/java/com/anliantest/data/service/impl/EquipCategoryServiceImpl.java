package com.anliantest.data.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.anliantest.common.security.utils.SecurityUtils;
import com.anliantest.data.entity.EquipCategory;
import com.anliantest.data.entity.EquipWarehouseRecord;
import com.anliantest.data.mapper.EquipCategoryMapper;
import com.anliantest.data.mapper.EquipWarehouseRecordMapper;
import com.anliantest.data.service.EquipCategoryService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author yrb
 * @Date 2023/8/14 18:06
 * @Version 1.0
 * @Description 仪器类型实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class EquipCategoryServiceImpl extends ServiceImpl<EquipCategoryMapper, EquipCategory> implements EquipCategoryService {
    private final EquipCategoryMapper equipCategoryMapper;
    private final EquipWarehouseRecordMapper equipWarehouseRecordMapper;

    public EquipCategoryServiceImpl(EquipCategoryMapper equipCategoryMapper,
                                    EquipWarehouseRecordMapper equipWarehouseRecordMapper) {
        this.equipCategoryMapper = equipCategoryMapper;
        this.equipWarehouseRecordMapper = equipWarehouseRecordMapper;
    }

    /**
     * 添加仪器类型
     *
     * @param equipCategory 类型信息
     * @return result
     */
    @Override
    public int add(EquipCategory equipCategory) {
        if (equipCategory.getParentId() == null) {
            equipCategory.setParentId(0L);
        }
        QueryWrapper<EquipCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_name", equipCategory.getCategoryName());
        List<EquipCategory> list = equipCategoryMapper.selectList(queryWrapper);
        if (CollUtil.isNotEmpty(list)) {
            EquipCategory category = list.get(0);
            if (category.getDelFlag() == 1) {
                String username = SecurityUtils.getUsernameCn();
                equipCategory.setDelFlag(0);
                equipCategory.setCreateBy(username);
                equipCategory.setUpdateBy(username);
                equipCategory.setCreateTime(new Date());
                equipCategory.setUpdateTime(new Date());
                equipCategory.setId(category.getId());
                return equipCategoryMapper.updateById(equipCategory);
            }
            return 2;
        }
        String username = SecurityUtils.getUsernameCn();
        equipCategory.setDelFlag(0);
        equipCategory.setCreateBy(username);
        equipCategory.setUpdateBy(username);
        equipCategory.setCreateTime(new Date());
        equipCategory.setUpdateTime(new Date());
        return equipCategoryMapper.insert(equipCategory);
    }

    /**
     * 编辑仪器类型
     *
     * @param equipCategory 类型信息
     * @return result
     */
    @Override
    public int edit(EquipCategory equipCategory) {
        if (equipCategory.getParentId() == null) {
            equipCategory.setParentId(0L);
        }
        QueryWrapper<EquipCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("id", equipCategory.getId());
        queryWrapper.eq("category_name", equipCategory.getCategoryName());
        List<EquipCategory> list = equipCategoryMapper.selectList(queryWrapper);
        if (CollUtil.isNotEmpty(list)) {
            return 2;
        }
        equipCategory.setUpdateBy(SecurityUtils.getUsernameCn());
        equipCategory.setUpdateTime(new Date());
        return equipCategoryMapper.updateById(equipCategory);
    }

    /**
     * 删除仪器类型
     *
     * @param id 类型信息
     * @return result
     */
    @Override
    public int delete(Long id) {
        // 获取当前类型及子类型ID集合
        Set<Long> set = getIds(id);
        // 判断是否绑定仪器设备
        QueryWrapper<EquipWarehouseRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("equip_type", set);
        Long count = equipWarehouseRecordMapper.selectCount(queryWrapper);
        if (count > 0) {
            return 0;
        }
        // 批量删除
        ArrayList<EquipCategory> list = new ArrayList<>();
        for (Long i : set) {
            EquipCategory category = new EquipCategory();
            category.setId(i);
            category.setDelFlag(1);
            list.add(category);
        }
        return this.updateBatchById(list) ? 1 : 0;
    }

    /**
     * 递归删除子集
     *
     * @param id 主键
     */
    private Set<Long> getIds(Long id) {
        Set<Long> set = new HashSet<>();
        set.add(id);
        QueryWrapper<EquipCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", id);
        queryWrapper.eq("del_flag", 0);
        List<EquipCategory> categoryList = equipCategoryMapper.selectList(queryWrapper);
        if (CollUtil.isNotEmpty(categoryList)) {
            for (EquipCategory category : categoryList) {
                Set<Long> set1 = getIds(category.getId());
                set.addAll(set1);
            }
        }
        return set;
    }

    /**
     * 获取仪器类型一级类目下的子类下拉列表（单个一级类目）
     *
     * @param equipCategory 类型信息
     * @return 集合
     */
    @Override
    public List<EquipCategory> getDropdownList(EquipCategory equipCategory) {
        List<EquipCategory> list = new ArrayList<>();
        QueryWrapper<EquipCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("del_flag", 0);
        queryWrapper.eq("category_name", equipCategory.getCategoryName());
        EquipCategory category = equipCategoryMapper.selectOne(queryWrapper);
        if (category != null && category.getId() != null) {
            QueryWrapper<EquipCategory> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("parent_id", category.getId());
            list = equipCategoryMapper.selectList(queryWrapper1);
        }
        return list;
    }

    /**
     * 获取仪器类型列表
     *
     * @param equipCategory 类型信息
     * @return 集合
     */
    @Override
    public List<EquipCategory> getList(EquipCategory equipCategory) {
        QueryWrapper<EquipCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("del_flag", 0);
        List<EquipCategory> categoryList = equipCategoryMapper.selectList(queryWrapper);
        if (StrUtil.isNotBlank(equipCategory.getCategoryName())) {
            Map<Long, EquipCategory> map = categoryList.stream().collect(Collectors.toMap(EquipCategory::getId, EquipCategory -> EquipCategory));
            queryWrapper.like("category_name", equipCategory.getCategoryName());
            List<EquipCategory> categorys = equipCategoryMapper.selectList(queryWrapper);
            Map<Long, EquipCategory> map1 = categorys.stream().collect(Collectors.toMap(EquipCategory::getId, EquipCategory -> EquipCategory));
            for (EquipCategory equipCategory1 : categorys) {
                getParent(equipCategory1, map, map1);
            }
            return getInfo(new ArrayList<>(map1.values()));
        }
        return getInfo(categoryList);
    }

    private void getParent(EquipCategory equipCategory, Map<Long, EquipCategory> map, Map<Long, EquipCategory> map1) {
        map1.put(equipCategory.getId(), equipCategory);
        Long parentId = equipCategory.getParentId();
        if (parentId != 0) {
            EquipCategory category = map.get(parentId);
            getParent(category, map, map1);
        }
    }

    private List<EquipCategory> getInfo(List<EquipCategory> categoryList) {
        Map<Long, EquipCategory> map = categoryList.stream().collect(Collectors.toMap(EquipCategory::getId, EquipCategory -> EquipCategory));
        if (CollUtil.isNotEmpty(categoryList)) {
            for (EquipCategory category : categoryList) {
                Long parentId = category.getParentId();
                if (parentId == 0) {
                    continue;
                }
                EquipCategory category1 = map.get(parentId);
                if (category1 != null) {
                    List<EquipCategory> chidren = category1.getChidren();
                    if (chidren == null) {
                        chidren = new ArrayList<>();
                    }
                    chidren.add(category);
                    category1.setChidren(chidren);
                }
            }
        }
        categoryList = categoryList.stream()
                .filter(c -> c.getParentId() == 0)
                .collect(Collectors.toList());
        return categoryList;
    }
}
