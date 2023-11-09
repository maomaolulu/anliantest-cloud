package com.anliantest.project.domain.dto;

import com.anliantest.project.entity.ContractField;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author ZhuYiCheng
 * @date 2023/10/16 16:50
 */
@Data
public class ContractTermDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
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
     * 更新人/最后操作人
     */
    private String updateBy;
    /**
     * 条款关联字段列表
     */
    private List<ContractField> fieldList;
}
