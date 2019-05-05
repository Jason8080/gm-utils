package com.gm.kit.thread;

import com.gm.kit.Kit;
import com.gm.utils.ext.Resource;

import java.util.concurrent.*;

/**
 * The type Thread pool kit.
 *
 * @author Jason
 * @deprecated 定长线程池
 */
public class Fixed implements Kit {

    private final static int THREAD_POOL = Resource.getInt("gm.thread.pool.size",50);

    private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(THREAD_POOL);

    /**
     * Get executor service.
     *
     * @return the executor service
     */
    public static ExecutorService get() {
        if (fixedThreadPool == null) {
            fixedThreadPool = Executors.newFixedThreadPool(THREAD_POOL);
        }
        return fixedThreadPool;
    }


}

