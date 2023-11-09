package com.anliantest.project.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 客户联系方式表
 * @author gy
 * @date 2023-08-18 16:20
 */
@Data
@TableName("custom_contacters")
public class CustomContacterEntity implements Serializable {
    /**
     * id
     */
    private Long id;
    /**
     * 客户id
     */
    private Long customerId;
    /**
     * 联系人姓名
     */
    private String contacterName;
    /**
     * 是否默认(0:否,1:是)
     */
    private Integer ifDefault;
    /**
     * 联系人类型
     */
    private String contactClass;
    /**
     * 固定电话
     */
    private String fixedPhone;
    /**
     * 移动电话
     */
    private Long mobilePhone;
    /**
     * 邮箱
     */
    private String mail;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 更新人
     */
    private String updateBy;
    /**
     * 删除标记
     */
    private int deleteFlag;
}
