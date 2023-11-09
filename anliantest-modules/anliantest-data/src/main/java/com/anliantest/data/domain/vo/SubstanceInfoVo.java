package com.anliantest.data.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ZhuYiCheng
 * @date 2023/6/25 16:00
 */
@Data
public class SubstanceInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *物质名称
     */
    private String substanceNameOrOtherName;
    /**
     * CAS编号
     */
    private String casCode;
    /**
     * 物质类型 (1.毒物(包括co/co2) 2.粉尘 3.噪声 4.高温 5.紫外辐射 6.手传振动 7工频电场 8.高频电磁场 9:超高频辐射 10:微波辐射 11:激光辐射)
     */
    private Integer substanceType;
}
