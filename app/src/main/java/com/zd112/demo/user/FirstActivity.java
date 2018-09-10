package com.zd112.demo.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.zd112.demo.R;
import com.zd112.demo.utils.Constant;
import com.zd112.framework.BaseActivity;
import com.zd112.framework.utils.LogUtils;
import com.zd112.framework.utils.ShareParamUtils;
import com.zd112.framework.utils.SystemUtils;
import com.zd112.framework.utils.ViewUtils.ViewInject;
import com.zd112.framework.view.CusButton;
import com.zd112.framework.view.CusViewPager;

public class FirstActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    @ViewInject(R.id.firstFirstView)
    private CusViewPager firstFirstView;
    @ViewInject(R.id.userFirstJump)
    private CusButton userFirstJump;
    @ViewInject(R.id.userFirstExperience)
    private CusButton userFirstExperience;
    @ViewInject(R.id.userFirstGoLogin)
    private CusButton userFirstGoLogin;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setView(R.layout.user_first, this);
    }

    @Override
    protected void setListener() {
        userFirstJump.setOnClickListener(this);
        userFirstExperience.setOnClickListener(this);
        userFirstGoLogin.setOnClickListener(this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        firstFirstView.setResImg(R.mipmap.guide_three, R.mipmap.guide_two, R.mipmap.guide_one, R.mipmap.guide_go);
        firstFirstView.addOnPageChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        ShareParamUtils.INSTANCE.putBoolean(Constant.GUIDE, true);
        LogUtils.e("-----------v:",v.getId());
        SystemUtils.sendSMS(this,"18311271399","你已收到短信验证码:56399,请忽泄漏!");
        if (v.getId() == R.id.userFirstJump || v.getId() == R.id.userFirstExperience) {
            startActivity(new Intent(this, SplashActivity.class));
        } else {
            startActivity(new Intent(this, RegActivity.class));
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        if (position == 3) {
            userFirstJump.setVisibility(View.GONE);
            userFirstExperience.setVisibility(View.VISIBLE);
            userFirstGoLogin.setVisibility(View.VISIBLE);
        } else {
            userFirstJump.setVisibility(View.VISIBLE);
            userFirstExperience.setVisibility(View.GONE);
            userFirstGoLogin.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
