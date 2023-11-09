package com.anliantest.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author yrb
 * @Date 2023/8/1 15:29
 * @Version 1.0
 * @Description 仪器设备采购记录
 */
@Data
@TableName("equip_purchase_record")
public class EquipPurchaseRecord implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 申请单号
     */
    private String purchaseCode;
    /**
     * 申请公司
     */
    @TableField(exist = false)
    private String companyName;
    /**
     * 申请部门
     */
    @TableField(exist = false)
    private String deptName;
    /**
     * 公司ID
     */
    private Long companyId;
    /**
     * 部门ID
     */
    private Long deptId;
    /**
     * 申请人ID
     */
    private Long userId;
    /**
     * 申请人
     */
    private String applier;
    /**
     * 采购人
     */
    private String payer;
    /**
     * 采购单价
     */
    private String singlePrice;
    /**
     * 到货时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date arrivedTime;
    /**
     * 物品名称
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
     * 采购数
     */
    private String actualAmount;
    /**
     * 待入库
     */
    private String remainAmount;
    /**
     * 供应商
     */
    private String supplier;
    /**
     *  创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 是否完全入库：0未完全入库，1完全入库
     */
    private String fullyStored;
    /**
     * 采购订单id
     */
    private Long orderId;
}
