package com.zd112.framework.view.refresh.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zd112.framework.R;
import com.zd112.framework.utils.DimenUtils;
import com.zd112.framework.view.refresh.interfaces.RefreshHeader;
import com.zd112.framework.view.refresh.interfaces.RefreshLayout;
import com.zd112.framework.view.refresh.util.RefreshState;
import com.zd112.framework.view.refresh.util.SpinnerStyle;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class ClassicsHeader extends InternalClassics<ClassicsHeader> implements RefreshHeader {

    public static final byte ID_TEXT_UPDATE = 4;

    public static String REFRESH_HEADER_PULLING = null;//"下拉可以刷新";
    public static String REFRESH_HEADER_REFRESHING = null;//"正在刷新...";
    public static String REFRESH_HEADER_LOADING = null;//"正在加载...";
    public static String REFRESH_HEADER_RELEASE = null;//"释放立即刷新";
    public static String REFRESH_HEADER_FINISH = null;//"刷新完成";
    public static String REFRESH_HEADER_FAILED = null;//"刷新失败";
    public static String REFRESH_HEADER_UPDATE = null;//"上次更新 M-d HH:mm";
    public static String REFRESH_HEADER_SECONDARY = null;//"释放进入二楼";

    protected String KEY_LAST_UPDATE_TIME = "LAST_UPDATE_TIME";

    protected Date mLastTime;
    protected TextView mLastUpdateText;
    protected SharedPreferences mShared;
    protected DateFormat mLastUpdateFormat;
    protected boolean mEnableLastTime = true;

    public ClassicsHeader(Context context) {
        this(context, null);
    }

    public ClassicsHeader(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClassicsHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (REFRESH_HEADER_PULLING == null) {
            REFRESH_HEADER_PULLING = context.getString(R.string.rf_header_pulling);
        }
        if (REFRESH_HEADER_REFRESHING == null) {
            REFRESH_HEADER_REFRESHING = context.getString(R.string.rf_header_refreshing);
        }
        if (REFRESH_HEADER_LOADING == null) {
            REFRESH_HEADER_LOADING = context.getString(R.string.rf_header_loading);
        }
        if (REFRESH_HEADER_RELEASE == null) {
            REFRESH_HEADER_RELEASE = context.getString(R.string.rf_header_release);
        }
        if (REFRESH_HEADER_FINISH == null) {
            REFRESH_HEADER_FINISH = context.getString(R.string.rf_header_finish);
        }
        if (REFRESH_HEADER_FAILED == null) {
            REFRESH_HEADER_FAILED = context.getString(R.string.rf_header_failed);
        }
        if (REFRESH_HEADER_UPDATE == null) {
            REFRESH_HEADER_UPDATE = context.getString(R.string.rf_header_update);
        }
        if (REFRESH_HEADER_SECONDARY == null) {
            REFRESH_HEADER_SECONDARY = context.getString(R.string.rf_header_secondary);
        }

        mLastUpdateText = new TextView(context);
        mLastUpdateText.setTextColor(ContextCompat.getColor(context, R.color.font_gray));
        mLastUpdateFormat = new SimpleDateFormat(REFRESH_HEADER_UPDATE, Locale.getDefault());

        final View thisView = this;
        final View arrowView = mArrowView;
        final View updateView = mLastUpdateText;
        final View progressView = mProgressView;
        final ViewGroup centerLayout = mCenterLayout;

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ClassicsHeader);

        LayoutParams lpArrow = (LayoutParams) arrowView.getLayoutParams();
        LayoutParams lpProgress = (LayoutParams) progressView.getLayoutParams();
        LinearLayout.LayoutParams lpUpdateText = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        lpUpdateText.topMargin = ta.getDimensionPixelSize(R.styleable.ClassicsHeader_rfTextTimeMarginTop, DimenUtils.dip2px(0));
        lpProgress.rightMargin = ta.getDimensionPixelSize(R.styleable.ClassicsFooter_rfDrawableMarginRight, DimenUtils.dip2px(20));
        lpArrow.rightMargin = lpProgress.rightMargin;

        lpArrow.width = ta.getLayoutDimension(R.styleable.ClassicsHeader_rfDrawableArrowSize, lpArrow.width);
        lpArrow.height = ta.getLayoutDimension(R.styleable.ClassicsHeader_rfDrawableArrowSize, lpArrow.height);
        lpProgress.width = ta.getLayoutDimension(R.styleable.ClassicsHeader_rfDrawableProgressSize, lpProgress.width);
        lpProgress.height = ta.getLayoutDimension(R.styleable.ClassicsHeader_rfDrawableProgressSize, lpProgress.height);

        lpArrow.width = ta.getLayoutDimension(R.styleable.ClassicsHeader_rfDrawableSize, lpArrow.width);
        lpArrow.height = ta.getLayoutDimension(R.styleable.ClassicsHeader_rfDrawableSize, lpArrow.height);
        lpProgress.width = ta.getLayoutDimension(R.styleable.ClassicsHeader_rfDrawableSize, lpProgress.width);
        lpProgress.height = ta.getLayoutDimension(R.styleable.ClassicsHeader_rfDrawableSize, lpProgress.height);

        mFinishDuration = ta.getInt(R.styleable.ClassicsHeader_rfFinishDuration, mFinishDuration);
        mEnableLastTime = ta.getBoolean(R.styleable.ClassicsHeader_rfEnableLastTime, mEnableLastTime);
        mSpinnerStyle = SpinnerStyle.values()[ta.getInt(R.styleable.ClassicsHeader_rfClassicsSpinnerStyle, mSpinnerStyle.ordinal())];

        if (ta.hasValue(R.styleable.ClassicsHeader_rfDrawableArrow)) {
            mArrowView.setImageDrawable(ta.getDrawable(R.styleable.ClassicsHeader_rfDrawableArrow));
        } else {
            mArrowDrawable = new ArrowDrawable();
            mArrowDrawable.setColor(0xff666666);
            mArrowView.setImageDrawable(mArrowDrawable);
        }

        if (ta.hasValue(R.styleable.ClassicsHeader_rfDrawableProgress)) {
            mProgressView.setImageDrawable(ta.getDrawable(R.styleable.ClassicsHeader_rfDrawableProgress));
        } else {
            mProgressDrawable = new ProgressDrawable();
            mProgressDrawable.setColor(0xff666666);
            mProgressView.setImageDrawable(mProgressDrawable);
        }

        if (ta.hasValue(R.styleable.ClassicsHeader_rfTextSizeTitle)) {
            mTitleText.setTextSize(TypedValue.COMPLEX_UNIT_PX, ta.getDimensionPixelSize(R.styleable.ClassicsHeader_rfTextSizeTitle, DimenUtils.dp2px(16)));
        } else {
            mTitleText.setTextSize(12);
        }

        if (ta.hasValue(R.styleable.ClassicsHeader_rfTextSizeTime)) {
            mLastUpdateText.setTextSize(TypedValue.COMPLEX_UNIT_PX, ta.getDimensionPixelSize(R.styleable.ClassicsHeader_rfTextSizeTime, DimenUtils.dp2px(12)));
        } else {
            mLastUpdateText.setTextSize(10);
        }

        if (ta.hasValue(R.styleable.ClassicsHeader_rfPrimaryColor)) {
            setPrimaryColor(ta.getColor(R.styleable.ClassicsHeader_rfPrimaryColor, 0));
        }
        if (ta.hasValue(R.styleable.ClassicsHeader_rfAccentColor)) {
            setAccentColor(ta.getColor(R.styleable.ClassicsHeader_rfAccentColor, 0));
        }

        ta.recycle();

        updateView.setId(ID_TEXT_UPDATE);
        updateView.setVisibility(mEnableLastTime ? VISIBLE : GONE);
        centerLayout.addView(updateView, lpUpdateText);
        mTitleText.setText(thisView.isInEditMode() ? REFRESH_HEADER_REFRESHING : REFRESH_HEADER_PULLING);

        try {//try 不能删除-否则会出现兼容性问题
            if (context instanceof FragmentActivity) {
                FragmentManager manager = ((FragmentActivity) context).getSupportFragmentManager();
                if (manager != null) {
                    @SuppressLint("RestrictedApi")
                    List<Fragment> fragments = manager.getFragments();
                    if (fragments != null && fragments.size() > 0) {
                        setLastUpdateTime(new Date());
                        return;
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        KEY_LAST_UPDATE_TIME += context.getClass().getName();
        mShared = context.getSharedPreferences("ClassicsHeader", Context.MODE_PRIVATE);
        setLastUpdateTime(new Date(mShared.getLong(KEY_LAST_UPDATE_TIME, System.currentTimeMillis())));

    }

    @Override
    public int onFinish(@NonNull RefreshLayout layout, boolean success) {
        if (success) {
            mTitleText.setText(layout.getWord());
            if (mLastTime != null) {
                setLastUpdateTime(new Date());
            }
        } else {
            mTitleText.setText(REFRESH_HEADER_FAILED);
        }
        return super.onFinish(layout, success);//延迟500毫秒之后再弹回
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        final View arrowView = mArrowView;
        final View updateView = mLastUpdateText;
        switch (newState) {
            case None:
                updateView.setVisibility(mEnableLastTime ? VISIBLE : GONE);
            case PullDownToRefresh:
                mTitleText.setText(refreshLayout.getWord());
                arrowView.setVisibility(VISIBLE);
                arrowView.animate().rotation(0);
                break;
            case Refreshing:
            case RefreshReleased:
                mTitleText.setText(refreshLayout.getWord());
                arrowView.setVisibility(GONE);
                break;
            case ReleaseToRefresh:
                mTitleText.setText(refreshLayout.getWord());
                arrowView.animate().rotation(180);
                break;
            case ReleaseToTwoLevel:
                mTitleText.setText(refreshLayout.getWord());
                arrowView.animate().rotation(0);
                break;
            case Loading:
                arrowView.setVisibility(GONE);
                updateView.setVisibility(mEnableLastTime ? INVISIBLE : GONE);
                mTitleText.setText(REFRESH_HEADER_LOADING);
                break;
        }
    }

    public ClassicsHeader setLastUpdateTime(Date time) {
        final View thisView = this;
        mLastTime = time;
        mLastUpdateText.setText(mLastUpdateFormat.format(time));
        if (mShared != null && !thisView.isInEditMode()) {
            mShared.edit().putLong(KEY_LAST_UPDATE_TIME, time.getTime()).apply();
        }
        return this;
    }

    public ClassicsHeader setTimeFormat(DateFormat format) {
        mLastUpdateFormat = format;
        if (mLastTime != null) {
            mLastUpdateText.setText(mLastUpdateFormat.format(mLastTime));
        }
        return this;
    }

    public ClassicsHeader setLastUpdateText(CharSequence text) {
        mLastTime = null;
        mLastUpdateText.setText(text);
        return this;
    }

    public ClassicsHeader setAccentColor(@ColorInt int accentColor) {
        mLastUpdateText.setTextColor(accentColor & 0x00ffffff | 0xcc000000);
        return super.setAccentColor(accentColor);
    }

    public ClassicsHeader setEnableLastTime(boolean enable) {
        final View updateView = mLastUpdateText;
        mEnableLastTime = enable;
        updateView.setVisibility(enable ? VISIBLE : GONE);
        if (mRefreshKernel != null) {
            mRefreshKernel.requestRemeasureHeightFor(this);
        }
        return this;
    }

    public ClassicsHeader setTextSizeTime(float size) {
        mLastUpdateText.setTextSize(size);
        if (mRefreshKernel != null) {
            mRefreshKernel.requestRemeasureHeightFor(this);
        }
        return this;
    }

    public ClassicsHeader setTextTimeMarginTop(float dp) {
        final View updateView = mLastUpdateText;
        MarginLayoutParams lp = (MarginLayoutParams) updateView.getLayoutParams();
        lp.topMargin = DimenUtils.dp2px(dp);
        updateView.setLayoutParams(lp);
        return this;
    }
}
