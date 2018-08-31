package com.zd112.framework.apdater;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class CusFragmentPagerAdapter extends FragmentPagerAdapter {

    private String[] mTitle;
    private Fragment[] mFragmentList;
    private Bundle mBundle;

    public CusFragmentPagerAdapter(FragmentManager fm, Fragment... list) {
        super(fm);
        this.mFragmentList = list;
    }

    public CusFragmentPagerAdapter(FragmentManager fm, String[] titles, Fragment... list) {
        super(fm);
        this.mTitle = titles;
        this.mFragmentList = list;
    }

    public CusFragmentPagerAdapter(FragmentManager fm, Bundle bundle, Fragment... list) {
        super(fm);
        this.mFragmentList = list;
        this.mBundle = bundle;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return (null != mTitle && mTitle.length > 0 && position <= mTitle.length) ? mTitle[position] : null;
    }

    @Override
    public float getPageWidth(int position) {
        return super.getPageWidth(position);
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
