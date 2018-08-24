package com.zd112.framework.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.zd112.framework.MyAppGlideModule;
import com.zd112.framework.R;
import com.zd112.framework.apdater.GlideImageLoader;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by etongdai on 2018/1/15.
 */

public class BannerView extends RelativeLayout {
    private ViewPager mPager;
    //指示器容器
    private LinearLayout mIndicatorContainer;

    private Drawable mUnSelectedDrawable;
    private Drawable mSelectedDrawable;

    private final int WHAT_AUTO_PLAY = 1000;

    private boolean mIsAutoPlay = true;

    private int mItemCount;

    private int mSelectedIndicatorColor = 0xffff0000;
    private int mUnSelectedIndicatorColor = 0x88888888;

    private Shape mIndicatorShape = Shape.oval;
    private int mSelectedIndicatorHeight = 6;
    private int mSelectedIndicatorWidth = 6;
    private int mUnSelectedIndicatorHeight = 6;
    private int mUnSelectedIndicatorWidth = 6;

    private Position mIndicatorPosition = Position.centerBottom;
    private int mAutoPlayDuration = 4000;
    private int mScrollDuration = 900;

    private int mIndicatorSpace = 3;
    private int mIndicatorMargin = 10;

    private int mCurrentPosition;

    private MyAppGlideModule.ImageLoader mImageLoader;

    private enum Shape {
        rect, oval
    }

    private enum Position {
        centerBottom,
        rightBottom,
        leftBottom,
        centerTop,
        rightTop,
        leftTop
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAutoPlay();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAutoPlay();
    }

