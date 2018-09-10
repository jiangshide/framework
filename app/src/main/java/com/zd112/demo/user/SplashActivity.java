package com.zd112.demo.user;

import android.os.Bundle;

import com.zd112.demo.R;
import com.zd112.framework.BaseActivity;
import com.zd112.framework.utils.IntentUtils;
import com.zd112.framework.view.UpdateView;

public class SplashActivity extends BaseActivity implements UpdateView.OnUpdateListener {
    @Override
    protected void initView(Bundle savedInstanceState) {
        setView(R.layout.activity_splash, this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        new UpdateView(this).init().setListener(this);
    }

    @Override
    public void onResult(int code) {
        new IntentUtils().setClass(FirstActivity.class).start();
    }
}
