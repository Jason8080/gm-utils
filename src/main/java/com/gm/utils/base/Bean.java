package com.gm.utils.base;

import com.gm.help.base.Quick;
import com.gm.utils.Utils;
import com.gm.utils.ext.Json;
import org.springframework.beans.BeanUtils;

import java.util.*;
import java.util.Collection;

/**
 * Bean工具类
 *
 * @author Jason
 */
public class Bean implements Utils {

    /**
     * 转换集合.
     *
     * @param <K>     the type parameter
     * @param <T>     the type parameter
     * @param sources the sources
     * @param tClass  the t class
     * @return the list
     */
    public static <K, T> List<T> toBeans(Collection<K> sources, Class<T> tClass) {
        List<T> ts = new ArrayList<>();
        if (!Bool.isNull(sources)) {
            for (K source : sources) {
                T target = Quick.exec(x -> tClass.newInstance());
                if (source != null) {
                    BeanUtils.copyProperties(source, target);
                }
                ts.add(target);
            }
        }
        return ts;
    }

    /**
     * 转换集合.
     *
     * @param <K>     the type parameter
     * @param <T>     the type parameter
     * @param sources the sources
     * @param exec    the exec
     * @return the list
     */
    public static <K, T> List<T> toBeans(Collection<K> sources, Quick.Exec<T> exec) {
        List<T> ts = new ArrayList<>();
        if (!Bool.isNull(sources)) {
            for (K source : sources) {
                try {
                    T target = exec.exec(null);
                    if (source != null) {
                        BeanUtils.copyProperties(source, target);
                    }
                    ts.add(target);
                } catch (Throwable throwable) {
                }
            }
        }
        return ts;
    }

    /**
     * 转换对象.
     *
     * @param <K>    the type parameter
     * @param <T>    the type parameter
     * @param source the source
     * @param tClass the t class
     * @return the t
     */
    public static <K, T> T toBean(K source, Class<T> tClass) {
        T target = Quick.exec(x -> tClass.newInstance());
        if (source != null) {
            BeanUtils.copyProperties(source, target);
        }
        return target;
    }

    /**
     * 转换对象.
     *
     * @param <K>    the type parameter
     * @param <T>    the type parameter
     * @param source the source
     * @param exec   the exec
     * @return the t
     */
    public static <K, T> T toBean(K source, Quick.Exec<T> exec) {
        try {
            T target = exec.exec(null);
            if (Bool.haveNull(source, target)) {
                return target;
            }
            BeanUtils.copyProperties(source, target);
            return target;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }

    /**
     * 补全属性.
     *
     * @param <K>    the type parameter
     * @param <T>    the type parameter
     * @param source the source
     * @param target the target
     * @return the t
     */
    public static <K, T> T copy(K source, T target) {
        if (Bool.haveNull(source, target)) {
            return target;
        }
        Map<String, Object> sourceMap = Json.o2o(source, Map.class);
        Map<String, Object> targetMap = Json.o2o(target, Map.class);
        Iterator<Map.Entry<String, Object>> it = targetMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> next = it.next();
            final String key = next.getKey();
            final Object value = next.getValue();
            // 如果目标属性值是空
            if (Bool.isNull(value)) {
                final Object obj = sourceMap.get(key);
                // 使用源属性值补充
                if (!Bool.isNull(obj)) {
                    targetMap.put(key, obj);
                }
            }
        }
        return Json.o2o(targetMap, (Class<T>) target.getClass());
    }
}
