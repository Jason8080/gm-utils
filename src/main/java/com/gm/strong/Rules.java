package com.gm.strong;


import com.gm.enums.Regexp;
import com.gm.ex.BusinessException;
import com.gm.utils.base.Bool;
import com.gm.utils.base.Convert;
import com.gm.utils.base.ExceptionUtils;
import com.gm.utils.base.Reflection;
import com.gm.utils.ext.Json;
import com.gm.utils.ext.Math;
import com.gm.utils.ext.Regex;
import lombok.Data;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 超级规则.
 *
 * @param <M> the type parameter
 * @param <R> the type parameter
 * @author Jason
 */
public class Rules<M, R extends Rules.Rule> {

    /**
     * 规则模型
     */
    private M m;
    /**
     * 规则集
     */
    private List<R> rs;

    /**
     * 初始化规则模型
     *
     * @param m  the m
     * @param rs the rs
     */
    public Rules(M m, List<R> rs) {
        this.m = m;
        this.rs = sort(rs);
    }

    /**
     * 初始化规则模型.
     *
     * @param m    the m
     * @param rule the rule
     */
    public Rules(M m, String rule) {
        this.m = m;
        List<R> rs = new ArrayList();
        rs.add((R) new Rule(rule));
        this.rs = rs;
    }

    /**
     * 解析多条规则.
     * 优先标识匹配.
     *
     * @return the string
     */
    public String parse() {
        R r = identity();
        if (Bool.isNull(r)) {
            rs = sort(rs);
            for (R o : rs) {
                try {
                    return parse(m, o.getRule());
                } catch (BusinessException e) {
                    continue;
                }
            }
            return ExceptionUtils.cast(String.format("匹配规则模型{%s}失败", Json.toJson(m)));
        }
        return parse(m, r.getRule());
    }

    /**
     * 解析规则.
     *
     * @param <M>  the type parameter
     * @param m    the m
     * @param rule the rule
     * @return the string
     */
    public static <M> String parse(M m, String rule) {
        //获取模型字段名集合
        List<String> fields = Reflection.getNames(m);
        //提取规则@Example [price]*[qty]*[profitability]
        //提取规则动态列
        List<String> columns = Regex.find(rule, Regexp.BRACKET_STR.getCode());
        for (String column : columns) {
            //去除规则框@Example [price] -> price
            String c = column.substring(1, column.length() - 1);
            String value = String.valueOf(Reflection.getValueBy(c, m));
            if (m instanceof Map) {
                value = String.valueOf(((Map) m).get(c));
            }
            if (!fields.contains(c) && Bool.isNull(value)) {
                ExceptionUtils.cast(String.format("匹配规则模型{%s}失败: {%s}", Json.toJson(m), column));
            }
            //替换动态列@Example [price]*[qty]*[profitability] -> 8.88*3*0.5
            rule = rule.replace(column, Convert.toEmpty(value));
        }
        return rule;
    }

    /**
     * 执行规则并相应结果.
     *
     * @param <T>    the type parameter
     * @param rule   the a rule
     * @param aClass the a class
     * @return the big decimal
     */
    public <T> T execute(String rule, Class<T> aClass) {
        String expression = parse(m, rule);
        return Math.execute(expression, aClass);
    }

    /**
     * 执行规则并相应结果.
     *
     * @param <T>    the type parameter
     * @param aClass the a class
     * @return the big decimal
     */
    public <T> T execute(Class<T> aClass) {
        String expression = parse();
        return Math.execute(expression, aClass);
    }


    /**
     * 根据ruleId字段值匹配.
     *
     * @return the r
     */
    public R identity() {
        String identity = m instanceof Map ? String.valueOf(((Map) m).get(R.identity)) : String.valueOf(Reflection.getValueBy(R.identity, m));
        Optional<R> op = rs.stream().filter(o -> identity.equalsIgnoreCase(o.getRuleId())).findFirst();
        R r = op.isPresent() ? op.get() : null;
        return r;
    }


    /**
     * 根据sort排序号排序 min to max.
     * 根据operatorTime更新时间排序 new to old.
     * 存在空值不移动位置, 最后更新的放前面
     *
     * @param rs the rs
     * @return the r
     */
    public List<R> sort(List<R> rs) {
        return rs.stream().sorted((o1, o2) -> {
            //1. 排序号排序
            int i = Bool.haveNull(o1.getSort(), o2.getSort()) ? 0 : o1.getSort() - o2.getSort();
            //2. 更新时间排序
            if (i == 0) {
                Long l = Bool.haveNull(o1.getOperatorTime(), o2.getOperatorTime()) ? 0 : o2.getOperatorTime().getTime() - o1.getOperatorTime().getTime();
                return l.intValue();
            } else {
                return i;
            }
        }).collect(Collectors.toList());
    }

    @Data
    public static class Rule implements Serializable {

        /**
         * The constant identity.
         */
        public static final String identity = "ruleId";

        /**
         * Instantiates a new Rule.
         */
        public Rule() {
        }

        /**
         * Instantiates a new Rule.
         *
         * @param rule the rule
         */
        public Rule(String rule) {
            this.rule = rule;
        }

        /**
         * Instantiates a new Rule.
         *
         * @param ruleId the rule id
         * @param rule   the rule
         */
        public Rule(String ruleId, String rule) {
            this.ruleId = ruleId;
            this.rule = rule;
        }

        /**
         * The Rule id.
         */
        protected String ruleId;
        /**
         * The Sort.
         */
        protected Integer sort;
        /**
         * The Biz code.
         */
        protected String bizCode;
        /**
         * The Biz desc.
         */
        protected String bizDesc;
        /**
         * The Rule.
         * <p>
         * example: "[price]*[qty]*[profitability]"
         */
        protected String rule;
        /**
         * The Rule desc.
         */
        protected String ruleDesc;
        /**
         * The Operator.
         */
        protected String operator;
        /**
         * The Operator time.
         */
        protected Date operatorTime;
    }
}
