package com.gm.utils.base;

import com.gm.utils.Utils;
import com.gm.utils.ext.Json;

import java.text.Collator;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 集合工具类
 *
 * @author Jason
 */
public class Collection implements Utils {

    /**
     * 合并集合.
     *
     * @param <T> the type parameter
     * @param cs  the cs
     * @return the list
     */
    public static <T> List<T> merge(java.util.Collection<T>... cs) {
        List<T> all = new ArrayList();
        for (java.util.Collection<T> c : cs) {
            all.addAll(c);
        }
        return all;
    }

    /**
     * 把集合toString.
     *
     * @param <T>       泛型
     * @param c         集合
     * @param separator 分隔符
     * @return String string
     */
    public static <T> String toArrString(java.util.Collection<T> c, String separator) {
        StringBuilder sb = new StringBuilder();
        if (Bool.isNull(c)) {
            return sb.toString();
        }
        for (T t : c) {
            sb.append(t.toString());
            sb.append(separator);
        }
        return sb.substring(0, sb.length() - separator.length());
    }

    /**
     * 把集合toString.
     *
     * @param <T>       泛型
     * @param c         集合
     * @param separator 分隔符
     * @return String string
     */
    public static <T> String toArrString(T[] c, String separator) {
        StringBuilder sb = new StringBuilder();
        if (Bool.isNull(c)) {
            return sb.toString();
        }
        for (T t : c) {
            sb.append(t.toString());
            sb.append(separator);
        }
        return sb.substring(0, sb.length() - separator.length());
    }

    /**
     * 获取Map的所有Key集合.
     *
     * @param <K> the type parameter
     * @param <V> the type parameter
     * @param map the map
     * @return the keys
     */
    public static <K, V> java.util.Collection<K> getKeys(Map<K, V> map) {
        return map.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toList());
    }

    /**
     * 批量删除Key.
     *
     * @param <K>  the type parameter
     * @param <V>  the type parameter
     * @param more map
     * @param keys 删除的keys
     */
    public static <K, V> void removeKeys(Map<K, V> more, Object... keys) {
        java.util.Collection<K> allKeys = getKeys(more);
        for (Object key : keys) {
            if (Bool.contains(allKeys, key)) {
                more.remove(key);
            }
        }
    }

    /**
     * 根据Key批量删除子集.
     *
     * @param <K>  the type parameter
     * @param <V>  the type parameter
     * @param more map
     * @param sub  删除的子集
     */
    public static <K, V> void removeKeys(Map<K, V> more, Map sub) {
        removeKeys(more, getKeys(sub));
    }

    /**
     * 批量删除子集.
     *
     * @param <K>  the type parameter
     * @param <V>  the type parameter
     * @param more map
     * @param sub  删除的子集
     */
    public static <K, V> void remove(Map<K, V> more, Map<Object, Object> sub) {
        Iterator<Map.Entry<K, V>> it = more.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<K, V> next = it.next();
            Object obj = sub.get(next.getKey());
            if (Bool.isEqual(next.getValue(), obj)) {
                it.remove();
            }
        }
    }

    /**
     * 批量删除Value.
     *
     * @param <K>    the type parameter
     * @param <V>    the type parameter
     * @param more   map
     * @param values 删除的values
     */
    public static <K, V> void removeValues(Map<K, V> more, java.util.Collection<Object> values) {
        Iterator<Map.Entry<K, V>> it = more.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<K, V> next = it.next();
            if (values.contains(next.getValue())) {
                it.remove();
            }
        }
    }

    /**
     * 根据value批量删除子集.
     *
     * @param <K>  the type parameter
     * @param <V>  the type parameter
     * @param more map
     * @param sub  删除的子集
     */
    public static <K, V> void removeValues(Map<K, V> more, Map sub) {
        removeValues(more, sub.values());
    }

    /**
     * 按key排序(中文以首字母)
     *
     * @param <K> Key类型
     * @param <V> Value类型
     * @param map 排序的集合
     * @return 排序后的集合 map
     */
    public static <K, V> Map<K, V> sortK(Map<K, V> map) {
        TreeMap<K, V> tree = new TreeMap(Collator.getInstance(java.util.Locale.CHINA));
        tree.putAll(map);
        return tree;
    }

    /**
     * 按val排序(中文以首字母)
     *
     * @param <K> Key类型
     * @param <V> Val类型
     * @param map 排序的集合
     * @return 排序后的集合 map
     */
    public static <K, V> Map<K, V> sortV(Map<K, V> map) {
        TreeMap<K, V> tree = new TreeMap(new Comparator<K>() {

            @Override
            public int compare(K o1, K o2) {
                V v1 = map.get(o1);
                V v2 = map.get(o2);
                Collator collator = Collator.getInstance(Locale.CHINA);
                int compare = collator.compare(v1, v2);
                // 如果值相同: 返回0会导致key覆盖, 所以返回1(保持原Map顺序)ASC
                // (collator.compare(o1, o2)!=0?collator.compare(o1, o2):1)
                return compare != 0 ? compare : 1;
            }
        });
        tree.putAll(map);
        return tree;
    }


    /**
     * 判断目标在参考值的左边
     * 对象需要实现equals方法
     *
     * @param <T>    the type parameter
     * @param target 目标
     * @param t     参考值
     * @param ts     所有元素
     * @return boolean boolean
     */
    public static <T> Boolean less(T target, T t, List<T> ts) {
        if (Bool.haveNull(target, t, ts)) {
            Logger.info(String.format("{%s|%s|%s}是空", target, t, Json.toJson(ts)));
            return false;
        }
        if (target.equals(t)) {
            return true;
        }
        if (ts.indexOf(target) < 0) {
            Logger.info(String.format("目标{%s}不在这个{%s}范围", target, Json.toJson(ts)));
            return false;
        }
        if (ts.indexOf(t) < 0) {
            Logger.info(String.format("参考值{%s}不在这个{%s}范围", t, Json.toJson(ts)));
            return false;
        }
        return ts.indexOf(t) > ts.indexOf(target);
    }

    /**
     * 判断目标在参考值的左边
     * 对象需要实现equals方法
     *
     * @param <T>    the type parameter
     * @param target the target
     * @param t     the t
     * @param ts    the ts
     * @return the boolean
     */
    public static <T> Boolean less(T target, T t, T... ts) {
        if (Bool.haveNull(target, t, ts)) {
            Logger.info(String.format("{%s|%s|%s}是空", target, t, Json.toJson(ts)));
            return false;
        }
        return less(target, t, Arrays.asList(ts));
    }


    /**
     * 获取参考值左边的元素
     *
     * @param <T> the type parameter
     * @param t  参考值
     * @param all 所有元素
     * @return list list
     */
    public static <T> List<T> getLess(T t, List<T> all) {
        if (Bool.haveNull(t, all)) {
            Logger.info(String.format("{%s|%s}是空", t, Json.toJson(all)));
            return new ArrayList();
        }
        if (all.indexOf(t) < 0) {
            Logger.info(String.format("参考值{%s}不在这个{%s}范围", t, Json.toJson(all)));
            return new ArrayList();
        }
        return all.subList(0, all.indexOf(t) + 1);
    }
}
