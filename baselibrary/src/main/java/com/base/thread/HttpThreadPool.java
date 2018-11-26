package com.base.thread;

import java.util.concurrent.Callable;
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
 * HTTP线程池，主要应用在没有包装线程池的Http请求IO，例如UrlConnection
 */
public class HttpThreadPool implements ISubmitable {
    public static final String THREAD_POOL_NAME = "HttpThreadPool";

    private Object mLock = new Object();
    private ExecutorService mExecutor;

    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "WebRequestThreadPool(CachedThreadPool) Thread #" + mCount.getAndIncrement());
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

    public <T> void submit(Callable<T> task){
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
                    mExecutor = Executors.newCachedThreadPool(sThreadFactory);
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
