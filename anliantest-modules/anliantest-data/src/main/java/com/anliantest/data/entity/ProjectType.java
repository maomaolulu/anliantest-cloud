package com.anliantest.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * @author zhanghao
 * @email
 * @date 2023-07-17
 * @desc : 项目类型管理表
 */
@Data
@TableName("data_project_type")
public class ProjectType implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 自增主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 所属公司
     */
    private Long companyId;
    /**
     * 所属公司名称
     */
    @TableField(exist = false)
    private String companyName;
    /**
     * 类型编号
     */
    private String code;
    /**
     * 类型名称
     */
    private String name;
    /**
     * 状态（0：在用，1：禁用）
     */
    private Integer status;
    /**
     * 是否产出报告(0:否，1：是)
     */
    private Integer isOutputReport;
    /**
     * 是否线上生产(0:否，1：是)
     */
    private Integer isOnlineProduction;
    /**
     * 是否需公示(0:否，1：是)
     */
    private Integer isPublicity;
    /**
     * 是否需采样(0:否，1：是)
     */
    private Integer isSampling;
    /**
     * 是否需检测(0:否，1：是)
     */
    private Integer isDetection;
    /**
     * 检测类型(0:无，1：仅物理，2：仅化学，3：自定义)
     */
    private Integer detectionType;
    /**
     * 创建人id
     */
    private Integer createById;
    /**
     * 创建人名称
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

}
