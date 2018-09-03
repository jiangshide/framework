package com.zd112.demo.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zd112.demo.R;
import com.zd112.demo.home.fragment.CommentFragment;
import com.zd112.demo.home.fragment.ShareFragment;
import com.zd112.framework.BaseFragment;
import com.zd112.framework.annotation.Transformer;
import com.zd112.framework.apdater.CusFragmentPagerAdapter;
import com.zd112.framework.utils.LogUtils;
import com.zd112.framework.utils.ViewUtils;
import com.zd112.framework.view.CusToast;
import com.zd112.framework.view.CusViewPager;

/**
 * @author jiangshide
 * @Created by Ender on 2018/8/28.
 * @Emal:18311271399@163.com
 */
public class HomeFragment extends BaseFragment {

    @ViewUtils.ViewInject(R.id.testClick)
    private Button testClick;
    @ViewUtils.ViewInject(R.id.navigationTitle)
    private TabLayout navigationTitle;
    @ViewUtils.ViewInject(R.id.tabViewPager)
    private CusViewPager tabViewPager;

    private boolean isTrue;

    private TextView tv_tab_title;

    private static final String[] CHANNELS = new String[]{"CUPCAKE", "DONUT", "Test"};

    @Override
    protected void initView(Bundle savedInstanceState) {
        setView(R.layout.home, this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        testClick.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                if (isTrue) {
                    tabViewPager.setMode(isTrue, Transformer.VERTICAL);
                    isTrue = false;
                } else {
                    tabViewPager.setMode(isTrue, Transformer.ACCORDION);
                    isTrue = true;
                }
//                new DialogUtils(getActivity()).toast("this is the test!",2).setToastIcon(R.mipmap.ic_launcher);
//                CusToast.showUpdateSuccessDialog();
                CusToast.fixTxt(getActivity(), "ssss");
            }
        });
        tabViewPager.setAdapter(new CusFragmentPagerAdapter(getChildFragmentManager(), CHANNELS, new CommentFragment(), new ShareFragment(), new CommentFragment()));
        LogUtils.e("-----------tabViewPager:", tabViewPager);
        navigationTitle.setupWithViewPager(tabViewPager);
        navigationTitle.getTabAt(0).setCustomView(R.layout.red_hot);
//        navigationTitle.getTabAt(0).getCustomView().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ToastUtils.show(getActivity(), "sss");
//                v.findViewById(R.id.iv_tab_red).setVisibility(View.GONE);
//            }
//        });
        navigationTitle.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                CusToast.fixTxt(getActivity(), "tab:" + tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
