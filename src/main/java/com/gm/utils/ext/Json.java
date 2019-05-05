package com.gm.utils.ext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gm.utils.Utils;
import com.gm.utils.base.Bool;
import com.gm.utils.base.Logger;

import java.util.*;

/**
 * Json工具类
 *
 * @author Jason
 */
public class Json implements Utils {

    /**
     * The constant mapper.
     */
    public static final ObjectMapper mapper = new ObjectMapper();

    /**
     * 转换成Json字符串
     *
     * @param <T>    the type parameter
     * @param object the object
     * @return string string
     */
    public static <T> String toJson(T object) {
        if (object == null) {
            return "";
        }
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            Logger.error(String.format("Json 对象{%s}转Json出错", object.getClass()), e);
            return null;
        }
    }

    /**
     * 转换成Json字符串(美化版)
     *
     * @param <T>    the type parameter
     * @param object the object
     * @return string string
     */
    public static <T> String toFormat(T object) {
        return format(toJson(object));
    }

    /**
     * 对象转换.
     *
     * @param <T>    the type parameter
     * @param obj    the obj
     * @param tClass the t class
     * @return the t
     */
    public static <T> T o2o(Object obj, Class<T> tClass) {
        if (!Bool.isNull(obj)) {
            if ((obj instanceof String) && tClass.equals(Map.class)) {
                Map o = toObject((String) obj, HashMap.class);
                if (Bool.isNull(o)) {
                    Map map = new HashMap(1);
                    map.put(obj, obj);
                    return (T) map;
                }
                return (T) o;
            }
            try {
                return mapper.convertValue(obj, tClass);
            } catch (IllegalArgumentException e) {
                try {
                    return tClass.newInstance();
                } catch (Exception e1) {
                    if (tClass.equals(Map.class)) {
                        return (T) new HashMap(0);
                    }
                    Logger.error(String.format("Json O2O{%s}转对象出错", obj), e);
                }
            }
        }
        return null;
    }

    /**
     * 转换成对象
     *
     * @param <T>    the type parameter
     * @param obj    the json
     * @param tClass the t class
     * @param ts     the ts
     * @return t t
     */
    public static <T> T toObject(Object obj, Class<T> tClass, Class... ts) {
        try {
            T t = o2o(obj, tClass);
            if (ts.length > 0) {
                if (Collection.class.isAssignableFrom(tClass)) {
                    return recursionT((Collection) t, ts);
                }
            }
            return t;
        } catch (Exception e) {
            if (tClass.equals(Map.class)) {
                return (T) new HashMap(0);
            }
            Logger.error(String.format("Json Json{%s}转对象出错", obj), e);
            return null;
        }
    }

    /**
     * 转换成对象
     *
     * @param <T>    the type parameter
     * @param json   the json
     * @param tClass the t class
     * @param ts     the ts
     * @return t t
     */
    public static <T> T toObject(String json, Class<T> tClass, Class... ts) {
        try {
            if (json == null) {
                return tClass.newInstance();
            }
            if (tClass.equals(String.class)) {
                return (T) json;
            }
            T t = mapper.readValue(json, tClass);
            if (ts.length > 0) {
                if (Collection.class.isAssignableFrom(tClass)) {
                    return recursionT((Collection) t, ts);
                }
            }
            return t;
        } catch (Exception e) {
            if (tClass.equals(Map.class)) {
                return (T) new HashMap(0);
            }
            Logger.error(String.format("Json Json{%s}转对象出错", json), e);
            return null;
        }
    }

    private static <T> T recursionT(Collection t, Class[] ts) {
        List source = new ArrayList();
        List<Class> classes = Arrays.asList(ts);
        for (int i = 0; i < classes.size(); i++) {
            Iterator it = t.iterator();
            while (it.hasNext()) {
                source.add(toObject(toJson(it.next()), classes.get(0),
                        classes.subList(1, classes.size()).toArray(new Class[]{})));
            }
        }
        return (T) source;
    }

    /**
     * 转换成对象
     *
     * @param json the json
     * @return the map
     */
    public static Map toMap(String json) {
        return toObject(json, Map.class);
    }


    /**
     * 对json字符串格式化输出
     *
     * @param json the json str
     * @return string string
     */
    public static String format(String json) {
        if (Bool.isNull(json)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        char last, current = '\0';
        int indent = 0;
        boolean entire = true;
        for (int i = 0; i < json.length(); i++) {
            last = current;
            current = json.charAt(i);
            switch (current) {
                case '{':
                case '[':
                    sb.append(current);
                    sb.append('\n');
                    indent++;
                    space(sb, indent);
                    break;
                case '}':
                case ']':
                    sb.append('\n');
                    indent--;
                    space(sb, indent);
                    sb.append(current);
                    break;
                case ',':
                    sb.append(current);
                    if (last != '\\' && entire) {
                        sb.append('\n');
                        space(sb, indent);
                    }
                    break;
                case '\"':
                    sb.append(current);
                    entire  = !entire;
                    break;
                default:
                    sb.append(current);
            }
        }

        return sb.toString();
    }

    /**
     * 添加space
     *
     * @param sb
     * @param indent
     */
    private static void space(StringBuilder sb, int indent) {
        for (int i = 0; i < indent; i++) {
            sb.append('\t');
        }
    }
}
