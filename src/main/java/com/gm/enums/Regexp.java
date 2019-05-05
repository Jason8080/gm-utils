package com.gm.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 正则表达式枚举常量
 *
 * @author Jason
 */
public enum Regexp {
    /* 规则类 */
    BRACKET_STR("找出所有[*]的字符串", "\\[[^\\[\\]]*\\]"),
    /* 时间类 */
    FIND_CHINESE_DATE("查找中文时间", "(\\d{1,4}[-|\\/|年|\\.]\\d{1,2}[-|\\/|月|\\.]\\d{1,2}([日|号])?(\\s)" +
            "*(\\d{1,2}([点|时])?((:)?\\d{1,2}(分)?((:)?\\d{1,2}(秒)?)?)?)?(\\s)*(PM|AM)?)"),
    FIND_NUMBER_DATE("查找数字时间", "\\d{4}(([-|\\/|\\\\|\\.| ])?\\d{2})?(([-|\\/|\\\\|\\.| ])?\\d{2})" +
            "?(( )?\\d{2}(:)?\\d{2}(:)?\\d{2})?((.)?\\d{3})?"),
    /* 号码类 */
    FIND_MOBILE("查找手机号码", "((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18([0-3]|[5-9]))|(177))\\d{8}"),
    FIND_PHONE("查找电话号码", "(0\\d{2}-\\d{8}(-\\d{1,4})?)|(0\\d{3}-\\d{7,8}(-\\d{1,4})?)"),
    /* 地址类 */
    FIND_URL("查找URL地址", "((http|ftp|https):)?\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.," +
            "@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?"),
    /* 页面类 */
    FIND_HTML_TITLE("找出页面标题", "<title>.*?</title>"),
    FIND_HTML_ENCODE("找出页面编码", "<meta.*charset=(\"|\')?.*(\"|\')?.*>"),
    FIND_HTML_LABEL("找出页面标题", "<.*?>"),
    ;

    private String desc;

    private String code;

    Regexp(String desc, String code) {
        this.desc = desc;
        this.code = code;
    }

    /**
     * Get desc object.
     *
     * @param code the code
     * @return the object
     */
    public static String getDesc(Object code) {
        return getMap().get(code);
    }

    /**
     * Gets map.
     *
     * @return the map
     */
    public static Map<String, String> getMap() {
        HashMap<String, String> map = new HashMap();
        for (Regexp es : Regexp.values()) {
            map.put(es.getCode(), es.getDesc());
        }
        return map;
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets code.
     *
     * @param code the code
     */
    public void setCode(String code) {
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