    private OnBannerItemClickListener onBannerItemClickListener;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == WHAT_AUTO_PLAY) {
                if (mPager != null && mIsAutoPlay) {
                    mPager.setCurrentItem(mPager.getCurrentItem() + 1, true);
                    handler.sendEmptyMessageDelayed(WHAT_AUTO_PLAY, mAutoPlayDuration);
                }
            }
            return false;
        }
    });

    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyle) {

        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.BannerLayoutStyle, defStyle, 0);
        mSelectedIndicatorColor = array.getColor(R.styleable.BannerLayoutStyle_selectedIndicatorColor, mSelectedIndicatorColor);
        mUnSelectedIndicatorColor = array.getColor(R.styleable.BannerLayoutStyle_unSelectedIndicatorColor, mUnSelectedIndicatorColor);

        int shape = array.getInt(R.styleable.BannerLayoutStyle_indicatorShape, Shape.oval.ordinal());
        for (Shape shape1 : Shape.values()) {
            if (shape1.ordinal() == shape) {
                mIndicatorShape = shape1;
                break;
            }
        }
        mSelectedIndicatorHeight = (int) array.getDimension(R.styleable.BannerLayoutStyle_selectedIndicatorHeight, mSelectedIndicatorHeight);
        mSelectedIndicatorWidth = (int) array.getDimension(R.styleable.BannerLayoutStyle_selectedIndicatorWidth, mSelectedIndicatorWidth);
        mUnSelectedIndicatorHeight = (int) array.getDimension(R.styleable.BannerLayoutStyle_unSelectedIndicatorHeight, mUnSelectedIndicatorHeight);
        mUnSelectedIndicatorWidth = (int) array.getDimension(R.styleable.BannerLayoutStyle_unSelectedIndicatorWidth, mUnSelectedIndicatorWidth);

        int position = array.getInt(R.styleable.BannerLayoutStyle_indicatorPosition, Position.centerBottom.ordinal());
        for (Position position1 : Position.values()) {
            if (position == position1.ordinal()) {
                mIndicatorPosition = position1;
            }
        }
        mIndicatorSpace = (int) array.getDimension(R.styleable.BannerLayoutStyle_indicatorSpace, mIndicatorSpace);
        mIndicatorMargin = (int) array.getDimension(R.styleable.BannerLayoutStyle_indicatorMargin, mIndicatorMargin);
        mAutoPlayDuration = array.getInt(R.styleable.BannerLayoutStyle_autoPlayDuration, mAutoPlayDuration);
        mScrollDuration = array.getInt(R.styleable.BannerLayoutStyle_scrollDuration, mScrollDuration);
        mIsAutoPlay = array.getBoolean(R.styleable.BannerLayoutStyle_isAutoPlay, mIsAutoPlay);
        array.recycle();

        //绘制未选中状态图形
        LayerDrawable unSelectedLayerDrawable;
        LayerDrawable selectedLayerDrawable;
        GradientDrawable unSelectedGradientDrawable;
        unSelectedGradientDrawable = new GradientDrawable();

        //绘制选中状态图形
        GradientDrawable selectedGradientDrawable;
        selectedGradientDrawable = new GradientDrawable();
        switch (mIndicatorShape) {
            case rect:
                unSelectedGradientDrawable.setShape(GradientDrawable.RECTANGLE);
                selectedGradientDrawable.setShape(GradientDrawable.RECTANGLE);
                break;
            case oval:
                unSelectedGradientDrawable.setShape(GradientDrawable.OVAL);
                selectedGradientDrawable.setShape(GradientDrawable.OVAL);
                break;
        }
        unSelectedGradientDrawable.setColor(mUnSelectedIndicatorColor);
        unSelectedGradientDrawable.setSize(mUnSelectedIndicatorWidth, mUnSelectedIndicatorHeight);
        unSelectedLayerDrawable = new LayerDrawable(new Drawable[]{unSelectedGradientDrawable});
        mUnSelectedDrawable = unSelectedLayerDrawable;

        selectedGradientDrawable.setColor(mSelectedIndicatorColor);
        selectedGradientDrawable.setSize(mSelectedIndicatorWidth, mSelectedIndicatorHeight);
        selectedLayerDrawable = new LayerDrawable(new Drawable[]{selectedGradientDrawable});
        mSelectedDrawable = selectedLayerDrawable;
    }


    //添加网络图片路径
    public void setViewUrls(List<String> urls) {
        setImageLoader(new GlideImageLoader());
        List<View> views = new ArrayList<>();
        mItemCount = urls.size();
        //主要是解决当item为小于3个的时候滑动有问题，这里将其拼凑成3个以上
        if (mItemCount < 1) {//当item个数0
            throw new IllegalStateException("item count not equal zero");
        } else if (mItemCount < 2) { //当item个数为1
            views.add(getImageView(urls.get(0), 0));
            views.add(getImageView(urls.get(0), 0));
            views.add(getImageView(urls.get(0), 0));
        } else if (mItemCount < 3) {//当item个数为2
            views.add(getImageView(urls.get(0), 0));
            views.add(getImageView(urls.get(1), 1));
            views.add(getImageView(urls.get(0), 0));
            views.add(getImageView(urls.get(1), 1));
        } else {
            for (int i = 0; i < urls.size(); i++) {
                views.add(getImageView(urls.get(i), i));
            }
        }
        setViews(views);
    }

    //添加Res资源
    public void setViewRes(int... resImgs) {
        List<View> views = new ArrayList<>();
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        for (int i : resImgs) {
            //不确定1
            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(i);
            imageView.setLayoutParams(layoutParams);
            views.add(imageView);
        }
        setViews(views);
    }

    @NonNull
    private ImageView getImageView(String url, final int position) {
        ImageView imageView = new ImageView(getContext());
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onBannerItemClickListener != null) {
                    onBannerItemClickListener.onItemClick(position);
                }
            }
        });
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mImageLoader.displayImage(getContext(), url, imageView);
        return imageView;
    }


    public void setImageLoader(MyAppGlideModule.ImageLoader imageLoader) {
        this.mImageLoader = imageLoader;
    }

    public ViewPager getPager() {
        return mPager != null ? mPager : null;
    }

    //添加任意View视图
    public void setViews(final List<View> views) {
        //初始化pager
        mPager = new ViewPager(getContext());
        //添加viewpager到SliderLayout
        addView(mPager);
        setSliderTransformDuration(mScrollDuration);
        //初始化indicatorContainer
        mIndicatorContainer = new LinearLayout(getContext());
        mIndicatorContainer.setGravity(Gravity.CENTER_VERTICAL);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        switch (mIndicatorPosition) {
            case centerBottom:
                params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                break;
            case centerTop:
                params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                break;
            case leftBottom:
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                break;
            case leftTop:
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                break;
            case rightBottom:
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                break;
            case rightTop:
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                break;
        }
        //设置margin
        params.setMargins(mIndicatorMargin, mIndicatorMargin, mIndicatorMargin, mIndicatorMargin);
        //添加指示器容器布局到SliderLayout
        addView(mIndicatorContainer, params);

        //初始化指示器，并添加到指示器容器布局
        for (int i = 0; i < mItemCount; i++) {
            ImageView indicator = new ImageView(getContext());
            indicator.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            indicator.setPadding(mIndicatorSpace, mIndicatorSpace, mIndicatorSpace, mIndicatorSpace);
            indicator.setImageDrawable(mUnSelectedDrawable);
            mIndicatorContainer.addView(indicator);
        }
        LoopPagerAdapter pagerAdapter = new LoopPagerAdapter(views);
        mPager.setAdapter(pagerAdapter);
        //设置当前item到Integer.MAX_VALUE中间的一个值，看起来像无论是往前滑还是往后滑都是ok的
        //如果不设置，用户往左边滑动的时候已经划不动了
        int targetItemPosition = Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % mItemCount;
        mCurrentPosition = targetItemPosition;
        mPager.setCurrentItem(targetItemPosition);
        switchIndicator(targetItemPosition % mItemCount);
        mPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mCurrentPosition = position;
                switchIndicator(position % mItemCount);
            }
        });
        if (mIsAutoPlay) {
            startAutoPlay();
        }

    }

    public void setSliderTransformDuration(int duration) {
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(mPager.getContext(), null, duration);
            mScroller.set(mPager, scroller);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    /**
     * 开始自动轮播
     */
    private void startAutoPlay() {
        stopAutoPlay(); // 避免重复消息
        if (mIsAutoPlay) {
            handler.sendEmptyMessageDelayed(WHAT_AUTO_PLAY, mAutoPlayDuration);
        }
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);

        if (visibility == VISIBLE) {
            startAutoPlay();
        } else {
            stopAutoPlay();
        }
    }


    /**
     * 停止自动轮播
     */
    private void stopAutoPlay() {
        if (mPager != null) {
            mPager.setCurrentItem(mPager.getCurrentItem(), false);
        }
        if (mIsAutoPlay) {
            handler.removeMessages(WHAT_AUTO_PLAY);
            if (mPager != null) {
                mPager.setCurrentItem(mPager.getCurrentItem(), false);
            }
        }
    }

    /**
     * @param autoPlay 是否自动轮播
     */
    public void setAutoPlay(boolean autoPlay) {
        mIsAutoPlay = autoPlay;
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                stopAutoPlay();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                startAutoPlay();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 切换指示器状态
     *
     * @param currentPosition 当前位置
     */
    private void switchIndicator(int currentPosition) {
        for (int i = 0; i < mIndicatorContainer.getChildCount(); i++) {
            ((ImageView) mIndicatorContainer.getChildAt(i)).setImageDrawable(i == currentPosition ? mSelectedDrawable : mUnSelectedDrawable);
        }
    }


    public void setOnBannerItemClickListener(OnBannerItemClickListener onBannerItemClickListener) {
        this.onBannerItemClickListener = onBannerItemClickListener;
    }

    public interface OnBannerItemClickListener {
        void onItemClick(int position);
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        mCurrentPosition = savedState.currentPosition;
        requestLayout();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentPosition = mCurrentPosition;
        return savedState;
    }

    private static class SavedState extends BaseSavedState {
        int currentPosition;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            currentPosition = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(currentPosition);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }


    private class LoopPagerAdapter extends PagerAdapter {
        private List<View> views;

        LoopPagerAdapter(List<View> views) {
            this.views = views;
        }

        @Override
        public int getCount() {
            //Integer.MAX_VALUE = 2147483647
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if (views.size() > 0) {
                //position % view.size()是指虚拟的position会在[0，view.size()）之间循环
                View view = views.get(position % views.size());
                if (container.equals(view.getParent())) {
                    container.removeView(view);
                }
                container.addView(view);
                return view;
            }
            return null;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
        }
    }

    public class FixedSpeedScroller extends Scroller {

        private int mDuration = 1000;

        public FixedSpeedScroller(Context context) {
            super(context);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator, int duration) {
            this(context, interpolator);
            mDuration = duration;
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration);
        }
    }
}
