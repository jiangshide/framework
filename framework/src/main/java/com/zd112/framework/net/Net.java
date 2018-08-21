package com.zd112.framework.net;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.zd112.framework.BaseApplication;
import com.zd112.framework.net.annotation.CacheType;
import com.zd112.framework.net.annotation.Encoding;
import com.zd112.framework.net.annotation.RequestType;
import com.zd112.framework.net.bean.DownloadFileInfo;
import com.zd112.framework.net.bean.UploadFileInfo;
import com.zd112.framework.net.callback.BaseCallback;
import com.zd112.framework.net.callback.ProgressCallback;
import com.zd112.framework.net.helper.HelperInfo;
import com.zd112.framework.net.helper.NetInfo;
import com.zd112.framework.net.helper.OkHttpHelper;
import com.zd112.framework.net.interfaces.NetInterface;
import com.zd112.framework.net.interfaces.interceptor.ExceptionInterceptor;
import com.zd112.framework.net.interfaces.interceptor.ResultInterceptor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

import static com.zd112.framework.net.annotation.CacheType.FORCE_NETWORK;

public class Net implements NetInterface {
    private final String TAG = getClass().getSimpleName();
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private static Builder builderGlobal;
    private static OkHttpClient httpClient;
    private static ScheduledExecutorService executorService;
    private Builder builder;
    private int cacheSurvivalTime = 0;//缓存存活时间（秒）
    private @CacheType
    int cacheType = FORCE_NETWORK;//缓存类型

    /**
     * 获取默认请求配置
     *
     * @return OkHttpUtil
     */
    public static NetInterface getDefault() {
        return new Builder(false).isDefault(true).build();
    }

    /**
     * 获取默认请求配置：该方法会绑定Activity、fragment生命周期
     *
     * @param requestTag 请求标识
     * @return OkHttpUtil
     */
    public static NetInterface getDefault(Object requestTag) {
        return new Builder(false).isDefault(true).build(requestTag);
    }

    /**
     * 同步请求
     *
     * @param info 请求信息体
     * @return HttpInfo
     */
    @Override
    public NetInfo doSync(NetInfo info) {
        return OkHttpHelper.Builder()
                .httpInfo(info)
                .requestType(info.getRequestType())
                .helperInfo(packageHelperInfo(info))
                .build()
                .doRequestSync();
    }

    /**
     * 异步请求
     *
     * @param info     请求信息体
     * @param callback 结果回调接口
     */
    @Override
    public void doAsync(final NetInfo info, final BaseCallback callback) {
        executorService.schedule(new Runnable() {
            @Override
            public void run() {
                OkHttpHelper.Builder()
                        .httpInfo(info)
                        .requestType(info.getRequestType())
                        .callback(callback)
                        .helperInfo(packageHelperInfo(info))
                        .build()
                        .doRequestAsync();
            }
        }, info.getDelayExecTime(), info.getDelayExecUnit());
    }

    /**
     * 同步Post请求
     *
     * @param info 请求信息体
     * @return HttpInfo
     */
    @Override
    public NetInfo doPostSync(NetInfo info) {
        return OkHttpHelper.Builder()
                .httpInfo(info)
                .requestType(RequestType.POST)
                .helperInfo(packageHelperInfo(info))
                .build()
                .doRequestSync();
    }

    /**
     * 同步Post请求
     *
     * @param info     请求信息体
     * @param callback 进度回调接口
     * @return HttpInfo
     */
    @Override
    public NetInfo doPostSync(NetInfo info, ProgressCallback callback) {
        return OkHttpHelper.Builder()
                .httpInfo(info)
                .requestType(RequestType.POST)
                .progressCallback(callback)
                .helperInfo(packageHelperInfo(info))
                .build()
                .doRequestSync();
    }

