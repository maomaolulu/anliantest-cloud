package com.anliantest.data.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zh
 * @date 2023-08-04
 */
@Data
@Accessors(chain = true)
public class EquipWarehouseRecordDto implements Serializable {

    /**
     * ID
     */
    private Long id ;
    /** 申请编号 */
    private String purchaseCode ;
    /** 所属公司id */
    private Long companyId ;
    /** 设备类型 */
    private Long equipType ;
    /** 设备编号 */
    private String equipCode ;
    /** 设备名称 */
    private String goodsName ;
    /** 规格型号 */
    private String model ;
    /** 单位 */
    private String unit ;
    /** 供应商 */
    private String supplier ;
    /** 生产厂家 */
    private String productCompany ;
    /** 出厂编号 */
    private String factoryCode ;
    /** 当前估值 */
    private String value ;
    /** 责任部门id */
    private Long chargeDeptId ;
    /** 责任人id */
    private Long chargeId ;
    /** 保管人id */
    private Long keeperId ;
    /** 使用部门id */
    private Long useDeptId ;
    /** 检定/校准 */
    private String calibration ;
    /** 证书有效期 */
    private Date useful_time ;
    /** 精度 */
    private String precision ;
    /** 允许误差 */
    private String allowanceError ;
    /** 测量范围 */
    private String testRange ;
    /** 测量范围;系统）-min */
    private String testRangeMin ;
    /** 测量范围;系统）-max */
    private String testRangeMax ;
    /** 入库类型：0直接入库、1采购入库 */
    private Integer warehouseType ;
    /** 打印标签：0未打印、1已打印 */
    private Integer printLabel ;
    /** 入库时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime ;
    /** 入库人 */
    private String createName ;
    /** 打印条码 */
    private String labelCode ;

    /** 责任人名称 */
    private String chargeName ;

    /** 责任人名称 */
    private Date startTime ;

    /** 责任人名称 */
    private Date endTime ;



}
