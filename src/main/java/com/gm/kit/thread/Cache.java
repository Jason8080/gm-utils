package com.gm.kit.thread;

import com.gm.kit.Kit;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The type Thread pool kit.
 *
 * @author Jason
 * @deprecated 定长线程池
 */
public class Cache implements Kit {

    private static ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

    /**
     * Get executor service.
     *
     * @return the executor service
     */
    public static ExecutorService get() {
        if (cachedThreadPool == null) {
            cachedThreadPool = Executors.newCachedThreadPool();
        }
        return cachedThreadPool;
    }


}

