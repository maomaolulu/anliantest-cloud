package com.anliantest.data.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 期间核查vo
 *
 * @author: liyongqiang
 * @create: 2023-08-08 13:15
 */
@Data
public class PeriodCheckVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 设备id
     **/
    private Long equipId;
    /**
     * 设备编号
     **/
    private String equipCode;
    /**
     * 设备名称
     **/
    private String goodsName;
    /**
     * 规格型号
     **/
    private String model;
    /**
     * 状态：0待核查，1核查完成
     **/
    private Integer verifyState;
    /**
     * 责任部门id
     **/
    private Long chargeDeptId;
    /**
     * 证书有效期
     */
    private String expirationDate;
    /**
     * 核查日期
     **/
    private String verifyDate;

}
