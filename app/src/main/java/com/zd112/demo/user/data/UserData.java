package com.zd112.demo.user.data;

import com.google.gson.annotations.SerializedName;
import com.zd112.framework.data.BaseData;

import java.io.Serializable;

/**
 * @author jiangshide
 * @Created by Ender on 2018/8/28.
 * @Emal:18311271399@163.com
 */
public class UserData extends BaseData{
    @SerializedName("res")
    public Res res;

    public class Res implements Serializable{
        @SerializedName("id")
        public int id;
    }
}
