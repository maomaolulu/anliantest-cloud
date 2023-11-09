package com.anliantest.common.core.checker;

import org.apache.commons.collections4.CollectionUtils;

import java.util.*;

/**
 * @Description 时间段校验
 * @Date 2023/7/27 14:11
 * @Author maoly
 **/
public class TimeOverlapCheckerUtils {

    /**
     * 校验时间段是否存在重叠
     * @param timeRanges 待校验时间段列表
     * @return true:重叠  false 不重叠
     */
    public static boolean timeOverlapCheck(List<TimeRange> timeRanges){
        boolean hasOverlap = false;
        if(CollectionUtils.isNotEmpty(timeRanges)){
            Collections.sort(timeRanges, new Comparator<TimeRange>() {
                public int compare(TimeRange tr1, TimeRange tr2) {
                    return tr1.getStartTime().compareTo(tr2.getStartTime());
                }
            });
            Date prevEndTime = null;
            for (TimeRange tr : timeRanges) {
                if (prevEndTime != null && tr.getStartTime().before(prevEndTime)) {
                    hasOverlap = true;
                    break;
                }
                prevEndTime = tr.getEndTime();
            }
        }
        return hasOverlap;
    }
}

