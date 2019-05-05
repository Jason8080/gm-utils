package com.gm.utils.ext;

import com.gm.enums.Regexp;
import com.gm.utils.Utils;
import com.gm.utils.base.Convert;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则工具类
 *
 * @author Jason
 */
public class Regex implements Utils {

    /**
     * (1)能匹配的年月日类型有：
     * 2014年4月19日
     * 2014年4月19号
     * 2014-4-19
     * 2014/4/19
     * 2014.4.19
     * (2)能匹配的时分秒类型有：
     * 15:28:21
     * 15:28
     * 5:28 pm
     * 15点28分21秒
     * 15点28分
     * 15点
     * (3)能匹配的年月日时分秒类型有：
     * (1)和(2)的任意组合，二者中间可有任意多个空格
     * 如果dateStr中有多个时间串存在，只会匹配第一个串，其他的串忽略
     *
     * @param str the str
     * @return string string
     */
    public static String findChineseDate(String str) {
        Pattern p = Pattern.compile(Regexp.FIND_CHINESE_DATE.getCode(), Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        Matcher matcher = p.matcher(str);
        if (matcher.find()) {
            return matcher.group();
        }
        return str;
    }


    /**
     * Find date string.
     *
     * @param str the str
     * @return the string
     */
    public static String findDate(String str) {
        Pattern pattern = Pattern.compile(Regexp.FIND_NUMBER_DATE.getCode(), Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            return matcher.group();
        }
        return str;
    }

    /**
     * 找出字符串中的手机号码.
     *
     * @param str 文本
     * @return 所有手机号码 list
     */
    public static List<String> findMobile(String str) {
        List<String> mobiles = new ArrayList();
        Pattern pattern = Pattern.compile(Regexp.FIND_MOBILE.getCode());
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            mobiles.add(matcher.group());
        }
        return mobiles;
    }

    /**
     * 找出字符串中的电话号码.
     *
     * @param str 文本
     * @return 所有手机号码 list
     */
    public static List<String> findPhone(String str) {
        List<String> mobiles = new ArrayList();
        Pattern pattern = Pattern.compile(Regexp.FIND_PHONE.getCode());
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            mobiles.add(matcher.group());
        }
        return mobiles;
    }

    /**
     * 判断字符串{source}是否符合{regex}正则规格
     *
     * @param source 判断字符串
     * @param regex  目标正则表达式字符串
     * @return true 符合 false  不符合
     */
    public static boolean isPattern(String source, String regex) {
        return Pattern.matches(regex, source);
    }


    /**
     * 查找出符合条件的集合
     *
     * @param source the source
     * @param regex  the regex
     * @return list list
     */
    public static List<String> find(String source, String regex) {
        List<String> result = new ArrayList();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(source);
        while (matcher.find()){
            result.add(Convert.toEmpty(matcher.group()));
        }
        return result;
    }

    /**
     * 查找出符合条件的字符
     *
     * @param source the source
     * @param regex  the regex
     * @return list string
     */
    public static String findFirst(String source, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(source);
        while (matcher.find()){
            return matcher.group();
        }
        return "";
    }


    /**
     * 获取页面编码.
     *
     * @param html the html
     * @return the string
     */
    public static String getCharset(String html){
        String meta = Convert.toEmpty(Regex.findFirst(html, Regexp.FIND_HTML_ENCODE.getCode()), "<meta charset=\"UTF-8\"/>");
        String[] split = meta.split("charset=");
        String encode = split[1].replaceAll("\"", "");
        encode = encode.replaceAll("\'", "");
        String charset = encode.split(" ")[0];
        charset = charset.split("/")[0];
        charset = charset.split(">")[0];
        return charset;
    }
}
