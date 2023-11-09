package com.anliantest.system.api.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description 乡、镇、街道
 * @Date 2023/8/28 10:49
 * @Author maoly
 **/
@Data
public class Street implements Serializable {

    private static final long serialVersionUID = 5416025699106915097L;
    private String adcode;
    private String name;
    private String center;
    private String level;
}
