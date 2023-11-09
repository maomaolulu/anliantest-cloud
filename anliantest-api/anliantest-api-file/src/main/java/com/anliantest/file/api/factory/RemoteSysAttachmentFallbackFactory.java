package com.anliantest.file.api.factory;

import com.anliantest.common.core.domain.R;
import com.anliantest.file.api.RemoteSysAttachmentService;
import com.anliantest.file.api.domain.ProgressManage;
import com.anliantest.file.api.domain.SysAttachment;
import com.anliantest.file.api.domain.dto.MinioDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.Map;

/**
 * @author ZhuYiCheng
 * @date 2023/8/8 10:56
 */
@Component
public class RemoteSysAttachmentFallbackFactory implements FallbackFactory<RemoteSysAttachmentService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteSysAttachmentFallbackFactory.class);

    @Override
    public RemoteSysAttachmentService create(Throwable throwable) {

        log.error("文件服务调用失败:{}", throwable.getMessage());
        return new RemoteSysAttachmentService() {

            @Override
            public Boolean transformByIds(MinioDto minioDto, String source) {
                return null;
            }

            @Override
            public R<Map<Long, List<SysAttachment>>> getSysAttachmentMap(MinioDto minioDto, String source) {
                return R.fail("获取附件列表map失败:" + throwable.getMessage());
            }

            @Override
            public R<String> updateByPid(MinioDto minioDto, String source) {
                return null;
            }

            @Override
            public Boolean updateById(SysAttachment sysAttachment, String source) {
                return false;
            }

            @Override
            public void deleteTempFile(String source) {
                log.error("delete TempFile fail");
            }

            @Override
            public R updateProgressManageById(ProgressManage progressManage, String source) {
                return null;
            }

            @Override
            public Long saveSysAttachment(SysAttachment sysAttachment, String source) {
                return null;
            }
        };
    }
}
