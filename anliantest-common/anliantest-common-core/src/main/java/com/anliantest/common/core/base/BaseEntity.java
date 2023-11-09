package com.anliantest.common.core.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 抽象实体
 *
 * @author maoly
 * @date 2023/4/26
 */
@Data
public class BaseEntity implements Serializable {

	/**
	 * 创建者
	 */
	@TableField(fill = FieldFill.INSERT)
	private String createBy;

	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/**
	 * 更新者
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private String updateBy;

	/**
	 * 更新时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;

	/**
	 * 删除标识
	 */
	private int deleteFlag;

}
