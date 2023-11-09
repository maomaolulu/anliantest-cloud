package com.anliantest.project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author gy
 * @date 2023-08-24 15:50
 */
@Data
@Accessors(chain = true)
@TableName("area_address")
public class Chinas implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "region_id", type = IdType.AUTO)
    private Long regionId;
    /**
     * 区域代码
     */
    private Long regionCode;
    /**
     * 名称
     */
    private String regionName;
    /**
     * 地区缩写
     */
    private String regionShortName;
    /**
     * Pid
     */
    private Long regionParentId;
    /**
     * 级别(String)
     */
    @TableField(exist = false)
    private String level;
    /**
     * 经纬度
     */
    private String center;
    /**
     * 级别1省、2市、3县区
     */
    private Integer regionLevel;
    /**
     * 子集列表
     */
    @TableField(exist = false)
    private List<Chinas> districts;
    /**
     * 城市
     */
    @TableField(exist = false)
    private List<Chinas> cityList;

    /**
     * 县区
     */
    @TableField(exist = false)
    private List<Chinas> areaList;
}
