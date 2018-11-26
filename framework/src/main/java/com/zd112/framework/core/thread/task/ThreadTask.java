package com.zd112.framework.core.thread.task;

import android.support.annotation.NonNull;

import com.zd112.framework.core.thread.annotation.ThreadPriority;
import com.zd112.framework.utils.LogUtils;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author jiangshide
 * @Created by Ender on 2018/11/23.
 * @Emal:18311271399@163.com
 */
public class ThreadTask<T> extends FutureTask<T> implements ThreadPriorityComparable {

    private ThreadPriority mThreadPriority;
    private int mKey = -1;

    public ThreadTask(Runnable runnable, T result, ThreadPriority threadPriority) {
        super(runnable, result);
        this.mThreadPriority = threadPriority;
    }

    public void setKey(int key) {
        if (key >= 0) this.mKey = key;
    }

    @Override
    public void setThreadPriority(ThreadPriority threadPriority) {
        this.mThreadPriority = threadPriority;
    }

    @Override
    public ThreadPriority getThreadPriority() {
        return mThreadPriority;
    }

    @Override
    public int compareTo(@NonNull ThreadPriorityComparable o) {
        if (null == o) return 1;
        return getThreadPriority().getPriorityValue() - o.getThreadPriority().getPriorityValue();
    }

    @Override
    public boolean equals(Object obj) {
        return null != obj && obj instanceof ThreadPriorityComparable && ((ThreadPriorityComparable) obj).getThreadPriority() == getThreadPriority() && super.equals(obj);
    }

    @Override
    protected void done() {
        try {
            get();
        } catch (InterruptedException | CancellationException e) {
            LogUtils.e("done:", e);
        } catch (ExecutionException e) {
            throw new RuntimeException("An error occurred while executing FutureTask", e.getCause());
        } finally {

        }
    }
}