    /**
     * 异步Post请求
     *
     * @param info     请求信息体
     * @param callback 回调接口
     */
    @Override
    public void doPostAsync(final NetInfo info, final BaseCallback callback) {
        executorService.schedule(new Runnable() {
            @Override
            public void run() {
                OkHttpHelper.Builder()
                        .httpInfo(info)
                        .requestType(RequestType.POST)
                        .callback(callback)
                        .helperInfo(packageHelperInfo(info))
                        .build()
                        .doRequestAsync();
            }
        }, info.getDelayExecTime(), info.getDelayExecUnit());
    }

    /**
     * 异步Post请求
     *
     * @param info     请求信息体
     * @param callback 进度回调接口
     */
    @Override
    public void doPostAsync(final NetInfo info, final ProgressCallback callback) {
        executorService.schedule(new Runnable() {
            @Override
            public void run() {
                OkHttpHelper.Builder()
                        .httpInfo(info)
                        .requestType(RequestType.POST)
                        .progressCallback(callback)
                        .helperInfo(packageHelperInfo(info))
                        .build()
                        .doRequestAsync();
            }
        }, info.getDelayExecTime(), info.getDelayExecUnit());
    }

    /**
     * 同步Get请求
     *
     * @param info 请求信息体
     * @return HttpInfo
     */
    @Override
    public NetInfo doGetSync(NetInfo info) {
        return OkHttpHelper.Builder()
                .httpInfo(info)
                .requestType(RequestType.GET)
                .helperInfo(packageHelperInfo(info))
                .build()
                .doRequestSync();
    }

    /**
     * 异步Get请求
     *
     * @param info     请求信息体
     * @param callback 回调接口
     */
    @Override
    public void doGetAsync(final NetInfo info, final BaseCallback callback) {
        executorService.schedule(new Runnable() {
            @Override
            public void run() {
                OkHttpHelper.Builder()
                        .httpInfo(info)
                        .requestType(RequestType.GET)
                        .callback(callback)
                        .helperInfo(packageHelperInfo(info))
                        .build()
                        .doRequestAsync();
            }
        }, info.getDelayExecTime(), info.getDelayExecUnit());
    }

    /**
     * 异步上传文件
     *
     * @param info 请求信息体
     */
    @Override
    public void doUploadFileAsync(final NetInfo info) {
        List<UploadFileInfo> uploadFiles = info.getUploadFiles();
        for (final UploadFileInfo fileInfo : uploadFiles) {
            executorService.schedule(new Runnable() {
                @Override
                public void run() {
                    OkHttpHelper.Builder()
                            .httpInfo(info)
                            .uploadFileInfo(fileInfo)
                            .requestType(RequestType.POST)
                            .helperInfo(packageHelperInfo(info))
                            .build()
                            .uploadFile();
                }
            }, info.getDelayExecTime(), info.getDelayExecUnit());
        }
    }

    /**
     * 批量异步上传文件
     *
     * @param info 请求信息体
     */
    @Override
    public void doUploadFileAsync(final NetInfo info, final ProgressCallback callback) {
        final List<UploadFileInfo> uploadFiles = info.getUploadFiles();
        executorService.schedule(new Runnable() {
            @Override
            public void run() {
                OkHttpHelper.Builder()
                        .httpInfo(info)
                        .uploadFileInfoList(uploadFiles)
                        .requestType(RequestType.POST)
                        .progressCallback(callback)
                        .helperInfo(packageHelperInfo(info))
                        .build()
                        .uploadFile();
            }
        }, info.getDelayExecTime(), info.getDelayExecUnit());
    }

    /**
     * 同步上传文件
     *
     * @param info 请求信息体
     */
    @Override
    public void doUploadFileSync(final NetInfo info) {
        List<UploadFileInfo> uploadFiles = info.getUploadFiles();
        for (final UploadFileInfo fileInfo : uploadFiles) {
            OkHttpHelper.Builder()
                    .httpInfo(info)
                    .uploadFileInfo(fileInfo)
                    .requestType(RequestType.POST)
                    .helperInfo(packageHelperInfo(info))
                    .build()
                    .uploadFile();
        }
    }

