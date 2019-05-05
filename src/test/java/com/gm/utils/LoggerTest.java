package com.gm.utils;

import com.gm.builder.LoggerBuilder;
import com.gm.utils.base.Logger;
import org.junit.Test;

import java.util.Date;

/**
 * The type Logger test.
 *
 * @author Jason
 */
public class LoggerTest {

    /**
     * com.gm.utils.Main.
     */
    @Test
    public void main() {

        LoggerBuilder.c.isStart();
        LoggerBuilder.c
                .isOperator("Jason")
                .isIp("127.0.0.1")
                .isPath("/blog/write/0")
                .isTime(666);
        LoggerBuilder.c.isEnd();
        Logger.info(LoggerBuilder.c.builder());
    }
}
