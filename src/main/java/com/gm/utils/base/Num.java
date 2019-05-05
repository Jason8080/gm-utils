package com.gm.utils.base;

import com.gm.constant.Cn;
import com.gm.enums.Maths;
import com.gm.utils.Utils;

import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * 数字类工具
 *
 * @author Jason
 */
public class Num implements Utils {


    /**
     * 小于0的数将替换之.
     *
     * @param num the num
     * @return 自然数
     */
    public static Integer nature(Number num) {
        if (!Bool.isNull(num) && num.intValue() > 0) {
            return num.intValue();
        }
        return 0;
    }

    /**
     * 判断数字是否在指定范围
     *
     * @param number 判断的数字
     * @param range  指定的范围
     * @return 1 {number}在{range}范围 0 小于最小值 2大于等于最大值
     */
    public static int insofar(Number number, String range) {
        Assert.Null(range);
        String rangeSignal = Maths.RANGE_SIGNAL.getCode().toString();
        String[] split = range.split(rangeSignal);
        try {
            BigDecimal num = new BigDecimal(String.valueOf(number));
            if (!range.contains(rangeSignal)) {
                BigDecimal b = new BigDecimal(split[0]);
                if (num.compareTo(b) > 0) {
                    return 2;
                } else if (num.compareTo(b) < 0) {
                    return 0;
                } else {
                    return 1;
                }

            } else if (Bool.isNull(split[0])) {
                BigDecimal end = new BigDecimal(split[1]);
                if (num.compareTo(end) < 0) {
                    return 1;
                } else {
                    return 2;
                }
            } else if (split.length < Cn.TWO) {
                BigDecimal start = new BigDecimal(split[0]);
                if (num.compareTo(start) >= 0) {
                    return 1;
                } else {
                    return 0;
                }
            }
            BigDecimal start = new BigDecimal(split[0]);
            BigDecimal end = new BigDecimal(split[1]);
            return getResult(start, num, end);
        } catch (NumberFormatException e) {
            Logger.error("无法解析范围{}和预判值{}关系", range, number);
            return -1;
        }
    }

    private static Integer getResult(BigDecimal startNumber, BigDecimal num, BigDecimal endNumber) {
        if (startNumber.compareTo(num) <= 0 && num.compareTo(endNumber) < 0) {
            return 1;
        } else if (num.compareTo(startNumber) < 0) {
            return 0;
        } else if (num.compareTo(endNumber) >= 0) {
            return 2;
        }
        return -1;
    }

    /**
     * 把数字格式化成指定位数的字符串数字(左边补零)
     *
     * @param number 格    * @return
     * @param digits the digits
     * @return the string
     */
    public static String format(long number, int digits) {
        NumberFormat format = NumberFormat.getInstance();
        //设置是否使用分组
        format.setGroupingUsed(false);
        //设置最大整数位数
        format.setMaximumIntegerDigits(digits);
        //设置最小整数位数
        format.setMinimumIntegerDigits(digits);
        //格式化
        return format.format(number);
    }

    /**
     * 把字符串数字与指定数字相加后
     * 格式化成指定位数的字符串数字(左边补零)
     *
     * @param number 格式化数字
     * @param digits the digits
     * @param add    the add
     * @return string string
     */
    public static String format(String number, int digits, long add) {
        long l = Long.parseLong(number);
        return format(l + add, digits);
    }
}