    /**
     * 批量同步上传文件
     *
     * @param info 请求信息体
     */
    @Override
    public void doUploadFileSync(final NetInfo info, final ProgressCallback callback) {
        final List<UploadFileInfo> uploadFiles = info.getUploadFiles();
        OkHttpHelper.Builder()
                .httpInfo(info)
                .uploadFileInfoList(uploadFiles)
                .requestType(RequestType.POST)
                .progressCallback(callback)
                .helperInfo(packageHelperInfo(info))
                .build()
                .uploadFile();
    }

    /**
     * 异步下载文件
     *
     * @param info 请求信息体
     */
    @Override
    public void doDownloadFileAsync(final NetInfo info) {
        List<DownloadFileInfo> downloadFiles = info.getDownloadFiles();
        for (final DownloadFileInfo fileInfo : downloadFiles) {
            executorService.schedule(new Runnable() {
                @Override
                public void run() {
                    OkHttpHelper.Builder()
                            .httpInfo(info)
                            .downloadFileInfo(fileInfo)
                            .requestType(RequestType.GET)
                            .clientBuilder(newBuilderFromCopy())
                            .helperInfo(packageHelperInfo(info))
                            .build()
                            .downloadFile();
                }
            }, info.getDelayExecTime(), info.getDelayExecUnit());
        }
    }

    /**
     * 同步下载文件
     *
     * @param info 请求信息体
     */
    @Override
    public void doDownloadFileSync(final NetInfo info) {
        List<DownloadFileInfo> downloadFiles = info.getDownloadFiles();
        for (final DownloadFileInfo fileInfo : downloadFiles) {
            OkHttpHelper.Builder()
                    .httpInfo(info)
                    .downloadFileInfo(fileInfo)
                    .requestType(RequestType.GET)
                    .clientBuilder(newBuilderFromCopy())
                    .helperInfo(packageHelperInfo(info))
                    .build()
                    .downloadFile();
        }
    }

    /**
     * 同步Delete请求
     *
     * @param info 请求信息体
     * @return HttpInfo
     */
    @Override
    public NetInfo doDeleteSync(NetInfo info) {
        return OkHttpHelper.Builder()
                .httpInfo(info)
                .requestType(RequestType.DELETE)
                .helperInfo(packageHelperInfo(info))
                .build()
                .doRequestSync();
    }

    /**
     * 异步Delete请求
     *
     * @param info     请求信息体
     * @param callback 回调接口
     */
    @Override
    public void doDeleteAsync(final NetInfo info, final BaseCallback callback) {
        executorService.schedule(new Runnable() {
            @Override
            public void run() {
                OkHttpHelper.Builder()
                        .httpInfo(info)
                        .requestType(RequestType.DELETE)
                        .callback(callback)
                        .helperInfo(packageHelperInfo(info))
                        .build()
                        .doRequestAsync();
            }
        }, info.getDelayExecTime(), info.getDelayExecUnit());
    }

    /**
     * 同步Put请求
     *
     * @param info 请求信息体
     * @return HttpInfo
     */
    @Override
    public NetInfo doPutSync(NetInfo info) {
        return OkHttpHelper.Builder()
                .httpInfo(info)
                .requestType(RequestType.PUT)
                .helperInfo(packageHelperInfo(info))
                .build()
                .doRequestSync();
    }

    /**
     * 异步Put请求
     *
     * @param info     请求信息体
     * @param callback 回调接口
     */
    @Override
    public void doPutAsync(final NetInfo info, final BaseCallback callback) {
        executorService.schedule(new Runnable() {
            @Override
            public void run() {
                OkHttpHelper.Builder()
                        .httpInfo(info)
                        .requestType(RequestType.PUT)
                        .callback(callback)
                        .helperInfo(packageHelperInfo(info))
                        .build()
                        .doRequestAsync();
            }
        }, info.getDelayExecTime(), info.getDelayExecUnit());
    }

