package com.anliantest.project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 合同条款类型表
 *
 * @author ZhuYiCheng
 * @date 2023/10/13 17:33
 */
@Data
@TableName("contract_term_type")
public class ContractTermType implements Serializable {
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
     * 类型名称
     */
    private String name;
    /**
     * 显示顺序
     */
    private Integer orderNum;
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
}
