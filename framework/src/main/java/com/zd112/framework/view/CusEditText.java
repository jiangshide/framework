package com.zd112.framework.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.zd112.framework.R;
import com.zd112.framework.net.Net;
import com.zd112.framework.net.annotation.CacheType;
import com.zd112.framework.net.annotation.RequestType;
import com.zd112.framework.net.callback.Callback;
import com.zd112.framework.net.helper.NetInfo;

import java.util.HashMap;

public class CusEditText extends android.support.v7.widget.AppCompatEditText implements View.OnFocusChangeListener, TextWatcher {

    private Drawable mClearDrawable;
    private boolean hasFoucs;
    private int requestType;
    private int cacheType;
    private String action;
    private HashMap<String, String> params;
    private Callback callback;
    private int length;
    public final int MOBILE = 1, ID_CARD = 2, BRAND_CARD = 3;
    private int mStart, mEnd;
    private int format;

    public CusEditText(Context context) {
        this(context, null);
    }

    public CusEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public CusEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CusEditText, 0, 0);
        if (array != null) {
            format = array.getInteger(R.styleable.CusEditText_format, 0);
            mStart = array.getInteger(R.styleable.CusEditText_formatStart, 0);
            mEnd = array.getInteger(R.styleable.CusEditText_formatEnd, 0);
            array.recycle();
        }
        if (format > 0) {
            format(format);
        } else {
            format(mStart, mEnd);
        }
        init();
        setTransformationMethod(new AsteriskPasswordTransformationMethod());
    }

    public void format(int format) {
        format(format, 0, 0);
    }

    public void format(int start, int end) {
        format(0, start, end);
    }

    private void format(int format, int start, int end) {
        switch (format) {
            case MOBILE:
                mStart = 2;
                mEnd = 7;
                break;
            case ID_CARD:
                mStart = 3;
                mEnd = 11;
                break;
            case BRAND_CARD:
                mStart = 2;
                mEnd = 14;
                break;
            default:
                mStart = start;
                mEnd = end;
                break;
        }
    }

    public CusEditText setRequestParam(String action, HashMap<String, String> params, Callback callback) {
        setRequestParam(RequestType.GET, CacheType.FORCE_NETWORK, action, params, callback);
        return this;
    }

    public CusEditText setRequestParam(String action, int cacheType, HashMap<String, String> params, Callback callback) {
        setRequestParam(RequestType.GET, cacheType, action, params, callback);
        return this;
    }

    public CusEditText setRequestParam(int requestType, int cacheType, String action, HashMap<String, String> params, Callback callback) {
        this.requestType = requestType;
        this.cacheType = cacheType;
        this.action = action;
        this.params = params;
        this.callback = callback;
        return this;
    }

    public CusEditText setRequestSize(int length) {
        this.length = length;
        return this;
    }

    private void init() {
        // 获取EditText的DrawableRight,假如没有设置我们就使用默认的图片,获取图片的顺序是左上右下（0,1,2,3,）
        mClearDrawable = getCompoundDrawables()[2];
        if (mClearDrawable == null) {
            mClearDrawable = getResources().getDrawable(
                    R.drawable.input_delete);
        }

        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(),
                mClearDrawable.getIntrinsicHeight());
        // 默认设置隐藏图标
        setClearIconVisible(false);
        // 设置焦点改变的监听
        setOnFocusChangeListener(this);
        // 设置输入框里面内容发生改变的监听
        addTextChangedListener(this);
    }

    /* @说明：isInnerWidth, isInnerHeight为ture，触摸点在删除图标之内，则视为点击了删除图标
     * event.getX() 获取相对应自身左上角的X坐标
     * event.getY() 获取相对应自身左上角的Y坐标
     * getWidth() 获取控件的宽度
     * getHeight() 获取控件的高度
     * getTotalPaddingRight() 获取删除图标左边缘到控件右边缘的距离
     * getPaddingRight() 获取删除图标右边缘到控件右边缘的距离
     * isInnerWidth:
     * getWidth() - getTotalPaddingRight() 计算删除图标左边缘到控件左边缘的距离
     * getWidth() - getPaddingRight() 计算删除图标右边缘到控件左边缘的距离
     * isInnerHeight:
     * distance 删除图标顶部边缘到控件顶部边缘的距离
     * distance + height 删除图标底部边缘到控件顶部边缘的距离
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                Rect rect = getCompoundDrawables()[2].getBounds();
                int height = rect.height();
                int distance = (getHeight() - height) / 2;
                boolean isInnerWidth = x > (getWidth() - getTotalPaddingRight()) && x < (getWidth() - getPaddingRight());
                boolean isInnerHeight = y > distance && y < (distance + height);
                if (isInnerWidth && isInnerHeight) {
                    this.setText("");
                }
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 当ClearEditText焦点发生变化的时候，
     * 输入长度为零，隐藏删除图标，否则，显示删除图标
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        this.hasFoucs = hasFocus;
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }
    }

    protected void setClearIconVisible(boolean visible) {
        Drawable right = visible ? mClearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int count, int after) {
        if (hasFoucs) {
            setClearIconVisible(s.length() > 0);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (callback != null && s.length() > length) {
            Net.Builder().setCacheType(cacheType).build().doAsync(NetInfo.Builder().setRequestType(requestType).setAction(action).addParams(params).build(), callback);
        }
    }

    public class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new PasswordCharSequence(source);
        }

        private class PasswordCharSequence implements CharSequence {
            private CharSequence mSource;

            public PasswordCharSequence(CharSequence source) {
                this.mSource = source;
            }

            @Override
            public int length() {
                return mSource.length();
            }

            @Override
            public char charAt(int index) {
                return (index > mStart && index < mEnd) ? '*' : mSource.charAt(index);
            }

            @Override
            public CharSequence subSequence(int start, int end) {
                return mSource.subSequence(start, end);
            }
        }
    }
}
