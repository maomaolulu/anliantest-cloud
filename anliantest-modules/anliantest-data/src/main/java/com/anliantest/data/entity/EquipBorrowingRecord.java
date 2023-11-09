package com.anliantest.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * @author zhanghao
 * @email
 * @date 2023-08-04
 * @desc : 设备借用记录
 */
@Data
@TableName("equip_borrowing_record")
public class EquipBorrowingRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增主键 ID
	 */
	@TableId(type = IdType.AUTO)
	private Long id ;
	/** 设备id */
	private Long equipId ;
	/** 借用公司 */
	private Long companyId ;
	/** 借用开始时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
	private Date startTime ;
	/** 借用结束时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
	private Date endTime ;
	/** 实际结束时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
	private Date actualEndTime ;
	/** 状态(0待借出,1待归还,2已归还3撤销) */
	private Integer status ;
	/** 状态(0内部设备，1：外部设备) */
	private Integer type ;
	/** 创建人id */
	private Long createById ;
	/** 创建人名称 */
	private String createBy ;
	/** 创建时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime ;
	/** 更新人 */
	private String updateBy ;
	/** 更新时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime ;

	/**
	 * 设备编号
	 */
	@TableField(exist = false)
	private String equipCode;
	/**
	 * 设备名称
	 */
	@TableField(exist = false)
	private String goodsName;
	/**
	 * 外部公司名称
	 */
	@TableField(exist = false)
	private String companyName;
	/**
	 * 内部设备公司id
	 */
	@TableField(exist = false)
	private Long internalCompanyId;

}
