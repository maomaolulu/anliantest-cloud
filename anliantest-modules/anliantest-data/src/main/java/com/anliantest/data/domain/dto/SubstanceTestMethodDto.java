package com.anliantest.data.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zhanghao
 * @date 2023-06-26
 */
@Data
public class SubstanceTestMethodDto implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
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
    private Double detectionLimit;
    /**
     * 最低检出浓度
     */
    private Double limitOfDetection;
    /**
     * 标准采样体积
     */
    private Double standardSamplingVolume;
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
    private Integer decimalPlaces;
    /**
     * 危害因素
     */
    private String substanceName;
    /**检测类别
     * 1	公共场所
     * 2	公共场所集中空调通风系统
     * 3	人防工程
     * 4	水和废水
     * 5	生活饮用水
     * 6	一次性使用卫生用品
     * 7	消毒效果
     * 8	工作场所空气
     * 9	工作场所物理因素
     * 10	工作场所通风工程
     * 11	工作场所环境卫生条件
     * 12	环境空气和废气
     * 13	土壤
     * 14	固体废物
     * 15	噪声
     * 16	教室环境卫生
     * 17	学校厕所
     * 18	学生宿舍
     * 19	加油站油气回收
     * 20	室内空气
     * 21	固体废物
     * 22	水和废水（包括地下水）
     * 23	水（含大气降水）和废水
     * 24	水和废水（含大气降水）
     * 25	空气和废气
     * 26	土壤和沉积物
     * 27	振动
     * 28	洁净室（区）
     */
    private Integer testCategory;
    /**
     * 标准号
     */
    private String testStandards;
    /**
     * 标准名
     */
    private String testStandardsName;
    /**
     * 状态 (1现行，0废止，2发布)',
     */
    private Integer status;
    /**
     * 开始实施日期
     */
    private Date implementationDate;
    /**
     * 废止日期
     */
    private Date abolitionDate;

}
