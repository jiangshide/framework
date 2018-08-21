package com.zd112.framework.net.handler;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.zd112.framework.BaseApplication;
import com.zd112.framework.net.bean.CallbackMessage;
import com.zd112.framework.net.bean.DownloadMessage;
import com.zd112.framework.net.bean.ProgressMessage;
import com.zd112.framework.net.bean.UploadMessage;
import com.zd112.framework.net.callback.BaseCallback;
import com.zd112.framework.net.helper.NetInfo;

import okhttp3.Call;
import okhttp3.Response;

public class OkMainHandler extends Handler {
    private static OkMainHandler singleton;

    public static OkMainHandler getInstance() {
        if (null == singleton) {
            synchronized (OkMainHandler.class) {
                if (null == singleton)
                    singleton = new OkMainHandler();
            }
        }
        return singleton;
    }

    private OkMainHandler() {
        super(Looper.getMainLooper());
    }

    /**
     * 网络请求回调标识
     */
    public static final int RESPONSE_CALLBACK = 0x01;

    /**
     * 进度回调标识
     */
    public static final int PROGRESS_CALLBACK = 0x02;

    /**
     * 上传结果回调标识
     */
    public static final int RESPONSE_UPLOAD_CALLBACK = 0x03;

    /**
     * 下载结果回调标识
     */
    public static final int RESPONSE_DOWNLOAD_CALLBACK = 0x04;


    @Override
    public void handleMessage(Message msg) {
        final int what = msg.what;
        String requestTag = "";
        try {
            switch (what) {
                case RESPONSE_CALLBACK://网络请求
                    CallbackMessage callMsg = (CallbackMessage) msg.obj;
                    if (null != callMsg.callback) {
                        //开始回调
                        requestTag = callMsg.requestTag;
                        if (!BaseApplication.application.isActivityDestroyed(callMsg.requestTag)) {
                            BaseCallback callback = callMsg.callback;
                            NetInfo info = callMsg.info;
                            if (info.isSuccessful()) {
                                ((com.zd112.framework.net.callback.Callback) callback).onSuccess(info);
                            } else {
                                ((com.zd112.framework.net.callback.Callback) callback).onFailure(info);
                            }
                            //当返回结果是Response时自动关闭ResponseBody
                            if (info.isNeedResponse()) {
                                Response res = info.getResponse();
                                if (null != res) {
                                    res.close();
                                }
                            }
                        }
                    }
                    Call call = callMsg.call;
                    if (call != null) {
                        if (!call.isCanceled()) {
                            call.cancel();
                        }
                        BaseApplication.application.cancel(requestTag, call);
                    }
                    break;
                case PROGRESS_CALLBACK://进度回调
                    ProgressMessage proMsg = (ProgressMessage) msg.obj;
                    if (null != proMsg.progressCallback) {
                        requestTag = proMsg.requestTag;
                        if (!BaseApplication.application.isActivityDestroyed(proMsg.requestTag)) {
                            proMsg.progressCallback.onProgressMain(proMsg.percent, proMsg.bytesWritten, proMsg.contentLength, proMsg.done);
                        }
                    }
                    break;
                case RESPONSE_UPLOAD_CALLBACK://上传结果回调
                    UploadMessage uploadMsg = (UploadMessage) msg.obj;
                    if (null != uploadMsg.progressCallback) {
                        requestTag = uploadMsg.requestTag;
                        if (!BaseApplication.application.isActivityDestroyed(requestTag)) {
                            uploadMsg.progressCallback.onResponseMain(uploadMsg.filePath, uploadMsg.info);
                            BaseApplication.application.cancel(requestTag);
                        }
                    }
                    break;
                case RESPONSE_DOWNLOAD_CALLBACK://下载结果回调
                    DownloadMessage downloadMsg = (DownloadMessage) msg.obj;
                    if (null != downloadMsg) {
                        requestTag = downloadMsg.requestTag;
                        if (!BaseApplication.application.isActivityDestroyed(requestTag)) {
                            downloadMsg.progressCallback.onResponseMain(downloadMsg.filePath, downloadMsg.info);
                            BaseApplication.application.cancel(requestTag);
                        }
                    }
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        } catch (Exception e) {
            BaseApplication.application.cancel(requestTag);
        }
    }
}
