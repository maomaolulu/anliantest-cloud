package com.anliantest.project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 合同范本基础信息表
 * @author ZhuYiCheng
 * @date 2023/10/13 17:26
 */
@Data
@TableName("contract_sample")
public class ContractSample implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 合同范本名称
     */
    private String name;
    /**
     * 合同类型id
     */
    private Long contractTypeId;
    /**
     * 合同类型名称
     */
    @TableField(exist = false)
    private String contractTypeName;
    /**
     * 合同范本状态;0 停用，1正常)
     */
    private Integer status;
    /**
     * 所属公司id
     */
    private Long companyId;
    /**
     * 所属公司名称
     */
    @TableField(exist = false)
    private String companyName;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 逻辑删除
     */
    private Integer delFlag;
    /**
     * 创建人id
     */
    private Long createById;
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
     * 合同管理条款
     */
    @TableField(exist = false)
    private List<ContractSampleTerm> contractSampleTerms;
}
