package com.gm.utils.base;

import com.gm.builder.LoggerBuilder;
import com.gm.help.base.Quick;
import com.gm.utils.Utils;
import com.gm.utils.ext.Json;
import org.slf4j.LoggerFactory;

/**
 * 日志工具类.
 *
 * @author Jason
 */
public class Logger implements Utils {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(Utils.class);

    public static final String ALIGN = "--\t";

    private static final String ADORN =
            "\n" +
//                    "\t\t\t\t\t\t\t\t\t\t\t" + "[　Logger Format　]" + "\t\t\t\t\t\t\t\t\t\t\t\n" +
                    "----------------------------------------------------------------------------------------------------\n" +
                    ALIGN + "%s" + "\t\n" +
                    "----------------------------------------------------------------------------------------------------\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t" + " 　      ^\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t" + " 　      ^\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t" + " 　      ^\n";


    /**
     * 输出日志
     *
     * @param <T>  the type parameter
     * @param exec the exec
     * @param msg  the msg
     * @return the t
     */
    public static <T> T exec(Quick.Exec<T> exec, String msg) {
        T t = null;
        try {
            t = exec.exec(null);
            if (Bool.isNull(t)) {
                error(msg.concat("\n").concat(exec.toString()));
            }
        } catch (Throwable throwable) {
            error(ExceptionUtils.getMsg(throwable));
        }
        return t;
    }


    /**
     * 输出日志
     *
     * @param <T>     the type parameter
     * @param content the content
     * @param os      the os
     * @return the t
     */
    public static <T> T error(T content, Object... os) {
        if (content instanceof String) {
            logger.error(String.format(ADORN, content), os);
        } else if (content instanceof LoggerBuilder) {
            error(((LoggerBuilder) content).builder(), os);
        } else if (content instanceof Throwable) {
            logger.trace(String.format(ADORN, getMsg((Throwable) content), os));
        } else {
            logger.error(String.format(ADORN, Json.toFormat(content)), os);
        }
        return content;
    }

    /**
     * 输出日志
     *
     * @param <T>     the type parameter
     * @param content the content
     * @param os      the os
     * @return the t
     */
    public static <T> T warn(T content, Object... os) {
        if (content instanceof String) {
            logger.warn(String.format(ADORN, content), os);
        } else if (content instanceof LoggerBuilder) {
            warn(((LoggerBuilder) content).builder(), os);
        } else if (content instanceof Throwable) {
            logger.trace(String.format(ADORN, getMsg((Throwable) content), os));
        } else {
            logger.warn(String.format(ADORN, Json.toFormat(content)), os);
        }
        return content;
    }

    /**
     * 输出日志
     *
     * @param <T>     the type parameter
     * @param content the content
     * @param os      the os
     * @return the t
     */
    public static <T> T info(T content, Object... os) {
        if (content instanceof String) {
            logger.info(String.format(ADORN, content), os);
        } else if (content instanceof LoggerBuilder) {
            info(((LoggerBuilder) content).builder(), os);
        } else if (content instanceof Throwable) {
            logger.trace(String.format(ADORN, getMsg((Throwable) content), os));
        } else {
            logger.info(String.format(ADORN, Json.toFormat(content)), os);
        }
        return content;
    }

    /**
     * 输出日志
     *
     * @param <T>     the type parameter
     * @param content the content
     * @param os      the os
     * @return the t
     */
    public static <T> T debug(T content, Object... os) {
        if (content instanceof String) {
            logger.debug(String.format(ADORN, content), os);
        } else if (content instanceof LoggerBuilder) {
            debug(((LoggerBuilder) content).builder(), os);
        } else if (content instanceof Throwable) {
            logger.trace(String.format(ADORN, getMsg((Throwable) content), os));
        } else {
            logger.debug(String.format(ADORN, Json.toFormat(content)), os);
        }
        return content;
    }

    /**
     * 输出日志
     *
     * @param <T>     the type parameter
     * @param content the content
     * @param os      the os
     * @return the t
     */
    public static <T> T trace(T content, Object... os) {
        if (content instanceof String) {
            logger.trace(String.format(ADORN, content), os);
        } else if (content instanceof LoggerBuilder) {
            trace(((LoggerBuilder) content).builder(), os);
        } else if (content instanceof Throwable) {
            logger.trace(String.format(ADORN, getMsg((Throwable) content), os));
        } else {
            logger.trace(String.format(ADORN, Json.toFormat(content)), os);
        }
        return content;
    }

    private static String getMsg(Throwable t) {
        return ExceptionUtils.getMsg(t) + ExceptionUtils.getLog(t);
    }

}
