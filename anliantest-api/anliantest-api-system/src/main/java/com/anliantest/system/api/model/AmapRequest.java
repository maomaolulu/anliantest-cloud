package com.anliantest.system.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description
 * @Date 2023/8/25 15:10
 * @Author maoly
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AmapRequest implements Serializable {
    private static final long serialVersionUID = -3889937875980156354L;
    /**
     * 地址数据
     */
    private String data;
}
