package com.anliantest.data.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * @author zhanghao
 * @email
 * @date 2023-07-17
 * @desc : 流程配置表
 */
@Data
@TableName("date_process_configuration")
public class ProcessConfiguration implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增主键ID
	 */
	@TableId
	private Long id ;
	/** 项目类型id */
	private Long projectTypeId ;
	/** 类型 （0：项目，1：合同） */
	private Integer type ;
	/** 子类型 （项目：0：项目下发，1：公示审批，2：结束确认） */
	private Integer subtype ;
	/** 角色id */
	private String roleIds ;
	/** 创建人id */
	private Long createById ;
	/** 创建人名称 */
	private String createBy ;
	/** 创建时间 */
	private Date createTime ;
	/** 更新人 */
	private String updateBy ;
	/** 更新时间 */
	private Date updateTime ;

}
