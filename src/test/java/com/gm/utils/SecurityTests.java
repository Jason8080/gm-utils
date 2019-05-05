package com.gm.utils;

import com.gm.security.base.Sha1;

public class SecurityTests {
    public static void main(String[] args) {
        String encrypt = Sha1.encrypt("666我的a3.");
        System.out.println(encrypt);
    }
}
