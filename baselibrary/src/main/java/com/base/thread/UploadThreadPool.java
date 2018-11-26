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
 * 消息图片上传线程池, 单线程线程池
 */
public class UploadThreadPool implements ISubmitable {
    public static final String THREAD_POOL_NAME = "UploadThreadPool";

    private final Object mLock = new Object();
    private ExecutorService mExecutor;

    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "UploadThreadPool(CachedThreadPool) Thread #" + mCount.getAndIncrement());
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

    /**
     * 线程缓存的线程池，60秒无请求自动关闭等待的缓存线程
     *
     * @return 线程池
     */
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
