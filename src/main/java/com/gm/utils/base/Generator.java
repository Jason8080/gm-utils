package com.gm.utils.base;

import com.gm.utils.Utils;

import java.util.Random;
import java.util.UUID;

/**
 * ID生成器工具类
 *
 * @author Jason
 */
public class Generator implements Utils {
    /**
     * The constant random.
     */
    public static Random random = new Random();

    /**
     * uuid生成器
     *
     * @return string string
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * Id生成器
     *
     * @return string string
     */
    @Deprecated
    public static String idString() {
        return "";
    }

    /**
     * number生成器
     *
     * @param len 生成长度
     * @return Id string
     */
    public static String random(int len) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < len; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }


    /**
     * int生成器
     *
     * @param min 最小值
     * @param max 最大值
     * @return 随机数 integer
     */
    public static Integer random(int min, int max) {
        return random.nextInt(max) % (max - min + 1) + min;
    }
}
