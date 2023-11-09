package com.anliantest.system.domain.vo;

import lombok.Data;
import nonapi.io.github.classgraph.json.Id;

/**
 * @author gy
 * @date 2023-08-29 13:17
 */
@Data
public class ChineseAddressVo {

    @Id
    private String id;
    /**
     * 地区名称
     */
    private String regionName;
    /**
     * 地区级别 地区级别 1-省、自治区、直辖市 2-地级市、地区、自治州、盟 3-市辖区、县级市、县 4-乡镇街道
     */
    private int regionLevel;
}
