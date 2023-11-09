package com.anliantest.project.domain.vo;

import com.anliantest.project.entity.CustomContacterEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author gy
 * @date 2023-08-21 15:30
 */
@Data
public class CompanyAndContactVo implements Serializable {
    /**
     * 客户隶属人或者客户隶属公司
     */
    private String customerOrder;
    /**
     * 客户状态
     */
    private Integer customerStatus;
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
     * 风险分类
     */
    private String riskClass;
    /**
     * 产品信息
     */
    private String productInfo;
    /**
     * 业务状态
     */
    private Integer businessStatus;
    /**
     * 联系人列表
     */
    @TableField(exist = false)
    private List<CustomContacterEntity> contacts;

    /**
     * 线索来源
     */
    private String clueFrom;
}
