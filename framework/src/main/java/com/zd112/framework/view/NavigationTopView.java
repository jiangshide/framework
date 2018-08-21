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

    public static final int leftId = 0x100000;
    public static final int rightId = 0x200000;

    private int bgColor;

    private String leftName;
    private int leftTextColor;
    private int leftTextSize;
    private int leftBg;
    private Drawable leftBgDraw;

    private String titleName;
    private int titleTextColor;
    private int titleTextSize;

    private String rightName;
    private int rightTextColor;
    private int rightTextSize;
    private int rightBg;

    private Button leftBtn, rightBtn;
    private TextView titleView;

    private RelativeLayout menusLayout;
    private OnClickListener onLeftClickListener, onRightClickListener;

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
            bgColor = array.getColor(R.styleable.NavigationTopView_bgColor, getResources().getColor(R.color.colorPrimaryDark));
            leftName = array.getString(R.styleable.NavigationTopView_leftName);
            leftTextColor = array.getColor(R.styleable.NavigationTopView_leftTextColor, getResources().getColor(R.color.white));
            leftTextSize = array.getInteger(R.styleable.NavigationTopView_leftTextSize, 15);
//            leftBg = array.getInteger(R.styleable.NavigationTopView_leftBg, 0);

            titleName = array.getString(R.styleable.NavigationTopView_titleName);
            titleTextColor = array.getInteger(R.styleable.NavigationTopView_titleTextColor, getResources().getColor(R.color.white));
            titleTextSize = array.getInteger(R.styleable.NavigationTopView_titleTextSize, 18);

            rightName = array.getString(R.styleable.NavigationTopView_rightName);
            rightTextColor = array.getColor(R.styleable.NavigationTopView_rightTextColor, getResources().getColor(R.color.white));
            rightTextSize = array.getInteger(R.styleable.NavigationTopView_rightTextSize, 15);
//            rightBg = array.getInteger(R.styleable.NavigationTopView_rightBg, 0);
            array.recycle();
        }
        menusLayout = new RelativeLayout(getContext());
        menusLayout.setPadding(10, 5, 10, 5);
        init();
    }

    private void init() {
        setBackgroundColor(bgColor);
        leftBtn = new Button(getContext());
        leftBtn.setTextColor(leftTextColor);
        leftBtn.setTextSize(leftTextSize);
        leftBtn.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        leftBtn.setId(leftId);
        leftBtn.setOnClickListener(this);
        if (TextUtils.isEmpty(leftName)) {
            leftBtn.setBackgroundResource(R.drawable.back);
            RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(80, 80);
            params2.addRule(RelativeLayout.CENTER_VERTICAL);
            menusLayout.addView(leftBtn, params2);
        } else {
            leftBtn.setBackgroundResource(R.drawable.alpha);
            leftBtn.setText(leftName);
            RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params2.addRule(RelativeLayout.CENTER_VERTICAL);
            menusLayout.addView(leftBtn, params2);
            leftBtn.setPadding(20, 0, 0, 0);
        }

        titleView = new TextView(getContext());
        titleView.setText(TextUtils.isEmpty(titleName) ? "" : titleName);
        titleView.setTextColor(titleTextColor);
        titleView.setTextSize(titleTextSize);
        titleView.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params1.addRule(RelativeLayout.CENTER_IN_PARENT);
        menusLayout.addView(titleView, params1);

        rightBtn = new Button(getContext());
        rightBtn.setText(TextUtils.isEmpty(rightName) ? "" : rightName);
        rightBtn.setTextColor(rightTextColor);
        rightBtn.setTextSize(rightTextSize);
        rightBtn.setBackgroundResource(R.drawable.alpha);
        rightBtn.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        rightBtn.setId(rightId);
        if (!TextUtils.isEmpty(rightName)) {
            rightBtn.setOnClickListener(this);
        }
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        menusLayout.addView(rightBtn, params);
        rightBtn.setPadding(0, 0, 20, 0);
        this.addView(menusLayout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    public NavigationTopView setLeftName(String name) {
        if (!TextUtils.isEmpty(name)) {
            leftBtn.setText(name);
            leftBtn.setOnClickListener(this);
        }
        return this;
    }

    public NavigationTopView setTitle(String name) {
        if (!TextUtils.isEmpty(name)) {
            titleView.setText(name);
        }
        return this;
    }

    public NavigationTopView setRightName(String name) {
        if (!TextUtils.isEmpty(name)) {
            rightBtn.setText(name);
            rightBtn.setOnClickListener(this);
        }
        return this;
    }

    public NavigationTopView setOnLeftClick(OnClickListener listener) {
        this.onLeftClickListener = listener;
        return this;
    }

    public NavigationTopView setOnRightClick(OnClickListener listener) {
        this.onRightClickListener = listener;
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case leftId:
                if (onLeftClickListener != null) {
                    onLeftClickListener.onClick(v);
                } else {
                    ((Activity) getContext()).finish();
                }
                break;
            case rightId:
                if (onRightClickListener != null) {
                    onRightClickListener.onClick(v);
                } else {
                    ((Activity) getContext()).finish();
                }
                break;
        }
    }
}
