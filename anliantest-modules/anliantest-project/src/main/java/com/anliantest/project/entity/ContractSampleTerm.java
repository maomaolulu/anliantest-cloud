package com.anliantest.project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 合同范本与合同条款关联表
 * @author ZhuYiCheng
 * @date 2023/10/13 17:28
 */
@Data
@TableName("contract_sample_term")
public class ContractSampleTerm implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 合同范本ID
     */
    private Long contractSampleId;
    /**
     * 合同条款ID
     */
    private Long contractTermId;
    /**
     * 是否必填;0：否，1：是
     */
    private Integer must;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 条款类型
     */
    @TableField(exist = false)
    private String name;
    /**
     * 条款内容
     */
    @TableField(exist = false)
    private String content;
    /**
     * 关联字段(中文)拼接字符串
     */
    @TableField(exist = false)
    private String associatedFields;
    /**
     * 内容类型;0 仅文字，1 含字段 )
     */
    @TableField(exist = false)
    private Integer contentType;
}
