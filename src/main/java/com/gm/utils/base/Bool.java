package com.gm.utils.base;

import com.gm.utils.Utils;
import com.gm.utils.ext.Time;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Map;

/**
 * 布尔值工具类
 *
 * @author Jason
 */
public class Bool implements Utils {

    private static final String NULL_STR = "null";

    /**
     * 判断基本是空
     * 数字为0返回{true}字符串是空返回{true}
     *
     * @param obj 对象
     * @return true 是空, false 不是空
     */
    public static boolean isNull(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof String) {
            if (NULL_STR.equalsIgnoreCase((String) obj)) {
                return true;
            }
            return ((String) obj).isEmpty();
        }
        if (obj instanceof java.util.Collection) {
            return ((java.util.Collection) obj).isEmpty();
        }
        if (obj instanceof Map) {
            return ((Map) obj).size() <= 0;
        }
        if (obj instanceof Object[]) {
            return ((Object[]) obj).length <= 0;
        }
        return false;
    }

    /**
     * 判断全部是空
     *
     * @param obj the obj
     * @param os  the os
     * @return the boolean
     */
    public static boolean isNull(Object obj, Object... os) {
        if (!isNull(obj)) {
            return false;
        }
        if (isNull(obj) && isNull(os)) {
            return true;
        }
        for (int i = 0; !isNull(os) && i < os.length; i++) {
            if (!isNull(os[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断有空格
     *
     * @param cs the cs
     * @return boolean boolean
     */
    public static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs != null && (strLen = cs.length()) != 0) {
            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }
            return true;
        } else {
            return true;
        }
    }

    /**
     * 判断全部有空格
     *
     * @param css the css
     * @return boolean boolean
     */
    public static boolean isBlank(CharSequence... css) {
        if (isNull(css)) {
            return true;
        } else {
            CharSequence[] arr$ = css;
            int len$ = css.length;

            for (int i$ = 0; i$ < len$; ++i$) {
                CharSequence cs = arr$[i$];
                if (isBlank(cs)) {
                    return true;
                }
            }

            return false;
        }
    }


    /**
     * 判断是否相等
     * 数字将会使用toString对比
     *
     * @param o1 first obj
     * @param o2 second obj
     * @return boolean 一致返回true 否则返回false
     */
    public static boolean isEqual(Object o1, Object o2) {
        if (o1 == o2) {
            return true;
        } else if (o1 == null || o2 == null) {
            return false;
        } else if (o1.equals(o2)) {
            return true;
        } else if (o1 instanceof Number) {
            return o1.toString().equals(o2.toString());
        }
        return false;
    }

    /**
     * 根据get+Filed提取值
     * List Object values = Reflection.getFieldValues(t)
     * for (Object obj : values) {
     * if(!isNull(obj)){
     * return false
     * }
     * }
     * <p>
     * 判断对象是否为空.
     *
     * @param <T> the type parameter
     * @param t   the t
     * @return the boolean
     */
    public static <T> boolean isEmpty(T t) {
        if (isNull(t)) {
            return true;
        } else if (isPrimitive(t)) {
            return false;
        }
        Class<?> clazz = t.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            try {
                f.setAccessible(true);
                Object val = f.get(t);
                if (!isEmpty(val)) {
                    return false;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return true;
    }


    /**
     * 判断是基本类型
     *
     * @param <T> the type parameter
     * @param t   the t
     * @return the boolean
     */
    public static <T> boolean isPrimitive(T t) {
        try {
            if (((Class) t.getClass().getField(FIELD_TYPE).get(null)).isPrimitive()) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }


    /**
     * 判断是否在范围内
     *
     * @param source 判断源
     * @param range  区间范围
     * @return true在范围, 反之不在 boolean
     */
    public static boolean inRange(String source, String range) {
        int i = source.indexOf(":");
        int j = source.indexOf("-");
        if (i + j >= 0) {
            Logger.debug("正在..判断时间{{}}是否在范围{{}}之间", source, range);
            return Time.insofar(source, range);
        } else {
            Logger.debug("正在..判断数字{{}}是否在范围{{}}之间", source, range);
            BigDecimal number = new BigDecimal(source);
            if (Num.insofar(number, range) == 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * Have null boolean.
     *
     * @param <T> the type parameter
     * @param ts  the ts
     * @return the boolean
     */
    public static <T> boolean haveNull(T... ts) {
        for (T t : ts) {
            if (isNull(t)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是不是数字.
     *
     * @param <T> the type parameter
     * @param t   the t
     * @return the boolean
     */
    public static <T> boolean isNumber(T t) {
        if (t instanceof Number) {
            return true;
        } else if (t instanceof String) {
            String str = (String) t;
            try {
                Object o = str.contains(".") ? new Double(str) : new BigInteger(str);
                return !isNull(o);
            } catch (Exception e) {
                return false;
            }
        } else {
            try {
                int i = (Integer) t;
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    /**
     * 是0.
     *
     * @param <T> the type parameter
     * @param t   the t
     * @return the boolean
     */
    public static <T> boolean isZero(T t) {
        if (t instanceof Number) {
            return ((Number) t).doubleValue() == 0;
        } else {
            return new Double(t.toString()) == 0;
        }
    }

    /**
     * 是1.
     *
     * @param <T> the type parameter
     * @param t   the t
     * @return the boolean
     */
    public static <T> boolean isOne(T t) {
        if (t instanceof Number) {
            return ((Number) t).doubleValue() == 1;
        } else {
            return new Double(t.toString()) == 1;
        }
    }

    /**
     * 包含.
     * 支持数组,集合(包括Map)以及单个Object
     *
     * @param more 更多的集合
     * @param sub  更少的子集
     * @return the 是否包含
     */
    public static boolean contains(Object more, Object... sub) {
        java.util.Collection all;
        if (more instanceof Object[]) {
            all = Arrays.asList(more);
        } else if (more instanceof java.util.Collection) {
            all = Arrays.asList(more);
        } else if (more instanceof Map) {
            all = Collection.getKeys((Map<? extends Object, ? extends Object>) more);
        } else {
            all = Arrays.asList(more);
        }
        for (Object obj : sub) {
            if (obj instanceof Object[]) {
                if (!all.containsAll(Arrays.asList((Object[]) obj))) {
                    return false;
                }
            } else if (obj instanceof java.util.Collection) {
                if (!all.containsAll((java.util.Collection<?>) obj)) {
                    return false;
                }
            } else if (obj instanceof Map){
                if (!all.containsAll(Collection.getKeys((Map<? extends Object, ? extends Object>) obj))) {
                    return false;
                }
            } else {
                if (!all.contains(obj)) {
                    return false;
                }
            }
        }
        return true;
    }
}
