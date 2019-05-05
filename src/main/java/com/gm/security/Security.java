package com.gm.security;

import sun.misc.BASE64Encoder;

/**
 * 安全工具
 *
 * @author Jason
 */
public interface Security {

    /**
     * The constant base64.
     */
    BASE64Encoder base64 = new BASE64Encoder();

    /**
     * The constant ENCRYPT_TYPE_MD5.
     */
    String ENCRYPT_TYPE_MD5 = "MD5";

    /**
     * The constant ENCRYPT_TYPE_SHA1.
     */
    String ENCRYPT_TYPE_SHA1 = "SHA1";

    /**
     * The constant CHARSET_UTF_8.
     */
    String CHARSET_UTF_8 = "UTF-8";

    /**
     * The constant SIGN_FIELD_TIMESTAMP.
     */
    String SIGN_FIELD_TIMESTAMP = "timestamp";

    /**
     * The constant SIGN_FIELD_USERNAME.
     */
    String SIGN_FIELD_USERNAME = "username";

    /**
     * The constant SIGN_FIELD_SYS_CODE.
     */
    String SIGN_FIELD_SYS_CODE = "sysCode";

    /**
     * The constant SIGN_FIELD_SIGN.
     */
    String SIGN_FIELD_SIGN = "sign";

    /**
     * The constant SIGN_FIELD_SECURITY_CODE.
     */
    String SIGN_FIELD_SECURITY_CODE = "securityCode";

}
