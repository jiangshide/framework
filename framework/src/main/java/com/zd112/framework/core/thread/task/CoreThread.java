package com.zd112.framework.core.thread.task;

import android.support.annotation.NonNull;

import com.zd112.framework.core.thread.annotation.ThreadPriority;

/**
 * @author jiangshide
 * @Created by Ender on 2018/11/23.
 * @Emal:18311271399@163.com
 */
public abstract class CoreThread extends Thread implements ThreadPriorityComparable {
    private ThreadPriority mThreadPriority = ThreadPriority.LOW;

    public CoreThread(ThreadPriority threadPriority) {
        this.mThreadPriority = threadPriority;
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
        return null == o ? 1 : getThreadPriority().getPriorityValue() - o.getThreadPriority().getPriorityValue();
    }

    @Override
    public boolean equals(Object obj) {
        return null != obj && obj instanceof ThreadPriorityComparable && ((ThreadPriorityComparable) obj).getThreadPriority() == getThreadPriority() && super.equals(obj);
    }
}
