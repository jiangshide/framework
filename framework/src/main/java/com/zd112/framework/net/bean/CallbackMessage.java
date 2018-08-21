package com.zd112.framework.net.bean;

import com.zd112.framework.net.callback.BaseCallback;
import com.zd112.framework.net.helper.NetInfo;

import okhttp3.Call;

public class CallbackMessage extends NetMessage{
    public BaseCallback callback;
    public NetInfo info;
    public Call call;


    public CallbackMessage(int what, BaseCallback callback, NetInfo info,
                           String requestTag, Call call) {
        this.what = what;
        this.callback = callback;
        this.info = info;
        super.requestTag = requestTag;
        this.call = call;
    }
}
