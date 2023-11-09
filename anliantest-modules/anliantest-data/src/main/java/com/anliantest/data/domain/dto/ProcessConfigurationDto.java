package com.anliantest.data.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhanghao
 * @date 2023-07-18
 */
@Data
public class ProcessConfigurationDto implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    private Long id;
    /**
     * 物质信息表id
     */
    private String roleIds;
    /**
     * 物质信息表id
     */
    private String roleNames;
    /**
     * 物质表ID
     */
    private Long projectTypeId;
    /**
     * 所属公司id
     */
    private Long companyId;
    /** 类型 （0：项目，1：合同） */
    private Integer type ;
    /** 子类型 （项目：0：项目下发，1：公示审批，2：结束确认） */
    private Integer subtype ;
    /**
     * 所属公司名称
     */
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
     * 负责人员
     */
    private String roleUserNames;

}
