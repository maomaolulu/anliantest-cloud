package com.anliantest.system.api.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description
 * @Date 2023/8/25 14:49
 * @Author maoly
 **/
@Data
public class AmapData implements Serializable {
    private static final long serialVersionUID = 2313972265866792848L;

    private String status;
    private String infocode;
    private List<Country> districts;
}
