package com.gm.enums;

/**
 * @author Jason
 */

public enum Pattern {
    DATE_YEAR("yyyy"),
    DATE_MONTH("yyyy-MM"),
    DATE_DAY("yyyy-MM-dd"),
    DATE_HOUR("yyyy-MM-dd HH"),
    DATE_MINUTE("yyyy-MM-dd HH:mm"),
    DATE_SECOND("yyyy-MM-dd HH:mm:ss"),
    DATE_MILLISECOND("yyyy-MM-dd HH:mm:ss.SSS"),
    ;
    private String pattern;

    Pattern(String pattern) {
        this.pattern = pattern;
    }

    public String get() {
        return pattern;
    }
}
