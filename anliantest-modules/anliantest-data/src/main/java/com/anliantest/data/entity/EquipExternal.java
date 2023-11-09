package com.anliantest.data.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * @author zhanghao
 * @email
 * @date 2023-08-18
 * @desc : 外部设备
 */
@Data
@TableName("equip_external")
public class EquipExternal implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 自增主键ID
     */
    @TableId
    private Long id;

    /**
     * 所属公司id
     */
    private String companyName;
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
     * 使用部门id
     */
    private Long useDeptId;

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
     * 证书有效期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date usefulTime;
    /**
     * 修正因子min
     */
    private Integer correctionFactorMin;
    /**
     * 修正因子max
     */
    private Integer correctionFactorMax;
    /**
     * 上次检定时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date verificationTime;
    /**
     * 逻辑删
     */
    private Integer del_flag;
    /**
     * 入库时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 入库人
     */

    private String createName;

    /**
     * 更新者
     **/
    private String updateBy;
    /**
     * 更新时间
     **/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

}
