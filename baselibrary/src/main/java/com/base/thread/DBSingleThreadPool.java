package com.base.thread;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author:GuosongBai
 * Date:2018/10/12 10:12
 */

public class DBSingleThreadPool implements ISubmitable {
    public static final String THREAD_POOL_NAME = "DBSingleThreadPool";

    private Object mLock = new Object();
    private ExecutorService mExecutor;

    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "DBSingleThreadPool(1 SingleThread) Thread #" + mCount.getAndIncrement());
        }
    };

    @Override
    public String getName() {
        return THREAD_POOL_NAME;
    }

    @Override
    public void submit(Runnable task) {
        if (task != null) {
            getExecutorService().submit(task);
        }
    }

    @Override
    public Future submit(Runnable task, long delay, TimeUnit unit) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Future submit(Runnable task, long delay, long period, TimeUnit unit) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ExecutorService getExecutorService() {
        if (mExecutor == null) {
            synchronized (mLock) {
                if (mExecutor == null) {
                    mExecutor = Executors.newSingleThreadExecutor(sThreadFactory);
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
