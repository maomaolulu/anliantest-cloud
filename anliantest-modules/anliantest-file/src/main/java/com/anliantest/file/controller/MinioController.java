package com.anliantest.file.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.anliantest.common.core.domain.R;
import com.anliantest.common.log.annotation.Log;
import com.anliantest.common.security.annotation.InnerAuth;
import com.anliantest.file.api.domain.SysAttachment;
import com.anliantest.file.api.domain.dto.MinioDto;
import com.anliantest.file.domain.Res;
import com.anliantest.file.service.SysAttachmentService;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.DeleteError;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 文件服务
 *
 */
@Slf4j
@RestController
@RequestMapping("/minio")
public class MinioController {
    @Resource
    private MinioClient minioClient;

    private final SysAttachmentService attachmentService;


    @Autowired
    public MinioController(SysAttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }


    /**
     * 上传文件
     *
     */
    @PostMapping("/upload")
    public Res upload(SysAttachment sysAttachment) {

        return attachmentService.upload(sysAttachment);
    }
    /**
     * 后台上传文件
     *
     */
    @PostMapping("/save")
    public Long internalUpload(@RequestBody SysAttachment sysAttachment) {

        return  attachmentService.internalUpload(sysAttachment);
    }
    /**
     * 修改文件（根据id修改）
     *
     */
    @PostMapping("/updateById")
    public Boolean updateById(@RequestBody SysAttachment sysAttachment) {
        return    attachmentService.updateById(sysAttachment);
    }


    /**
     * 临时文件转有效文件
     *
     */
    @PostMapping("/transform")
    public int transform() {
        return attachmentService.transform();
    }


    /**
     * 根据id临时文件转有效文件
     *
     */
    @InnerAuth
    @PostMapping("/transformByIds")
    public Boolean transformByIds(@RequestBody MinioDto minioDto) {
        List<SysAttachment> sysAttachmentList = minioDto.getSysAttachmentList();
        return attachmentService.transformByIds(sysAttachmentList);
    }


    /**
     * 根据父级id,新附件列表更新附件信息
     *
     */
    @InnerAuth
    @PostMapping("/updateByPId")
    public R<String> updateByPid(@RequestBody MinioDto minioDto) {
        Long id = minioDto.getId();
        List<SysAttachment> sysAttachmentList = minioDto.getSysAttachmentList();
        return attachmentService.updateByPid(id, sysAttachmentList);
    }


    /**
     * 根据父级id获取附件列表map
     *
     */
    @InnerAuth
    @PostMapping("/getSysAttachmentMap")
    public R<Map<Long, List<SysAttachment>>> getSysAttachmentMap(@RequestBody MinioDto minioDto) {
        String bucketName = minioDto.getBucketName();
        List<Long> idList = minioDto.getIdList();
        Map<Long, List<SysAttachment>> sysAttachmentMap = attachmentService.getSysAttachmentMap(bucketName, idList);
        if (CollectionUtil.isNotEmpty(sysAttachmentMap)) {
            return R.ok(sysAttachmentMap);
        } else {
            return R.fail();
        }
    }

    /**
     * 每个月最后一天的23:59:59执行 定时清理临时文件
     */
    @InnerAuth
    @GetMapping("/deleteTempFile")
    public void deleteTempFile(){

        attachmentService.deleteTempFile();
    }


    /**
     * 获取文件列表
     *
     */
    @GetMapping("/list")
    public List<SysAttachment> getList(@RequestParam("bucketName") String bucketName) {
        List<SysAttachment> list = attachmentService.getList(bucketName);
        for (SysAttachment attachment : list) {
            attachment.setPreUrl(getFileUrl(bucketName, attachment.getPath()));
        }
        return list;
    }



    /**
     * 预览url
     *
     */
    @SneakyThrows(Exception.class)
    private String getFileUrl(String bucketName, String path) {

        return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().bucket(bucketName).object(path).method(Method.GET).expiry(60, TimeUnit.MINUTES).build());
    }


    /**
     * 获取url
     *
     */
    @SneakyThrows(Exception.class)
    @GetMapping("/getUrls")
    public String getFileUrls(String bucketName, String path) {

        return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().bucket(bucketName).object(path).method(Method.GET).build());
    }

    /**
     * 下载
     *
     */
    @PostMapping("/download")
    public void download(HttpServletResponse response, @RequestParam(name = "bucketName") String bucketName,
                         @RequestParam(name = "path") String path, @RequestParam(name = "fileName") String fileName) {
        // 打印参数
        System.out.println("bucketName:" + bucketName + " path:" + path + " fileName:" + fileName);
        InputStream in = null;
        try {

            StatObjectResponse stat = minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(path).build());

            response.setContentType(stat.contentType());

            fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName);

            in = minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(path).build());
            IOUtils.copy(in, response.getOutputStream());
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        }
    }


    /**
     * 预览文件
     *
     */
    @GetMapping("/pre_file")
    public void preFile(HttpServletResponse response, @RequestParam(name = "bucketName") String bucketName, @RequestParam(name = "path") String path) {
        // 打印参数
        System.out.println("bucketName:" + bucketName + " path:" + path);
        InputStream in = null;
        try {

            StatObjectResponse stat = minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(path).build());

            response.setContentType(stat.contentType());

            in = minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(path).build());
            IOUtils.copy(in, response.getOutputStream());
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        }
    }

    /**
     * 删除文件
     *
     */
    @GetMapping("/delete")
    @Log(title = "删除文件")
    @Transactional(rollbackFor = Exception.class)
    public Res delete(String bucketName, String path) {
        Res res = new Res();
        res.setCode(200);
        res.setMessage("删除成功");
        try {
            attachmentService.delete(bucketName, path);
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(path).build());
        } catch (Exception e) {
            res.setCode(500);
            res.setMessage("删除失败");
            log.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return res;
    }


    /**
     * 批量删除文件
     *
     */
    @GetMapping("/deleteBatch")
    @Log(title = "批量删除文件")
    @Transactional(rollbackFor = Exception.class)
    public List<DeleteError> deleteBatch(@RequestParam("pathList") List<String> pathList, @RequestParam("bucketName") String bucketName) {

        return attachmentService.deleteBatch(pathList, bucketName);
    }


    /**
     * 删除文件记录和文件
     *
     */
    @SneakyThrows(Exception.class)
    private void extractedDelete(String bucketName, String path) {
        attachmentService.delete(bucketName, path);
        minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(path).build());
    }

}

