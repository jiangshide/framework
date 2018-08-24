package com.zd112.framework.net.progress;

import android.os.Message;

import com.zd112.framework.net.bean.DownloadFileInfo;
import com.zd112.framework.net.bean.ProgressMessage;
import com.zd112.framework.net.callback.ProgressCallback;
import com.zd112.framework.net.handler.OkMainHandler;
import com.zd112.framework.utils.LogUtils;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

public class ProgressResponseBody extends ResponseBody {
    private final ResponseBody mOriginalResponseBody;
    private BufferedSource mBufferedSink;
    private DownloadFileInfo mDownloadFileInfo;
    private String mTimeStamp;
    private String mRequestTag;

    public ProgressResponseBody(ResponseBody originalResponseBody, DownloadFileInfo downloadFileInfo,
                                String timeStamp, String requestTag) {
        this.mOriginalResponseBody = originalResponseBody;
        this.mDownloadFileInfo = downloadFileInfo;
        this.mTimeStamp = timeStamp;
        this.mRequestTag = requestTag;
    }

    @Override
    public long contentLength() {
        return mOriginalResponseBody.contentLength();
    }

    @Override
    public MediaType contentType() {
        return mOriginalResponseBody.contentType();
    }

    @Override
    public BufferedSource source() {
        if (mBufferedSink == null) {
            mBufferedSink = Okio.buffer(source(mOriginalResponseBody.source()));
        }
        return mBufferedSink;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;
            long contentLength = 0L;
            ProgressCallback progressCallback;
            int lastPercent = 0;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                if (totalBytesRead == 0) {
                    totalBytesRead = mDownloadFileInfo.getCompletedSize();
                    LogUtils.d(mRequestTag + "[" + mTimeStamp + "]", "从节点[" + totalBytesRead + "]开始下载"
                            + mDownloadFileInfo.getSaveFileNameWithExtension());
                }
                if (contentLength == 0) {
                    //文件总长度=当前需要下载长度+已完成长度
                    contentLength = contentLength() + totalBytesRead;
                }
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                if (null == progressCallback)
                    progressCallback = mDownloadFileInfo.getProgressCallback();
                if (null != progressCallback) {
                    int percent = (int) ((100 * totalBytesRead) / contentLength);
                    //每处理1%则立即回调
                    if (percent != lastPercent) {
                        lastPercent = percent;
                        progressCallback.onProgressAsync(percent, totalBytesRead, contentLength, totalBytesRead == -1);
                        //主线程回调
                        Message msg = new ProgressMessage(OkMainHandler.PROGRESS_CALLBACK,
                                progressCallback,
                                percent,
                                totalBytesRead,
                                contentLength,
                                bytesRead == -1, mRequestTag)
                                .build();
                        OkMainHandler.getInstance().sendMessage(msg);
                    }
                }
                return bytesRead;
            }
        };
    }
}
