package com.zd112.framework.data;

import com.google.gson.annotations.SerializedName;
import com.zd112.framework.BuildConfig;

import java.io.Serializable;

public class BaseData implements Serializable {
    @SerializedName(BuildConfig.CODE)
    public int code;
    @SerializedName(BuildConfig.SUCCESS)
    public boolean success;
    @SerializedName(BuildConfig.TIME)
    public long date;
    @SerializedName(BuildConfig.MSG)
    public String msg;
}
