package com.gm.utils.ext;

import com.gm.ex.BusinessException;
import com.gm.utils.Utils;
import com.gm.utils.base.Bool;
import com.gm.utils.base.Day;

/**
 * 时间范围工具类
 *
 * @author Jason
 */
public class Time implements Utils {

    /**
     * 判断当前时间是否属于指定范围
     *
     * @param range 时间范围
     * @param date  可以同时判定多个
     * @return true 在当前范围, false 不在当前范围
     */
    public static boolean insofar(String range, String... date) {
        Long currentTime = Day.getCurrentTime(Long.class);
        if(date.length>0){
            currentTime = Day.getTime(date[0]);
        }
        String[] split = range.split("~");
        if (range.indexOf("~") < 0){
            throw new BusinessException(String.format("字符串%s不是时间范围", range));
        }
        String start = split[0];
        String end = split.length < 2 ? "" : split[1];
        if (Bool.isNull(start) && Bool.isNull(end)) {
            throw new BusinessException(String.format("范围%表示有误", range));
        } else if (Bool.isNull(start) && !Bool.isNull(end)) {
            long endTime = Day.getTime(end);
            return currentTime < endTime;
        } else if (!Bool.isNull(start) && !Bool.isNull(end)) {
            long startTime = Day.getTime(start);
            long endTime = Day.getTime(end);
            return startTime <= currentTime && currentTime < endTime;
        } else if (!Bool.isNull(start) && Bool.isNull(end)) {
            long startTime = Day.getTime(start);
            return startTime <= currentTime;
        }
        return false;
    }
}
