package com.anliantest.project.domain.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author gy
 * @date 2023-10-31 11:58
 */
@Data
public class CompanyPublicVo {
    /**
     * 客户id
     */
    private Long id;

    /**
     * 客户隶属
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
     * 最近合作日期
     */
    private Date contractLast;

    /**
     * 客户状态
     */
    private Integer customerStatus;

}
