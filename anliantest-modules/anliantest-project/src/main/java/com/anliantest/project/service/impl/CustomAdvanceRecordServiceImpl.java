package com.anliantest.project.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.exceptions.StatefulException;
import com.anliantest.common.core.constant.MinioConstants;
import com.anliantest.common.security.utils.SecurityUtils;
import com.anliantest.file.api.domain.SysAttachment;
import com.anliantest.project.domain.vo.HistoryAdvanceVo;
import com.anliantest.project.entity.CustomAdvanceRecordEntity;
import com.anliantest.project.entity.CustomAdvanceTaskEntity;
import com.anliantest.project.mapper.CustomAdvanceRecordMapper;
import com.anliantest.project.mapper.CustomAdvanceTaskMapper;
import com.anliantest.project.service.CustomAdvanceRecordService;
import com.anliantest.project.service.CustomAdvanceTaskService;
import com.anliantest.project.service.CustomFileService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author yrb
 * @Date 2023/8/21 11:49
 * @Version 1.0
 * @Description 跟进记录
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CustomAdvanceRecordServiceImpl extends ServiceImpl<CustomAdvanceRecordMapper, CustomAdvanceRecordEntity> implements CustomAdvanceRecordService {
    private final CustomAdvanceRecordMapper customAdvanceRecordMapper;
    private final CustomAdvanceTaskMapper customAdvanceTaskMapper;
    private final CustomAdvanceTaskService customAdvanceTaskService;
    private final CustomFileService customFileService;

    public CustomAdvanceRecordServiceImpl(CustomAdvanceRecordMapper customAdvanceRecordMapper,
                                          CustomFileService customFileService,
                                          CustomAdvanceTaskMapper customAdvanceTaskMapper,
                                          CustomAdvanceTaskService customAdvanceTaskService) {
        this.customAdvanceRecordMapper = customAdvanceRecordMapper;
        this.customFileService = customFileService;
        this.customAdvanceTaskMapper = customAdvanceTaskMapper;
        this.customAdvanceTaskService = customAdvanceTaskService;
    }

    /**
     * 新增跟进记录
     *
     * @param customAdvanceRecord 跟进信息
     * @return 操作结果
     */
    @Override
    public int add(CustomAdvanceRecordEntity customAdvanceRecord) {
        customAdvanceRecord.setCreateBy(SecurityUtils.getUsernameCn());
        customAdvanceRecord.setCreateTime(new Date());
        int i = customAdvanceRecordMapper.insert(customAdvanceRecord);
        if (i != 0) {
            // 更新任务最新跟进记录
            CustomAdvanceTaskEntity taskEntity = customAdvanceTaskMapper.selectById(customAdvanceRecord.getTaskId());
            if (taskEntity == null || taskEntity.getId() == null) {
                throw new StatefulException("获取任务信息异常");
            }
            CustomAdvanceTaskEntity advanceTask = new CustomAdvanceTaskEntity();
            if (taskEntity.getAdvanceFirstTime() == null) {
                advanceTask.setAdvanceFirstTime(new Date());
            }
            advanceTask.setBusinessStatus(2);
            advanceTask.setId(customAdvanceRecord.getTaskId());
            advanceTask.setAdvanceLastTime(new Date());
            if (!customAdvanceTaskService.updateById(advanceTask)) {
                throw new StatefulException("更新任务最新记录异常");
            }
            // 删除图片缓存
            List<SysAttachment> sysAttachmentList = customAdvanceRecord.getSysAttachmentList();
            if (CollUtil.isNotEmpty(sysAttachmentList)) {
                sysAttachmentList.forEach(s -> s.setPId(customAdvanceRecord.getId()));
                if (!customFileService.uploadFiles(sysAttachmentList)) {
                    throw new UnsupportedOperationException("将上传的文件转为有效文件失败");
                }
            }
        }
        return i;
    }

    /**
     * 跟进详情
     *
     * @param customAdvanceRecord 任务ID
     * @return 集合
     */
    @Override
    public List<CustomAdvanceRecordEntity> getDetail(CustomAdvanceRecordEntity customAdvanceRecord) {
        QueryWrapper<CustomAdvanceRecordEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("t1.task_id", customAdvanceRecord.getTaskId())
                .eq(customAdvanceRecord.getAdvancePattern() != null, "t1.advance_pattern", customAdvanceRecord.getAdvancePattern())
                .eq(customAdvanceRecord.getAdvanceDate() != null, "t1.advance_date", customAdvanceRecord.getAdvanceDate());
        queryWrapper.orderByDesc("t1.create_time");
        List<CustomAdvanceRecordEntity> details = customAdvanceRecordMapper.getDetails(queryWrapper);
        if (CollUtil.isNotEmpty(details)) {
            List<Long> ids = details.stream().map(CustomAdvanceRecordEntity::getId).collect(Collectors.toList());
            Map<Long, List<SysAttachment>> files = customFileService.getFiles(ids, MinioConstants.CUSTOMER_ADVANCE_RECORD);
            if (CollUtil.isNotEmpty(files)) {
                for (CustomAdvanceRecordEntity customAdvanceRecordEntity : details) {
                    customAdvanceRecordEntity.setSysAttachmentList(files.get(customAdvanceRecordEntity.getId()));
                }
            }
        }
        return details;
    }

    /**
     * 修改跟进记录
     *
     * @param customAdvanceRecord 跟进信息
     * @return 操作结果
     */
    @Override
    public int updateAdvanceInfo(CustomAdvanceRecordEntity customAdvanceRecord) {
        customAdvanceRecord.setUpdateBy(SecurityUtils.getUsernameCn());
        customAdvanceRecord.setUpdateTime(new Date());
        int i = customAdvanceRecordMapper.updateById(customAdvanceRecord);
        if (i != 0 && CollUtil.isNotEmpty(customAdvanceRecord.getSysAttachmentList())) {
            if (!customFileService.updateFiles(customAdvanceRecord.getSysAttachmentList(), customAdvanceRecord.getId())) {
                throw new StatefulException("更新文件失败");
            }
        }
        return i;
    }

    /**
     * 删除跟进记录
     *
     * @param id 跟进记录主键ID
     * @return result
     */
    @Override
    public int delete(Long id) {
        return customAdvanceRecordMapper.deleteById(id);
    }

    /**
     * 历史跟进
     */
    @Override
    public List<HistoryAdvanceVo> historyAdvance(Long companyId) {
        List<HistoryAdvanceVo> historyAdvanceVoList = customAdvanceRecordMapper.selectHistoryAdvanceByCompanyId(companyId);
        if (CollUtil.isNotEmpty(historyAdvanceVoList)) {
            for (HistoryAdvanceVo historyAdvanceVo : historyAdvanceVoList) {
                if (historyAdvanceVo.getIfHasFinished() == 0) {
                    historyAdvanceVo.setCustomerOrder(historyAdvanceVo.getPersonBelong());
                } else {
                    historyAdvanceVo.setCustomerOrder(historyAdvanceVo.getDataBelong());
                }
            }
        }
        return historyAdvanceVoList;
    }

}
