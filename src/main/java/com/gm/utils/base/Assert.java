package com.gm.utils.base;

import com.gm.ex.JsonBusinessException;
import com.gm.model.response.JsonResult;
import com.gm.utils.Utils;

import java.util.Collection;

/**
 * 断言工具类
 *
 * @author Jason
 */
public class Assert implements Utils {

    /**
     * 断言数字大于0
     *
     * @param <T> 泛型
     * @param t   断言数字
     * @param msg 小于1提示信息
     * @return t 大于0原样返回
     */
    public static <T extends Number> T lessOne(T t, Object msg) {
        Null(t, "断言数字是空");

        if (t.intValue() < 1) {
            throw new JsonBusinessException(msg);
        }

        return t;
    }

    /**
     * 断言数字大于0
     *
     * @param <T> 泛型
     * @param t   断言数字
     * @return t 大于0原样返回
     */
    public static <T extends Number> T lessOne(T t) {
        return lessOne(t, "[Assertion failed] - the object argument must be less than one");
    }

    /**
     * 断言响应成功{@link JsonResult}
     *
     * @param <T> 泛型
     * @param t   响应对象
     * @return 200原样返回 t
     */
    public static <T extends JsonResult> T successful(T t) {
        if (JsonResult.SUCCESS.equals(t.getCode())) {
            return t;
        }
        throw new JsonBusinessException(t);
    }

    /**
     * 断言包含
     *
     * @param <T>  泛型
     * @param more 更大的对象
     * @param sub  更小的对象
     * @param msg  大的包含小的提示信息
     * @return t 不包含原样返回
     */
    public static <T> T isContains(T more, T sub, Object msg) {
        if (more instanceof String) {
            if (!((String) more).contains(sub.toString())) {
                throw new JsonBusinessException(msg);
            }
        } else if (more instanceof Collection) {
            if (!((Collection) more).contains(sub)) {
                throw new JsonBusinessException(msg);
            }
        }

        return sub;
    }

    /**
     * 断言不包含
     *
     * @param <T>  泛型
     * @param more 更大的对象
     * @param sub  更小的对象
     * @param msg  大的包含小的提示信息
     * @return t 不包含原样返回
     */
    public static <T> T contains(T more, T sub, Object msg) {
        if (more instanceof String) {
            if (((String) more).contains(sub.toString())) {
                throw new JsonBusinessException(msg);
            }
        } else if (more instanceof Collection) {
            if (((Collection) more).contains(sub)) {
                throw new JsonBusinessException(msg);
            }
        }

        return sub;
    }

    /**
     * 断言包含
     *
     * @param <T>  泛型
     * @param more 更大的对象
     * @param sub  更小的对象
     * @return t 不包含原样返回
     */
    public static <T> T isContains(T more, T sub) {
        return isContains(sub, more, "[Assertion failed] - the object argument must be contain");
    }

    /**
     * 断言不包含
     *
     * @param <T>  泛型
     * @param more 更大的对象
     * @param sub  更小的对象
     * @return t 不包含原样返回
     */
    public static <T> T contains(T more, T sub) {
        return isContains(sub, more, "[Assertion failed] - the object argument must be contain");
    }

    /**
     * 断言是空
     *
     * @param <T> 泛型
     * @param t   内容
     * @param msg 空提示
     * @return 非空原样返回 t
     */
    public static <T> T isEmpty(T t, Object msg) {
        if(!Bool.isEmpty(t)){
            throw new JsonBusinessException(msg);
        }
        return t;
    }

    /**
     * 断言非空.
     *
     * @param <T> the type parameter
     * @param t   the t
     * @param msg the msg
     * @return the t
     */
    public static <T> T empty(T t, Object msg) {
        if(Bool.isEmpty(t)){
            throw new JsonBusinessException(msg);
        }
        return t;
    }


    /**
     * 断言是空
     *
     * @param <T> 泛型
     * @param t   内容
     * @return 非空原样返回 t
     */
    public static <T> T isEmpty(T t) {
        return isEmpty(t, "[Assertion failed] - the object argument must be not null");
    }

    /**
     * 断言非空.
     *
     * @param <T> the type parameter
     * @param t   the t
     * @return the t
     */
    public static <T> T empty(T t) {
        return empty(t, "[Assertion failed] - the object argument must be not null");
    }


