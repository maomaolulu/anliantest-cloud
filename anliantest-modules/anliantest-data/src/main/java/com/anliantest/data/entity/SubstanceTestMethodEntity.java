package com.anliantest.data.entity;

import com.anliantest.common.core.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author gy
 * @date 2023-06-06
 */
@Data
@TableName("substance_test_method")
@Accessors(chain = true)
public class SubstanceTestMethodEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * ID
     */
    @TableId
    private Long id;
    /**
     * 物质信息表id
     */
    private Long substanceTestLawId;
    /**
     * 物质表ID
     */
    private Long substanceInfoId;
    /**
     * 方法号
     */
    private String testNumber;
    /**
     * 检测方法名称
     */
    private String testName;
    /**
     * 是否直读   0 否  1 是
     */
    private Integer isDirectReading;
    /**
     * 是否支持定点采样    0 不支持  1 支持
     */
    private Integer isAreaSample;
    /**
     * 是否支持个体采样 0不支持  1支持
     */
    private Integer isPersonalSample;
    /**
     * 定点采样流量
     */
    private String fixedSampleTraffic;
    /**
     * 定点采样时长
     */
    private String fixedSampleDuration;
    /**
     * 定点采样时长说明
     */
    private String fixedSampleNote;
    /**
     * 定点采样设备
     */
    private String fixedSampleEquipment;
    /**
     * 定点收集器
     */
    private String fixedCollector;

    /**
     * 个体采样流量
     */
    private String personalSampleTraffic;
    /**
     * 个体采样时长
     */
    private String personalSampleDuration;
    /**
     * 个体采样时长说明
     */
    private String personalSampleNote;
    /**
     * 个体采样设备
     */
    private String personalSampleEquipment;
    /**
     * 个体收集器
     */
    private String personalCollector;

    /**
     * 空白样
     */
    private String blankSample;
    /**
     * 保存方式
     */
    private String saveMethod;
    /**
     * 保存期限
     */
    private String saveTerm;
    /**
     * 保存要求
     */
    private String saveRequirement;
//    /**
//     * 采样人
//     */
//    private String collector;
//    /**
//     * 是否需要吸收液0否  1是（为了提前通知实验室）
//     */
//    private Integer isAbsorbentSolution;
    /**
     * 吸收液
     */
    private String absorbentSolution;
    /**
     * 检测设备
     */
    private String detectionEquipment;
    /**
     * 检出限
     */
    private String detectionLimit;
    /**
     * 最低检出浓度
     */
    private String limitOfDetection;
    /**
     * 标准采样体积
     */
    private String standardSamplingVolume;
    /**
     * 检出限说明
     */
    private String explanationOfDetectionLimit;
    /**
     * 计算
     */
    private String calculation;
    /**
     * 保留位数
     */
    private String decimalPlaces;
}
