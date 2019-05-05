package com.gm.utils.base;

import com.gm.help.base.Quick;
import com.gm.utils.Utils;
import com.gm.utils.ext.Json;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * The type String utils.
 *
 * @author Jason
 */
public class Convert implements Utils {

    /**
     * 将空对象转为null
     *
     * @param <T> the type parameter
     * @param t   the t
     * @return string t
     */
    public static <T> T toNull(T t) {
        if (Bool.isEmpty(t)) {
            return null;
        }
        return t;
    }

    /**
     * 将null转换成空对象.
     *
     * @param <T>   the type parameter
     * @param obj   the obj
     * @param empty the obj class
     * @return obj obj
     */
    public static <T> T toEmpty(Object obj, T empty) {
        if (obj == null) {
            return empty;
        }
        return Quick.exes(x -> Assert.Null((T) obj), x -> empty);
    }

    /**
     * 将null转换成空字符.
     *
     * @param str the str
     * @return the string
     */
    public static String toEmpty(String str) {
        if (Bool.isNull(str)) {
            return "";
        }
        return str;
    }

    /**
     * 将null转换成0.
     *
     * @param <T> the type parameter
     * @param t   the T
     * @return the T
     */
    public static <T extends Number> Integer toInt(T t) {
        if (Bool.isNull(t)) {
            return 0;
        }
        return t.intValue();
    }

    /**
     * 将null转换成0.
     *
     * @param <T> the type parameter
     * @param t   the T
     * @return the T
     */
    public static <T extends Number> Long toLong(T t) {
        if (Bool.isNull(t)) {
            return 0L;
        }
        return t.longValue();
    }

    /**
     * 将null转换成0.
     *
     * @param <T> the type parameter
     * @param t   the T
     * @return the T
     */
    public static <T extends Number> BigDecimal toDecimal(T t) {
        if (Bool.isNull(t)) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(t.toString());
    }


    /**
     * 转换时间
     *
     * @param <T> 泛型
     * @param obj 如果obj不是时间类型将使用当前时间
     * @param t   目标类型
     * @return t t
     */
    public static <T> T toTime(Object obj, T t) {
        if (obj == null || obj.getClass().equals(t.getClass())) {
            return (T) obj;
        }
        Long time = Day.getTime(obj);
        Date date = new Date(time);
        return Day.switchT(t, date);
    }


    /**
     * 通过json转换成对象
     *
     * @param <T>    泛型
     * @param map    map转json
     * @param tClass 再转对象
     * @return 目标对象 t
     */
    public static <T> T toJson(Map<String, Object> map, Class<T> tClass) {
        String json = Json.toJson(map);
        return Json.toObject(json, tClass);
    }


    /**
     * 对象转换成字节数组.
     *
     * @param obj the obj
     * @return the byte [ ]
     */
    public static byte[] toBytes(Object obj) {
        byte[] bytes = new byte[]{};
        if (Bool.isNull(obj)) {
            return bytes;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
            oos.close();
            bos.close();
        } catch (Exception e) {
            Logger.error(String.format("对象{%s}转换成字节数组出错", Json.toJson(obj)), e);
        }
        return bytes;
    }


    /**
     * 字节数组转换成对象.
     *
     * @param <T>   the type parameter
     * @param bytes the bytes
     * @return object t
     */
    public static <T> T toObj(byte[] bytes) {
        Object obj = new Object();
        if (Bool.isNull(bytes)) {
            return (T) obj;
        }
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = ois.readObject();
            ois.close();
            bis.close();
        } catch (Exception e) {
            Logger.error("字节数组转换成对象出错", e);
        }
        return (T) obj;
    }
}
