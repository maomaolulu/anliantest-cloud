package com.anliantest.file.api;

import com.anliantest.common.core.constant.SecurityConstants;
import com.anliantest.common.core.constant.ServiceNameConstants;
import com.anliantest.common.core.domain.R;
import com.anliantest.file.api.domain.ProgressManage;
import com.anliantest.file.api.domain.SysAttachment;

import com.anliantest.file.api.domain.dto.MinioDto;
import com.anliantest.file.api.factory.RemoteSysAttachmentFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author ZhuYiCheng
 * @date 2023/8/8 10:55
 */
@FeignClient(contextId = "remoteSysAttachmentService", value = ServiceNameConstants.FILE_SERVICE, fallbackFactory = RemoteSysAttachmentFallbackFactory.class)
public interface RemoteSysAttachmentService {


    /**
     * 临时文件转有效文件
     *
     * @param minioDto  本方法用到 附件信息列表 sysAttachmentList
     * @param source 请求来源
     * @return result
     */
    @PostMapping("/minio/transformByIds")
    Boolean transformByIds(@RequestBody MinioDto minioDto, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);


    /**
     * 根据父级id获取附件列表map
     * @param minioDto  本方法用到  bucketName 桶名 ,idList 父级id列表
     * @param source 请求来源
     * @return result
     */
    @PostMapping("/minio/getSysAttachmentMap")
    R<Map<Long, List<SysAttachment>>> getSysAttachmentMap(@RequestBody MinioDto minioDto, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);


    /**
     * 根据父级id,新附件列表更新附件信息
     *
     * @param minioDto  本方法用到  id 父级id , sysAttachmentList 新附件列表
     * @param source 请求来源
     * @return result
     */
    @PostMapping("/minio/updateByPId")
    R<String> updateByPid(@RequestBody MinioDto minioDto, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 根据id更新附件信息
     *
     * @param sysAttachment 新附件信息
     * @param source 请求来源
     * @return result
     */
    @PostMapping("/minio/updateById")
    Boolean updateById(@RequestBody SysAttachment sysAttachment, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);


    /**
     * 每个月最后一天的23:59:59执行 定时清理临时文件
     * @param source 请求来源
     */
    @GetMapping("/minio/deleteTempFile")
    void deleteTempFile(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);
    /**
     * 跟新进度表状态
     *
     * @param progressManage 进度参数
     * @param source 请求来源
     * @return result
     */
    @PostMapping("/progress_manage/update")
    R updateProgressManageById(@RequestBody ProgressManage progressManage, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
    /**
     * 补充文件数据（后台文件上传到文件夹）
     *
     * @param sysAttachment  文件信息
     * @param source 请求来源
     * @return result
     */
    @PostMapping("/minio/save")
    Long saveSysAttachment(@RequestBody SysAttachment sysAttachment, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

}
