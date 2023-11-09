package com.anliantest.file.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * minio文件处理相关接口
 * @Author maoly
 * @Date 2023/7/26 14:05
 */
public interface ISysFileMinioService {

    /**
     * 文件上传接口
     *
     * @param file 上传的文件
     * @return 访问地址
     * @throws Exception
     */
    public String uploadFile(MultipartFile file) throws Exception;
}
