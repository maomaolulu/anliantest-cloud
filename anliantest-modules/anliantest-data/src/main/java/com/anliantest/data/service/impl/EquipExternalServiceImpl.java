package com.anliantest.data.service.impl;

import cn.hutool.core.util.StrUtil;
import com.anliantest.data.entity.EquipExternal;
import com.anliantest.data.mapper.EquipExternalMapper;
import com.anliantest.data.service.EquipExternalService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhanghao
 * @date 2023-08-18
 * @desc : 外部设备
 */
@Service
public class EquipExternalServiceImpl extends ServiceImpl<EquipExternalMapper, EquipExternal> implements EquipExternalService {

    /**
     * 外部设备分页列表
     *
     * @param equipExternal
     * @return
     */
    @Override
    public List<EquipExternal> listPage(EquipExternal equipExternal) {

        List<EquipExternal> list = this.list(new QueryWrapper<EquipExternal>()
                //仪器类型
                .eq(equipExternal.getEquipType() != null, "equip_type", equipExternal.getEquipType())
                //仪器编号
                .like(StrUtil.isNotBlank(equipExternal.getEquipCode()), "equip_code", equipExternal.getEquipCode())
                //仪器名称
                .like(StrUtil.isNotBlank(equipExternal.getGoodsName()), "goods_name", equipExternal.getGoodsName())
                //设备隶属
                .like(StrUtil.isNotBlank(equipExternal.getCompanyName()), "company_name", equipExternal.getCompanyName())
                //逻辑删除
                .eq("del_flag", 0)
        );

        return list;
    }
}
