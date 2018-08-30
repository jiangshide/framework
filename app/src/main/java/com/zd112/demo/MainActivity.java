package com.zd112.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.zd112.demo.circle.CircleFragment;
import com.zd112.demo.home.HomeFragment;
import com.zd112.demo.mine.MineFragment;
import com.zd112.demo.pulish.PulishFragment;
import com.zd112.demo.shop.ShopFragment;
import com.zd112.demo.user.AppSessionEngine;
import com.zd112.demo.user.LoginActivity;
import com.zd112.framework.BaseActivity;

public class MainActivity extends BaseActivity {

    private Fragment[] fragments = {new HomeFragment(), new CircleFragment(), new PulishFragment(), new ShopFragment(), new MineFragment()};
    private int id = 0;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setView(mNavigationBar.initView(id, new int[]{R.mipmap.tab_home, R.mipmap.tab_circle, R.mipmap.tab_publish, R.mipmap.tab_shop, R.mipmap.tab_mine}, new int[]{R.mipmap.tab_home_selected, R.mipmap.tab_circle_selected, R.mipmap.tab_publish_selected, R.mipmap.tab_shop_selected, R.mipmap.tab_mine_selected}, getResArrStr(R.array.tab_main_title), R.color.font_gray, R.color.colorPrimary, this)
                .setBgColor(getResColor(R.color.app_bg)), this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        push(fragments[id]);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        filterLogin(v.getId());
    }

    @Override
    public void onClick(View v, Bundle bundle) {
        super.onClick(v, bundle);
        filterLogin(v.getId());
    }

    private void filterLogin(int id) {
        if (AppSessionEngine.getUseId() > 0 && id == 2) {
            startActivity(new Intent(this, LoginActivity.class));
        }
        mNavigationBar.changeBarStatus(id);
        push(fragments[id]);
    }
}
