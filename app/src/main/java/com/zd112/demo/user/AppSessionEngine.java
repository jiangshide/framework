package com.zd112.demo.user;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.zd112.demo.user.data.UserData;
import com.zd112.framework.utils.LogUtils;
import com.zd112.framework.utils.ShareParamUtils;

/**
 * @author jiangshide
 * @Created by Ender on 2018/8/28.
 * @Emal:18311271399@163.com
 */
public class AppSessionEngine {


    public static int getUseId() {
        UserData userInfo = AppSessionEngine.getUser();
        if (userInfo == null || userInfo.res == null || userInfo.res.id == 0) {
            return 0;
        }
        return userInfo.res.id;
    }

    public static void setUser(UserData userInfo) {
        if (userInfo == null) return;
        try {
            String json = new Gson().toJson(userInfo);
            if (!TextUtils.isEmpty(json)) {
                ShareParamUtils.INSTANCE.putString("user", json);
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
    }

    public static UserData getUser() {
        String userInfo = ShareParamUtils.getString("user");
        if (userInfo == null) {
            return null;
        }
        try {
            return new Gson().fromJson(userInfo, UserData.class);
        } catch (Exception e) {
            return null;
        }
    }
}
