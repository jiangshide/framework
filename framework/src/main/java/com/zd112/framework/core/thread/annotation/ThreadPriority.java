package com.zd112.framework.core.thread.annotation;

/**
 * @author jiangshide
 * @Created by Ender on 2018/11/23.
 * @Emal:18311271399@163.com
 */
public enum  ThreadPriority {
    REAL_TIME(-1),
    HIGH(0),
    NORMAL(10),
    LOW(20),
    BACKGROUND(30);
    private int mPriority;
    ThreadPriority(int priority){
        mPriority = priority;
    }
    public int getPriorityValue(){
        return mPriority;
    }
}
