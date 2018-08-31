package com.zd112.demo;

import android.os.Bundle;

import com.zd112.demo.circle.CircleFragment;
import com.zd112.demo.home.HomeFragment;
import com.zd112.demo.mine.MineFragment;
import com.zd112.demo.pulish.PulishFragment;
import com.zd112.demo.shop.ShopFragment;
import com.zd112.framework.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void initView(Bundle savedInstanceState) {
        setView(mNavigationBar.initView(new HomeFragment(), new CircleFragment(), new PulishFragment(), new ShopFragment(), new MineFragment()).initData(mTabIndex, new int[]{R.mipmap.tab_home, R.mipmap.tab_circle, R.mipmap.ic_launcher_round, R.mipmap.tab_shop, R.mipmap.tab_mine}, new int[]{R.mipmap.tab_home_selected, R.mipmap.tab_circle_selected, R.mipmap.tab_publish_selected, R.mipmap.tab_shop_selected, R.mipmap.tab_mine_selected}, getResArrStr(R.array.tab_main_title), R.color.font_gray, R.color.colorPrimary,this)
                .setBgColor(getResColor(R.color.app_bg)), this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        changeTab(mTabIndex);
    }

}
