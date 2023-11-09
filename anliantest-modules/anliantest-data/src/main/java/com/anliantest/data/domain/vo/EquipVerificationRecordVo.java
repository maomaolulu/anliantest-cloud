package com.anliantest.data.domain.vo;

import com.anliantest.file.api.domain.SysAttachment;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author ZhuYiCheng
 * @date 2023/8/8 15:24
 */
@Data
public class EquipVerificationRecordVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    private Long id;
    /**
     * 设备id
     */
    private Long equipId;
    /**
     * 送检时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date inspectionTime;
    /**
     * 退检时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date returnTime;
    /**
     * 完成检定时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date verificationCompletionTime;
    /**
     * 证书有效期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date usefulTime;
    /**
     * 检定/校准单位
     */
    private String verificationUnit;
    /**
     * 证书编号
     */
    private String certificateCode;
    /**
     * 校准员
     */
    private String calibrator;
    /**
     * 修正因子
     */
    private String correctionFactor;
    /**
     * 检定费用
     */
    private BigDecimal verificationFee;
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
    /**
     * 逻辑删除
     */
    private Integer delFlag;
    /**
     * 设备编号
     */
    private String equipCode;
    /**
     * 设备名称
     */
    private String goodsName;
    /**
     * 规格型号
     */
    private String model;
    /**
     * 档案对应附件列表
     */
    @TableField(exist = false)
    private List<SysAttachment> sysAttachmentList;
}
