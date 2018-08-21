package com.zd112.framework.net.interfaces;

import com.zd112.framework.net.callback.BaseCallback;
import com.zd112.framework.net.callback.ProgressCallback;
import com.zd112.framework.net.helper.NetInfo;

import okhttp3.OkHttpClient;

public interface NetInterface {
    /**
     * 同步请求
     *
     * @param info 请求信息体
     * @return HttpInfo
     */
    NetInfo doSync(NetInfo info);

    /**
     * 异步请求
     *
     * @param info     请求信息体
     * @param callback 结果回调接口
     */
    void doAsync(NetInfo info, BaseCallback callback);

    /**
     * 同步Post请求
     *
     * @param info 请求信息体
     * @return HttpInfo
     */
    NetInfo doPostSync(NetInfo info);

    /**
     * 同步Post请求
     *
     * @param info     请求信息体
     * @param callback 进度回调接口
     * @return HttpInfo
     */
    NetInfo doPostSync(NetInfo info, ProgressCallback callback);

    /**
     * 异步Post请求
     *
     * @param info     请求信息体
     * @param callback 结果回调接口
     */
    void doPostAsync(NetInfo info, BaseCallback callback);

    /**
     * 异步Post请求
     *
     * @param info     请求信息体
     * @param callback 进度回调接口
     */
    void doPostAsync(NetInfo info, ProgressCallback callback);

    /**
     * 同步Get请求
     *
     * @param info 请求信息体
     */
    NetInfo doGetSync(NetInfo info);

    /**
     * 异步Get请求
     *
     * @param info     请求信息体
     * @param callback 结果回调接口
     */
    void doGetAsync(NetInfo info, BaseCallback callback);

    /**
     * 异步上传文件
     *
     * @param info 请求信息体
     */
    void doUploadFileAsync(final NetInfo info);

    /**
     * 批量异步上传文件
     *
     * @param info     请求信息体
     * @param callback 进度回调接口
     */
    void doUploadFileAsync(final NetInfo info, ProgressCallback callback);

    /**
     * 同步上传文件
     *
     * @param info 请求信息体
     */
    void doUploadFileSync(final NetInfo info);

    /**
     * 批量同步上传文件
     *
     * @param info     请求信息体
     * @param callback 进度回调接口
     */
    void doUploadFileSync(final NetInfo info, ProgressCallback callback);

    /**
     * 异步下载文件
     *
     * @param info 请求信息体
     */
    void doDownloadFileAsync(final NetInfo info);


    /**
     * 同步下载文件
     *
     * @param info 请求信息体
     */
    void doDownloadFileSync(final NetInfo info);

    /**
     * 同步Delete请求
     *
     * @param info 请求信息体
     * @return HttpInfo
     */
    NetInfo doDeleteSync(NetInfo info);

    /**
     * 异步Delete请求
     *
     * @param info     请求信息体
     * @param callback 结果回调接口
     */
    void doDeleteAsync(NetInfo info, BaseCallback callback);

    /**
     * 同步Put请求
     *
     * @param info 请求信息体
     * @return HttpInfo
     */
    NetInfo doPutSync(NetInfo info);

    /**
     * 异步PUT请求
     *
     * @param info     请求信息体
     * @param callback 结果回调接口
     */
    void doPutAsync(NetInfo info, BaseCallback callback);

    /**
     * 取消请求
     *
     * @param requestTag 请求标识
     */
    void cancelRequest(Object requestTag);


    /**
     * 获取默认的HttpClient
     */
    OkHttpClient getDefaultClient();

    /**
     * 清理缓存：只清理网络请求的缓存，不清理下载文件
     */
    boolean deleteCache();
}
