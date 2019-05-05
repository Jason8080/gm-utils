package com.gm.utils.base;

/**
 * Created by Jason on 2017/7/7.
 */

import com.gm.enums.Pattern;
import com.gm.ex.BusinessException;
import com.gm.utils.Utils;
import com.gm.utils.ext.Json;
import com.gm.utils.ext.Regex;
import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 *
 * @author Jason
 */
public class Day implements Utils {
    private static final String SPACE_SYMBOL = " ";
    private static final String BAR_SYMBOL = "-";
    private static final String COLON_SYMBOL = ":";
    private static final String DOT_SYMBOL = "\\.";
    /**
     * 精确到日.
     */
    public static final SimpleDateFormat format_day = new SimpleDateFormat(Pattern.DATE_DAY.get());
    /**
     * 精确到秒.
     */
    public static final SimpleDateFormat format_second = new SimpleDateFormat(Pattern.DATE_SECOND.get());
    /**
     * 精确到毫秒.
     */
    public static final SimpleDateFormat format_millisecond = new SimpleDateFormat(Pattern.DATE_MILLISECOND.get());
    /**
     * 精确到日(去除-).
     */
    @Deprecated
    public static final SimpleDateFormat formatDay = new SimpleDateFormat(Pattern.DATE_DAY.get().replace(BAR_SYMBOL, ""));
    /**
     * 精确到秒(去除-).
     */
    @Deprecated
    public static final SimpleDateFormat formatSecond = new SimpleDateFormat(Pattern.DATE_SECOND.get().replace(BAR_SYMBOL, ""));

    /**
     * 获取当前时间
     *
     * @param pattern the pattern
     * @return 当前时间 t
     */
    public static String getCurrentTime(Pattern pattern) {
        if (Bool.isNull(pattern)) {
            return getCurrentTime(Pattern.DATE_SECOND);
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern.get());
        return format.format(System.currentTimeMillis());
    }

    /**
     * 获取当前时间
     *
     * @param <T>   the type parameter
     * @param clazz 支持Date、String
     * @return 当前时间 t
     */
    public static <T> T getCurrentTime(Class<T> clazz) {
        switch (clazz.getName()) {
            case CLASS_JAVA_UTIL_CALENDAR:
                return (T) Calendar.getInstance();
            case CLASS_JAVA_LANG_LONG:
                return (T) new Long(System.currentTimeMillis());
            case CLASS_JAVA_UTIL_DATE:
                return (T) new Date();
            case CLASS_JAVA_LANG_STRING:
                return (T) new DateTime().toString("yyyy-MM-dd HH:mm:ss");
            default:
                return (T) new DateTime().toString("yyyy-MM-dd");
        }
    }

    /**
     * 获取当天最后的'时刻'
     *
     * @param <T> the type parameter
     * @param t   the t
     * @return the last time
     */
    public static <T> T getLastTime(T t) {
        try {
            Calendar current = getCalendar(t);
            current.set(Calendar.HOUR_OF_DAY, 23);
            current.set(Calendar.MINUTE, 59);
            current.set(Calendar.SECOND, 59);
            current.set(Calendar.MILLISECOND, 999);
            return switchT(t, current.getTime());
        } catch (ParseException e) {
            Logger.error("日历工具获取当天最后时刻出错,原样返回", e);
            return t;
        }
    }

    /**
     * 位移 i 年
     *
     * @param <T> the type parameter
     * @param t   the t
     * @param i   the
     * @return after after
     */
    public static <T> T offsetYear(T t, int i) {
        try {
            Calendar current = getCalendar(t);
            Date date = getOffset(current, i, Calendar.YEAR).getTime();
            return switchT(t, date);
        } catch (ParseException e) {
            Logger.error("日历工具位移{}年出错,原样返回", i, e);
            return t;
        }
    }

    /**
     * 位移 i 月
     *
     * @param <T> the type parameter
     * @param t   the t
     * @param i   the
     * @return after after
     */
    public static <T> T offsetMonth(T t, int i) {
        try {
            Calendar current = getCalendar(t);
            Date date = getOffset(current, i, Calendar.MONTH).getTime();
            return switchT(t, date);
        } catch (ParseException e) {
            Logger.error("日历工具位移{}月出错,原样返回", i, e);
            return t;
        }
    }

    /**
     * 位移 i 天
     *
     * @param <T> the type parameter
     * @param t   the t
     * @param i   the
     * @return after after
     */
    public static <T> T offsetDay(T t, int i) {
        try {
            Calendar current = getCalendar(t);
            Date date = getOffset(current, i, Calendar.DATE).getTime();
            return switchT(t, date);
        } catch (ParseException e) {
            Logger.error("日历工具位移{}天出错,原样返回", i, e);
            return t;
        }
    }

    /**
     * 位移 i 小时
     *
     * @param <T> the type parameter
     * @param t   the t
     * @param i   the
     * @return after after
     */
    public static <T> T offsetHour(T t, int i) {
        try {
            Calendar current = getCalendar(t);
            Date date = getOffset(current, i, Calendar.HOUR_OF_DAY).getTime();
            return switchT(t, date);
        } catch (ParseException e) {
            Logger.error("日历工具位移{}小时出错,原样返回", i, e);
            return t;
        }
    }

    /**
     * 位移 i 分钟
     *
     * @param <T> the type parameter
     * @param t   the t
     * @param i   the
     * @return after after
     */
    public static <T> T offsetMinute(T t, int i) {
        try {
            Calendar current = getCalendar(t);
            Date date = getOffset(current, i, Calendar.MINUTE).getTime();
            return switchT(t, date);
        } catch (ParseException e) {
            Logger.error("日历工具位移{}分钟出错,原样返回", i, e);
            return t;
        }
    }

