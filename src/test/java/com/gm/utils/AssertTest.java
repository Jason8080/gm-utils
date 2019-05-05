package com.gm.utils;

import com.gm.utils.base.Assert;
import org.junit.Test;

/**
 * The type Assert test.
 *
 * @author Jason
 */
public class AssertTest {
    /**
     * Test.
     */
    @Test
    public void test() {
        Assert.isEqual(0L, 0, "不是相等的");
    }
}
