package com.zd112.framework.net.interfaces;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * @author jiangshide
 * @Created by Ender on 2018/9/6.
 * @Emal:18311271399@163.com
 */
public class OnWebSocketListener extends WebSocketListener {

    private WebSocket mWebSocket;

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        this.mWebSocket = webSocket;
    }

    public void sendMsg(String msg) {
        if (null != mWebSocket) {
            mWebSocket.send(msg);
        }
    }

    public void sendMsg(ByteString byteString) {
        if (null != mWebSocket) {
            mWebSocket.send(byteString);
        }
    }


}
