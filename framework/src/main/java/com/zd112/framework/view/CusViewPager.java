package com.zd112.framework.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zd112.framework.apdater.CusFragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class CusViewPager extends ViewPager {

    public CurrPagerAdapter currPagerAdapter;
    private boolean isCanScroll = true;

    public CusViewPager(@NonNull Context context) {
        super(context);
    }

    public CusViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScanScroll(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isCanScroll && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return isCanScroll && super.onTouchEvent(ev);
    }

    public void setResImg(int... resImgs) {
        List<View> views = new ArrayList<>();
        for (int resImg : resImgs) {
            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(resImg);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
            views.add(imageView);
        }
        setViews(views);
    }

    public void setViews(List<View> views) {
        this.setAdapter(currPagerAdapter = new CurrPagerAdapter(views));
    }

    public void setAdapter(FragmentManager fragmentManager, Fragment... fragment) {
        this.setAdapter(new CusFragmentPagerAdapter(fragmentManager, fragment));
    }

    class CurrPagerAdapter extends PagerAdapter {

        private List<View> mViews;

        public CurrPagerAdapter(List<View> views) {
            this.mViews = views;
        }

        @Override
        public int getCount() {
            return mViews.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ((ViewPager) container).addView(mViews.get(position));
            return mViews.get(position);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//            super.destroyItem(container, position, object);
            ((ViewPager) container).removeView(mViews.get(position));
        }
    }
}
