package com.zd112.framework.net.callback;

import com.zd112.framework.net.helper.NetInfo;

import java.io.IOException;

public interface Callback extends BaseCallback{
    /**
     * 请求成功：该回调方法已切换到UI线程
     */
    void onSuccess(NetInfo info) throws IOException;

    /**
     * 请求失败：该回调方法已切换到UI线程
     */
    void onFailure(NetInfo info) throws IOException;
}
