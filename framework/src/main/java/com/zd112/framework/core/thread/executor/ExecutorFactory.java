package com.zd112.framework.core.thread.executor;

import com.zd112.framework.core.thread.annotation.ThreadPoolCont;
import com.zd112.framework.core.thread.annotation.ThreadPoolParams;
import com.zd112.framework.core.thread.annotation.ThreadType;
import com.zd112.framework.utils.LogUtils;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author jiangshide
 * @Created by Ender on 2018/11/23.
 * @Emal:18311271399@163.com
 */
public class ExecutorFactory {
    private static AtomicBoolean mAtomicBoolean = new AtomicBoolean(true);

    public static BaseExecutor getExecutor(ThreadType threadType) {
        if (!mAtomicBoolean.get()) return null;
        return threadType == ThreadType.REAL_TIME_THREAD ? IOExecutorInstanceHolder.getInstance() : IOExecutorInstanceHolder.getInstance();//todo:切换线程类型
    }

    private static class IOExecutorInstanceHolder {
        private static final IoExecutor mIoExecutor = new IoExecutor(getIOExecutorParams());

        public static IoExecutor getInstance() {
            return mIoExecutor;
        }

        private static ThreadPoolParams getIOExecutorParams() {
            ThreadPoolParams threadPoolParams = new ThreadPoolParams();
            threadPoolParams.corePoolSize = ThreadPoolCont.IO_CORE_POOL_SIZE;
            threadPoolParams.keepAliveTimeSec = ThreadPoolCont.IO_KEEP_ALIVE_TIME;
            threadPoolParams.maxPoolSize = ThreadPoolCont.IO_MAXIMUM_POOL_SIZE;
            threadPoolParams.poolQueueSize = ThreadPoolCont.IO_POOL_QUEUE_SIZE;
            return threadPoolParams;
        }
    }

    public static void shutdownAll() {
        try {
            mAtomicBoolean.set(false);
            IOExecutorInstanceHolder.getInstance().shutdown();
        } catch (Throwable throwable) {
            LogUtils.e("ExecutorFactory:", throwable);
        }
    }
}