    /**
     * 取消请求
     *
     * @param requestTag 请求标识
     */
    @Override
    public void cancelRequest(Object requestTag) {
        BaseApplication.application.cancel(parseRequestTag(requestTag));
    }

    @Override
    public OkHttpClient getDefaultClient() {
        return httpClient;
    }

    public void setDefaultClient(OkHttpClient client) {
        httpClient = client;
    }

    @SuppressWarnings("all")
    public boolean isNetworkAvailable() {
        if (BaseApplication.application == null)
            return true;
        ConnectivityManager cm = (ConnectivityManager) BaseApplication.application
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = cm.getActiveNetworkInfo();
        return net != null && net.getState() == NetworkInfo.State.CONNECTED;
    }

    private Net(Builder builder) {
        //初始化参数
        this.builder = builder;
        this.cacheType = builder.cacheType;
        this.cacheSurvivalTime = builder.cacheSurvivalTime;
        if (null == BaseApplication.application)
            this.cacheType = FORCE_NETWORK;
        if (null == executorService)
            executorService = new ScheduledThreadPoolExecutor(20);
        BaseApplication.application.setShowLifecycleLog(builder.showLifecycleLog);
        if (builder.isGlobalConfig) {
            OkHttpHelper.Builder()
                    .helperInfo(packageHelperInfo(null))
                    .build();
        }
        if (httpClient == null) {
            OkHttpHelper.Builder()
                    .helperInfo(packageHelperInfo(null))
                    .build()
                    .doRequestAsync();
        }
    }

    /**
     * 封装业务类信息
     */
    private HelperInfo packageHelperInfo(NetInfo info) {
        HelperInfo helperInfo = new HelperInfo();
        helperInfo.setShowHttpLog(builder.showHttpLog);
        helperInfo.setRequestTag(builder.requestTag);
        int random = 1000 + (int) (Math.random() * 999);
        String timeStamp = System.currentTimeMillis() + "_" + random;
        helperInfo.setTimeStamp(timeStamp);
        helperInfo.setExceptionInterceptors(builder.exceptionInterceptors);
        helperInfo.setResultInterceptors(builder.resultInterceptors);
        helperInfo.setDownloadFileDir(builder.downloadFileDir);
        helperInfo.setClientBuilder(newBuilderFromCopy());
        helperInfo.setOkHttpUtil(this);
        helperInfo.setDefault(builder.isDefault);
        helperInfo.setLogTAG(builder.httpLogTAG == null ? TAG : builder.httpLogTAG);
        helperInfo.setResponseEncoding(builder.responseEncoding);
        helperInfo.setRequestEncoding(builder.requestEncoding);
        helperInfo.setCacheSurvivalTime(cacheSurvivalTime);
        helperInfo.setCacheType(cacheType);
        helperInfo.setGzip(builder.isGzip);
        String httpsCertificate = null;
        InputStream httpsCertificateStream = null;
        if (builder.httpsCertificate != null) {
            httpsCertificate = builder.httpsCertificate;
        }
        if (builder.httpsCertificateStream != null) {
            httpsCertificateStream = builder.httpsCertificateStream;
        }
        if (info != null) {
            if (info.getHttpsCertificate() != null) {
                httpsCertificate = info.getHttpsCertificate();
            }
            if (info.getHttpsCertificateStream() != null) {
                httpsCertificateStream = info.getHttpsCertificateStream();
            }
        }
        if (httpsCertificate != null) {
            InputStream inputStream = null;
            try {
                inputStream = BaseApplication.application.getAssets().open(httpsCertificate);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (inputStream == null) {
                Log.e(builder.httpLogTAG, "Https证书不存在：" + httpsCertificate);
            }
            helperInfo.setHttpsCertificateStream(inputStream);
        }
        if (httpsCertificateStream != null) {
            helperInfo.setHttpsCertificateStream(httpsCertificateStream);
        }
        return helperInfo;
    }

    private OkHttpClient.Builder newBuilderFromCopy() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .connectTimeout(builder.connectTimeout, TimeUnit.SECONDS)
                .readTimeout(builder.readTimeout, TimeUnit.SECONDS)
                .writeTimeout(builder.writeTimeout, TimeUnit.SECONDS)
                .retryOnConnectionFailure(builder.retryOnConnectionFailure);
        if (builder.cachedDir != null) {
            clientBuilder.cache(new Cache(builder.cachedDir, builder.maxCacheSize));
        }
        if (null != builder.networkInterceptors && !builder.networkInterceptors.isEmpty())
            clientBuilder.networkInterceptors().addAll(builder.networkInterceptors);
        if (null != builder.interceptors && !builder.interceptors.isEmpty())
            clientBuilder.interceptors().addAll(builder.interceptors);
        if (null != builder.cookieJar)
            clientBuilder.cookieJar(builder.cookieJar);
        return clientBuilder;
    }

