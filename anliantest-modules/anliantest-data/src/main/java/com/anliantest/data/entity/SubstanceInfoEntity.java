package com.anliantest.data.entity;

import com.anliantest.common.core.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description 物质信息表
 * @Date 2023/6/6 8:49
 * @Author maoly
 **/
@TableName("substance_info")
@Data
public class SubstanceInfoEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 8619464141481198551L;

    @TableId
    private Long id;
    /**
     * 物质名称
     */
    private String substanceName;
    /**
     * 物质英文名称
     */
    private String substanceEnglishName;
    /**
     * 物质其他中文名称
     */
    private String substanceOtherName;
    /**
     * CAS编号
     */
    private String casCode;
    /**
     * 熔点(℃)
     */
    private Integer meltingPoint;
    /**
     * 沸点(℃)
     */
    private Integer boilingPoint;
    /**
     * 毒性资料
     */
    private String toxicityData;
    /**
     * 侵入途径
     */
    private String routesOfInvasion;
    /**
     * 对人体健康的影响
     */
    private String healthImpact;
    /**
     * 可能造成的职业病
     */
    private String occupationalDisease;
    /**
     * 是否需要折减 0否 1是
     */
    private Integer isReduction;
    /**
     * 物质类型 (1.毒物(包括co/co2) 2.粉尘 3.噪声 4.高温 5.紫外辐射 6.手传振动 7工频电场 8.高频电磁场 9:超高频辐射 10:微波辐射 11:激光辐射)
     */
    private Integer substanceType;

}
