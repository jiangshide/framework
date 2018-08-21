package com.zd112.framework.net.bean;

import com.zd112.framework.net.callback.ProgressCallback;

public class ProgressMessage extends NetMessage{
    public ProgressCallback progressCallback;
    public int percent;
    public long bytesWritten;
    public long contentLength;
    public boolean done;

    public ProgressMessage(int what, ProgressCallback progressCallback, int percent,
                           long bytesWritten, long contentLength, boolean done,String requestTag) {
        this.what = what;
        this.percent = percent;
        this.bytesWritten = bytesWritten;
        this.contentLength = contentLength;
        this.done = done;
        this.progressCallback = progressCallback;
        super.requestTag = requestTag;
    }
}
