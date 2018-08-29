package com.zd112.demo.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.Button;

import com.zd112.demo.R;
import com.zd112.demo.home.fragment.CommentFragment;
import com.zd112.demo.home.fragment.ShareFragment;
import com.zd112.framework.BaseFragment;
import com.zd112.framework.annotation.Transformer;
import com.zd112.framework.apdater.CusFragmentPagerAdapter;
import com.zd112.framework.utils.LogUtils;
import com.zd112.framework.utils.ViewUtils;
import com.zd112.framework.view.CusViewPager;

/**
 * @author jiangshide
 * @Created by Ender on 2018/8/28.
 * @Emal:18311271399@163.com
 */
public class HomeFragment extends BaseFragment {

    @ViewUtils.ViewInject(R.id.testClick)
    private Button testClick;
    @ViewUtils.ViewInject(R.id.tabTitle)
    private TabLayout tabTitle;
    @ViewUtils.ViewInject(R.id.tabViewPager)
    private CusViewPager tabViewPager;

    private boolean isTrue;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setView(R.layout.home, this);
        LogUtils.e("-------home");
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        testClick.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                if (isTrue) {
                    tabViewPager.setMode(isTrue,Transformer.VERTICAL);
                    isTrue = false;
                } else {
                    tabViewPager.setMode(isTrue,Transformer.DEFAULT);
                    isTrue = true;
                }
            }
        });
        tabViewPager.setAdapter(new CusFragmentPagerAdapter(getChildFragmentManager(), new String[]{"第一栏", "第二栏"}, new CommentFragment(), new ShareFragment()));
        tabTitle.setupWithViewPager(tabViewPager);
    }
}
