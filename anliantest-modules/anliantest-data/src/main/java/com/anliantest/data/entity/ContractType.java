package com.anliantest.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * @author zhanghao
 * @email
 * @date 2023-07-28
 * @desc : 合同类型
 */
@Data
@TableName("data_contract_type")
public class ContractType implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增主键ID
	 */
	@TableId(type = IdType.AUTO)
	private Long id;
	/** 父级id */
	private Long pid ;
	/** 所属事业部id */
	private Long businessUnitId ;
	/** 公司id */
	private Long companyId ;
	/** 编号 */
	private String code ;
	/** 名称 */
	private String name ;
	/** 状态 */
	private Integer status ;
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
