package com.zd112.framework.net.helper;

import android.text.TextUtils;

import com.zd112.framework.net.annotation.BusinessType;
import com.zd112.framework.net.annotation.Encoding;
import com.zd112.framework.net.annotation.RequestType;
import com.zd112.framework.net.bean.DownloadFileInfo;
import com.zd112.framework.net.bean.UploadFileInfo;
import com.zd112.framework.net.callback.BaseCallback;
import com.zd112.framework.net.callback.ProgressCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class OkHttpHelper {
    //** 请求参数定义**/
    private NetInfo netInfo;

    //** 系统辅助参数定义**/
    private HttpHelper httpHelper;
    private DownUpLoadHelper downUpLoadHelper;
    private DownloadFileInfo downloadFileInfo;
    private List<UploadFileInfo> uploadFileInfoList = new ArrayList<>();
    private OkHttpClient.Builder clientBuilder;
    private @RequestType int requestType;
    private BaseCallback callback;
    private ProgressCallback progressCallback;
    private Request request;
    private OkHttpClient httpClient;
    private @BusinessType int businessType;//业务类型
    private @Encoding String responseEncoding;//服务器响应编码
    private @Encoding String requestEncoding;//请求参数编码


    private OkHttpHelper(Builder builder) {
        netInfo = builder.httpInfo;
        downloadFileInfo = builder.downloadFileInfo;
        uploadFileInfoList = builder.uploadFileInfoList;
        clientBuilder = builder.clientBuilder;
        requestType = builder.requestType;
        callback = builder.callback;
        progressCallback = builder.progressCallback;
        businessType = builder.businessType;
        responseEncoding = builder.helperInfo.getResponseEncoding();
        requestEncoding = builder.helperInfo.getRequestEncoding();
        builder.helperInfo.setHttpInfo(netInfo);
        httpHelper = new HttpHelper(builder.helperInfo);
        if(null != downloadFileInfo || !uploadFileInfoList.isEmpty())
            downUpLoadHelper = new DownUpLoadHelper(builder.helperInfo);
    }

    public NetInfo doRequestSync(){
        return httpHelper.doRequestSync(this);
    }

    public void doRequestAsync(){
        httpHelper.doRequestAsync(this);
    }

    public void downloadFile(){
        downUpLoadHelper.downloadFile(this);
    }

    public void uploadFile(){
        downUpLoadHelper.uploadFile(this);
    }


    public static Builder Builder(){
        return new Builder();
    }

    public static final class Builder {
        private NetInfo httpInfo;
        private HelperInfo helperInfo;
        private DownloadFileInfo downloadFileInfo;
        private List<UploadFileInfo> uploadFileInfoList = new ArrayList<>();
        private OkHttpClient.Builder clientBuilder;
        private @RequestType int requestType;
        private BaseCallback callback;
        private ProgressCallback progressCallback;
        private @BusinessType int businessType;

        public Builder() {
        }

        public OkHttpHelper build(){
            if(!this.uploadFileInfoList.isEmpty()){
                this.businessType = BusinessType.UploadFile;//文件上传
                UploadFileInfo uploadFileInfo = uploadFileInfoList.get(0);
                if(!TextUtils.isEmpty(uploadFileInfo.getUrl())){
                    this.httpInfo.setUrl(uploadFileInfo.getUrl());
                }
            } else if(downloadFileInfo != null){
                this.businessType = BusinessType.DownloadFile;//文件下载
                if(!TextUtils.isEmpty(downloadFileInfo.getUrl())){
                    this.httpInfo.setUrl(downloadFileInfo.getUrl());
                }
            } else{
                this.businessType = BusinessType.HttpOrHttps;//http/https请求
            }
            return new OkHttpHelper(this);
        }

        public Builder httpInfo(NetInfo netInfo){
            this.httpInfo = netInfo;
            return this;
        }

        public Builder helperInfo(HelperInfo helperInfo){
            this.helperInfo = helperInfo;
            return this;
        }

        public Builder downloadFileInfo(DownloadFileInfo downloadFileInfo){
            this.downloadFileInfo = downloadFileInfo;
            return this;
        }

        public Builder uploadFileInfo(UploadFileInfo uploadFileInfo){
            uploadFileInfoList.add(uploadFileInfo);
            return this;
        }

        public Builder uploadFileInfoList(List<UploadFileInfo> uploadFiles){
            uploadFileInfoList.addAll(uploadFiles);
            return this;
        }


        public Builder clientBuilder(OkHttpClient.Builder clientBuilder){
            this.clientBuilder = clientBuilder;
            return this;
        }

        public Builder requestType(@RequestType int requestType){
            this.requestType = requestType;
            return this;
        }

        public Builder callback(BaseCallback callback){
            this.callback = callback;
            return this;
        }

        public Builder progressCallback(ProgressCallback progressCallback){
            this.progressCallback = progressCallback;
            return this;
        }
    }

    NetInfo getHttpInfo() {
        return netInfo;
    }

    HttpHelper getHttpHelper() {
        return httpHelper;
    }

    DownUpLoadHelper getDownUpLoadHelper() {
        return downUpLoadHelper;
    }

    int getBusinessType() {
        return businessType;
    }

    String getResponseEncoding() {
        return responseEncoding;
    }

    String getRequestEncoding() {
        return requestEncoding;
    }

    DownloadFileInfo getDownloadFileInfo() {
        return downloadFileInfo;
    }

    List<UploadFileInfo> getUploadFileInfoList() {
        return uploadFileInfoList;
    }

    OkHttpClient.Builder getClientBuilder() {
        return clientBuilder;
    }

    @RequestType int getRequestType() {
        return requestType;
    }

    BaseCallback getCallback() {
        return callback;
    }

    ProgressCallback getProgressCallback() {
        return progressCallback;
    }

    Request getRequest() {
        return request;
    }

    void setRequest(Request request) {
        this.request = request;
    }

    OkHttpClient getHttpClient() {
        return httpClient;
    }

    void setHttpClient(OkHttpClient httpClient) {
        this.httpClient = httpClient;
    }
}
