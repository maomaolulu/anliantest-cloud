package com.anliantest.file.service.impl;

import cn.hutool.extra.spring.SpringUtil;
import com.anliantest.common.core.constant.MinioConstants;
import com.anliantest.common.core.domain.R;
import com.anliantest.common.core.enums.DeleteFlag;
import com.anliantest.common.core.utils.DateUtils;
import com.anliantest.common.core.utils.StringUtils;
import com.anliantest.common.core.utils.file.FileUtils;
import com.anliantest.common.security.utils.SecurityUtils;
import com.anliantest.file.api.domain.SysAttachment;
import com.anliantest.file.domain.Res;
import com.anliantest.file.mapper.SysAttachmentMapper;
import com.anliantest.file.service.SysAttachmentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * @author zx
 * @date 2021/12/19 10:07
 */
@Service
@Slf4j
public class SysAttachmentServiceImpl extends ServiceImpl<SysAttachmentMapper, SysAttachment> implements SysAttachmentService {

    private final MinioClient minioClient;
    private final SysAttachmentMapper attachmentMapper;

    @Autowired
    public SysAttachmentServiceImpl(SysAttachmentMapper attachmentMapper, MinioClient minioClient) {
        this.attachmentMapper = attachmentMapper;
        this.minioClient = minioClient;
    }


