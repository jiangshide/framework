package com.zd112.framework.core.thread.task;

import com.zd112.framework.core.thread.annotation.ThreadPriority;

/**
 * @author jiangshide
 * @Created by Ender on 2018/11/23.
 * @Emal:18311271399@163.com
 */
public interface ThreadPriorityComparable extends Comparable<ThreadPriorityComparable>{
    void setThreadPriority(ThreadPriority threadPriority);
    ThreadPriority getThreadPriority();
}
