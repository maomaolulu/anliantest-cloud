package com.anliantest.file.service;

import com.anliantest.file.api.domain.ProgressManage;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author zhanghao
 * @date 2023-08-29
 * @desc : 进度管理 */
public interface ProgressManageService extends IService<ProgressManage> {

    /**
     * 分页数据
     */
    List<ProgressManage> listPage(ProgressManage progressManage) ;
}
