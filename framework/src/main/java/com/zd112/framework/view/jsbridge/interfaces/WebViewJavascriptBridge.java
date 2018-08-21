package com.zd112.framework.view.jsbridge.interfaces;


public interface WebViewJavascriptBridge {
    void send(String data);

    void send(String data, CallBackFunction responseCallback);
}
