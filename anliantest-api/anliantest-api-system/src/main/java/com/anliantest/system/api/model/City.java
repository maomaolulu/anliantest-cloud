package com.anliantest.system.api.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 市
 * @Date 2023/8/25 15:04
 * @Author maoly
 **/
@Data
public class City extends Street implements Serializable {
    private static final long serialVersionUID = -3594443090547686167L;

    private List<District> districts;
}
