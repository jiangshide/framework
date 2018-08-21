package com.zd112.demo;

import android.os.Bundle;

import com.zd112.framework.BaseActivity;
import com.zd112.framework.net.helper.NetInfo;
import com.zd112.framework.utils.LogUtils;

public class MainActivity extends BaseActivity {

    @Override
    protected void initView(Bundle savedInstanceState) {
        setView(R.layout.activity_main, this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        request("update");
    }

    @Override
    public void onSuccess(NetInfo info) {
        super.onSuccess(info);
        LogUtils.e("---------info:",info.getRetDetail());
    }
}
