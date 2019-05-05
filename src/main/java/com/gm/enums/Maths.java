package com.gm.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 常用常量枚举
 *
 * @author Jason
 */
public enum Maths {

    /**
     * Range signal constant enum.
     */
    RANGE_SIGNAL("~", "范围符号"),;

    private Object code;
    private String desc;

    Maths(Object code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * Get desc object.
     *
     * @param code the code
     * @return the object
     */
    public static Object getDesc(Object code) {
        return getMap().get(code);
    }

    /**
     * Gets map.
     *
     * @return the map
     */
    public static Map<Object, String> getMap() {
        HashMap<Object, String> map = new HashMap<>();
        for (Maths es : Maths.values()) {
            map.put(es.getCode(), es.getDesc());
        }
        return map;
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public Object getCode() {
        return code;
    }

    /**
     * Sets code.
     *
     * @param code the code
     */
    public void setCode(Object code) {
        this.code = code;
    }

    /**
     * Gets desc.
     *
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Sets desc.
     *
     * @param desc the desc
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }
}
