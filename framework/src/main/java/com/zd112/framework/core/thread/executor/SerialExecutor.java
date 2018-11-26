package com.zd112.framework.core.thread.executor;

import com.zd112.framework.core.thread.annotation.ThreadPoolCont;
import com.zd112.framework.core.thread.annotation.ThreadPriority;
import com.zd112.framework.core.thread.annotation.ThreadType;
import com.zd112.framework.core.thread.task.ThreadTask;
import com.zd112.framework.utils.LogUtils;

import java.util.ArrayDeque;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

/**
 * @author jiangshide
 * @Created by Ender on 2018/11/23.
 * @Emal:18311271399@163.com
 */
public class SerialExecutor {
    private final ArrayDeque<ThreadTask<Void>> mArrayDeque;
    private ThreadTask<Void> mThreadTask;
    private Future<?> mFuture;
    private final Executor mExecutor;
    private WaitRunnable mWaitRunnable;
    private volatile boolean isRunning = true;

    public SerialExecutor() {
        mArrayDeque = new ArrayDeque<>();
        mExecutor = ExecutorFactory.getExecutor(ThreadType.NORMAL_THREAD);
        mWaitRunnable = new WaitRunnable();
    }

    public synchronized void execute(final Runnable runnable, ThreadPriority threadPriority) {
        mArrayDeque.offer(new ThreadTask<Void>(runnable, null, threadPriority));
        if (null == mThreadTask) scheduleNext();
    }

    protected synchronized void scheduleNext() {
        if (isRunning && null != (mThreadTask = mArrayDeque.poll())) {
            BaseExecutor baseExecutor = ExecutorFactory.getExecutor(ThreadType.NORMAL_THREAD);
            if (null == baseExecutor) {
                shutdown();
                return;
            }
            mFuture = baseExecutor.submit(mThreadTask);
            mExecutor.execute(mWaitRunnable);
        }
    }

    public synchronized void shutdown() {
        try {
            isRunning = false;
            mArrayDeque.clear();
            if (null != mThreadTask) mThreadTask.cancel(true);
        } catch (Throwable throwable) {
            LogUtils.e("SerialExecutor:", throwable);
        }
    }

    private class WaitRunnable implements Runnable {

        @Override
        public void run() {
            try {
                if (null != mFuture)
                    mFuture.get(ThreadPoolCont.SERIAL_EXECUTOR_TIMEOUT, ThreadPoolCont.SERIAL_EXECUTOR_TIMEOUT_UNIT);
            } catch (CancellationException | InterruptedException e) {
                LogUtils.e("SerialExecutor:", e);
            } catch (ExecutionException e) {
                throw new RuntimeException("An error occurred while executing SerialExecutor", e);
            } catch (TimeoutException e) {
                LogUtils.e("SerialExecutor", "#task timmeout force stop and scheduleNext:", e);
            } finally {
                try {
                    if (null != mFuture) {
                        mFuture.cancel(true);
                    }
                } catch (Exception e) {
                    LogUtils.e(e);
                }
                scheduleNext();
            }
        }
    }
}
