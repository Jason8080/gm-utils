package com.gm.utils;

import org.junit.Test;

/**
 * The type Thread test.
 *
 * @author Jason
 */
public class ThreadTest {
    /**
     * com.gm.utils.Main.
     */
    @Test
    public void main() {
        for (int i=0;i<100;i++){
            ThreadMain main = new ThreadMain();
            main.start();
        }
    }

    /**
     * The type Thread main.
     */
    public class ThreadMain extends Thread{
        @Override
        public void run() {
            System.out.println(getName());
        }
    }
}
