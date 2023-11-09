package com.anliantest.common.core.checker;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @Description 时间段起始时间
 * @Date 2023/7/27 14:14
 * @Author maoly
 **/
@Data
@AllArgsConstructor
public class TimeRange {

    /**
     * 时间段开始时间
     */
    private Date startTime;

    /**
     * 时间段结束时间
     */
    private Date endTime;

}
