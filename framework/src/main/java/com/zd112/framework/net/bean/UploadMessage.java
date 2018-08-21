package com.zd112.framework.net.bean;

import com.zd112.framework.net.callback.ProgressCallback;
import com.zd112.framework.net.helper.NetInfo;

public class UploadMessage extends NetMessage{
    public String filePath;
    public NetInfo info;
    public ProgressCallback progressCallback;

    public UploadMessage(int what, String filePath, NetInfo info, ProgressCallback progressCallback, String requestTag) {
        this.what = what;
        this.filePath = filePath;
        this.info = info;
        this.progressCallback = progressCallback;
        super.requestTag = requestTag;
    }
}
