package com.anliantest.system.api.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 国家
 * @Date 2023/8/25 14:58
 * @Author maoly
 **/
@Data
public class Country extends Street implements Serializable {
    private static final long serialVersionUID = 4419945874418180298L;

    private List<Province> districts;
}
