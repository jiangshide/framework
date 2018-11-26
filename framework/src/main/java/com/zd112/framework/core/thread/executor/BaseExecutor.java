package com.zd112.framework.core.thread.executor;

import android.os.Looper;
import android.os.Process;

import com.zd112.framework.core.thread.annotation.ThreadPoolParams;
import com.zd112.framework.core.thread.annotation.ThreadPriority;
import com.zd112.framework.core.thread.factory.BaseThreadFactory;
import com.zd112.framework.core.thread.task.ThreadPriorityComparable;
import com.zd112.framework.core.thread.task.ThreadTask;
import com.zd112.framework.utils.LogUtils;

import java.lang.reflect.Field;
import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author jiangshide
 * @Created by Ender on 2018/11/23.
 * @Emal:18311271399@163.com
 */
public abstract class BaseExecutor extends ThreadPoolExecutor {

    public BaseExecutor(ThreadPoolParams threadPoolParams) {
        super(threadPoolParams.corePoolSize, threadPoolParams.maxPoolSize, threadPoolParams.keepAliveTimeSec, TimeUnit.SECONDS, new PriorityBlockingQueue<Runnable>(threadPoolParams.poolQueueSize), new BaseThreadFactory(), new DiscardOldestPolicy());
        setThreadFactory(getBaseThreadFactory());
        setRejectedExecutionHandler(getBaseRejectExecutionHandler());
    }

    abstract BaseThreadFactory getBaseThreadFactory();

    abstract RejectedExecutionHandler getBaseRejectExecutionHandler();

    @Override
    public Future<?> submit(Runnable task) {
        if (null == task) throw new NullPointerException();
        if (task instanceof ThreadTask) {
            execute(task);
            return (Future<?>) task;
        }
        ThreadTask<Void> threadTask = new ThreadTask<>(task, null, ThreadPriority.NORMAL);
        execute(task);
        return threadTask;
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        if (null == r) return;
        ThreadPriority threadPriority = null;
        if (r instanceof ThreadPriorityComparable) {
            threadPriority = ((ThreadPriorityComparable) r).getThreadPriority();
        }
        if (null != threadPriority)
            Process.setThreadPriority(Process.myTid(), threadPriority == ThreadPriority.REAL_TIME ? 0 : threadPriority == ThreadPriority.HIGH ? 1 : threadPriority == ThreadPriority.NORMAL ? 5 : threadPriority == ThreadPriority.LOW ? 10 : threadPriority == ThreadPriority.BACKGROUND ? 11 : 5);
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        try {
            Class _class = Looper.class;
            Field field = _class.getDeclaredField("sThreadLocal");
            if (null != field) {
                field.setAccessible(true);
                Object object = field.get(null);
                if (null != object && object instanceof ThreadLocal)
                    ((ThreadLocal) object).remove();
            }
        } catch (Exception e) {
            LogUtils.e("afterExecutor:", e);
        }
    }
}
