package com.gm.strong;

import com.gm.utils.base.Convert;

import java.util.Collection;

/**
 * 超级数组
 *
 * @param <T> the type parameter
 * @author Jason
 */
public class Array<T>  {

    private T[] ts;

    /**
     * 集合构造器.
     *
     * @param ts the ts
     */
    public Array(Collection<T> ts) {
        this.ts = ts.toArray((T[]) new Object[]{});
    }

    /**
     * 数组构造器.
     *
     * @param ts the ts
     */
    public Array(T... ts) {
        this.ts = Convert.toEmpty(ts, (T[]) new Object[0]);
    }


    /**
     * 获取下标位置的元素
     *
     * @param index the index
     * @return t
     */
    public T get(int index) {
        int l = ts.length;
        if (index < l) {
            return ts[index];
        }
        return null;
    }

    /**
     * 获取最后一个元素
     *
     * @return t
     */
    public T last() {
        int l = ts.length;
        if (l > 0) {
            return ts[l - 1];
        }
        return null;
    }
}
