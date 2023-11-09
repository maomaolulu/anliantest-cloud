package com.anliantest.data.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: liyongqiang
 * @create: 2023-08-07 10:40
 */
@Data
@TableName("equip_maintain_record")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class EquipMaintainRecord implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     **/
    @TableId
    private Long id;
    /**
     * 设备id
     */
    private Long equipId;
    /**
     * 设备故障时间（YYYY-MM-DD hh:00）
     **/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date breakdownTime;
    /**
     * 完成维修时间（年月日时）
     **/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date completeTime;
    /**
     * 维修费用
     **/
    private BigDecimal repairCost;
    /**
     * 维修状态：0待维修（默认），1已维修
     **/
    private Integer repairStatus;
    /**
     * 创建者
     **/
    @TableField(fill = FieldFill.INSERT)
    private String createBy;
    /**
     * 创建时间
     **/
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 更新者
     **/
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;
    /**
     * 更新时间
     **/
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    /**
     * 逻辑删除（0代表存在，1代表删除）
     **/
    private Integer delFlag;
    /**
     * 故障原因
     **/
    private String remark;

}
