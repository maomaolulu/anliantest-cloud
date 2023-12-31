package com.anliantest.system.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nonapi.io.github.classgraph.json.Id;

/**
 * @Description 全国地址数据
 * @Date 2023/8/28 9:12
 * @Author maoly
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChineseAddress {

    @Id
    private String id;
    /**
     * 地区编码
     */
    private String regionCode;
    /**
     * 地区名称
     */
    private String regionName;
    /**
     * 上一级地区编码
     */
    private String regionParentId;
    /**
     * 经纬度
     */
    private String center;
    /**
     * 地区级别 地区级别 1-省、自治区、直辖市 2-地级市、地区、自治州、盟 3-市辖区、县级市、县 4-乡镇街道
     */
    private int regionLevel;


}
