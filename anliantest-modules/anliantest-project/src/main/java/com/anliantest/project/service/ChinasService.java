package com.anliantest.project.service;

import com.anliantest.project.entity.Chinas;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author gy
 * @date 2023-08-24 15:57
 */
public interface ChinasService extends IService<Chinas> {

    List<Chinas> getRegions();

    /**
     * 导入省市区
     * @param chinasList
     * @return
     */
    Boolean importRegions(List<Chinas> chinasList);
}
