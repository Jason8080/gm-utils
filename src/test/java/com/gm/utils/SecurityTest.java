package com.gm.utils;

import com.gm.security.base.Md5;
import com.gm.utils.ext.Sign;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class SecurityTest {
    @Test
    public void testMain() {
        Map<String,Object> map = new HashMap();
        map.put("a", 777);
        System.out.println(Sign.sign(map,"9"));
        System.out.printf(Md5.encrypt("sso1521280435650Jason[\"1\"]"));
    }
}
