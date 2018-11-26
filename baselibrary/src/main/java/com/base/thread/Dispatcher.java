package com.base.thread;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Author:GuosongBai
 * Date:2018/10/12 10:12
 * 线程池管理器，统一的管理入口
 */
public class Dispatcher {
    private static Object sLock = new Object();
    private static List<ISubmitable> sThreadPools;
    private static Map<Class, ISubmitable> sThreadPoolMap;

    // 默认加载以下5个线程池
    // 单线程池，任务会串行执行
    private static SingleThreadPool sSingleThreadPool;
    // 单线程池，任务会串行执行，数据库写操作专用
    private static DBSingleThreadPool sDbSingleThreadPool;
    // 调度线程池，可定时执行任务
    private static ScheduledThreadPool sScheduledThreadPool;
    // 通用线程池，固定最大线程为5，超出5个任务会在队列中等待
    private static CommonThreadPool sCommonThreadPool;
    // 处理Http请求的线程池，无最大线程限制，如果有可缓存的线程则直接使用，否则新建一个线程来使用
    private static HttpThreadPool sHttpThreadPool;
    // 处理比较即时的任务的线程池，无最大线程限制，如果有可缓存的线程则直接使用，否则新建一个线程来使用
    private static CachedThreadPool sCachedThreadPool;

    // 处理消息附件上传的线程池, 最多3条线程
    private static UploadThreadPool sUploadThreadPool;
    // 独立的UI线程
    private static Thread sUiThread;
    private static Handler sHandler;

    public static void init(Thread uiThread) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalThreadStateException("Not on ui thread");
        }

        sUiThread = uiThread;
        sHandler = new Handler();

        sThreadPools = new ArrayList<ISubmitable>();
        sThreadPoolMap = new HashMap<Class, ISubmitable>();

        sSingleThreadPool = new SingleThreadPool();
        sThreadPools.add(sSingleThreadPool);
        sThreadPoolMap.put(sSingleThreadPool.getClass(), sSingleThreadPool);

        sDbSingleThreadPool = new DBSingleThreadPool();
        sThreadPools.add(sDbSingleThreadPool);
        sThreadPoolMap.put(sDbSingleThreadPool.getClass(), sDbSingleThreadPool);

        sScheduledThreadPool = new ScheduledThreadPool();
        sThreadPools.add(sScheduledThreadPool);
        sThreadPoolMap.put(sScheduledThreadPool.getClass(), sScheduledThreadPool);

        sCommonThreadPool = new CommonThreadPool();
        sThreadPools.add(sCommonThreadPool);
        sThreadPoolMap.put(sCommonThreadPool.getClass(), sCommonThreadPool);

        sHttpThreadPool = new HttpThreadPool();
        sThreadPools.add(sHttpThreadPool);
        sThreadPoolMap.put(sHttpThreadPool.getClass(), sHttpThreadPool);

        sCachedThreadPool = new CachedThreadPool();
        sThreadPools.add(sCachedThreadPool);
        sThreadPoolMap.put(sCachedThreadPool.getClass(), sCachedThreadPool);

        sUploadThreadPool = new UploadThreadPool();
        sThreadPools.add(sUploadThreadPool);
        sThreadPoolMap.put(sUploadThreadPool.getClass(), sUploadThreadPool);
    }

    // 如果需要其他线程池则需要注入才能使用，以便统一管理
    public static void registerThreadPool(ISubmitable pool) {
        synchronized (sLock) {
            sThreadPools.add(pool);
            sThreadPoolMap.put(pool.getClass(), pool);
        }
    }

    public static Handler getMainHandler() {
        return sHandler;
    }

    public static boolean isOnUiThread() {
        return Thread.currentThread() == sUiThread;
    }

    public static void delayRunOnUiThread(Runnable task, long delayMillis) {
        sHandler.postDelayed(task, delayMillis);
    }

    public static ISubmitable getThreadPool(Class tClazz) {
        boolean isAssignable = ISubmitable.class.isAssignableFrom(tClazz);
        if (!isAssignable) {
            throw new IllegalArgumentException("only support class implements ISubmitable.");
        }

        try {
            return sThreadPoolMap.get(tClazz);
        } catch (Exception e) {
        }

        return null;
    }

    public static List<ISubmitable> getThreadPools() {
        return sThreadPools;
    }

    public static void runOnUiThread(Runnable task) {
        if (isOnUiThread()) {
            task.run();
        } else {
            sHandler.post(task);
        }
    }

    /**
     * 在缓存线程池中执行。
     * 缓存线程池无最大线程限制，如果有闲置的线程，则直接使用，否则使用新建的线程
     */
    public static void runOnNewThread(Runnable task) {
        if (sCachedThreadPool != null) {
            sCachedThreadPool.submit(task);
        }
    }

    /**
     * 在通用线程池中执行。
     * 通用线程池最大线程限制是5，如果并行执行的任务数超过了5，其他任务会在队列中等待
     */
    public static void runOnCommonThread(Runnable task) {
        if (sCommonThreadPool != null) {
            sCommonThreadPool.submit(task);
        }
    }

    public static void runOnHttpThread(Runnable task) {
        if (sHttpThreadPool != null) {
            sHttpThreadPool.submit(task);
        }
    }

    public static <T> void runOnHttpThread(Callable<T> task) {
        if (sHttpThreadPool != null) {
            sHttpThreadPool.submit(task);
        }
    }

    /**
     * 在单线程池中执行。
     * 单线程池会串行执行添加的任务
     */
    public static void runOnSingleThread(Runnable task) {
        if (sSingleThreadPool != null) {
            sSingleThreadPool.submit(task);
        }
    }

    public static void runOnDBSingleThread(Runnable task) {
        if (sDbSingleThreadPool != null) {
            sDbSingleThreadPool.submit(task);
        }
    }

    public static Future runOnScheduledThread(Runnable task, long delay, TimeUnit unit) {
        if (sScheduledThreadPool != null) {
            return sScheduledThreadPool.submit(task, delay, unit);
        }
        return null;
    }

    public static Future runOnScheduledThread(Runnable task, long delay, long period, TimeUnit unit) {
        if (sScheduledThreadPool != null) {
            return sScheduledThreadPool.submit(task, delay, period, unit);
        }
        return null;
    }

    public static void runOnUploadThread(Runnable task) {
        if (sUploadThreadPool != null) {
            sUploadThreadPool.submit(task);
        }
    }
}

