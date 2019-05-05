package com.gm.utils;

import com.gm.utils.base.Bool;
import org.junit.Test;

import java.util.HashMap;

/**
 * The type Boolean utils test.
 *
 * @author Jason
 */
public class BooleanUtilsTest {

    @Test
    public void test() {
        int i = 1;
        System.out.println(Bool.isZero(i));
    }

    @Test
    public void main() {
        System.out.println(Bool.isNull(new HashMap()));
        System.out.println(Bool.isNull("",""));
        System.out.println(Bool.isNull("","123"));
        System.out.println(Bool.isNull("",666));
        System.out.println(Bool.isNull(null,null));
        System.out.println(Bool.isNull(null,666));
        System.out.println(Bool.isNull(123,null));
    }
}
