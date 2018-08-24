package com.zd112.framework.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zd112.framework.R;

public class NavigationTopView extends LinearLayout implements View.OnClickListener {

    public static final int LEFT_BTN_ID = 0x100000;
    public static final int RIGHT_BTN_ID = 0x200000;

    private int mBgColor;

    private String mLeftName;
    private int mLeftTextColor;
    private int mLeftTextSize;
    private int mLeftBg;
    private Drawable mLeftBgDraw;

    private String mTitleName;
    private int mTitleTextColor;
    private int mTitleTextSize;

    private String mRightName;
    private int mRightTextColor;
    private int mRightTextSize;
    private int mRightBg;

    private Button mLeftBtn, mRightBtn;
    private TextView mTitleView;

    private RelativeLayout mMenusLayout;
    private OnClickListener mOnLeftClickListener, mOnRightClickListener;

    public NavigationTopView(Context context) {
        this(context, null);
    }

    public NavigationTopView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NavigationTopView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.NavigationTopView, 0, 0);
        if (array != null) {
            mBgColor = array.getColor(R.styleable.NavigationTopView_bgColor, getResources().getColor(R.color.colorPrimaryDark));
            mLeftName = array.getString(R.styleable.NavigationTopView_leftName);
            mLeftTextColor = array.getColor(R.styleable.NavigationTopView_leftTextColor, getResources().getColor(R.color.white));
            mLeftTextSize = array.getInteger(R.styleable.NavigationTopView_leftTextSize, 15);
//            leftBg = array.getInteger(R.styleable.NavigationTopView_leftBg, 0);

            mTitleName = array.getString(R.styleable.NavigationTopView_titleName);
            mTitleTextColor = array.getInteger(R.styleable.NavigationTopView_titleTextColor, getResources().getColor(R.color.white));
            mTitleTextSize = array.getInteger(R.styleable.NavigationTopView_titleTextSize, 18);

            mRightName = array.getString(R.styleable.NavigationTopView_rightName);
            mRightTextColor = array.getColor(R.styleable.NavigationTopView_rightTextColor, getResources().getColor(R.color.white));
            mRightTextSize = array.getInteger(R.styleable.NavigationTopView_rightTextSize, 15);
//            rightBg = array.getInteger(R.styleable.NavigationTopView_rightBg, 0);
            array.recycle();
        }
        mMenusLayout = new RelativeLayout(getContext());
        mMenusLayout.setPadding(10, 5, 10, 5);
        init();
    }

    private void init() {
        setBackgroundColor(mBgColor);
        mLeftBtn = new Button(getContext());
        mLeftBtn.setTextColor(mLeftTextColor);
        mLeftBtn.setTextSize(mLeftTextSize);
        mLeftBtn.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        mLeftBtn.setId(LEFT_BTN_ID);
        mLeftBtn.setOnClickListener(this);
        if (TextUtils.isEmpty(mLeftName)) {
            mLeftBtn.setBackgroundResource(R.drawable.back);
            RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(80, 80);
            params2.addRule(RelativeLayout.CENTER_VERTICAL);
            mMenusLayout.addView(mLeftBtn, params2);
        } else {
            mLeftBtn.setBackgroundResource(R.drawable.alpha);
            mLeftBtn.setText(mLeftName);
            RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params2.addRule(RelativeLayout.CENTER_VERTICAL);
            mMenusLayout.addView(mLeftBtn, params2);
            mLeftBtn.setPadding(20, 0, 0, 0);
        }

        mTitleView = new TextView(getContext());
        mTitleView.setText(TextUtils.isEmpty(mTitleName) ? "" : mTitleName);
        mTitleView.setTextColor(mTitleTextColor);
        mTitleView.setTextSize(mTitleTextSize);
        mTitleView.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params1.addRule(RelativeLayout.CENTER_IN_PARENT);
        mMenusLayout.addView(mTitleView, params1);

        mRightBtn = new Button(getContext());
        mRightBtn.setText(TextUtils.isEmpty(mRightName) ? "" : mRightName);
        mRightBtn.setTextColor(mRightTextColor);
        mRightBtn.setTextSize(mRightTextSize);
        mRightBtn.setBackgroundResource(R.drawable.alpha);
        mRightBtn.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        mRightBtn.setId(RIGHT_BTN_ID);
        if (!TextUtils.isEmpty(mRightName)) {
            mRightBtn.setOnClickListener(this);
        }
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        mMenusLayout.addView(mRightBtn, params);
        mRightBtn.setPadding(0, 0, 20, 0);
        this.addView(mMenusLayout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    public NavigationTopView setLeftName(String name) {
        if (!TextUtils.isEmpty(name)) {
            mLeftBtn.setText(name);
            mLeftBtn.setOnClickListener(this);
        }
        return this;
    }

    public NavigationTopView setTitle(String name) {
        if (!TextUtils.isEmpty(name)) {
            mTitleView.setText(name);
        }
        return this;
    }

    public NavigationTopView setRightName(String name) {
        if (!TextUtils.isEmpty(name)) {
            mRightBtn.setText(name);
            mRightBtn.setOnClickListener(this);
        }
        return this;
    }

    public NavigationTopView setOnLeftClick(OnClickListener listener) {
        this.mOnLeftClickListener = listener;
        return this;
    }

    public NavigationTopView setOnRightClick(OnClickListener listener) {
        this.mOnRightClickListener = listener;
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case LEFT_BTN_ID:
                if (mOnLeftClickListener != null) {
                    mOnLeftClickListener.onClick(v);
                } else {
                    ((Activity) getContext()).finish();
                }
                break;
            case RIGHT_BTN_ID:
                if (mOnRightClickListener != null) {
                    mOnRightClickListener.onClick(v);
                } else {
                    ((Activity) getContext()).finish();
                }
                break;
        }
    }
}
