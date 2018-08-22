package com.zd112.framework.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UpdateData extends BaseData {
    @SerializedName("res")
    public Res res;

    public class Res implements Serializable {
        @SerializedName("Content")
        public String content;
        @SerializedName("Status")
        public int status;
        @SerializedName("Url")
        public String url;
        @SerializedName("Version")
        public String version;
    }
}
