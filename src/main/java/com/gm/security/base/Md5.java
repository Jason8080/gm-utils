package com.gm.security.base;

import com.gm.ex.BusinessException;
import com.gm.security.Security;
import com.gm.utils.base.Assert;
import com.gm.utils.base.ExceptionUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

/**
 * The type Md 5.
 *
 * @author Jason
 */
public class Md5 implements Security {


    /**
     * 加密.
     *
     * @param cs 加密内容和盐
     * @return 密文 string
     */
    public static String encrypt(Object... cs){
        try {
            MessageDigest md5 = MessageDigest.getInstance(ENCRYPT_TYPE_MD5);
            byte[] bytes = md5.digest(getBytes(cs));
            return salt(bytes);
        } catch (Exception e) {
            throw new BusinessException("加密失败", e);
        }
    }

    private static String base64(byte[] bytes) {
        try {
            return base64.encode(bytes);
        } catch (Exception e) {
            return ExceptionUtils.cast(e);
        }
    }

    private static String salt(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (byte b : bytes){
            // 与运算加盐
            int number = b & 0xff;
            String s = Integer.toHexString(number);
            if (s.length() == 1) {
                sb.append("0");
            }
            sb.append(s);
        }
        return sb.toString();
    }

    private static byte[] getBytes(Object... cs) throws UnsupportedEncodingException {
        Assert.Null(cs,String.format("加密内容是空"));
        String c = cs[0].toString();
        StringBuilder sb = new StringBuilder(c);
        for (int i=1;i<cs.length;i++){
            sb.append(cs[i]);
        }
        return sb.toString().getBytes(CHARSET_UTF_8);
    }
}
