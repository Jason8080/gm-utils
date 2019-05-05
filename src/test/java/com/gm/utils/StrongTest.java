package com.gm.utils;

import com.gm.strong.Str;
import org.junit.Test;

import java.util.Map;

/**
 * The type Strong test.
 */
public class StrongTest {
    /**
     * Test str.
     */
    @Test
    public void testStr(){
        Map<String, Integer> map = new Str(new String[]{"我爱我家", "家中有喜"}).containCount("我", "爱", "唉", "家");
        System.out.println(map);
    }
}
