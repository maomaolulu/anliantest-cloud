package com.anliantest.data.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 维修记录vo
 * @author: liyongqiang
 * @create: 2023-08-07 11:48
 */
@Data
public class EquipMaintainVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 维修记录表id
     **/
    private Long id;
    /**
     * 设备清单表id
     **/
    private Long equipId;
    /**
     * 编辑后的设备清单表id
     **/
    private Long afterEquipId;
    /**
     * 设备编号
     **/
    private String equipCode;
    /**
     * 设备名称
     **/
    private String goodsName;
    /**
     * 规格型号
     **/
    private String model;
    /**
     * 维修状态
     **/
    private Integer repairStatus;
    /**
     * 隶属公司id
     **/
    private Integer companyId;
    /**
     * 责任部门id
     **/
    private Long chargeDeptId;
    /**
     * 设备故障时间
     **/
    private String breakdownTime;
    /**
     * 维修完成时间
     **/
    private String completeTime;
    /**
     * 维修费用
     **/
    private BigDecimal repairCost;
    /**
     * 故障原因
     */
    private String remark;

}
