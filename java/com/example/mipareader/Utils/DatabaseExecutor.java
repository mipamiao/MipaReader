package com.example.mipareader.Utils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class DatabaseExecutor {
    private static final int CORE_POOL_SIZE = 2;
    private static final int MAX_POOL_SIZE = 4;
    private static final long KEEP_ALIVE_TIME = 30L;
    private static final int QUEUE_CAPACITY = 100;

    private final ThreadPoolExecutor diskIOExecutor;

    private static final DatabaseExecutor INSTANCE = new DatabaseExecutor();

    public static DatabaseExecutor getInstance() {
        return INSTANCE;
    }

    private DatabaseExecutor() {

        BlockingQueue<Runnable> workQueue =
                new LinkedBlockingQueue<>(QUEUE_CAPACITY);


        this.diskIOExecutor = new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                KEEP_ALIVE_TIME,
                TimeUnit.SECONDS,
                workQueue,
                new DatabaseThreadFactory(), // 自定义线程工厂
                new DatabaseRejectedExecutionHandler() // 自定义拒绝策略
        );


        this.diskIOExecutor.allowCoreThreadTimeOut(true);
    }

    public ThreadPoolExecutor getDiskIOExecutor() {
        return diskIOExecutor;
    }


    private static class DatabaseThreadFactory implements ThreadFactory {
        private final AtomicInteger threadNumber = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, "DB-Thread-" + threadNumber.getAndIncrement());
            thread.setPriority(Thread.MIN_PRIORITY); // 设置低优先级
            return thread;
        }
    }


    private static class DatabaseRejectedExecutionHandler implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            try {
                executor.getQueue().put(r);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Task submission interrupted", e);
            }
        }
    }
}