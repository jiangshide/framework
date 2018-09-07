package com.zd112.demo.mine;

import android.os.Bundle;

import com.zd112.demo.R;
import com.zd112.demo.home.data.HomeData;
import com.zd112.demo.home.fragment.CommentFragment;
import com.zd112.demo.home.fragment.ShareFragment;
import com.zd112.framework.BaseFragment;
import com.zd112.framework.apdater.CusFragmentPagerAdapter;
import com.zd112.framework.net.helper.NetInfo;
import com.zd112.framework.utils.LogUtils;
import com.zd112.framework.utils.ViewUtils;
import com.zd112.framework.view.CusCoordinatorLayout;
import com.zd112.framework.view.CusViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jiangshide
 * @Created by Ender on 2018/8/28.
 * @Emal:18311271399@163.com
 */
public class MineFragment extends BaseFragment {

    @ViewUtils.ViewInject(R.id.mineCoordinatorLayout)
    private CusCoordinatorLayout mineCoordinatorLayout;
    @ViewUtils.ViewInject(R.id.mineViewPager)
    private CusViewPager mineViewPager;

    private final String[] mTitles = {"痘痘测试", "战痘密器", "战痘指南", "免费试用"};
    private int[] mColorArray;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setView(R.layout.mine, this);
        mColorArray = new int[]{
                android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light};
//        request("more/ind", HomeData.class, true);
    }

    @Override
    public void onSuccess(NetInfo info) {
        super.onSuccess(info);
        LogUtils.e("------info:",info.getRetDetail());
//        HomeData homeData = info.getResponseObj();
//        List<String> bannerUrls = new ArrayList<>();
//        for (HomeData.Res.PicList picList : homeData.res.picLists) {
//            bannerUrls.add(picList.picUrl);
//        }
//        mineCoordinatorLayout.setTranslucentStatusBar(getActivity())
//                .setTitle("Demo")
//                .setBackEnable(true)
////                .setContentScrimColorArray(mColorArray)
//                .setBanner(bannerUrls)
//                .setupWithViewPager(mineViewPager);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        mineViewPager.setAdapter(new CusFragmentPagerAdapter(getChildFragmentManager(), mTitles, new CommentFragment(), new ShareFragment(), new CommentFragment()));
    }

}
