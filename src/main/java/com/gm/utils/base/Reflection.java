package com.gm.utils.base;

import com.gm.utils.Utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 反射工具类
 *
 * @author Jason
 */
public class Reflection implements Utils {

    /**
     * Set own value boolean.
     *
     * @param o   the o
     * @param map the map
     * @return the boolean
     */
    public static boolean setOwnValues(Object o, Map<String, Object> map){
        Field[] fields = o.getClass().getDeclaredFields();
        if (setValueBy(o, map, fields)){
            return true;
        }
        return false;
    }

    /**
     * Set value boolean.
     *
     * @param o   the o
     * @param map the map
     * @return the boolean
     */
    public static boolean setValues(Object o, Map<String,Object> map){
        if(setOwnValues(o,map)){
            return setSuperclassValues(o,map,o.getClass().getSuperclass());
        }
        return false;
    }

    /**
     * Sets superclass.
     *
     * @param o          the o
     * @param map        the map
     * @param superclass the superclass
     * @return the superclass
     */
    public static boolean setSuperclassValues(Object o, Map<String,Object> map, Class<?> superclass) {
        if(superclass!=null) {
            Field[] fields = superclass.getDeclaredFields();
            if (setValueBy(o, map, fields)) {
                return true;
            }
            return setSuperclassValues(o, map,superclass.getSuperclass());
        }
        return false;
    }

    private static boolean setValueBy(Object o, Map<String,Object> map, Field[] fields) {
        Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> next = it.next();
            String name = next.getKey();
            Object value = next.getValue();
            for (Field f : fields) {
                if (f.getName().equals(name)) {
                    f.setAccessible(true);
                    try {
                        f.set(o, value);
                    } catch (IllegalAccessException e) {
                        Logger.error(String.format("设置对象属性{%s}出错", name), e);
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * 获取对象的所有属性值，返回一个对象数组
     *
     * @param o the o
     * @return the filed values
     */
    @Deprecated
    public static List<Object> getFieldValues(Object o) {
        List<String> fieldNames = getNames(o);
        List<Object> values = new ArrayList();
        for (String name : fieldNames) {
            Object val = getValueBy(name, o);
            if (val == null) {
                continue;
            }
            values.add(val);
        }
        return values;
    }

    /**
     * 获取对象所有属性值.
     *
     * @param o the o
     * @return the values
     */
    public static List<Object> getValues(Object o) {
        List<Object> values = new ArrayList();
        Class<?> clazz = o.getClass();
        Field[] fields = clazz.getDeclaredFields();
        values.addAll(getValues(o,fields));
        values.addAll(getSuperclassValues(o, clazz.getSuperclass()));
        return values;
    }

    /**
     * 获取自身属性值.
     *
     * @param o the o
     * @return the own values
     */
    public static List<Object> getOwnValues(Object o) {
        Class<?> clazz = o.getClass();
        Field[] fields = clazz.getDeclaredFields();
        return getValues(o,fields);
    }

    /**
     * 获取对象所有上级属性值.
     *
     * @param o          the o
     * @param superclass the clazz
     * @return the superclass
     */
    public static List<Object> getSuperclassValues(Object o, Class<?> superclass) {
        List<Object> array = new ArrayList();
        if(superclass!=null) {
            for (Field f : superclass.getDeclaredFields()) {
                try {
                    f.setAccessible(true);
                    array.add(f.get(o));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            array.addAll(getSuperclassValues(o,superclass.getSuperclass()));
        }
        return array;
    }

    /**
     * 获取属性类型(type)，属性名(name)，属性值(value)的map组成的list
     *
     * @param o the o
     * @return the fileds info
     */
    public static List<Map<String,Object>> getOwnFieldsInfo(Object o) {
        Field[] fields = o.getClass().getDeclaredFields();
        List list = new ArrayList();
        Map<String, Object> infoMap;
        for (int i = 0; i < fields.length; i++) {
            Object val = getValueBy(fields[i].getName(), o);
            if (val == null) {
                continue;
            }
            infoMap = new HashMap(3);
            infoMap.put("type", fields[i].getType().toString());
            infoMap.put("name", fields[i].getName());
            infoMap.put("value", val);
            list.add(infoMap);
        }
        return list;
    }

    /**
     * 根据属性名获取属性值
     *
     * @param fieldName the field name
     * @param o         the o
     * @return the field value by name
     */
    @Deprecated
    public static Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[]{});
            Object value = method.invoke(o, new Object[]{});
            return value;
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 获取指定字段值.
     *
     * @param fieldName the field name
     * @param o         the o
     * @return the value by
     */
    public static Object getValueBy(String fieldName, Object o) {
        Class<?> clazz = o.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Object value = getValue(fieldName, o, fields);
        if (value != null) {
            return value;
        }
        return getSuperclassValueBy(fieldName, o, clazz.getSuperclass());
    }

    /**
     * 获取自身指定字段属性值
     *
     * @param fieldName the field name
     * @param o         the o
     * @return the own value by
     */
    public static Object getOwnValueBy(String fieldName, Object o) {
        Class<?> clazz = o.getClass();
        Field[] fields = clazz.getDeclaredFields();
        return getValue(fieldName, o, fields);
    }

    /**
     * 获取对象上级指定字段值.
     *
     * @param fieldName  the field name
     * @param o          the o
     * @param superclass the superclass
     * @return the superclass value by
     */
    public static Object getSuperclassValueBy(String fieldName, Object o, Class<?> superclass) {
        if(superclass!=null) {
            Object value = getValue(fieldName, o, superclass.getDeclaredFields());
            if(value!=null){
                return value;
            }
            return getSuperclassValueBy(fieldName,o,superclass.getSuperclass());
        }
        return null;
    }

    private static Object getValue(String fieldName, Object o, Field[] fields) {
        for (Field f : fields){
            if(f.getName().equalsIgnoreCase(fieldName)){
                try {
                    f.setAccessible(true);
                    return f.get(o);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private static List<Object> getValues(Object o, Field[] fields) {
        List<Object> array = new ArrayList();
        for (Field f : fields){
            try {
                f.setAccessible(true);
                array.add(f.get(o));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return array;
    }

    /**
     * 获取属性名数组
     *
     * @param o the o
     * @return the filed name
     */
    public static List<String> getNames(Object o) {
        List<String> fieldNames = new ArrayList();
        Field[] fields = o.getClass().getDeclaredFields();
        for (Field f : fields) {
            fieldNames.add(f.getName());
        }
        fieldNames.addAll(getSuperclassNames(o.getClass().getSuperclass()));
        return fieldNames;
    }

    /**
     * 获取自身属性名.
     *
     * @param o the o
     * @return the own names
     */
    public static List<String> getOwnNames(Object o) {
        List<String> fieldNames = new ArrayList();
        Field[] fields = o.getClass().getDeclaredFields();
        for (Field f : fields) {
            fieldNames.add(f.getName());
        }
        return fieldNames;
    }

    /**
     * 获取对象上级字段名.
     *
     * @param superclass the superclass
     * @return the superclass names
     */
    public static List<String> getSuperclassNames(Class<?> superclass) {
        List<String> fieldNames = new ArrayList();
        if (superclass != null) {
            for (Field f : superclass.getDeclaredFields()) {
                fieldNames.add(f.getName());
            }
            fieldNames.addAll(getSuperclassNames(superclass.getSuperclass()));
        }
        return fieldNames;
    }
}
