package com.anliantest.file.api.domain;

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
 * @author zhanghao
 * @email
 * @date 2023-08-29
 * @desc : 进度管理
 */
@Data
@TableName("progress_manage")
public class ProgressManage implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增主键ID
	 */
	@TableId(type = IdType.AUTO)
	private Long id ;
	/** 操作内容 */
	private String content ;
	/** 文件名称 */
	private String fileName ;
	/** 状态 0：导出中 1：成功，2：失败 */
	private Integer status ;
	/** 结果说明 */
	private String resultDescription ;
	/** 失败原因 */
	private String failureReason ;

	/** 创建人id */
	private Long createById ;
	/** 创建人名称 */
	private String createBy ;
	/** 更新人 */
	private String updateBy ;
	/** 执行结束时间 */
	private Date executionEndTime ;
	/** 更新时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime ;
	/** 创建时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime ;
	/** 导出参数json */
	@TableField(exist = false)
	private String exportJson ;
	/** 创建人 */
	@TableField(exist = false)
	private String nickName ;

	/**
	 * 图片上传
	 */
	@TableField(exist = false)
	private List<SysAttachment> sysAttachmentList;
	/**
	 * 导出类型
	 */
	@TableField(exist = false)
	private String exportType;


}
