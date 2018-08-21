package com.zd112.framework.utils;

import android.content.Intent;

import com.zd112.framework.BaseApplication;

public class IntentUtils extends Intent {

    public IntentUtils setClass(Class _class) {
        this.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.setClass(BaseApplication.application, _class);
        return this;
    }

    public void start() {
        BaseApplication.application.startActivity(this);
    }
}
