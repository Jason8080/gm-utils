package com.gm.strong;

import com.gm.utils.base.Bool;

import java.util.*;

/**
 * 超级字符串
 *
 * @author Jason
 */
public class Str {
    /**
     * 参照字符文本
     */
    private String[] ts;

    /**
     * 是否包涵
     *
     * @param target 词组集合
     * @return the list 包含的敏感词集合
     */
    public List<String> containAll(String... target) {
        List<String> array = new ArrayList();
        if (!Bool.isNull(ts)) {
            for (String s : target) {
                for (String t : ts) {
                    if (t.contains(s)) {
                        array.add(s);
                    }
                }
            }
        }
        return array;
    }

    /**
     * 是否包含
     *
     * @param target the 词组集合
     * @return the 包含的第一个词
     */
    public String containFirst(String... target) {
        if (!Bool.isNull(ts)) {
            for (String s : target) {
                for (String t : ts) {
                    if (t.contains(s)) {
                        return s;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 包含次数.
     *
     * @param target the target
     * @return the map
     */
    public Map<String, Integer> containCount(String... target) {
        Map<String, Integer> map = new HashMap(target.length);
        if (!Bool.isNull(ts)) {
            for (String s : target) {
                int count = 0;
                for (String t : ts) {
                    String[] split = t.split(s);
                    count += split.length - 1;
                    if (t.endsWith(s)) {
                        count += 1;
                    }
                }
                map.put(s, count);
            }
        }
        return map;
    }

    /**
     * 包含最大次数的词组.
     *
     * @param target the target
     * @return the map
     */
    public String containMax(String... target) {
        Map<String, Integer> map = containCount(target);
        if (!Bool.isNull(map)) {
            Map<String, Integer> sort = com.gm.utils.base.Collection.sortV(map);
            return sort.entrySet().iterator().next().getKey();
        }
        return null;
    }


    /**
     * 是否包涵
     *
     * @param target 词组集合
     * @return boolean boolean
     */
    public boolean contains(String... target) {
        return !Bool.isNull(containFirst(target));
    }

    /**
     * 是否有
     *
     * @param target 词组集合
     * @return boolean boolean
     */
    public boolean part(String... target) {
        if (!Bool.isNull(ts)) {
            for (String s : target) {
                for (String t : ts) {
                    if (t.equalsIgnoreCase(s)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 替换包含内容
     *
     * @param symbol 替换内容
     * @param target 包含内容
     * @return boolean boolean
     */
    public String[] replace(String symbol, String... target) {
        if (!Bool.isNull(ts)) {
            for (String s : target) {
                for (int i = 0; i < ts.length; i++) {
                    if (ts[i].contains(s)) {
                        ts[i] = ts[i].replace(s, symbol);
                    }
                }
            }
        }
        return ts;
    }


    /**
     * 初始化参考文本.
     *
     * @param ts 参考文本集
     */
    public Str(String[] ts) {
        this.ts = ts;
    }

    /**
     * Instantiates a new Str.
     *
     * @param ts the ts
     */
    public Str(Collection<String> ts) {
        this.ts = ts.toArray(new String[]{});
    }

    /**
     * Instantiates a new Str.
     *
     * @param ts the ts
     */
    public Str(String ts) {
        this.ts = new String[]{ts};
    }
}
