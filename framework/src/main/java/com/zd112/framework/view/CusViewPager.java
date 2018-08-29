package com.zd112.framework.view;

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

import com.zd112.framework.apdater.CusFragmentPagerAdapter;
import com.zd112.framework.apdater.CusPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class CusViewPager extends ViewPager implements ViewPager.PageTransformer {

    public CusPagerAdapter mCusPagerAdapter;
    private boolean mIsCanScroll = true;
    private boolean mIsVertical = false;
    public final int DEFAULT = 1;
    public final int STACK = 2;
    public final int ZOO_OUT = 3;
    private int mMode = DEFAULT;

    public CusViewPager(@NonNull Context context) {
        super(context);
    }

    public CusViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScanScroll(boolean isCanScroll) {
        this.mIsCanScroll = isCanScroll;
    }

    public void setMode(boolean isVertical) {
        this.setMode(isVertical, DEFAULT);
    }

    public void setMode(boolean isVertical, int mode) {
        this.setMode(isVertical, true, mode);
    }

    public void setMode(boolean isVertical, boolean reverseDrawingOrder, int mode) {
        this.mIsVertical = isVertical;
        this.mMode = mode;
        setPageTransformer(reverseDrawingOrder, this.mIsVertical ? this : null);
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
        if (mMode == STACK) {
            view.setTranslationX(view.getWidth() * -v);
            view.setTranslationY(v < 0 ? v * view.getHeight() : 0f);
        } else if (mMode == ZOO_OUT) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();
            float alpha = 0;
            if (0 <= v && v <= 1) {
                alpha = 1 - v;
            } else if (-1 < v && v < 0) {
                float scaleFactor = Math.max(0.90f, 1 - Math.abs(v));
                float verticalMargin = pageHeight * (1 - scaleFactor) / 2;
                float horizontalMargin = pageWidth * (1 - scaleFactor) / 2;
                if (v < 0) {
                    view.setTranslationX(horizontalMargin - verticalMargin / 2);
                } else {
                    view.setTranslationX(-horizontalMargin + verticalMargin / 2);
                }

                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                alpha = v + 1;
            }
            view.setAlpha(alpha);
            view.setTranslationX(view.getWidth() * -v);
            float yPosition = v * view.getHeight();
            view.setTranslationY(yPosition);
        } else {
            float alpha = 0;
            if (0 <= v && v <= 1) {
                alpha = 1 - v;
            } else if (-1 < v && v < 0) {
                alpha = v + 1;
            }
            view.setAlpha(alpha);
            view.setTranslationX(view.getWidth() * -v);
            float yPosition = v * view.getHeight();
            view.setTranslationY(yPosition);
        }
    }
}
