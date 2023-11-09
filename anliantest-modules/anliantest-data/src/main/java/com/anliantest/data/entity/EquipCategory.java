package com.anliantest.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author yrb
 * @Date 2023/8/14 17:59
 * @Version 1.0
 * @Description 仪器类型
 */
@Data
@TableName("equip_category")
public class EquipCategory implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 自增主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 类型名称
     */
    private String categoryName;

    /**
     * 父类id
     */
    private Long parentId;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 逻辑删除：0正常，1删除
     */
    private Integer delFlag;

    /**
     * 子类
     */
    @TableField(exist = false)
    private List<EquipCategory> chidren;
}
