package com.zd112.framework.net.bean;

import android.os.Message;

import java.io.Serializable;

public class NetMessage implements Serializable{
    public int what;

    public String requestTag;

    public Message build(){
        Message msg = new Message();
        msg.what = this.what;
        msg.obj = this;
        return msg;
    }
}
