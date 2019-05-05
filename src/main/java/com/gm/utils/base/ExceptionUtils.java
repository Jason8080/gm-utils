package com.gm.utils.base;

import com.gm.ex.BusinessException;
import com.gm.ex.GmException;
import com.gm.ex.ProcessException;
import com.gm.utils.Utils;

/**
 * 异常工具类
 *
 * @author Jason
 */
public class ExceptionUtils implements Utils {

    /**
     * 异常层级的分割符
     * 不是这个分隔符的异常信息无法处理
     */
    public static final String EXCEPTION_MESSAGE_SPLIT = "\\r\\n";

    /**
     * 异常换行分隔符
     * 不是这个分隔符信息无法继续简化
     */
    public static final String LINE_MESSAGE_SPLIT = "\\n";

    /**
     * 获取原始异常信息
     *
     * @param t the t
     * @return string string
     */
    public static String getMsg(Throwable t) {
        return cutMessage(cause(t));
    }

    /**
     * 获取各级异常数据.
     *
     * @param t the t
     * @return the log
     */
    public static String getLog(Throwable t) {
        StringBuilder sb = new StringBuilder();
        StackTraceElement[] es = t.getStackTrace();
        for (StackTraceElement e : es){
            sb.append("\n");
            sb.append(e.toString());
        }
        return sb.toString();
    }


    /**
     * 切割出最精简的消息
     *
     * @param message
     * @return
     */
    private static String cutMessage(String message) {
        if (Bool.isNull(message)) {
            return message;
        }
        String[] split = message.split(EXCEPTION_MESSAGE_SPLIT);
        if (!Bool.isNull(split)) {
            String detailMessage = split[0];
            String[] messages = getMessages(split, detailMessage, 1);
            String str = messages.length > 1 ? messages[1] : messages[0];
            return str.split(LINE_MESSAGE_SPLIT)[0];
        }
        return message;
    }

    /**
     * 获取具有意义的异常信息
     *
     * @param split
     * @param detailMessage
     * @return
     */
    private static String[] getMessages(String[] split, String detailMessage, int i) {
        //用默认为最后的的消息分割出有意义的提示字符
        String[] messages = detailMessage.split("Exception:");
        //获取的消息1下标信息字符数少于4个是没有意义的,如果之前分割的数组里面还有其他消息,那么看看下一组有没有
        if (messages.length > 1 && messages[1].length() < EXCEPTION_MESSAGE_SPLIT.length() - 1 && split.length > i) {
            detailMessage = split[i];
            return getMessages(split, detailMessage, i++);
        }
        return messages;
    }

    /**
     * 获取原始异常类型
     *
     * @param e
     * @return
     */
    private static String cause(Throwable e) {
        Throwable cause = e.getCause();
        if (cause != null) {
            return e.getCause().getLocalizedMessage();
        }
        return e.getLocalizedMessage();
    }


    /**
     * 包装为业务异常.
     *
     * @param <T> the type parameter
     * @param msg the msg
     * @param e   the e
     * @return the t
     */
    public static <T> T cast(String msg, Throwable e) {
        throw new BusinessException(msg, e);
    }

    /**
     * 包装为业务异常.
     *
     * @param <T> the type parameter
     * @param e   the e
     * @return the t
     */
    public static <T> T cast(Throwable e) {
        throw new BusinessException(e);
    }

    /**
     * Cast t.
     *
     * @param <T> the type parameter
     * @param msg the msg
     * @return the t
     */
    public static <T> T cast(String msg) {
        throw new BusinessException(msg);
    }

    /**
     * 抛出1个业务异常终结流程.
     *
     * @param <T> the type parameter
     * @return the t
     */
    public static <T> T cast() {
        throw new BusinessException("终结流程成功..");
    }

    /**
     * 包装为业务异常.
     *
     * @param <T> the type parameter
     * @param e   the e
     * @return the t
     */
    public static <T> T process(Throwable e) {
        throw new BusinessException(e);
    }

    /**
     * Process.
     *
     * @param <T> the type parameter
     * @param msg the msg
     * @return the t
     */
    public static <T> T process(String msg) {
        throw new ProcessException(msg);
    }

    /**
     * 抛出1个流程异常终结流程.
     *
     * @param <T> the type parameter
     * @return the t
     */
    public static <T> T process() {
        throw new ProcessException("终结流程成功..");
    }
}
