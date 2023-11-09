package com.anliantest.project.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author gy
 * @date 2023-08-18 17:28
 */
@Data
public class CustomCustomerVo implements Serializable {
    /**
     * 客户id
     */
    private Long customerId;

    /**
     * 任务id
     */
    private Long taskId;

    /**
     * 客户隶属人或者隶属公司
     */
    private String customerOrder;

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

//    /**
//     * 最近合作(最近的合同签订日期)
//     */
//    private Date lastCooperate;

    /**
     * 业务状态
     */
    private int businessStatus;

    /**
     * 客户状态(0:停用,1:正常)
     */
    private String customerStatus;
}
