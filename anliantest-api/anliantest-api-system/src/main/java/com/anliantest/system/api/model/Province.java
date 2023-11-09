package com.anliantest.system.api.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 省份
 * @Date 2023/8/25 15:02
 * @Author maoly
 **/
@Data
public class Province extends Street implements Serializable {
    private static final long serialVersionUID = 551018670468416560L;

    private List<City> districts;
}