    /**
     * 上传文件
     */
    @Override
    public Res upload(SysAttachment sysAttachment) {
        Res res = new Res();
        res.setCode(500);

        MultipartFile[] file = sysAttachment.getFile();
        String bucketName = sysAttachment.getBucketName();

        if (file == null || file.length == 0) {
            res.setMessage("上传文件不能为空");
            return res;
        }

        List<Map<String, Object>> originFileNameList = new ArrayList<>(file.length);

        try {
            for (MultipartFile multipartFile : file) {
                Date now = DateUtils.getNowDate();
                Date date = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyy/MM");
                String timestamp = format.format(now);
                String timestamp1 = String.valueOf(date.getTime());
                String originFileName = multipartFile.getOriginalFilename();
                assert originFileName != null;
                String type = originFileName.substring(originFileName.lastIndexOf("."));
                String path = timestamp.concat("/" + timestamp1 + originFileName);
                InputStream in = multipartFile.getInputStream();
                PutObjectArgs args = PutObjectArgs.builder()
                        //路径
                        .bucket(bucketName)
                        //文件名
                        .object(path)
                        //流
                        .stream(in, multipartFile.getSize(), -1)
                        //后缀
                        .contentType(multipartFile.getContentType())
                        .build();
                // 没有bucket则创建
                boolean exist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
                if (!exist) {
                    minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                }
                minioClient.putObject(args);
                in.close();
                sysAttachment.setFileName(originFileName);
                sysAttachment.setPath(path);
                sysAttachment.setFileType(type);
                sysAttachment.setTempId(MinioConstants.TEMPORARY);
                sysAttachment.setDelFlag(DeleteFlag.NO.ordinal());
                //保存附件信息
                this.addAttachment(sysAttachment);

                Map<String, Object> map = new HashMap<>(3);
                String fileUrl = getFileUrl(bucketName, path);
                String preUrl = fileUrl.substring(fileUrl.lastIndexOf("/al-"));
                map.put("preUrl", preUrl);
                map.put("path", path);
                map.put("fileName", originFileName);
                map.put("fileType", type);
                map.put("id", sysAttachment.getId());
                originFileNameList.add(map);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            res.setMessage("上传失败");
            //设置手动回滚
            return res;
        }

        Map<String, Object> data = new HashMap<>(1);
        data.put("url", originFileNameList);
        res.setCode(200);
        res.setMessage("上传成功");
        res.setData(data);

        return res;
    }


    /**
     * 后台上传文件
     */
    @Override
    public Long internalUpload(SysAttachment sysAttachment) {

        //文件路径
        String filePath =  sysAttachment.getPath() ;
        //桶名
        String bucketName = MinioConstants.PROGRESS_MANAGE;
        InputStream in =null;
        try {
            File file = new File(filePath);
            //文件名
            String originFileName = file.getName();
            //后缀
            String type = originFileName.substring(originFileName.lastIndexOf("."));
            //路径

            Date now = DateUtils.getNowDate();
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM");
            String timestamp = format.format(now);
            String timestamp1 = String.valueOf(date.getTime());
            String path = timestamp.concat("/" + timestamp1 + originFileName);


             in = new FileInputStream(filePath);

            PutObjectArgs args = PutObjectArgs.builder()
                    //路径
                    .bucket(bucketName)
                    //文件名
                    .object(path)
                    //流
                    .stream(in, file.length(), -1)
                    //后缀
                    .contentType(type)
                    .build();
            // 没有bucket则创建
            boolean exist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!exist) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
            minioClient.putObject(args);
            sysAttachment.setFileName(originFileName);
            sysAttachment.setPath(path);
            sysAttachment.setFileType(type);
            sysAttachment.setDelFlag(DeleteFlag.NO.ordinal());
            sysAttachment.setBucketName(bucketName);
            //临时
            sysAttachment.setTempId(1);
            //保存附件信息
            this.addAttachment(sysAttachment);

            return sysAttachment.getId();
        } catch (Exception e) {

            log.error(e.getMessage());
            return null;
        }finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                //删除临时文件
                FileUtils.deleteFile(filePath);
            }catch (Exception e){
                log.error("删除临时文件失败："+filePath+": "+e);
            }

        }

    }


    /**
     * 保存附件信息
     */
    @Override
    public void addAttachment(SysAttachment attachment) {
        String usernameCn = SecurityUtils.getUsernameCn();
        attachment.setCreatedBy(usernameCn);
        attachment.setUpdatedBy(usernameCn);
        attachment.setCreatedTime(new Date());
        attachment.setUpdatedTime(new Date());
        attachmentMapper.insert(attachment);
    }

    /**
     * 临时文件转有效文件
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int transform() {
        try {
            String usernameCn = SecurityUtils.getUsernameCn();
            QueryWrapper<SysAttachment> wrapper = new QueryWrapper<>();
            wrapper.eq("temp_id", MinioConstants.TEMPORARY);

            List<SysAttachment> sysAttachments = attachmentMapper.selectList(wrapper);
            if (sysAttachments.isEmpty()) {
                return 1;
            }
            sysAttachments.forEach(attachment -> {
                attachment.setUpdatedBy(usernameCn);
                attachment.setUpdatedTime(new Date());
                attachment.setTempId(MinioConstants.FOREVER);
                attachmentMapper.updateById(attachment);
            });

            return 1;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return 0;
        }

    }


    /**
     * 据id临时文件转有效文件
     */
    @Override
    public Boolean transformByIds(@RequestBody List<SysAttachment> sysAttachmentList) {

        return this.updateBatchById(sysAttachmentList);
    }


    /**
     * 根据父级id获取附件列表map
     */
    @Override
    public Map<Long, List<SysAttachment>> getSysAttachmentMap(String bucketName, List<Long> idList) {
        try {
            //根据桶名和档案id获取附件列表
            List<SysAttachment> sysAttachmentList = this.list(new QueryWrapper<SysAttachment>()
                    .in("p_id", idList)
                    .eq(StringUtils.isNotBlank(bucketName), "bucket_name", bucketName)
                    .eq("temp_id", MinioConstants.FOREVER)
                    .eq("del_flag", DeleteFlag.NO.ordinal()));
            //给每一个文件拼preUrl
            for (SysAttachment sysAttachment : sysAttachmentList) {
                String fileUrl = getFileUrl(sysAttachment.getBucketName(), sysAttachment.getPath());
                String preUrl = fileUrl.substring(fileUrl.lastIndexOf("/al-"));
                sysAttachment.setPreUrl(preUrl);
            }
            return sysAttachmentList.stream()
                    .collect(Collectors.groupingBy(SysAttachment::getPId));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 根据父级id,新附件列表更新附件信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<String> updateByPid(Long id, List<SysAttachment> sysAttachmentList) {

        Date nowDate = DateUtils.getNowDate();
        String usernameCn = SecurityUtils.getUsernameCn();
        List<SysAttachment> list = this.list(new QueryWrapper<SysAttachment>().eq(id != null, "p_id", id)
                .eq("temp_id", MinioConstants.FOREVER)
                .eq("del_flag", DeleteFlag.NO.ordinal()));
        for (SysAttachment sysAttachment : list) {
            sysAttachment.setTempId(MinioConstants.TEMPORARY);
            sysAttachment.setUpdatedBy(usernameCn);
            sysAttachment.setUpdatedTime(nowDate);
        }
        boolean b = this.updateBatchById(list);
        for (SysAttachment sysAttachment : sysAttachmentList) {
            sysAttachment.setPId(id);
            sysAttachment.setTempId(MinioConstants.FOREVER);
            sysAttachment.setUpdatedBy(usernameCn);
            sysAttachment.setUpdatedTime(nowDate);
        }
        boolean b1 = this.updateBatchById(sysAttachmentList);
        return b && b1 ? R.ok("转换成功") : R.fail("转换失败");
    }

    /**
     * 获取附件列表
     */
    @Override
    public List<SysAttachment> getList(String bucketName) {
        QueryWrapper<SysAttachment> wrapper = new QueryWrapper<>();
        wrapper.eq("bucket_name", bucketName)
                .eq("temp_id", MinioConstants.FOREVER)
                .eq("del_flag", DeleteFlag.NO.ordinal());
        return attachmentMapper.selectList(wrapper);
    }


    /**
     * 删除附件
     */
    @Override
    public void delete(String bucketName, String path) {
        QueryWrapper<SysAttachment> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(bucketName), "bucket_name", bucketName);
        wrapper.eq(StringUtils.isNotBlank(path), "path", path);
        attachmentMapper.delete(wrapper);
    }

    /**
     * 获取临时文件
     */
    @Override
    public List<SysAttachment> getTempList() {

        return attachmentMapper.selectList(new QueryWrapper<SysAttachment>()
                .eq("temp_id", MinioConstants.TEMPORARY));
    }


    /**
     * 批量删除文件
     */
    @Override
    public List<DeleteError> deleteBatch(List<String> pathList, String bucketName) {
        List<DeleteError> deleteErrors = new ArrayList<>();
        List<DeleteObject> deleteObjects = pathList.stream()
                .map(DeleteObject::new).collect(Collectors.toList());
        this.remove(new QueryWrapper<SysAttachment>()
                .eq(StringUtils.isNotBlank(bucketName), "bucketName", bucketName)
                .in("path", pathList));
        Iterable<Result<DeleteError>> results =
                minioClient.removeObjects(
                        RemoveObjectsArgs
                                .builder()
                                .bucket(bucketName)
                                .objects(deleteObjects)
                                .build());
        try {
            for (Result<DeleteError> result : results) {
                DeleteError error = result.get();
                deleteErrors.add(error);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deleteErrors;
    }


    /**
     * 预览url
     */
    @SneakyThrows(Exception.class)
    private String getFileUrl(String bucketName, String path) {

        return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                .bucket(bucketName).object(path).method(Method.GET).expiry(60, TimeUnit.MINUTES).build());
    }


    /**
     * 每个月最后一天的23:59:59执行 定时清理临时文件
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTempFile() {
        log.info("开始执行删除临时文件");
        SysAttachmentService attachmentService = SpringUtil.getBean(SysAttachmentService.class);
        MinioClient minioClient = SpringUtil.getBean(MinioClient.class);
        try {
            List<SysAttachment> tempList = attachmentService.getTempList();
            for (SysAttachment attachment : tempList) {
                log.info("删除临时文件：" + attachment.getBucketName() + "---" + attachment.getPath() + "---" + attachment.getTempId());
                String bucketName = attachment.getBucketName();
                String path = attachment.getPath();
                attachmentService.delete(bucketName, path);
                minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(path).build());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }

}
