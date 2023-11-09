package com.anliantest.project.service;

import com.anliantest.project.domain.dto.CustomAdvanceDto;
import com.anliantest.project.domain.vo.CustomAdvanceVo;
import com.anliantest.project.entity.CustomAdvanceTaskEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Author yrb
 * @Date 2023/8/21 11:45
 * @Version 1.0
 * @Description 跟进任务
 */
public interface CustomAdvanceTaskService extends IService<CustomAdvanceTaskEntity> {
    /**
     * 更新任务信息
     *
     * @param customAdvanceTask 任务信息
     * @return result
     */
    int modify(CustomAdvanceTaskEntity customAdvanceTask);

    /**
     * 人员替换
     *
     * @param customAdvanceTask  任务信息
     * @return result
     */
    int replaceUserBatch(CustomAdvanceTaskEntity customAdvanceTask);

    /**
     * 新建任务
     *
     * @param customAdvanceTask 任务信息
     * @return result
     */
    int add(CustomAdvanceTaskEntity customAdvanceTask);

    /**
     * 任务分配
     *
     * @param customAdvanceTask 任务信息
     * @return result
     */
    int allocationBatch(CustomAdvanceTaskEntity customAdvanceTask);

    /**
     * 查询跟进任务列表
     *
     * @param dto 查询条件
     * @return 结果计算
     */
    List<CustomAdvanceVo> listTasks(CustomAdvanceDto dto);

    /**
     * 释放跟进任务,客户至公海
     */
    boolean releaseCompany(CustomAdvanceTaskEntity dto);

    /**
     * 定时任务远程调用-定时释放客户至公海
     */
    void releaseCompanyJob();
}
