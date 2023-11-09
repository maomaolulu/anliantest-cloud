package com.anliantest.system.api.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 区县
 * @Date 2023/8/25 15:04
 * @Author maoly
 **/
@Data
public class District extends Street implements Serializable {
    private static final long serialVersionUID = -4009374353832064954L;

    private List<Street> districts;
}
