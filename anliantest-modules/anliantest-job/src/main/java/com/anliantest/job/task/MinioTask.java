package com.anliantest.job.task;

import com.anliantest.common.core.constant.SecurityConstants;
import com.anliantest.file.api.RemoteSysAttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author ZhuYiCheng
 * @date 2023/8/9 19:23
 */
@Component("minioTask")
public class MinioTask {
    
    @Autowired
    private RemoteSysAttachmentService remoteSysAttachmentService;

    /**
     * 每个月最后一天的23:59:59执行 定时清理临时文件
     */
    public void deleteTempFile(){
        remoteSysAttachmentService.deleteTempFile(SecurityConstants.INNER);
    }

}
