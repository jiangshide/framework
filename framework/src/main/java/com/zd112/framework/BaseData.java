package com.zd112.framework;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BaseData implements Serializable {
    @SerializedName("code")
    public int code;
    @SerializedName("date")
    public long date;
    @SerializedName("msg")
    public String msg;
}