    /**
     * 断言一致
     *
     * @param <T> 泛型
     * @param t1  first obj
     * @param t2  second obj
     * @param msg 一致时提示信息 msg
     */
    public static <T> void isEqual(T t1, T t2, Object msg) {
        if (!Bool.isEqual(t1, t2)) {
            throw new JsonBusinessException(msg);
        }
    }

    /**
     * 断言不一致.
     *
     * @param <T> the type parameter
     * @param t1  the t 1
     * @param t2  the t 2
     * @param msg the msg
     */
    public static <T> void equal(T t1, T t2, Object msg) {
        if (Bool.isEqual(t1, t2)) {
            throw new JsonBusinessException(msg);
        }
    }

    /**
     * 断言一致
     *
     * @param <T> 泛型
     * @param t1  first obj
     * @param t2  second obj
     */
    public static <T> void isEqual(T t1, T t2) {
        isEqual(t1, t2,"[Assertion failed] - the object argument must be equal");
    }

    /**
     * 断言不一致
     *
     * @param <T> 泛型
     * @param t1  first obj
     * @param t2  second obj
     */
    public static <T> void equal(T t1, T t2) {
        equal(t1, t2,"[Assertion failed] - the object argument must be equal");
    }

    /**
     * 断言是空
     *
     * @param <T> 泛型
     * @param t   内容
     * @param msg 空提示
     * @return 非空原样返回 t
     */
    public static <T> T isNull(T t, Object msg) {
        if (!Bool.isNull(t)) {
            throw new JsonBusinessException(msg);
        }

        return t;
    }

    /**
     * 断言非空
     *
     * @param <T> 泛型
     * @param t   内容
     * @param msg 空提示
     * @return 非空原样返回 t
     */
    public static <T> T Null(T t, Object msg) {
        if (Bool.isNull(t)) {
            throw new JsonBusinessException(msg);
        }

        return t;
    }


    /**
     * 断言是空
     *
     * @param <T> 泛型
     * @param t   内容
     * @return 非空原样返回 t
     */
    public static <T> T isNull(T t) {
        return isNull(t,"[Assertion failed] - the object argument must be not null");
    }

    /**
     * 断言非空
     *
     * @param <T> 泛型
     * @param t   内容
     * @return 非空原样返回 t
     */
    public static <T> T Null(T t) {
        return Null(t,"[Assertion failed] - the object argument must be not null");
    }

    /**
     * 断言都是空内容
     *
     * @param <T> 泛型
     * @param msg 空提示
     * @param ts  内容
     * @return 非空原样返回 t
     */
    public static <T> T isNull(Object msg, T... ts) {
        for (T t : ts){
            if(Bool.isNull(t)){
                return t;
            }
        }
        throw new JsonBusinessException(msg);
    }

    /**
     * 断言都不是空内容
     *
     * @param <T> 泛型
     * @param msg 空提示
     * @param ts  内容
     * @return 非空原样返回 t
     */
    public static <T> T Null(Object msg, T... ts) {
        for (T t : ts){
            if(!Bool.haveNull(t)){
                return t;
            }
        }
        throw new JsonBusinessException(msg);
    }

    /**
     * 断言都是空内容
     *
     * @param <T> 泛型
     * @param ts  内容
     * @return 非空原样返回 t
     */
    public static <T> T isNull(T... ts) {
        return isNull("[Assertion failed] - the object argument must be not null", ts);
    }

    /**
     * 断言都不是空内容
     *
     * @param <T> 泛型
     * @param ts  内容
     * @return 非空原样返回 t
     */
    public static <T> T Null(T... ts) {
        return Null("[Assertion failed] - the object argument must be not null", ts);
    }

    /**
     * 断言是真
     *
     * @param bool 内容
     * @param msg  空提示
     * @return 非空原样返回 bool
     */
    public static boolean isTrue(Boolean bool, Object msg) {
        if (Bool.isNull(bool) || !bool) {
            throw new JsonBusinessException(msg);
        }
        return bool;
    }

    /**
     * 断言真
     *
     * @param bool 内容
     * @return 非空原样返回 bool
     */
    public static boolean isTrue(Boolean bool) {
        return isTrue(bool, "[Assertion failed] - the object argument must be true");
    }
}
