package com.zd112.framework.core.thread;

import com.zd112.framework.core.thread.annotation.ThreadPriority;
import com.zd112.framework.core.thread.annotation.ThreadType;
import com.zd112.framework.core.thread.executor.ExecutorFactory;
import com.zd112.framework.core.thread.executor.SerialExecutor;
import com.zd112.framework.core.thread.task.CoreRunnable;
import com.zd112.framework.core.thread.task.CoreThread;
import com.zd112.framework.core.thread.task.ThreadTask;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author jiangshide
 * @Created by Ender on 2018/11/23.
 * @Emal:18311271399@163.com
 */
public class ThreadManager {
    private static final Object mCacheLock = new Object();
    private static AtomicBoolean mInitialized = new AtomicBoolean(false);
    private SerialExecutor mSerialExecutor;
    private AtomicInteger mKeyIndex;
    private HashMap<Integer, Future> mFutureCache;

    private ThreadManager() {
        mInitialized.set(false);
        init();
    }

    public static ThreadManager getInstance() {
        return InstanceHolder.mInstance;
    }

    private static class InstanceHolder {
        private static final ThreadManager mInstance = new ThreadManager();
    }

    public void init() {
        mSerialExecutor = new SerialExecutor();
        mKeyIndex = new AtomicInteger(0);
        mFutureCache = new HashMap<>();
        mInitialized.set(true);
    }

    public void shutdown() {
        synchronized (mCacheLock) {
            mInitialized.set(false);
            if (null != mFutureCache) {
                mFutureCache.clear();
                mFutureCache = null;
            }
            if (null != mSerialExecutor) {
                mSerialExecutor.shutdown();
            }
            ExecutorFactory.shutdownAll();
        }
    }

    public void execute(CoreThread coreThread, ThreadType threadType) {
        if (!mInitialized.get()) throw new RuntimeException("Thread pool has been shutdown");
        if (null == coreThread) throw new NullPointerException("Thread should not be null");
        if (null == threadType) threadType = ThreadType.NORMAL_THREAD;
        if (threadType == ThreadType.REAL_TIME_THREAD)
            coreThread.setThreadPriority(ThreadPriority.REAL_TIME);
        if (threadType != ThreadType.SERIAL_THREAD) executeThread(coreThread, threadType);
        else executeSerialThread(coreThread);
    }

    public void execute(CoreRunnable coreRunnable, ThreadType threadType) {
        if (!mInitialized.get()) throw new RuntimeException("Thread pool has been shutdown");
        if (null != coreRunnable) throw new NullPointerException("CoreRunnable should not be null");
        if (null == threadType) threadType = ThreadType.NORMAL_THREAD;
        if (threadType == ThreadType.REAL_TIME_THREAD)
            coreRunnable.setThreadPriority(ThreadPriority.REAL_TIME);
        if (threadType != ThreadType.SERIAL_THREAD) executeRunnable(coreRunnable, threadType);
        else executeSerialRunnable(coreRunnable);
    }

    public void removeWork(int key) {
        if (!mInitialized.get() || key < 0) return;
        synchronized (mCacheLock) {
            mFutureCache.remove(key);
        }
    }

    public boolean cancelWork(int key) {
        if (!mInitialized.get() || key < 0) return false;
        boolean ret = false;
        synchronized (mCacheLock) {
            Future future = mFutureCache.get(key);
            if (null != future)
                ret = future.isCancelled() || future.isDone() || future.cancel(false);
        }
        if (ret) removeWork(key);
        return ret;
    }

    public int submitCancelable(ThreadTask<?> threadTask, ThreadType threadType) {
        if (!mInitialized.get()) throw new RuntimeException("Thread pool has been shutdown");
        if (null == threadTask) throw new NullPointerException("FutureTask should not be null!");
        if (null == threadType) threadType = ThreadType.NORMAL_THREAD;
        if (threadType == ThreadType.REAL_TIME_THREAD)
            threadTask.setThreadPriority(ThreadPriority.REAL_TIME);
        Future future = submit(threadTask, threadType);
        int key = -1;
        if (null != future) {
            synchronized (mCacheLock) {
                if (mKeyIndex.get() < Integer.MAX_VALUE - 10) key = mKeyIndex.getAndIncrement();
                else {
                    mKeyIndex.set(0);
                    key = 0;
                }
                mFutureCache.put(key, future);
                threadTask.setKey(key);
            }
        }
        return key;
    }

    private Future<?> submit(ThreadTask<?> threadTask, ThreadType threadType) {
        if (!mInitialized.get()) throw new RuntimeException("Thread pool has been shutdown");
        if (null == threadTask) throw new NullPointerException("FutureTask should not be null");
        if (null == threadType) threadType = ThreadType.NORMAL_THREAD;
        if (threadType == ThreadType.REAL_TIME_THREAD)
            threadTask.setThreadPriority(ThreadPriority.REAL_TIME);
        if (threadType == ThreadType.SERIAL_THREAD) {
            submitSerial(threadTask);
            return null;
        } else {
            return submitThread(threadTask, threadType);
        }
    }

    private Future<?> submitThread(FutureTask<?> futureTask, ThreadType threadType) {
        ExecutorService executorService = ExecutorFactory.getExecutor(threadType);
        return null != executorService ? executorService.submit(futureTask) : null;
    }

    private void submitSerial(ThreadTask<?> threadTask) {
        mSerialExecutor.execute(threadTask, threadTask.getThreadPriority());
    }

    private void executeThread(CoreThread coreThread, ThreadType threadType) {
        ExecutorService executorService = ExecutorFactory.getExecutor(threadType);
        if (null != executorService) executorService.execute(coreThread);
    }

    private void executeRunnable(CoreRunnable coreRunnable, ThreadType threadType) {
        ExecutorService executorService = ExecutorFactory.getExecutor(threadType);
        if (null != executorService) executorService.execute(coreRunnable);
    }

    private void executeSerialThread(CoreThread coreThread) {
        mSerialExecutor.execute(coreThread, coreThread.getThreadPriority());
    }

    private void executeSerialRunnable(CoreRunnable coreRunnable) {
        mSerialExecutor.execute(coreRunnable, coreRunnable.getThreadPriority());
    }

}
