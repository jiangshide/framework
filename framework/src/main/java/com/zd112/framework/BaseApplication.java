package com.zd112.framework;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.SparseArray;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zd112.framework.data.BaseData;
import com.zd112.framework.net.helper.NetInfo;
import com.zd112.framework.net.interfaces.interceptor.ExceptionInterceptor;
import com.zd112.framework.net.interfaces.interceptor.ResultInterceptor;
import com.zd112.framework.net.status.NetworkStateListener;
import com.zd112.framework.net.status.NetworkStateReceiver;
import com.zd112.framework.utils.LogUtils;
import com.zd112.framework.utils.SystemUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Call;

public abstract class BaseApplication extends Application implements Application.ActivityLifecycleCallbacks, ResultInterceptor, ExceptionInterceptor {
    public static BaseApplication mApplication;

    private Thread.UncaughtExceptionHandler mUncaughtExceptionHandler;
    private boolean mInstalled = false;

    public IWXAPI mWxApi;

    private List<Activity> mActivityList = new ArrayList<>();

    public void pushActivity(Activity activity) {
        mActivityList.add(activity);
    }

    public void clearActivity() {
        for (Activity activity : mActivityList) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
        mActivityList.clear();
    }

    public void cleanJump(Class _class, int toSource) {
        List<Activity> activitys = SystemUtils.getActivities(this);
        if (activitys == null) return;
        for (Activity activity : activitys) {
            activity.finish();
        }

        Intent intent = new Intent();
        intent.setClass(this, _class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("source", toSource);
        startActivity(intent);
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        mApplication = this;
        install();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB_MR2) {
            registerActivityLifecycleCallbacks(this);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initNetworkStateListener();
        /**
         * 微信注册
         */
        mWxApi = WXAPIFactory.createWXAPI(this, BuildConfig.WECHAT_APPID);
    }

    /**
     * 网络状态监听器
     **/
    private NetworkStateListener networkStateListener;

    /**
     * 初始化网络状态监听器
     */
    private void initNetworkStateListener() {
        NetworkStateReceiver.registerNetworkStateReceiver(this);
        networkStateListener = new NetworkStateListener() {
            @Override
            public void onNetworkState(boolean isNetworkAvailable, com.zd112.framework.net.status.NetInfo netInfo) {
                LogUtils.e("isNetworkAvailable:", isNetworkAvailable, " | netInfo:", netInfo);
            }
        };
        //添加网络状态监听
        NetworkStateReceiver.addNetworkStateListener(networkStateListener);
    }

    @Override
    public NetInfo intercept(NetInfo info) {
        return info;
    }

    @Override
    public void onTerminate() {
        //移除网络状态监听
        if (null != networkStateListener) {
            NetworkStateReceiver.removeNetworkStateListener(networkStateListener);
            NetworkStateReceiver.unRegisterNetworkStateReceiver(this);
        }
        uninstall();
        super.onTerminate();
    }

    public synchronized void install() {
        if (mInstalled) {
            return;
        }
        mInstalled = true;
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Looper.loop();
                    } catch (Throwable throwable) {
                        if (throwable instanceof RunException) {
                            return;
                        }
                        LogUtils.e("exception~thread:", Looper.getMainLooper().getThread(), " | throwable:", throwable);
                    }
                }
            }
        });
        mUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                LogUtils.e("exception~thread:", Looper.getMainLooper().getThread(), " | throwable:", throwable);
            }
        });
    }

    public synchronized void uninstall() {
        if (!mInstalled) {
            return;
        }
        mInstalled = false;
        Thread.setDefaultUncaughtExceptionHandler(mUncaughtExceptionHandler);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                throw new RunException("exit the main...");
            }
        });
    }

    static final class RunException extends RuntimeException {
        public RunException(String msg) {
            super(msg);
        }
    }

    /**
     * 是否显示ActivityLifecycle日志
     */
    private boolean showLifecycleLog;

    /**
     * 请求集合: key=Activity value=Call集合
     */
    private Map<String, SparseArray<Call>> callsMap = new ConcurrentHashMap<>();

    /**
     * 保存请求集合
     *
     * @param tag  请求标识
     * @param call 请求
     */
    public void putCall(String tag, Call call) {
        if (!TextUtils.isEmpty(tag)) {
            SparseArray<Call> callList = callsMap.get(tag);
            if (null == callList) {
                callList = new SparseArray<>();
            }
            callList.put(call.hashCode(), call);
            callsMap.put(tag, callList);
            showLog(false, tag);
        }
    }

    /**
     * 取消请求
     *
     * @param activity 请求标识
     */
    private void cancelCallByActivityDestroy(Activity activity) {
        String tag = activity.getClass().getName();
        if (null == tag)
            return;
        destroy(tag, callsMap.get(tag));
        destroy(tag, callsMap.get(String.valueOf(activity.hashCode())));

    }

    private void destroy(String tag, SparseArray<Call> callList) {
        if (null != callList) {
            final int len = callList.size();
            for (int i = 0; i < len; i++) {
                Call call = callList.valueAt(i);
                if (null != call && !call.isCanceled())
                    call.cancel();
            }
            callList.clear();
            callsMap.remove(tag);
            showLog(true, tag);
        }
    }

    /**
     * 判断当前Activity是否已经销毁
     *
     * @param activity 请求标识
     * @return true 已经销毁  false 未销毁
     */
    public boolean isActivityDestroyed(Activity activity) {
        String tag = activity.getClass().getName();
        return callsMap.get(tag) == null;
    }

    /**
     * 判断当前tat是否已经销毁
     *
     * @param tag 请求标识
     * @return true 已经销毁  false 未销毁
     */
    public boolean isActivityDestroyed(String tag) {
        return !TextUtils.isEmpty(tag) && callsMap.get(tag) == null;
    }

    /**
     * 取消请求
     *
     * @param tag 请求标识
     */
    public void cancel(String tag) {
        cancel(tag, null);
    }

    /**
     * 取消请求
     *
     * @param tag          请求标识
     * @param originalCall call
     */
    public void cancel(String tag, Call originalCall) {
        if (TextUtils.isEmpty(tag)) {
            return;
        }
        if (null != originalCall) {
            SparseArray<Call> callList = callsMap.get(tag);
            if (null != callList) {
                Call c = callList.get(originalCall.hashCode());
                if (null != c && !c.isCanceled())
                    c.cancel();
                callList.delete(originalCall.hashCode());
                if (callList.size() == 0)
                    callsMap.remove(tag);
                showLog(true, tag);
            }
        } else {
            SparseArray<Call> callList = callsMap.get(tag);
            if (null != callList) {
                for (int i = 0; i < callList.size(); i++) {
                    Call call = callList.valueAt(i);
                    if (null != call && !call.isCanceled()) {
                        call.cancel();
                        callList.delete(call.hashCode());
                    }
                    if (callList.size() == 0)
                        callsMap.remove(tag);
                    showLog(true, tag);
                }
            }
        }
    }

    private void showLog(boolean isCancel, String tag) {
        if (!showLifecycleLog) {
            return;
        }
        LogUtils.d(isCancel ? "cancel request!" : "add request!" + ": " + tag);
    }

    public void setShowLifecycleLog(boolean showLifecycle) {
        showLifecycleLog = showLifecycle;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        cancelCallByActivityDestroy(activity);
    }

    public abstract void newGlobalError(Context context, BaseData baseData);
}
