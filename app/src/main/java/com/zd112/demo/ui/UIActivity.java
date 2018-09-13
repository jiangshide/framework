package com.zd112.demo.ui;

import android.os.Bundle;

import com.zd112.demo.R;
import com.zd112.demo.ui.fragment.CircleFragment;
import com.zd112.demo.ui.fragment.HomeFragment;
import com.zd112.demo.ui.fragment.MineFragment;
import com.zd112.demo.ui.fragment.PulishFragment;
import com.zd112.demo.ui.fragment.ShopFragment;
import com.zd112.framework.BaseActivity;

/**
 * @author jiangshide
 * @Created by Ender on 2018/9/10.
 * @Emal:18311271399@163.com
 */
public class UIActivity extends BaseActivity {

    @Override
    protected void initView(Bundle savedInstanceState) {
        setView(R.layout.ui, this);
        setView(mNavigationBar.initView(getSupportFragmentManager(), new HomeFragment(), new CircleFragment(), new PulishFragment(), new ShopFragment(), new MineFragment()).initData(new int[]{R.mipmap.tab_home, R.mipmap.tab_circle, R.mipmap.ic_launcher_round, R.mipmap.tab_shop, R.mipmap.tab_mine}, new int[]{R.mipmap.tab_home_selected, R.mipmap.tab_circle_selected, R.mipmap.ic_launcher_round, R.mipmap.tab_shop_selected, R.mipmap.tab_mine_selected}, getResArrStr(R.array.tab_main_title), R.color.font_gray, R.color.colorPrimary)
                .setBgColor(getResColor(R.color.app_bg)), this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        mNavigationBar.showView(mTabIndex);
    }
}
