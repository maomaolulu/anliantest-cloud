package com.anliantest.project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 合同条款与字段关联表
 *
 * @author ZhuYiCheng
 * @date 2023/10/13 17:32
 */
@Data
@TableName("contract_term_field")
public class ContractTermField implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 合同条款ID
     */
    private Long contractTermId;
    /**
     * 字段ID
     */
    private Long contractFieldId;
    /**
     * 字段描述
     */
    private String fieldDescribe;
}
