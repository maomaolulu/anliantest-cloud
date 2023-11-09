package com.anliantest.project.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 客户表
 * @author gy
 * @date 2023-08-18 15:24
 */
@Data
@TableName("custom_customer")
public class CustomCustomerEntity implements Serializable {
    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 是否为新客户
     */
    private Integer ifNewCompany;
    /**
     * 客户隶属人
     */
    private String customerOrder;
    /**
     * 客户隶属公司
     */
    private String customerCompany;
    /**
     * 客户状态(0:停用,1:正常)
     */
    private String customerStatus;
    /**
     * 企业名称
     */
    private String enterpriseName;
    /**
     * 社会统一信用代码
     */
    private String creditCode;
    /**
     * 省
     */
    private String province;
    /**
     * 市
     */
    private String city;
    /**
     * 区
     */
    private String district;
    /**
     * 注册地址
     */
    private String registeredAddress;
    /**
     * 受检地址
     */
    private String inspectionAddress;
    /**
     * 法人代表
     */
    private String legalPerson;
    /**
     * 行业类别
     */
    private String industryCategory;
    /**
     * 人员规模
     */
    private String staffSize;
    /**
     * 风险风分类
     */
    private String riskClass;
    /**
     * 产品信息
     */
    private String productInfo;
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

    /**
     * 线索来源
     */
    private String clueFrom;
}
