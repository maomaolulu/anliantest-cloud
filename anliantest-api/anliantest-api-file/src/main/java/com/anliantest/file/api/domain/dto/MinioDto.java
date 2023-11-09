package com.anliantest.file.api.domain.dto;

import com.anliantest.file.api.domain.SysAttachment;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author ZhuYiCheng
 * @date 2023/8/9 17:52
 */
@Data
public class MinioDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 父级id
     */
    private Long id;
    /**
     * 附件List
     */
    private List<SysAttachment> sysAttachmentList;
    /**
     * 桶名
     */
    private String bucketName;
    /**
     * 父级idList
     */
    private List<Long> idList;
}
