package com.anliantest.project.domain.dto;

import com.anliantest.project.entity.CustomContacterEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author gy
 * @date 2023-08-18 17:16
 */
@Data
public class CustomCustomerDto implements Serializable {
    /**
     * id (占用)
     */
    private Long id;
    /**
     * 客户id
     */
    private Long customerId;
    /**
     * 客户隶属人
     */
    private String customerOrder;
    /**
     * 客户隶属公司
     */
    private String customerCompany;
    /**
     * 企业名称
     */
    private String enterpriseName;
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
     * 最近最近合作开始
     */
    private Date advanceTimeStart;
    /**
     * 最近最近合作结束
     */
    private Date advanceTimeEnd;
    /**
     * 业务状态
     */
    private Integer businessStatus;
    /**
     * 客户状态(0:停用,1:正常)
     */
    private String customerStatus;

    // 以上是查询条件 部分字段可以通用与创建用户和修改用户
    /**
     * 社会统一信用代码
     */
    private String creditCode;
    /**
     * 注册地址
     */
    private String registeredAddress;
    /**
     * 受检地址
     */
    private String inspectionAddress;
    /**
     * 联系人列表
     */
    private List<CustomContacterEntity> contacts;

    /**
     * 业务员id
     */
    private Long businessPeopleId;

    /**
     * 线索来源
     */
    private String clueFrom;
}

