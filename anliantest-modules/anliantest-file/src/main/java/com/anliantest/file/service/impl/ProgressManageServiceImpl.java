package com.anliantest.file.service.impl;

import cn.hutool.core.util.StrUtil;
import com.anliantest.common.core.constant.HttpStatus;
import com.anliantest.common.core.constant.MinioConstants;
import com.anliantest.common.core.constant.SecurityConstants;
import com.anliantest.common.core.domain.R;
import com.anliantest.file.api.RemoteSysAttachmentService;
import com.anliantest.file.api.domain.ProgressManage;
import com.anliantest.file.api.domain.SysAttachment;
import com.anliantest.file.api.domain.dto.MinioDto;
import com.anliantest.file.mapper.ProgressManageMapper;
import com.anliantest.file.service.ProgressManageService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhanghao
 * @date 2023-08-29
 * @desc : 进度管理
 */
@Service
public class ProgressManageServiceImpl extends ServiceImpl<ProgressManageMapper, ProgressManage> implements ProgressManageService {
    private final RemoteSysAttachmentService remoteSysAttachmentService;
    @Autowired
    public ProgressManageServiceImpl(RemoteSysAttachmentService remoteSysAttachmentService) {
        this.remoteSysAttachmentService = remoteSysAttachmentService;
    }

    @Override
    public List<ProgressManage> listPage(ProgressManage progressManage) {
        QueryWrapper<ProgressManage> wrapper = new QueryWrapper<>();
        //操作人
        wrapper.like(StrUtil.isNotBlank(progressManage.getNickName()),"u.nick_name",progressManage.getNickName());
        //文件名
        wrapper.like(StrUtil.isNotBlank(progressManage.getFileName()),"pr.file_name",progressManage.getFileName());
        //内容
        wrapper.like(StrUtil.isNotBlank(progressManage.getContent()),"pr.content",progressManage.getContent());
        //状态
        wrapper.eq(progressManage.getStatus()!=null,"pr.status",progressManage.getStatus());
        wrapper.orderByDesc("pr.id");

        List<ProgressManage> progressManages = baseMapper.listPage(wrapper);


        //收集id
        List<Long> idList = progressManages.stream().map(ProgressManage::getId).collect(Collectors.toList());
        //根据父级id获取附件列表map
        MinioDto minioDto = new MinioDto();
        minioDto.setBucketName(MinioConstants.PROGRESS_MANAGE);
        minioDto.setIdList(idList);
        R<Map<Long, List<SysAttachment>>> r = remoteSysAttachmentService.getSysAttachmentMap(minioDto, SecurityConstants.INNER);
        if (r.getCode() == HttpStatus.SUCCESS) {
            Map<Long, List<SysAttachment>> sysAttachmentMap = r.getData();
            if (sysAttachmentMap != null) {
                progressManages.stream().forEach(v -> v.setSysAttachmentList(sysAttachmentMap.get(v.getId())));
            }
        }


        return progressManages;
    }
}
