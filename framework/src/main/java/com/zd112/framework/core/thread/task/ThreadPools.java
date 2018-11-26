package com.zd112.framework.core.thread.task;

import com.zd112.framework.core.thread.ThreadManager;
import com.zd112.framework.core.thread.annotation.ThreadPriority;
import com.zd112.framework.core.thread.annotation.ThreadType;
import com.zd112.framework.core.thread.task.ThreadTask;

/**
 * @author jiangshide
 * @Created by Ender on 2018/11/23.
 * @Emal:18311271399@163.com
 */
public class ThreadPools {
    public static int executeWithThreadPool(Runnable runnable) {
        return executeWithThreadPool(runnable, ThreadType.NORMAL_THREAD, ThreadPriority.NORMAL);
    }

    public static int executeWithThreadPool(final Runnable runnable, ThreadType threadType, ThreadPriority threadPriority) {
        if (null == runnable) return -1;
        if (null == threadType) threadType = ThreadType.NORMAL_THREAD;
        if (null == threadPriority) threadPriority = ThreadPriority.NORMAL;
        ThreadTask<Void> threadTask = new ThreadTask<>(runnable, null, threadPriority);
        return ThreadManager.getInstance().submitCancelable(threadTask, threadType);
    }

    public static boolean cancelWork(int key) {
        return key >= 0 && ThreadManager.getInstance().cancelWork(key);
    }
}
