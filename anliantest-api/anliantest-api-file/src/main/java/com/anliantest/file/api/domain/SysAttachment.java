package com.anliantest.file.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.Date;

/**
 * 附件表
 *
 * @author zx
 * @date 2021/12/19 9:31
 */
@Data
@TableName("sys_attachment")
public class SysAttachment implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 上传文件列表
     */
    @TableField(exist = false)
    private MultipartFile[] file;
    /**
     * 桶名
     */
    private String bucketName;
    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 文件类型
     */
    private String fileType;
    /**
     * 文件路径
     */
    private String path;
    /**
     * 父级Id即对应业务表id
     */
    private Long pId;
    /**
     * 临时唯一标识(0有效1临时)
     */
    private Integer tempId;
    /**
     * 备注
     */
    private String remarks;

    /**
     * 逻辑删
     */
    private Integer delFlag;
    /**
     * 创建人
     */
    private String createdBy;
    /**
     * 创建时间
     */
    private Date createdTime;
    /**
     * 更新人
     */
    private String updatedBy;
    /**
     * 更新时间
     */
    private Date updatedTime;
    /**
     * 预览链接
     */
    @TableField(exist = false)
    private String preUrl;
}
