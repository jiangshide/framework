package com.zd112.framework.core;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author jiangshide
 * @Created by Ender on 2018/10/16.
 * @Emal:18311271399@163.com
 */
public class WeakHandler {
    private final Handler.Callback mCallback;
    private final ExecHandler mExecHandler;
    private Lock mLock = new ReentrantLock();
    final ChaineRef mChaineRef = new ChaineRef(mLock,null);

    public WeakHandler(){
        mCallback = null;
        mExecHandler = new ExecHandler();
    }

    public WeakHandler(@Nullable Handler.Callback callback){
        mCallback = callback;
        mExecHandler = new ExecHandler(new WeakReference<Handler.Callback>(callback));
    }

    public WeakHandler(@NonNull Looper looper){
        mCallback = null;
        mExecHandler = new ExecHandler(looper);
    }

    public WeakHandler(@NonNull Looper looper,@NonNull Handler.Callback callback){
        mCallback = callback;
        mExecHandler = new ExecHandler(looper,new WeakReference<Handler.Callback>(callback));
    }

    public final boolean post(@NonNull Runnable runnable){
        return mExecHandler.post(wrapRunnable(runnable));
    }

    public final boolean postAtTime(@NonNull Runnable runnable,long timeMillis){
        return mExecHandler.postAtTime(wrapRunnable(runnable),timeMillis);
    }

    public final boolean postAtTime(@NonNull Runnable runnable,Object token,long timeMillis){
        return mExecHandler.postAtTime(wrapRunnable(runnable),token,timeMillis);
    }

    public final boolean postDelayed(Runnable runnable,long delayMillis){
        return mExecHandler.postDelayed(wrapRunnable(runnable),delayMillis);
    }

    public final boolean postAtFrontOfQueue(Runnable runnable){
        return mExecHandler.postAtFrontOfQueue(wrapRunnable(runnable));
    }

    public final void removeCallbacks(Runnable runnable){
        final WeakRunnable weakRunnable = mChaineRef.remove(runnable);
        if(null != weakRunnable){
            mExecHandler.removeCallbacks(weakRunnable);
        }
    }

    public final void removeCallbacks(Runnable runnable, Object token){
        final WeakRunnable weakRunnable = mChaineRef.remove(runnable);
        if(null != weakRunnable){
            mExecHandler.removeCallbacks(weakRunnable,token);
        }
    }

    public final boolean sendMessage(Message message){
        return mExecHandler.sendMessage(message);
    }

    public final boolean sendEmptyMessage(int what){
        return mExecHandler.sendEmptyMessage(what);
    }

    public final boolean sendEmptyMessageDelayed(int what,long delayMillis){
        return mExecHandler.sendEmptyMessageDelayed(what,delayMillis);
    }

    public final boolean sendEmptyMessageAtTime(int what,long timeMillis){
        return mExecHandler.sendEmptyMessageAtTime(what,timeMillis);
    }

    public final boolean sendMessageDelayed(Message message,long delayMillis){
        return mExecHandler.sendMessageDelayed(message,delayMillis);
    }

    public final boolean sendMessageAtTime(Message message,long timeMillis){
        return mExecHandler.sendMessageAtTime(message,timeMillis);
    }

    public final boolean sendMessageAtFrontOfQueue(Message message){
        return mExecHandler.sendMessageAtFrontOfQueue(message);
    }

    public final void removeMessages(int what){
        mExecHandler.removeMessages(what);
    }

    public final void removeMessages(int what,Object obj){
        mExecHandler.removeMessages(what,obj);
    }

    public final void removeCallbacksAndMessages(Object token){
        mExecHandler.removeCallbacksAndMessages(token);
    }

    public final boolean hasMessages(int what){
        return mExecHandler.hasMessages(what);
    }

    public final boolean hasMessages(int what,Object obj){
        return mExecHandler.hasMessages(what,obj);
    }

    public final Looper getLooper(){
        return mExecHandler.getLooper();
    }

    private WeakRunnable wrapRunnable(@NonNull Runnable runnable){
        if(runnable == null)throw new NullPointerException("runable is null!");
        final ChaineRef chaineRef = new ChaineRef(mLock,runnable);
        mChaineRef.insertAfter(chaineRef);
        return chaineRef.wrapper;
    }

    private static class ExecHandler extends Handler{
        private final WeakReference<Callback> mCallback;

        ExecHandler(){
            mCallback = null;
        }

        ExecHandler(WeakReference<Callback> callbackWeakReference){
            this.mCallback = callbackWeakReference;
        }

        ExecHandler(Looper looper){
            super(looper);
            mCallback = null;
        }

        ExecHandler(Looper looper,WeakReference<Callback> callbackWeakReference){
            super(looper);
            mCallback = callbackWeakReference;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            if( null == mCallback)return;
            final Callback callback = mCallback.get();
            if(null == callback)return;
            callback.handleMessage(msg);
        }
    }

    static class WeakRunnable implements Runnable{

        private final WeakReference<Runnable> mDelegate;
        private final WeakReference<ChaineRef> mReference;

        WeakRunnable(WeakReference<Runnable> delegate,WeakReference<ChaineRef> reference){
            this.mDelegate = delegate;
            this.mReference = reference;
        }

        @Override
        public void run() {
            final Runnable delegate = mDelegate.get();
            final ChaineRef reference = mReference.get();
            if(null != reference){
                reference.remove();
            }
            if(null != delegate){
                delegate.run();
            }
        }
    }

    static class ChaineRef{
        @Nullable
        ChaineRef next;
        @Nullable
        ChaineRef prev;
        @NonNull
        final Runnable runnable;
        @NonNull
        final WeakRunnable wrapper;
        @NonNull
        Lock lock;

        public ChaineRef(@NonNull Lock lock,@NonNull Runnable runnable){
            this.runnable = runnable;
            this.lock  =lock;
            this.wrapper = new WeakRunnable(new WeakReference<Runnable>(runnable),new WeakReference<ChaineRef>(this));
        }

        public WeakRunnable remove(){
            lock.lock();
            try {
                if(prev != null){
                    prev.next = next;
                }
                if(next != null){
                    next.prev = prev;
                }
                prev = null;
                next = null;
            }finally {
                lock.unlock();
            }
            return wrapper;
        }

        public void insertAfter(@NonNull ChaineRef chaineRef){
            lock.lock();
            try {
                if(null != this.next){
                    this.next.prev = chaineRef;
                }
                chaineRef.next = this.next;
                this.next = chaineRef;
                chaineRef.prev = this;
            }finally {
                lock.unlock();
            }
        }

        @Nullable
        public WeakRunnable remove(Runnable runnable){
            lock.lock();
            try {
                ChaineRef chaineRef = this.next;
                while (null != chaineRef){
                    if(chaineRef.runnable == runnable)return chaineRef.remove();
                    chaineRef = chaineRef.next;
                }
            }finally {
                lock.unlock();
            }
            return null;
        }
    }
}