    public static Builder Builder() {
        return new Builder(false);
    }

    private static Builder BuilderGlobal() {
        return new Builder(true).isDefault(true);
    }

    public static final class Builder {

        private int maxCacheSize;//缓存大小
        private File cachedDir;//缓存目录
        private int connectTimeout;//连接超时
        private int readTimeout;//读超时
        private int writeTimeout;//写超时
        private boolean retryOnConnectionFailure;//失败重新连接
        private List<Interceptor> networkInterceptors;//网络拦截器
        private List<Interceptor> interceptors;//应用拦截器
        private List<ResultInterceptor> resultInterceptors;//请求结果拦截器
        private List<ExceptionInterceptor> exceptionInterceptors;//请求链路异常拦截器
        private int cacheSurvivalTime;//缓存存活时间（秒）
        private @CacheType
        int cacheType;//缓存类型
        private boolean isGlobalConfig;//是否全局配置
        private boolean showHttpLog;//是否显示Http请求日志
        private String httpLogTAG;//显示Http请求日志标识
        private boolean showLifecycleLog;//是否显示ActivityLifecycle日志
        private String downloadFileDir;//下载文件保存目录
        private String requestTag;
        private CookieJar cookieJar;
        private boolean isDefault;//是否默认请求
        private @Encoding
        String responseEncoding;//服务器响应编码
        private @Encoding
        String requestEncoding;//请求参数应编码
        private boolean isGzip = false;//Gzip压缩
        private String httpsCertificate;//Https证书
        private InputStream httpsCertificateStream;//Https证书

        public Builder() {
        }

        public Builder(boolean isGlobal) {
            isGlobalConfig = isGlobal;
            //系统默认配置
            initDefaultConfig();
            if (!isGlobal && null != builderGlobal) {
                //全局自定义配置
                initGlobalConfig(builderGlobal);
            }
        }

        public NetInterface build() {
            return build(null);
        }

        public NetInterface build(Object requestTag) {
            if (isGlobalConfig && null == builderGlobal) {
                builderGlobal = this;
            }
            if (null != requestTag)
                setRequestTag(requestTag);
            return new Net(this);
        }

        /**
         * 系统默认配置
         */
        private void initDefaultConfig() {
            setMaxCacheSize(10 * 1024 * 1024);
            setConnectTimeout(30);
            setReadTimeout(30);
            setWriteTimeout(30);
            setRetryOnConnectionFailure(true);
            setCacheSurvivalTime(0);
            setCacheType(FORCE_NETWORK);
            setNetworkInterceptors(null);
            setInterceptors(null);
            setResultInterceptors(null);
            setExceptionInterceptors(null);
            setShowHttpLog(true);
            setShowLifecycleLog(false);
            setDownloadFileDir(Environment.getExternalStorageDirectory().getPath() + "/okHttp_download/");
            setCachedDir(new File(Environment.getExternalStorageDirectory().getPath() + "/okHttp_cache"));
            setIsGzip(false);
            setResponseEncoding(Encoding.UTF_8);
            setRequestEncoding(Encoding.UTF_8);
        }

