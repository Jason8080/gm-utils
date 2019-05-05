package com.gm.builder;

import com.gm.utils.base.Day;
import com.gm.utils.ext.Json;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Date;

/**
 * The type Logger builder.
 *
 * @author Jason
 */
public class LoggerBuilder implements Serializable {
    /**
     * The constant prefix.
     */
    public static final String prefix = "--\t";
    /**
     * The constant suffix.
     */
    public static final String suffix = "\n";
    /**
     * 初始化快捷键.
     */
    public static LoggerBuilder c = new LoggerBuilder();


    /**
     * The Loc.
     */
    private static final ThreadLocal<StringBuilder> loc = new ThreadLocal();

    private ThreadLocal<StringBuilder> loc() {
        if(loc.get()!=null){
            return loc;
        }
        loc.set(new StringBuilder());
        return loc;
    }

    /**
     * is operator logger builder.
     *
     * @param operator the operator
     * @return the logger builder
     */
    public LoggerBuilder isOperator(String operator){
        loc().get().append(prefix.concat("操作人:\t\t").concat(operator).concat(suffix));
        return this;
    }

    /**
     * is ip logger builder.
     *
     * @param ip the ip
     * @return the logger builder
     */
    public LoggerBuilder isIp(String ip){
        loc().get().append(prefix.concat("操作IP:\t\t").concat(ip).concat(suffix));
        return this;
    }

    /**
     * is path logger builder.
     *
     * @param path the path
     * @return the logger builder
     */
    public LoggerBuilder isPath(String path){
        loc().get().append(prefix.concat("请求地址:\t").concat(Json.toJson(path)).concat(suffix));
        return this;
    }

    /**
     * is params logger builder.
     *
     * @param params the params
     * @return the logger builder
     */
    public LoggerBuilder isParams(Object params){
        loc().get().append(prefix.concat("请求参数:\t").concat(Json.toJson(params)).concat(suffix));
        return this;
    }

    /**
     * is start logger builder.
     *
     * @param start the start
     * @return the logger builder
     */
    public LoggerBuilder isStart(Date start){
        loc().get().append(prefix.concat("请求时间:\t").concat(Day.format_second.format(start)).concat(suffix));
        return this;
    }

    /**
     * is start logger builder.
     *
     * @return the logger builder
     */
    public LoggerBuilder isStart(){
        loc().get().append(prefix.concat("请求时间:\t").concat(new DateTime().toString("yyyy-MM-dd HH:mm:ss")).concat(suffix));
        return this;
    }

    /**
     * is end logger builder.
     *
     * @param end the end
     * @return the logger builder
     */
    public LoggerBuilder isEnd(Date end){
        loc().get().append(prefix.concat("响应时间:\t").concat(Day.format_second.format(end)).concat(suffix));
        return this;
    }

    /**
     * is end logger builder.
     *
     * @return the logger builder
     */
    public LoggerBuilder isEnd(){
        loc().get().append(prefix.concat("响应时间:\t").concat(new DateTime().toString("yyyy-MM-dd HH:mm:ss")).concat(suffix));
        return this;
    }

    /**
     * is msg logger builder.
     *
     * @param msg the msg
     * @return the logger builder
     */
    public LoggerBuilder isMsg(Object msg){
        loc().get().append(prefix.concat("响应内容:\t").concat(Json.toJson(msg)).concat(suffix));
        return this;
    }

    /**
     * 总消耗时间.
     *
     * @param n the n
     * @return the logger builder
     */
    public LoggerBuilder isTime(Number n){
        loc().get().append(prefix.concat("耗时:\t\t").concat(n.toString()).concat(" ms").concat(suffix));
        return this;
    }

    /**
     * Builder string.
     *
     * @return the string
     */
    public String builder(){
        StringBuilder sb = loc().get();
        loc.remove();
        return sb.substring(prefix.length(),sb.length()-suffix.length());
    }
}
