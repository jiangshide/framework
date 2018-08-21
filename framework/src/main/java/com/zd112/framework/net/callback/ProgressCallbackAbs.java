package com.zd112.framework.net.callback;

import com.zd112.framework.net.helper.NetInfo;

public abstract class ProgressCallbackAbs {
    public abstract void onResponseMain(String filePath, NetInfo info);

    public abstract void onResponseSync(String filePath, NetInfo info);

    public abstract void onProgressAsync(int percent, long bytesWritten, long contentLength, boolean done);

    public abstract void onProgressMain(int percent, long bytesWritten, long contentLength, boolean done);
}
