package com.zd112.framework.view.refresh.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;

import com.zd112.framework.R;
import com.zd112.framework.utils.DimenUtils;
import com.zd112.framework.view.refresh.interfaces.RefreshFooter;
import com.zd112.framework.view.refresh.interfaces.RefreshLayout;
import com.zd112.framework.view.refresh.util.RefreshState;
import com.zd112.framework.view.refresh.util.SpinnerStyle;

@SuppressWarnings({"unused", "UnusedReturnValue", "deprecation"})
public class ClassicsFooter extends InternalClassics<ClassicsFooter> implements RefreshFooter {

    public static String REFRESH_FOOTER_PULLING = null;//"上拉加载更多";
    public static String REFRESH_FOOTER_RELEASE = null;//"释放立即加载";
    public static String REFRESH_FOOTER_LOADING = null;//"正在加载...";
    public static String REFRESH_FOOTER_REFRESHING = null;//"正在刷新...";
    public static String REFRESH_FOOTER_FINISH = null;//"加载完成";
    public static String REFRESH_FOOTER_FAILED = null;//"加载失败";
    public static String REFRESH_FOOTER_NOTHING = null;//"没有更多数据了";

    protected boolean mNoMoreData = false;

    public ClassicsFooter(Context context) {
        this(context, null);
    }

    public ClassicsFooter(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClassicsFooter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (REFRESH_FOOTER_PULLING == null) {
            REFRESH_FOOTER_PULLING = context.getString(R.string.rf_footer_pulling);
        }

        if (REFRESH_FOOTER_RELEASE == null) {
            REFRESH_FOOTER_RELEASE = context.getString(R.string.rf_footer_release);
        }

        if (REFRESH_FOOTER_LOADING == null) {
            REFRESH_FOOTER_LOADING = context.getString(R.string.rf_footer_loading);
        }

        if (REFRESH_FOOTER_REFRESHING == null) {
            REFRESH_FOOTER_REFRESHING = context.getString(R.string.rf_footer_refreshing);
        }

        if (REFRESH_FOOTER_FINISH == null) {
            REFRESH_FOOTER_FINISH = context.getString(R.string.rf_footer_finish);
        }

        if (REFRESH_FOOTER_FAILED == null) {
            REFRESH_FOOTER_FAILED = context.getString(R.string.rf_footer_failed);
        }

        if (REFRESH_FOOTER_NOTHING == null) {
            REFRESH_FOOTER_NOTHING = context.getString(R.string.rf_footer_nothing);
        }

        final View thisView = this;
        final View arrowView = mArrowView;
        final View progressView = mProgressView;

        mTitleText.setTextColor(0xff666666);
        mTitleText.setText(thisView.isInEditMode() ? REFRESH_FOOTER_LOADING : REFRESH_FOOTER_PULLING);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ClassicsFooter);

        RelativeLayout.LayoutParams lpArrow = (RelativeLayout.LayoutParams) arrowView.getLayoutParams();
        RelativeLayout.LayoutParams lpProgress = (RelativeLayout.LayoutParams) progressView.getLayoutParams();
        lpProgress.rightMargin = ta.getDimensionPixelSize(R.styleable.ClassicsFooter_rfDrawableMarginRight, DimenUtils.dip2px(20));
        lpArrow.rightMargin = lpProgress.rightMargin;

        lpArrow.width = ta.getLayoutDimension(R.styleable.ClassicsFooter_rfDrawableArrowSize, lpArrow.width);
        lpArrow.height = ta.getLayoutDimension(R.styleable.ClassicsFooter_rfDrawableArrowSize, lpArrow.height);
        lpProgress.width = ta.getLayoutDimension(R.styleable.ClassicsFooter_rfDrawableProgressSize, lpProgress.width);
        lpProgress.height = ta.getLayoutDimension(R.styleable.ClassicsFooter_rfDrawableProgressSize, lpProgress.height);

        lpArrow.width = ta.getLayoutDimension(R.styleable.ClassicsFooter_rfDrawableSize, lpArrow.width);
        lpArrow.height = ta.getLayoutDimension(R.styleable.ClassicsFooter_rfDrawableSize, lpArrow.height);
        lpProgress.width = ta.getLayoutDimension(R.styleable.ClassicsFooter_rfDrawableSize, lpProgress.width);
        lpProgress.height = ta.getLayoutDimension(R.styleable.ClassicsFooter_rfDrawableSize, lpProgress.height);

