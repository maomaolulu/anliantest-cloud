package com.anliantest.project.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author gy
 * @date 2023-09-21 18:26
 */
@Data
public class RoleAndCompanyDto implements Serializable {
    /**
     * 角色
     */
    private String roleKey;
    /**
     * 所属公司
     */
    private String dictLabel;
}
