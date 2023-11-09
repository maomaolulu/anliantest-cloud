package com.anliantest.data.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.anliantest.data.entity.EquipExternal;

import java.util.List;

/**
 * @author zhanghao
 * @date 2023-08-18
 * @desc : 外部设备 */
public interface EquipExternalService extends IService<EquipExternal> {
    /**
     * 外部设备分页列表
     * @param equipExternal
     * @return
     */
     List<EquipExternal> listPage(EquipExternal equipExternal);
}