        /**
         * 全局自定义配置
         *
         * @param builder builder
         */
        private void initGlobalConfig(Builder builder) {
            setMaxCacheSize(builder.maxCacheSize);
            setCachedDir(builder.cachedDir);
            setConnectTimeout(builder.connectTimeout);
            setReadTimeout(builder.readTimeout);
            setWriteTimeout(builder.writeTimeout);
            setRetryOnConnectionFailure(builder.retryOnConnectionFailure);
            setCacheSurvivalTime(builder.cacheSurvivalTime);
            setCacheType(builder.cacheType);
            setNetworkInterceptors(builder.networkInterceptors);
            setInterceptors(builder.interceptors);
            setResultInterceptors(builder.resultInterceptors);
            setExceptionInterceptors(builder.exceptionInterceptors);
            setShowHttpLog(builder.showHttpLog);
            setHttpLogTAG(builder.httpLogTAG);
            setShowLifecycleLog(builder.showLifecycleLog);
            if (!TextUtils.isEmpty(builder.downloadFileDir)) {
                setDownloadFileDir(builder.downloadFileDir);
            }
            setCookieJar(builder.cookieJar);
            setResponseEncoding(builder.responseEncoding);
            setRequestEncoding(builder.requestEncoding);
            setIsGzip(builder.isGzip);
            if (builder.httpsCertificate != null) {
                setHttpsCertificate(builder.httpsCertificate);
            }
            if (builder.httpsCertificateStream != null) {
                setHttpsCertificate(builder.httpsCertificateStream);
            }
        }

        private Builder isDefault(boolean isDefault) {
            this.isDefault = isDefault;
            return this;
        }

        //设置缓存大小
        public Builder setMaxCacheSize(int maxCacheSize) {
            this.maxCacheSize = maxCacheSize;
            return this;
        }

        //设置缓存目录
        public Builder setCachedDir(File cachedDir) {
            if (null != cachedDir)
                this.cachedDir = cachedDir;
            return this;
        }

        //设置连接超时（单位：秒）
        public Builder setConnectTimeout(int connectTimeout) {
            if (connectTimeout <= 0)
                throw new IllegalArgumentException("connectTimeout must be > 0");
            this.connectTimeout = connectTimeout;
            return this;
        }

        //设置读超时（单位：秒）
        public Builder setReadTimeout(int readTimeout) {
            if (readTimeout <= 0)
                throw new IllegalArgumentException("readTimeout must be > 0");
            this.readTimeout = readTimeout;
            return this;
        }

        //设置写超时（单位：秒）
        public Builder setWriteTimeout(int writeTimeout) {
            if (writeTimeout <= 0)
                throw new IllegalArgumentException("writeTimeout must be > 0");
            this.writeTimeout = writeTimeout;
            return this;
        }

        //设置失败重新连接
        public Builder setRetryOnConnectionFailure(boolean retryOnConnectionFailure) {
            this.retryOnConnectionFailure = retryOnConnectionFailure;
            return this;
        }

        //设置网络拦截器：每次Http请求时都会执行该拦截器
        public Builder setNetworkInterceptors(List<Interceptor> networkInterceptors) {
            if (null != networkInterceptors)
                this.networkInterceptors = networkInterceptors;
            return this;
        }

        //设置应用拦截器：每次Http、缓存请求时都会执行该拦截器
        public Builder setInterceptors(List<Interceptor> interceptors) {
            if (null != interceptors)
                this.interceptors = interceptors;
            return this;
        }

        //设置请求结果拦截器
        public Builder setResultInterceptors(List<ResultInterceptor> resultInterceptors) {
            if (null != resultInterceptors)
                this.resultInterceptors = resultInterceptors;
            return this;
        }

