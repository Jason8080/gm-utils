package com.gm.security.base;

import com.gm.help.base.Quick;
import com.gm.security.Security;

import java.security.MessageDigest;

/**
 * <p>Title: SHA1算法</p>
 *
 * @author levi
 */
public final class Sha1 implements Security {

    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};


    /**
     * 加密字符串并转成16进制.
     *
     * @param str the str
     * @return the string
     */
    public static String encrypt(String str) {
        byte[] bytes = encode(str);
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        // 把密文转换成十六进制的字符串形式
        for (int j = 0; j < len; j++) {
            buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
        }
        return buf.toString();
    }

    /**
     * 加密字符串
     *
     * @param str the str
     * @return the string
     */
    public static byte[] encode(String str) {
        if (str != null) {
            return Quick.exec(x -> {
                MessageDigest messageDigest = MessageDigest.getInstance(ENCRYPT_TYPE_SHA1);
                messageDigest.update(str.getBytes());
                return messageDigest.digest();
            });
        }
        return null;
    }
}