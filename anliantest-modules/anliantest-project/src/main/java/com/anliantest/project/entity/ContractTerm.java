package com.anliantest.project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 合同条款表
 *
 * @author ZhuYiCheng
 * @date 2023/10/13 17:30
 */
@Data
@TableName("contract_term")
public class ContractTerm implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 所属公司id
     */
    private Long companyId;
    /**
     * 条款类型id
     */
    private Long termTypeId;
    /**
     * 内容
     */
    private String content;
    /**
     * 关联字段(中文)拼接字符串
     */
    private String associatedFields;
    /**
     * 内容类型;0 仅文字，1 含字段 )
     */
    private Integer contentType;
    /**
     * 条款状态;0 停用， 1 正常，2 待开发)
     */
    private Integer termStatus;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 更新人
     */
    private String updateBy;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 条款关联字段list
     */
    @TableField(exist = false)
    private List<ContractTermField> contractTermFields;
    /**
     * 条款关联字段列表
     */
    @TableField(exist = false)
    private List<ContractField> fieldList;
    /**
     * 字段类型
     */
    @TableField(exist = false)
    private String termType;
    /**
     * 所属公司名称
     */
    @TableField(exist = false)
    private String companyName;
}
