package com.zd112.framework.apdater;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class CusFragmentPagerAdapter extends FragmentPagerAdapter {

    private FragmentManager mFragmentManager;
    private Fragment[] mFragmentList;
    private Bundle mBundle;

    public CusFragmentPagerAdapter(FragmentManager fm, Fragment[] list) {
        super(fm);
        this.mFragmentManager = fm;
        this.mFragmentList = list;
    }

    public CusFragmentPagerAdapter(FragmentManager fm, Fragment[] list, Bundle bundle) {
        super(fm);
        this.mFragmentManager = fm;
        this.mFragmentList = list;
        this.mBundle = bundle;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = mFragmentList[position];
        fragment.setArguments(mBundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return mFragmentList.length;
    }
}
