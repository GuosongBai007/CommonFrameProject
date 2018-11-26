package com.base.thread;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Author:GuosongBai
 * Date:2018/10/12 10:12
 */
public interface ISubmitable {

    public String getName();

    public void submit(Runnable task);

    public Future submit(Runnable task, long delay, TimeUnit unit);

    public Future submit(Runnable task, long delay, long period, TimeUnit unit);

    public ExecutorService getExecutorService();

    public Executor getExecutor();
}
