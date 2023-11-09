package com.anliantest.data.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ZhuYiCheng
 * @date 2023/8/8 15:13
 */
@Data
public class EquipVerificationRecordDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 设备编号
     */
    private String equipCode;
    /**
     * 设备名称
     */
    private String goodsName;
    /**
     * 证书编号
     */
    private String certificateCode;
}
