package com.zd112.framework.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zd112.framework.annotation.Transformer;
import com.zd112.framework.apdater.CusFragmentPagerAdapter;
import com.zd112.framework.apdater.CusPagerAdapter;
import com.zd112.framework.transforms.AccordionTransformer;
import com.zd112.framework.transforms.BackgroundToForegroundTransformer;
import com.zd112.framework.transforms.CubeInTransformer;
import com.zd112.framework.transforms.CubeOutTransformer;
import com.zd112.framework.transforms.DepthPageTransformer;
import com.zd112.framework.transforms.DrawerTransformer;
import com.zd112.framework.transforms.FlipHorizontalTransformer;
import com.zd112.framework.transforms.FlipVerticalTransformer;
import com.zd112.framework.transforms.ForegroundToBackgroundTransformer;
import com.zd112.framework.transforms.RotateDownTransformer;
import com.zd112.framework.transforms.RotateUpTransformer;
import com.zd112.framework.transforms.ScaleInOutTransformer;
import com.zd112.framework.transforms.StackTransformer;
import com.zd112.framework.transforms.TabletTransformer;
import com.zd112.framework.transforms.VerticalTransformer;
import com.zd112.framework.transforms.ZoomInTransformer;
import com.zd112.framework.transforms.ZoomOutSlideTransformer;
import com.zd112.framework.transforms.ZoomOutTransformer;
import com.zd112.framework.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class CusViewPager extends ViewPager implements ViewPager.PageTransformer {

    public CusPagerAdapter mCusPagerAdapter;
    private boolean mIsCanScroll = true;
    private boolean mIsVertical = false;
    @SuppressLint("WrongConstant")
    private @Transformer
    int mMode = -1;
    private Class[] mTransformClass = {AccordionTransformer.class, BackgroundToForegroundTransformer.class, CubeInTransformer.class, CubeOutTransformer.class, DepthPageTransformer.class,
            DrawerTransformer.class, FlipHorizontalTransformer.class, FlipVerticalTransformer.class, ForegroundToBackgroundTransformer.class, RotateDownTransformer.class, RotateUpTransformer.class,
            ScaleInOutTransformer.class, StackTransformer.class, TabletTransformer.class, VerticalTransformer.class, ZoomInTransformer.class, ZoomOutSlideTransformer.class, ZoomOutTransformer.class};

    public CusViewPager(@NonNull Context context) {
        super(context);
    }

    public CusViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScanScroll(boolean isCanScroll) {
        this.mIsCanScroll = isCanScroll;
    }

    @SuppressLint("WrongConstant")
    public void setMode(boolean isVertical) {
        this.setMode(isVertical, -1);
    }

    public void setMode(boolean isVertical, @Transformer int mode) {
        this.setMode(isVertical, true, mode);
    }

    public void setMode(boolean isVertical, boolean reverseDrawingOrder, @Transformer int mode) {
        this.mIsVertical = isVertical;
        this.mMode = mode;
        try {
            LogUtils.e("----------mode:", mode);
            setPageTransformer(reverseDrawingOrder, mode < 0 ? this : (PageTransformer) mTransformClass[mMode].newInstance());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        this.invalidate();
    }

    private MotionEvent swapTouchEvent(MotionEvent event) {
        float width = getWidth();
        float height = getHeight();
        event.setLocation((event.getY() / height) * width, (event.getX() / width) * height);
        return event;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return mIsCanScroll && super.onInterceptTouchEvent(mIsVertical ? swapTouchEvent(MotionEvent.obtain(event)) : event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mIsCanScroll && super.onTouchEvent(mIsVertical ? swapTouchEvent(MotionEvent.obtain(event)) : event);
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
        this.setAdapter(mCusPagerAdapter = new CusPagerAdapter(views));
    }

    public void setAdapter(FragmentManager fragmentManager, Fragment... fragment) {
        this.setAdapter(new CusFragmentPagerAdapter(fragmentManager, fragment));
    }

    @Override
    public void transformPage(@NonNull View view, float v) {
        float alpha = 0;
        if (0 <= v && v <= 1) {
            alpha = 1 - v;
        } else if (-1 < v && v < 0) {
            alpha = v + 1;
        }
        //view.setAlpha(alpha);
        view.setTranslationX(view.getWidth() * -v);
        float yPosition = v * view.getHeight();
        view.setTranslationY(yPosition);
    }
}
