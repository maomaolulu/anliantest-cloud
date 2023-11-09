package com.anliantest.project.service.impl;

import com.anliantest.common.core.constant.HttpStatus;
import com.anliantest.common.core.constant.SecurityConstants;
import com.anliantest.common.core.domain.R;
import com.anliantest.file.api.RemoteSysAttachmentService;
import com.anliantest.file.api.domain.SysAttachment;
import com.anliantest.file.api.domain.dto.MinioDto;
import com.anliantest.project.service.CustomFileService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件服务
 *
 * @Author yrb
 * @Date 2023/8/24 11:16
 * @Version 1.0
 * @Description 文件服务
 */
@Service
public class CustomFileServiceImpl implements CustomFileService {
    private final RemoteSysAttachmentService remoteSysAttachmentService;

    public CustomFileServiceImpl(RemoteSysAttachmentService remoteSysAttachmentService){
        this.remoteSysAttachmentService = remoteSysAttachmentService;
    }

    /**
     * 获取文件
     *
     * @param ids        ID集合
     * @param bucketName 桶名
     * @return 列表
     */
    @Override
    public Map<Long, List<SysAttachment>> getFiles(List<Long> ids, String bucketName) {
        MinioDto minioDto = new MinioDto();
        minioDto.setBucketName(bucketName);
        minioDto.setIdList(ids);
        R<Map<Long, List<SysAttachment>>> r = remoteSysAttachmentService.getSysAttachmentMap(minioDto, SecurityConstants.INNER);
        if (r.getCode() == HttpStatus.SUCCESS) {
            return r.getData();
        }
        return new HashMap<>();
    }

    /**
     * 上传文件
     *
     * @param sysAttachmentList 文件信息
     * @return result
     */
    @Override
    public boolean uploadFiles(List<SysAttachment> sysAttachmentList) {
        MinioDto minioDto = new MinioDto();
        minioDto.setSysAttachmentList(sysAttachmentList);
        return remoteSysAttachmentService.transformByIds(minioDto, SecurityConstants.INNER);
    }

    /**
     * 更新文件
     *
     * @param sysAttachmentList 文件信息
     * @param id                主键ID
     */
    @Override
    public boolean updateFiles(List<SysAttachment> sysAttachmentList, Long id) {
        MinioDto minioDto = new MinioDto();
        minioDto.setId(id);
        minioDto.setSysAttachmentList(sysAttachmentList);
        R r = remoteSysAttachmentService.updateByPid(minioDto, SecurityConstants.INNER);
        if (r.getCode() == HttpStatus.SUCCESS) {
            return true;
        }
        return false;
    }
}