    /**
     * 位移 i 秒
     *
     * @param <T> the type parameter
     * @param t   the t
     * @param i   the
     * @return after after
     */
    public static <T> T offsetSecond(T t, int i) {
        try {
            Calendar current = getCalendar(t);
            Date date = getOffset(current, i, Calendar.SECOND).getTime();
            return switchT(t, date);
        } catch (ParseException e) {
            Logger.error("日历工具位移{}秒出错,原样返回", i, e);
            return t;
        }
    }

    /**
     * 位移 i 毫秒
     *
     * @param <T> the type parameter
     * @param t   the t
     * @param i   the
     * @return after after
     */
    public static <T> T offsetMillisecond(T t, int i) {
        try {
            Calendar current = getCalendar(t);
            Date date = getOffset(current, i, Calendar.MILLISECOND).getTime();
            return switchT(t, date);
        } catch (ParseException e) {
            Logger.error("日历工具位移{}毫秒出错,原样返回", i, e);
            return t;
        }
    }


    /**
     * 根据参数泛型和给出的时间返回相应时间相应形式的对象
     *
     * @param <T>  参数泛型
     * @param t    参数泛型
     * @param date 参数时间
     * @return 相应时间相应形式的对象 t
     */
    public static <T> T switchT(T t, Date date) {
        switch (t.getClass().getName()) {
            case CLASS_JAVA_UTIL_DATE:
                return (T) date;
            case CLASS_JAVA_LANG_LONG:
                return (T) new Long(date.getTime());
            case CLASS_JAVA_LANG_STRING:
                return (T) getFormatDate((String) t).format(date);
            case CLASS_JAVA_UTIL_CALENDAR: {
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                return (T) c;
            }
            case CLASS_JAVA_UTIL_GREGORIAN_CALENDAR: {
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                return (T) c;
            }
            default:
                throw new BusinessException(String.format("%s转换%s出错", date, Json.toJson(date)));
        }
    }
    /**
     * 获取当前时间位移i个单位时间
     *
     * @param current
     * @param offset
     * @return
     */
    private static Calendar getOffset(Calendar current, int offset, int unit) {
        int day = current.get(unit);
        current.set(unit, day + offset);
        return current;
    }

    /**
     * 获取日历对象
     *
     * @param t   仅支持Date、String
     * @param <T> 参数泛型
     * @return 日历对象
     * @throws ParseException 日历异常
     */
    private static <T> Calendar getCalendar(T t) throws ParseException {
        Calendar c = Calendar.getInstance();
        switch (t.getClass().getName()) {
            case CLASS_JAVA_UTIL_DATE: {
                c.setTime((Date) t);
                return c;
            }
            case CLASS_JAVA_LANG_LONG: {
                c.setTimeInMillis((Long) t);
                return c;
            }
            case CLASS_JAVA_LANG_STRING: {
                String date = (String) t;
                c.setTime(Bool.isNull(date) ? getCurrentTime(Date.class) : getFormatDate(date).parse(date));
                return c;
            }
            default: {
                Date date = format_second.parse((String) t);
                c.setTime(date);
                return c;
            }
        }
    }

    /**
     * 根据时间字符串获取时间戳
     *
     * @param date 时间字符串
     * @return time time
     */
    public static Long getTime(Object date) {
        if(Bool.isNull(date)){
            return System.currentTimeMillis();
        } else if (date instanceof Date) {
            return ((Date) date).getTime();
        } else if (date instanceof Long) {
            return (Long) date;
        } else if (date instanceof Calendar) {
            return ((Calendar) date).getTimeInMillis();
        } else if (date instanceof String && Bool.isNumber(date)) {
            return Long.parseLong((String) date);
        } else if (date instanceof String) {
            return getTime((String) date);
        }
        throw new BusinessException(String.format("%s不是时间", date));
    }

    /**
     * 在字符串中找出时间并转化成时间戳
     *
     * @param date 包含时间的字符串
     * @return
     */
    private static Long getTime(String date) {
        String str = Regex.findDate(date);
        if (Bool.isNull(str)) {
            throw new BusinessException(String.format("%s没有匹配到时间", date));
        } else {
            try {
                Date d = getFormatDate(date).parse(date);
                Calendar c = getCalendar(d);
                Calendar current = getCurrentTime(Calendar.class);
                if (date.indexOf(BAR_SYMBOL) < 0) {
                    c.set(current.get(Calendar.YEAR), current.get(Calendar.MONTH), current.get(Calendar.DATE));
                }
                return c.getTime().getTime();
            } catch (ParseException e) {
                throw new BusinessException(String.format("%s不是时间内容", date));
            }
        }
    }


    /**
     * 根据时间字符串获取相应的格式化对象
     *
     * @param date 时间字符串
     * @return 可格式化 {date}的对象
     */
    public static SimpleDateFormat getFormatDate(String date) {
        if (date.contains(BAR_SYMBOL)) {
            if (date.contains(COLON_SYMBOL)) {
                if (date.contains(DOT_SYMBOL)) {
                    return format_millisecond;
                }
                return format_second;
            }
            return format_day;
        }
        String pattern = Pattern.DATE_MILLISECOND.get();
        if (!date.contains(BAR_SYMBOL)) {
            pattern = pattern.replaceAll(BAR_SYMBOL, "");
            if (!date.contains(SPACE_SYMBOL)) {
                pattern = pattern.replaceAll(SPACE_SYMBOL, "");
                if (!date.contains(COLON_SYMBOL)) {
                    pattern = pattern.replaceAll(COLON_SYMBOL, "");
                    if (!date.contains(DOT_SYMBOL)) {
                        pattern = pattern.replaceAll(DOT_SYMBOL, "");
                    }
                }
            }
        }
        return new SimpleDateFormat(pattern.substring(0, date.length()));
    }
}
