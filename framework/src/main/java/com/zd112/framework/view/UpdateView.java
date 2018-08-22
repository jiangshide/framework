package com.zd112.framework.view;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.zd112.framework.BaseApplication;
import com.zd112.framework.BuildConfig;
import com.zd112.framework.R;
import com.zd112.framework.data.UpdateData;
import com.zd112.framework.net.annotation.RequestType;
import com.zd112.framework.net.callback.Callback;
import com.zd112.framework.net.callback.ProgressCallback;
import com.zd112.framework.net.helper.NetInfo;
import com.zd112.framework.service.UpdateService;
import com.zd112.framework.utils.DialogUtils;
import com.zd112.framework.utils.LogUtils;
import com.zd112.framework.utils.SystemUtils;

import java.io.IOException;

public class UpdateView implements Callback, DialogView.DialogViewListener {

    private Context mContext;
    private int mStatus;
    private String mContent;
    private String mUrl;
    private String mVersion;
    private DialogUtils mDialogUtils;
    private ProgressCallback mProgressCallback;
    private CusButton mUpdateBtn;
    private OnUpdateListener mOnUpdateListener;

    public UpdateView(Context context) {
        this.mContext = context;
        mDialogUtils = new DialogUtils(mContext);
    }

    public UpdateView start(String url) {
        start(null, url);
        return this;
    }

    public UpdateView start(String content, String url) {
        start(0, content, url);
        return this;
    }

    public UpdateView start(int status, String content, String url) {
        start(status, content, url, null);
        return this;
    }

    public UpdateView start(int status, String content, String url, String version) {
        this.mStatus = status;
        this.mContent = content;
        this.mUrl = url;
        this.mVersion = version;
        if (mStatus == 0 || mStatus == 1) {
            show();
        } else {
            BaseApplication.application.startService(new Intent(BaseApplication.application, UpdateService.class).putExtra("url", mUrl));
            result();
        }
        return this;
    }

    public void show() {
        show(-1);
    }

    public void show(int layout) {
        if (mContext == null) {
            result();
            return;
        }
        mDialogUtils.cancelLoading();
        mDialogUtils.loading(layout != -1 ? layout : R.layout.default_update, this).setFull(true).setOutsideClose(false).setReturn(mStatus == 1).setListener(new DialogView.DialogOnClickListener() {
            @Override
            public void onDialogClick(boolean isCancel) {
                result();
            }
        });
    }

    public UpdateView init() {
        return init(null);
    }

    public UpdateView init(Callback callback) {
        BaseApplication.application.request(mContext, RequestType.GET, BuildConfig.UPDATE, null, callback != null ? callback : this, UpdateData.class, false);
        return this;
    }

    public UpdateView setListener(OnUpdateListener listener) {
        this.mOnUpdateListener = listener;
        return this;
    }

    public UpdateView setProgressListener(ProgressCallback progressListener) {
        this.mProgressCallback = progressListener;
        return this;
    }

    @Override
    public void onSuccess(NetInfo info) throws IOException {
        final UpdateData updateData = info.getResponseObj();
        if (null != updateData && null != updateData.res && !TextUtils.isEmpty(updateData.res.url)) {
            start(updateData.res.status, updateData.res.content, updateData.res.url, updateData.res.version);
        } else {
            result();
        }
    }

    private void download() {
        BaseApplication.application.download(mUrl, mProgressCallback != null ? mProgressCallback : new ProgressCallback() {
            @Override
            public void onProgressMain(int percent, long bytesWritten, long contentLength, boolean done) {
                super.onProgressMain(percent, bytesWritten, contentLength, done);
                if (null != mContext) {
                    mUpdateBtn.setText(resStr(R.string.updating) + percent + "%");
                    mUpdateBtn.setEnabled(false);
                    mUpdateBtn.setNormalColor(mContext.getResources().getColor(R.color.font_gray));
                } else {
                    result();
                }
            }

            @Override
            public void onResponseMain(String filePath, NetInfo info) {
                super.onResponseMain(filePath, info);
                String apkPath = info.getRetDetail();
                if (!TextUtils.isEmpty(apkPath) && apkPath.contains(".apk")) {
                    SystemUtils.installApkFile(BaseApplication.application, apkPath);
                } else {
                    LogUtils.e("err:", info);
                    result();
                }
            }
        });
    }

    private String resStr(int resId) {
        String resStr = "";
        if (mContext != null) {
            resStr = mContext.getString(resId);
        }
        return resStr;
    }

    @Override
    public void onFailure(NetInfo info) throws IOException {
        result();
    }

    @Override
    public void onView(View view) {
        ((TextView) view.findViewById(R.id.updateTitle)).setText(mContext.getString(R.string.update_title) + mVersion);
        ((TextView) view.findViewById(R.id.updateDes)).setText(mContent);
        mUpdateBtn = view.findViewById(R.id.updateBtn);
        mUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String netType = SystemUtils.getNetType();
                if (!netType.equals("WIFI")) {
                    new DialogUtils(mContext).loading(netType + resStr(R.string.update_down_tips)).setListener(new DialogView.DialogOnClickListener() {
                        @Override
                        public void onDialogClick(boolean isCancel) {
                            if (isCancel) {
                                mDialogUtils.cancelLoading();
                                result();
                            } else {
                                download();
                            }
                        }
                    });
                } else {
                    download();
                }
            }
        });
    }

    private void result() {
        if (mOnUpdateListener != null) {
            mOnUpdateListener.onResult();
        }
    }

    public interface OnUpdateListener {
        void onResult();
    }
}
