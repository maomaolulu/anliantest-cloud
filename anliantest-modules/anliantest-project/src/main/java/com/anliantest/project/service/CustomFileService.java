package com.anliantest.project.service;

import com.anliantest.file.api.domain.SysAttachment;

import java.util.List;
import java.util.Map;

/**
 * 文件服务
 *
 * @Author yrb
 * @Date 2023/8/24 11:15
 * @Version 1.0
 * @Description 文件服务
 */
public interface CustomFileService {
    /**
     * 获取文件
     *
     * @param ids        ID集合
     * @param bucketName 桶名
     * @return 列表
     */
    Map<Long, List<SysAttachment>> getFiles(List<Long> ids, String bucketName);

    /**
     * 上传文件
     *
     * @param sysAttachmentList 文件信息
     * @return result
     */
    boolean uploadFiles(List<SysAttachment> sysAttachmentList);

    /**
     * 更新文件
     *
     * @param sysAttachmentList 文件信息
     * @param id 主键ID
     */
    boolean updateFiles(List<SysAttachment> sysAttachmentList, Long id);
}
