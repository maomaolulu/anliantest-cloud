package com.anliantest.data.entity;

import com.anliantest.file.api.domain.SysAttachment;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Author yrb
 * @Date 2023/8/1 16:15
 * @Version 1.0
 * @Description 仪器设备入库记录
 */
@Data
@TableName("equip_warehouse_record")
public class EquipWarehouseRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 申请编号
     */
    private String purchaseCode;
    /**
     * 所属公司id
     */
    private Long companyId;
    /**
     * 设备类型
     */
    private Long equipType;
    /**
     * 设备编号
     */
    private String equipCode;
    /**
     * 设备名称
     */
    private String goodsName;
    /**
     * 规格型号
     */
    private String model;
    /**
     * 单位
     */
    private String unit;
    /**
     * 采购单价
     */
    private BigDecimal singlePrice;
    /**
     * 采购时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date purchaseDate;
    /**
     * 供应商
     */
    private String supplier;
    /**
     * 生产厂家
     */
    private String productCompany;
    /**
     * 出厂编号
     */
    private String factoryCode;
    /**
     * 当前估值
     */
    private String value;
    /**
     * 责任部门id
     */
    private Long chargeDeptId;
    /**
     * 责任人id
     */
    private Long chargeId;
    /**
     * 保管人id
     */
    private Long keeperId;
    /**
     * 使用部门id
     */
    private Long useDeptId;
    /**
     * 检定/校准
     */
    private String calibration;
    /**
     * 证书有效期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date usefulTime;
    /**
     * 精度
     */
    @TableField("`precision`")
    private String precision;
    /**
     * 允许误差
     */
    private String allowanceError;
    /**
     * 测量范围
     */
    private String testRange;
    /**
     * 测量范围;系统）-min
     */
    private String testRangeMin;
    /**
     * 测量范围;系统）-max
     */
    private String testRangeMax;
    /**
     * 入库类型：0直接入库、1采购入库
     */
    private Integer warehouseType;
    /**
     * 打印标签：0未打印、1已打印
     */
    private Integer printLabel;
    /**
     * 上传图片：0未上传、1已上传
     */
    private Integer hasImage;
    /**
     * 入库时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 入库人
     */
    private String createName;
    /**
     * 打印条码
     */
    private String labelCode;
    /**
     * 设备状态：1在用，4维修，6报废，7外借，8停用，10库存
     */
    private Integer status;
    /**
     * 检定状态：0无需检定，1待送检，2送检中
     */
    private Integer verificationStatus;
    /**
     * 期间核查标识：0待核查（默认），1核查完成
     */
    private Integer verifyState;
    /**
     * 核查日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date verifyDate;
    /**
     * 更新者
     **/
    private String updateBy;
    /**
     * 更新时间
     **/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
    /**
     * 入库数量
     **/
    private String warehouseAmount;
    /**
     * 到货时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date arrivedTime;

    /**
     * 采购记录主键id
     */
    @TableField(exist = false)
    private Long oldId;

    /**
     * 图片上传
     */
    @TableField(exist = false)
    private List<SysAttachment> sysAttachmentList;

    /**
     * 公司名称
     **/
    @TableField(exist = false)
    private String companyName;

    /**
     * 采购订单ID
     */
    @TableField(exist = false)
    private Long orderId;
}
