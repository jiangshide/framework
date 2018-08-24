package com.zd112.framework.net.progress;

import android.os.Message;

import com.zd112.framework.net.bean.ProgressMessage;
import com.zd112.framework.net.callback.ProgressCallback;
import com.zd112.framework.net.handler.OkMainHandler;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

public class ProgressRequestBody extends RequestBody {
    private final RequestBody mOriginalRequestBody;
    private final ProgressCallback mProgressCallback;
    private BufferedSink mBufferedSink;
    private String mTimeStamp;
    private String mRequestTag;


    public ProgressRequestBody(RequestBody originalRequestBody, ProgressCallback progressCallback,
                               String timeStamp, String requestTag) {
        this.mProgressCallback = progressCallback;
        this.mOriginalRequestBody = originalRequestBody;
        this.mTimeStamp = timeStamp;
        this.mRequestTag = requestTag;
    }

    @Override
    public MediaType contentType() {
        return mOriginalRequestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return mOriginalRequestBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink originalSink) throws IOException {
        if (mBufferedSink == null) {
            mBufferedSink = Okio.buffer(sink(originalSink));
        }
        mOriginalRequestBody.writeTo(mBufferedSink);
        mBufferedSink.flush();
    }


    private Sink sink(Sink originalSink) {
        return new ForwardingSink(originalSink) {
            long bytesWritten = 0L;
            long contentLength = 0L;
            int lastPercent = 0;
            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                if (contentLength == 0) {
                    contentLength = contentLength();
                }
                bytesWritten += byteCount;
                if(null != mProgressCallback){
                    int percent = (int) ((100 * bytesWritten) / contentLength);
                    //每处理1%则立即回调
                    if(percent != lastPercent) {
                        lastPercent = percent;
                        mProgressCallback.onProgressAsync(percent, bytesWritten, contentLength, bytesWritten == contentLength);
                        //主线程回调
                        Message msg = new ProgressMessage(OkMainHandler.PROGRESS_CALLBACK,
                                mProgressCallback,
                                percent,
                                bytesWritten,
                                contentLength,
                                bytesWritten == contentLength,mRequestTag)
                                .build();
                        OkMainHandler.getInstance().sendMessage(msg);
                    }
                }
            }
        };
    }
}
