package com.zd112.framework.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.zd112.framework.BaseApplication;
import com.zd112.framework.R;
import com.zd112.framework.net.callback.ProgressCallback;
import com.zd112.framework.net.helper.NetInfo;
import com.zd112.framework.utils.LogUtils;
import com.zd112.framework.utils.SystemUtils;

public class UpdateService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String url = intent.getStringExtra("url");
            final NotificationManager notificationManager = SystemUtils.notificationManager(this);
            final NotificationCompat.Builder build = SystemUtils.notificationBuild(this, R.drawable.update, SystemUtils.getAppName(this) + "下载更新", "正在下载");
            ((BaseApplication) getApplication()).download(url, "etd.apk", new ProgressCallback() {
                @Override
                public void onResponseMain(String filePath, NetInfo info) {
                    super.onResponseMain(filePath, info);
                    String apkPath = info.getRetDetail();
                    if (!TextUtils.isEmpty(apkPath) && apkPath.contains(".apk")) {
                        build.setProgress(0, 0, false).setContentText("已经下载完成,正在安装...");
                        notificationManager.notify(SystemUtils.notificationId, build.build());
                        notificationManager.cancel(SystemUtils.notificationId);
                        SystemUtils.installApkFile(getApplicationContext(), apkPath);
                    } else {
                        LogUtils.e("err:", info);
                    }
                }

                @Override
                public void onProgressMain(int percent, long bytesWritten, long contentLength, boolean done) {
                    super.onProgressMain(percent, bytesWritten, contentLength, done);
                    build.setProgress(100, percent, false);
                    notificationManager.notify(SystemUtils.notificationId, build.build());
                }
            }, this);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SystemUtils.notificationManager(this).cancel(SystemUtils.notificationId);
    }
}