        public Builder addResultInterceptor(ResultInterceptor resultInterceptor) {
            if (null != resultInterceptor) {
                if (null == this.resultInterceptors)
                    this.resultInterceptors = new ArrayList<>();
                this.resultInterceptors.add(resultInterceptor);
            }
            return this;
        }

        //设置请求链路异常拦截器
        public Builder setExceptionInterceptors(List<ExceptionInterceptor> exceptionInterceptors) {
            if (null != exceptionInterceptors) {
                this.exceptionInterceptors = exceptionInterceptors;
            }
            return this;
        }

        public Builder addExceptionInterceptor(ExceptionInterceptor exceptionInterceptor) {
            if (null != exceptionInterceptor) {
                if (null == this.exceptionInterceptors)
                    this.exceptionInterceptors = new ArrayList<>();
                this.exceptionInterceptors.add(exceptionInterceptor);
            }
            return this;
        }

        //设置缓存存活时间（秒）
        public Builder setCacheSurvivalTime(int cacheSurvivalTime) {
            if (cacheSurvivalTime < 0)
                throw new IllegalArgumentException("cacheSurvivalTime must be >= 0");
            this.cacheSurvivalTime = cacheSurvivalTime;
            return this;
        }

        //设置缓存类型
        public Builder setCacheType(@CacheType int cacheType) {
            this.cacheType = cacheType;
            return this;
        }

        //设置显示Http请求日志
        public Builder setShowHttpLog(boolean showHttpLog) {
            this.showHttpLog = showHttpLog;
            return this;
        }

        //设置Http请求日志标识
        public Builder setHttpLogTAG(String logTAG) {
            this.httpLogTAG = logTAG;
            return this;
        }

        //设置显示ActivityLifecycle日志
        public Builder setShowLifecycleLog(boolean showLifecycleLog) {
            this.showLifecycleLog = showLifecycleLog;
            return this;
        }

        //设置请求标识（与Activity/Fragment生命周期绑定）
        public Builder setRequestTag(Object requestTag) {
            this.requestTag = parseRequestTag(requestTag);
            return this;
        }

        //设置下载文件目录
        public Builder setDownloadFileDir(String downloadFileDir) {
            this.downloadFileDir = downloadFileDir;
            return this;
        }

        //设置cookie持久化
        public Builder setCookieJar(CookieJar cookieJar) {
            if (null != cookieJar)
                this.cookieJar = cookieJar;
            return this;
        }

        //设置服务器响应编码（默认：UTF-8）
        public Builder setResponseEncoding(@Encoding String responseEncoding) {
            this.responseEncoding = responseEncoding;
            return this;
        }

        //设置请求参数编码（默认：UTF-8）
        public Builder setRequestEncoding(@Encoding String requestEncoding) {
            this.requestEncoding = requestEncoding;
            return this;
        }

        //Gzip压缩，需要服务端支持
        public Builder setIsGzip(boolean isGzip) {
            this.isGzip = isGzip;
            return this;
        }

        //设置Https证书：证书必须放在assets文件夹下
        public Builder setHttpsCertificate(String httpsCertificate) {
            this.httpsCertificate = httpsCertificate;
            return this;
        }

        //设置Https证书
        public Builder setHttpsCertificate(InputStream httpsCertificate) {
            this.httpsCertificateStream = httpsCertificate;
            return this;
        }

    }

    private static String parseRequestTag(Object object) {
        String requestTag = null;
        if (null != object) {
            if (object instanceof String
                    || object instanceof Float
                    || object instanceof Double
                    || object instanceof Integer) {
                requestTag = String.valueOf(object);
            } else {
                requestTag = String.valueOf(object.hashCode());
            }
//            requestTag = object.getClass().getName();
//            if(requestTag.contains("$")){
//                requestTag = requestTag.substring(0,requestTag.indexOf("$"));
//            }
        }
        return requestTag;
    }

    @Override
    public boolean deleteCache() {
        try {
            if (httpClient != null && httpClient.cache() != null)
                httpClient.cache().delete();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
