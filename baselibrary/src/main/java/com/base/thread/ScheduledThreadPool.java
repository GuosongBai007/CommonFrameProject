package com.base.thread;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * Author:GuosongBai
 * Date:2018/10/12 10:12
 * 计划任务线程池线程池，主要应用在没有上下文时使用的延迟任务
 */
public class ScheduledThreadPool implements ISubmitable {
    private static final int POOL_SIZE = 1;

    public static final String THREAD_POOL_NAME = "ScheduledThreadPool";

    private Object mLock = new Object();
    private ScheduledExecutorService mExecutor;

    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "ScheduledThreadPool(1 ScheduledThreadPool) Thread #" + mCount.getAndIncrement());
        }
    };

    @Override
    public String getName() {
        return THREAD_POOL_NAME;
    }

    @Override
    public void submit(Runnable task) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Future submit(Runnable task, long delay, TimeUnit unit) {
        if (task != null) {
            return getExecutorService().schedule(task, delay, unit);
        }
        return null;
    }

    @Override
    public Future submit(Runnable task, long delay, long period, TimeUnit unit) {
        if (task != null) {
            return getExecutorService().scheduleAtFixedRate(task, delay, period, unit);
        }
        return null;
    }

    @Override
    public ScheduledExecutorService getExecutorService() {
        if (mExecutor == null) {
            synchronized (mLock) {
                if (mExecutor == null) {
                    mExecutor = Executors.newScheduledThreadPool(POOL_SIZE, sThreadFactory);
                }
            }
        }

        return mExecutor;
    }

    @Override
    public Executor getExecutor() {
        return mExecutor;
    }
}
