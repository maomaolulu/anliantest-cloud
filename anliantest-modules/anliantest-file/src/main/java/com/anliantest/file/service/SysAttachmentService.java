package com.anliantest.file.service;

import com.anliantest.common.core.domain.R;
import com.anliantest.file.api.domain.SysAttachment;
import com.anliantest.file.domain.Res;

import com.baomidou.mybatisplus.extension.service.IService;
import io.minio.messages.DeleteError;

import java.util.List;
import java.util.Map;

/**
 * @author zx
 * @date 2021/12/19 10:07
 */
public interface SysAttachmentService extends IService<SysAttachment> {


    /**
     * 上传文件
     * @param sysAttachment 附件信息
     * @return result
     */
    Res upload(SysAttachment sysAttachment);
    /**
     * 后台上传文件
     * @param sysAttachment 附件信息
     * @return result
     */
    Long internalUpload(SysAttachment sysAttachment);

    /**
     * 保存附件信息
     * @param attachment 附件信息
     *
     */
    void addAttachment(SysAttachment attachment);

    /**
     * 临时文件转有效文件
     * @return result
     */
    int transform();

    /**
     * 据id临时文件转有效文件
     * @param sysAttachmentList 附件信息列表
     * @return result
     */
    Boolean transformByIds(List<SysAttachment> sysAttachmentList);

    /**
     * 获取附件列表
     * @param bucketName 桶名
     * @return 查询结果
     */
    List<SysAttachment> getList(String bucketName);

    /**
     * 删除附件
     * @param bucketName 桶名
     * @param path 附件路径
     */
    void delete(String bucketName, String path);

    /**
     * 获取临时文件
     * @return 附件信息列表
     */
    List<SysAttachment> getTempList();

    /**
     * 批量删除文件
     * @param pathList 附件路径列表
     * @param bucketName 桶名
     * @return result
     */
    List<DeleteError> deleteBatch(List<String> pathList, String bucketName);


    /**
     * 根据父级id获取附件列表map
     * @param bucketName 桶名
     * @param idList 父级id列表
     * @return 查询结果
     */
    Map<Long, List<SysAttachment>> getSysAttachmentMap(String bucketName, List<Long> idList);

    /**
     * 根据父级id,新附件列表更新附件信息
     * @param id 父级id
     * @param sysAttachmentList 新附件列表
     * @return result
     */
    R<String> updateByPid(Long id, List<SysAttachment> sysAttachmentList);

    /**
     * 每个月最后一天的23:59:59执行 定时清理临时文件
     */
    void deleteTempFile();
}
