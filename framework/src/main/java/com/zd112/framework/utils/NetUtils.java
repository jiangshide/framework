package com.zd112.framework.utils;

import android.content.Context;
import android.text.TextUtils;

import com.zd112.framework.BaseApplication;
import com.zd112.framework.BuildConfig;
import com.zd112.framework.R;
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
import com.zd112.framework.view.DialogView;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author jiangshide
 * @Created by Ender on 2018/8/24.
 * @Emal:18311271399@163.com
 */
public enum NetUtils {

    INSTANCE;

    protected DialogUtils mDialogUtils;

    public static Net.Builder mNetBuilder;
    private NetInfo.Builder mBuilder;

    private Context mContext;
    private int mRequestType;
    private String mAction;
    public HashMap<String, String> mParams;
    private Callback callback;
    private Class _class;
    private boolean isLoading;

    static {
        String pkgName = BaseApplication.mApplication.getPackageName();
        mNetBuilder = Net.Builder().setConnectTimeout(BuildConfig.HTTP_CONNECT_TIME).setWriteTimeout(BuildConfig.HTTP_READ_TIME).setReadTimeout(BuildConfig.HTTP_WRITE_TIME).setMaxCacheSize(BuildConfig.HTTP_MAX_CACHE_SIZE)
                .setCacheType(BuildConfig.HTTP_CACHE_TYPE).setHttpLogTAG(pkgName).setIsGzip(BuildConfig.HTTP_IS_GZIP).setShowHttpLog(BuildConfig.DEBUG)
                .setShowLifecycleLog(true).setRetryOnConnectionFailure(false).setCachedDir(FileUtils.getCacheFile(BaseApplication.mApplication, pkgName + "_cache"))
                .setDownloadFileDir(BaseApplication.mApplication.getExternalCacheDir() + pkgName + "_download/").setRequestEncoding(Encoding.UTF_8).setResponseEncoding(Encoding.UTF_8)
//                .setHttpsCertificate("xxx.cer")//设置全局https自定义证书
                .addResultInterceptor(BaseApplication.mApplication).addExceptionInterceptor(BaseApplication.mApplication).setCookieJar(new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(BaseApplication.mApplication)));
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
        this.mContext = context;
        this.mRequestType = requestType;
        this.mAction = action;
        this.mParams = params;
        this.callback = callback;
        this._class = _class;
        this.isLoading = isLoading;
        return request(params, RequestStatus.NORMAL);
    }

    public NetInfo.Builder request(HashMap<String, String> params, int status) {
        if (isLoading)
            loading(mContext, R.layout.default_loading);
        if (params == null) {
            params = new HashMap<>();
        }
        params.put("platform", "1");
        params.put("version", SystemUtils.getAppVersion(mContext));
        params.put("appName", SystemUtils.getAppName(mContext));
        params.put("channel", BuildConfig.CHANNEL);
        mBuilder = NetInfo.Builder().setRequestType(mRequestType).setAction(mAction).addParams(mParams).addHeads(getHeader(ShareParamUtils.getString("token"))).setClass(_class).setStatus(status);
        return mBuilder;
    }

    public void request() {
        if (mBuilder == null) return;
        mNetBuilder.build().doAsync(mBuilder.build(), new Callback() {
            @Override
            public void onSuccess(NetInfo info) throws IOException {
                cancelLoading();
                response(info, callback);
            }

            @Override
            public void onFailure(NetInfo info) throws IOException {
                loading(mContext, info.getRetDetail()).setOnlySure();
                callback.onFailure(info);
            }
        });
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
        mNetBuilder.setReadTimeout(60).build(tag).doDownloadFileAsync(netInfo);
        return mNetBuilder;
    }

    private void response(NetInfo info, Callback callback) throws IOException {
        BaseData baseData = null;
        try {
            baseData = info.getRetDetail(BaseData.class);
            if (null == baseData) {
                loading(mContext, "no data!").setOnlySure();
                return;
            }
            if (baseData.code == 0) {
                callback.onSuccess(info);
            } else {
                boolean isGlobalError = false;
                for (int err : mContext.getResources().getIntArray(R.array.global_net_err)) {
                    LogUtils.e("code:", baseData.code, " | err:", err);
                    if (baseData.code == err) {
                        isGlobalError = true;
                        break;
                    }
                }
                LogUtils.e("context:", mContext, " | callback:", callback, " | isGlobalError:", isGlobalError);
                if (isGlobalError) {
                    BaseApplication.mApplication.newGlobalError(mContext, baseData);
                } else {
                    callback.onSuccess(info);
                }
            }
        } catch (Exception e) {
            LogUtils.e(e);
            loading(mContext, "data parse exception!").setOnlySure();
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
}
