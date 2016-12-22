package com.gh.utils.tools.control;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author: gh
 * @description: 线程池工具类
 * @date: 2016/10/14 11:31.
 */
public class PoolUtils {

    private static ScheduledExecutorService pool;

    /**
     * 创建线程池并开启
     *
     * @param corePoolSize 线程池大小
     * @param delayed      延迟执行时间,第一次执行
     * @param interval     间隔时间
     * @param unit         时间单位
     * @param doSomething  需要执行的操作
     */
    public static void newScheduled(int corePoolSize, int delayed, int interval, Enum unit, final DoSomething doSomething) {
        pool = Executors.newScheduledThreadPool(3);
        pool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
//                navigateTo();
                doSomething.dosSth();
            }
        }, 1, 3, TimeUnit.SECONDS);
    }

    /**
     * 停止线程
     */
    public static void shutdown() {
        if (pool != null)
            pool.shutdown();
    }

    public interface DoSomething {
        void dosSth();
    }
}
