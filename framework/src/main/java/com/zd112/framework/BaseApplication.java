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
import com.zd112.framework.net.Net;
import com.zd112.framework.net.annotation.Encoding;
import com.zd112.framework.net.annotation.RequestStatus;
import com.zd112.framework.net.callback.Callback;
import com.zd112.framework.net.callback.ProgressCallback;
import com.zd112.framework.net.cookie.PersistentCookieJar;
import com.zd112.framework.net.cookie.cache.SetCookieCache;
import com.zd112.framework.net.cookie.persistence.SharedPrefsCookiePersistor;
import com.zd112.framework.net.helper.NetInfo;
import com.zd112.framework.net.interfaces.interceptor.ExceptionInterceptor;
import com.zd112.framework.net.interfaces.interceptor.ResultInterceptor;
import com.zd112.framework.net.status.NetworkStateListener;
import com.zd112.framework.net.status.NetworkStateReceiver;
import com.zd112.framework.utils.DialogUtils;
import com.zd112.framework.utils.FileUtils;
import com.zd112.framework.utils.LogUtils;
import com.zd112.framework.utils.ShareParamUtils;
import com.zd112.framework.utils.SystemUtils;
import com.zd112.framework.view.DialogView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Call;

public abstract class BaseApplication extends Application implements Application.ActivityLifecycleCallbacks, ResultInterceptor, ExceptionInterceptor {
    public static BaseApplication application;

    private Thread.UncaughtExceptionHandler mUncaughtExceptionHandler;
    private boolean mInstalled = false;

    public static String word;
    protected DialogUtils mDialogUtils;
    public Net.Builder netBuilder;

    private Context context;
    public IWXAPI mWxApi;
    private int requestType;
    private String action;
    private HashMap<String, String> params;
    private Callback callback;
    private Class _class;
    private boolean isLoading;

    private List<Activity> activityList = new ArrayList<>();

    public void pushActivity(Activity activity) {
        activityList.add(activity);
    }

    public void clearActivity() {
        for (Activity activity : activityList) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
        activityList.clear();
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
        application = this;
        install();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB_MR2) {
            registerActivityLifecycleCallbacks(this);
        }
        String pkgName = getPackageName();
        netBuilder = Net.Builder().setConnectTimeout(BuildConfig.HTTP_CONNECT_TIME).setWriteTimeout(BuildConfig.HTTP_READ_TIME).setReadTimeout(BuildConfig.HTTP_WRITE_TIME).setMaxCacheSize(BuildConfig.HTTP_MAX_CACHE_SIZE)
                .setCacheType(BuildConfig.HTTP_CACHE_TYPE).setHttpLogTAG(pkgName).setIsGzip(BuildConfig.HTTP_IS_GZIP).setShowHttpLog(BuildConfig.DEBUG)
                .setShowLifecycleLog(true).setRetryOnConnectionFailure(false).setCachedDir(FileUtils.getCacheFile(this, pkgName + "_cache"))
                .setDownloadFileDir(getExternalCacheDir() + pkgName + "_download/").setRequestEncoding(Encoding.UTF_8).setResponseEncoding(Encoding.UTF_8)
//                .setHttpsCertificate("xxx.cer")//设置全局https自定义证书
                .addResultInterceptor(this).addExceptionInterceptor(this).setCookieJar(new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(this)));
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

    public HashMap<String, String> getHeader(String token) {
        HashMap<String, String> header = new HashMap<>();
        header.put("Accept", "*/*");
        if (!TextUtils.isEmpty(token)) {
            header.put("Authorization", token);
        }
        header.put("Content-Type", "application/x-www-form-urlencoded");
        return header;
    }

    public NetInfo.Builder request(final Context context, int requestType, String action, HashMap<String, String> params, final Callback callback, Class _class, boolean isLoading) {
        this.context = context;
        this.requestType = requestType;
        this.action = action;
        this.params = params;
        this.callback = callback;
        this._class = _class;
        this.isLoading = isLoading;
        return request(params, RequestStatus.NORMAL);
    }

    public NetInfo.Builder request(HashMap<String, String> params, int status) {
        if (isLoading)
            loading(context, R.layout.default_loading);
        if (params == null) {
            params = new HashMap<>();
        }
        params.put("platform", "1");
        params.put("version", SystemUtils.getAppVersion(this));
        params.put("appName", SystemUtils.getAppName(this));
        params.put("channel", BuildConfig.CHANNEL);
        NetInfo.Builder builder = NetInfo.Builder().setRequestType(requestType).setAction(action).addParams(params).addHeads(getHeader(ShareParamUtils.getString("token"))).setClass(_class).setStatus(status);
        netBuilder.build().doAsync(builder.build(), new Callback() {
            @Override
            public void onSuccess(NetInfo info) throws IOException {
                cancelLoading();
                response(context, info, callback);
            }

            @Override
            public void onFailure(NetInfo info) throws IOException {
                loading(context, info.getRetDetail()).setOnlySure();
                callback.onFailure(info);
            }
        });
        return builder;
    }

    public Net.Builder download(String url, ProgressCallback progressCallback) {
        return download(url, progressCallback, null);
    }

    public Net.Builder download(String url, ProgressCallback progressCallback, Object tag) {
        return download(url, "", progressCallback, tag);
    }

    public Net.Builder download(String url, String saveFileName, ProgressCallback progressCallback, Object tag) {
        if (!TextUtils.isEmpty(url) && TextUtils.isEmpty(saveFileName)) {
            saveFileName = url.substring(url.lastIndexOf("."), url.length());
        }
        LogUtils.e("file:", saveFileName);
        NetInfo netInfo = NetInfo.Builder().addDownloadFile(url, saveFileName, progressCallback).build();
        netBuilder.setReadTimeout(60).build(tag).doDownloadFileAsync(netInfo);
        return netBuilder;
    }

    private void response(Context context, NetInfo info, Callback callback) throws IOException {
        BaseData baseData = null;
        try {
            baseData = info.getRetDetail(BaseData.class);
            if (null == baseData) {
                loading(context, "no data!").setOnlySure();
                return;
            }
            if (baseData.code == 0) {
                callback.onSuccess(info);
            } else {
                boolean isGlobalError = false;
                for (int err : getResources().getIntArray(R.array.global_net_err)) {
                    LogUtils.e("code:", baseData.code, " | err:", err);
                    if (baseData.code == err) {
                        isGlobalError = true;
                        break;
                    }
                }
                LogUtils.e("context:", context, " | callback:", callback, " | isGlobalError:", isGlobalError);
                if (isGlobalError) {
                    newGlobalError(context, baseData);
                } else {
                    callback.onSuccess(info);
                }
            }
        } catch (Exception e) {
            LogUtils.e(e);
            loading(context, "data parse exception!").setOnlySure();
        }
    }

    public DialogView loading(Context context, String msg) {
        cancelLoading();
        return (mDialogUtils = new DialogUtils(context)).loading(msg);
    }

    public DialogView loading(Context context, int layout) {
        cancelLoading();
        return (mDialogUtils = new DialogUtils(context)).loading(layout);
    }

    public DialogView loading(Context context, int layout, DialogView.DialogViewListener dialogViewListener) {
        cancelLoading();
        return (mDialogUtils = new DialogUtils(context)).loading(layout, dialogViewListener);
    }

    public void cancelLoading() {
        if (mDialogUtils != null) {
            mDialogUtils.cancelLoading();
            mDialogUtils = null;
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
