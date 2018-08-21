package com.zd112.framework.apdater;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class CusFragmentPagerAdapter extends FragmentPagerAdapter {

    private FragmentManager fragmentManager;
    private Fragment[] fragmentList;
    private Bundle bundle;

    public CusFragmentPagerAdapter(FragmentManager fm, Fragment[] list) {
        super(fm);
        this.fragmentManager = fm;
        this.fragmentList = list;
    }

    public CusFragmentPagerAdapter(FragmentManager fm, Fragment[] list, Bundle bundle) {
        super(fm);
        this.fragmentManager = fm;
        this.fragmentList = list;
        this.bundle = bundle;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = fragmentList[position];
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return fragmentList.length;
    }
}
