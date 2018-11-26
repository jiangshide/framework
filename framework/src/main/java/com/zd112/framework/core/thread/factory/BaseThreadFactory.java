package com.zd112.framework.core.thread.factory;

import android.support.annotation.NonNull;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author jiangshide
 * @Created by Ender on 2018/11/23.
 * @Emal:18311271399@163.com
 */
public class BaseThreadFactory implements ThreadFactory {

    protected final AtomicInteger mAtomicInteger = new AtomicInteger();
    protected String mThreadNamePrefix = "BaseThread";

    @Override
    public Thread newThread(@NonNull Runnable r) {
        return new Thread(r,mThreadNamePrefix+" #"+mAtomicInteger.getAndIncrement());
    }
}
