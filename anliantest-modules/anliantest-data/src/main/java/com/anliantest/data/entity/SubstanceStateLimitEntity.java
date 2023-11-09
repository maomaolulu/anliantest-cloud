package com.anliantest.data.entity;

import com.anliantest.common.core.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * 物质不同条件限值表
 * @author gy
 * @date 2023-06-06
 */
@Data
@TableName("substance_state_limit")
@Accessors(chain = true)
public class SubstanceStateLimitEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId
    private Long id;

    /**
     * 物质信息表id
     */
    private Long substanceInfoId;

    /**
     * 物质类型(1.毒物(包括co/co2)  2.粉尘  3.噪声  4.高温  5.紫外辐射  6.手传振动  7工频电场  8.高频电磁场   9:超高频辐射  10:微波辐射  11:激光辐射)
     */
    @TableField(exist = false)
    private Long substanceType;

    /**
     * 条件
     */
    private String conditionNote;

    /**
     * MAC
     */
    private float mac;

    /**
     * PC-TWA
     */
    private float pcTwa;

    /**
     * PC-STEL
     */
    private float pcStel;

    /**
     * 临界不良反应
     */
    private String adverseReactions;

    /**
     * 备注
     */
    private String remark;

    /**
     * 噪声：接触时间  脉冲噪声：工作日接触脉冲次数（n,次）
     */
    private String noiseTimeFrequency;

    /**
     *  噪声：接触限值[dB(A)]   脉冲噪声：声压级峰值[dB(A)]
     */
    private int noisePeakValue;

    /**
     * 接触时间率(%)
     */
    private int contactTimeRate;
    /**
     * 体力劳动强度Ⅰ限值(℃)
     */
    private int laborIntensityOne;
    /**
     * 体力劳动强度Ⅱ限值(℃)
     */
    private int laborIntensityTwo;
    /**
     * 体力劳动强度Ⅲ限值(℃)
     */
    private int laborIntensityThree;
    /**
     * 体力劳动强度Ⅳ限值(℃)
     */
    private int laborIntensityFour;

    /**
     * 辐照度（紫外辐射时单位 μW/cm²）
      */
    private String irradiance;
    /**
     * 照射量（紫外辐射时单位mJ/cm²）
     */
    private String exposure;

    /**
     * 日剂量(μW·h/cm²)
     */
    private int dailyDose;
    /**
     * 8h平均功率密度(μW/cm²)
     */
    private int averagePowerDensity;
    /**
     * 非8h平均功率密度(μW/cm²)
     */
    private String nonAveragePowerDensity;
    /**
     * 短时间功率接触密度(mW/cm²)
     */
    private int shortPowerDensity;
    /**
     * 接触时间(h)
     */
    private int contactTime;
    /**
     * 等能量频率计权振动加速度(m/s²)
     */
    private int acceleration;
    /**
     * 频率（工频电场单位为：Hz，高频电磁场单位为 MHz）
     */
    private String frequency;
    /**
     *  电场强度（工频电场时单位为 kV/m，超高频辐射与高频电磁场时单位为V/m）
     */
    private int electricFieldIntensity;
    /**
     * 磁场强度(A/m)
     */
    private String magneticFieldIntensity;

    /**
     * 功率密度(mW/cm²)
     */
    private String powerDensity;
    /**
     * 波长(nm)
     */
    private String laserWavelength;
    /**
     * 照射时间(s)
     */
    private String laserExposureTime;
    /**
     * 照射部位
     */
    private String laserExposureSite;
    /**
     * 激光照射量(J/cm²)
     */
    private String laserExposure;
    /**
     * 激光辐照度(W/cm²)
     */
    private String laserIrradiance;





}
