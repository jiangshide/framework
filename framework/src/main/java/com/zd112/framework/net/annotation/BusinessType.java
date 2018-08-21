package com.zd112.framework.net.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({BusinessType.HttpOrHttps,BusinessType.UploadFile,BusinessType.DownloadFile})
@Retention(RetentionPolicy.SOURCE)
public @interface BusinessType {
    /**
     * http/https请求
     */
    int HttpOrHttps = 1;

    /**
     * 文件上传
     */
    int UploadFile = 2;

    /**
     * 文件下载
     */
    int DownloadFile = 3;
}