        mFinishDuration = ta.getInt(R.styleable.ClassicsFooter_rfFinishDuration, mFinishDuration);
        mSpinnerStyle = SpinnerStyle.values()[ta.getInt(R.styleable.ClassicsFooter_rfClassicsSpinnerStyle, mSpinnerStyle.ordinal())];

        if (ta.hasValue(R.styleable.ClassicsFooter_rfDrawableArrow)) {
            mArrowView.setImageDrawable(ta.getDrawable(R.styleable.ClassicsFooter_rfDrawableArrow));
        } else {
            mArrowDrawable = new ArrowDrawable();
            mArrowDrawable.setColor(0xff666666);
            mArrowView.setImageDrawable(mArrowDrawable);
        }

        if (ta.hasValue(R.styleable.ClassicsFooter_rfDrawableProgress)) {
            mProgressView.setImageDrawable(ta.getDrawable(R.styleable.ClassicsFooter_rfDrawableProgress));
        } else {
            mProgressDrawable = new ProgressDrawable();
            mProgressDrawable.setColor(0xff666666);
            mProgressView.setImageDrawable(mProgressDrawable);
        }

        if (ta.hasValue(R.styleable.ClassicsFooter_rfTextSizeTitle)) {
            mTitleText.setTextSize(TypedValue.COMPLEX_UNIT_PX, ta.getDimensionPixelSize(R.styleable.ClassicsFooter_rfTextSizeTitle, DimenUtils.dp2px(16)));
        } else {
            mTitleText.setTextSize(12);
        }

        if (ta.hasValue(R.styleable.ClassicsFooter_rfPrimaryColor)) {
            setPrimaryColor(ta.getColor(R.styleable.ClassicsFooter_rfPrimaryColor, 0));
        }
        if (ta.hasValue(R.styleable.ClassicsFooter_rfAccentColor)) {
            setAccentColor(ta.getColor(R.styleable.ClassicsFooter_rfAccentColor, 0));
        }

        ta.recycle();

    }

    @Override
    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
        if (!mNoMoreData) {
            super.onStartAnimator(refreshLayout, height, maxDragHeight);
        }
    }

    @Override
    public int onFinish(@NonNull RefreshLayout layout, boolean success) {
        if (!mNoMoreData) {
            String tips = "预期收益不承诺等于实际收益,投资需谨慎";
//            mTitleText.setText(success ? layout.getWord() : REFRESH_FOOTER_FAILED);
            mTitleText.setText(success ? tips : REFRESH_FOOTER_FAILED);
            return super.onFinish(layout, success);
        }
        return 0;
    }

    /**
     * ClassicsFooter 在(SpinnerStyle.FixedBehind)时才有主题色
     */
    @Override
    @Deprecated
    public void setPrimaryColors(@ColorInt int... colors) {
        if (mSpinnerStyle == SpinnerStyle.FixedBehind) {
            super.setPrimaryColors(colors);
        }
    }

    /**
     * 设置数据全部加载完成，将不能再次触发加载功能
     */
    @Override
    public boolean setNoMoreData(boolean noMoreData) {
        if (mNoMoreData != noMoreData) {
            mNoMoreData = noMoreData;
            final View arrowView = mArrowView;
            if (noMoreData) {
                mTitleText.setText(REFRESH_FOOTER_NOTHING);
                arrowView.setVisibility(GONE);
            } else {
                mTitleText.setText(REFRESH_FOOTER_PULLING);
                arrowView.setVisibility(VISIBLE);
            }
        }
        return true;
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        final View arrowView = mArrowView;
        String tips = "预期收益不承诺等于实际收益,投资需谨慎";//todo:the temp
        if (!mNoMoreData) {
            switch (newState) {
                case None:
                    arrowView.setVisibility(VISIBLE);
                case PullUpToLoad:
//                    mTitleText.setText(refreshLayout.getWord());
                    mTitleText.setText(tips);
                    arrowView.animate().rotation(180);
                    break;
                case Loading:
                case LoadReleased:
                    arrowView.setVisibility(GONE);
//                    mTitleText.setText(refreshLayout.getWord());
                    mTitleText.setText(tips);
                    break;
                case ReleaseToLoad:
//                    mTitleText.setText(refreshLayout.getWord());
                    mTitleText.setText(tips);
                    arrowView.animate().rotation(0);
                    break;
                case Refreshing:
//                    mTitleText.setText(refreshLayout.getWord());
                    mTitleText.setText(tips);
                    arrowView.setVisibility(GONE);
                    break;
            }
        }
    }
    //</editor-fold>

}
