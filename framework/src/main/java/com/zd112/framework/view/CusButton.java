package com.zd112.framework.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;

import com.zd112.framework.R;

public class CusButton extends AppCompatButton {
    //按下颜色
    private int mPressedColor;
    //当前颜色
    private int mNormalColor;
    //当前圆角
    private float mCurrCorner;
    private float mLeftCorner, mTopCorner, mRightCorner, mBottomCorner;
    // 四边宽度
    private float mStrokeWidth;
    // 颜色
    private int mStrokeColor;

    GradientDrawable mGradientDrawable;

    //按钮类型
    private int mType;

    boolean mIsTouchPass = true;

    public CusButton(Context context) {
        this(context, null);
    }

    public CusButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CusButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
        init();
    }

    private void init() {
        setGravity(Gravity.CENTER);
        mGradientDrawable = new GradientDrawable();
        //说明配置了快速按钮选项
        if (mType != 0) {
            setTextColor(Color.WHITE);
            switch (mType) {
                case 1:
                    mNormalColor = Color.parseColor("#5CB85C");
                    mPressedColor = Color.parseColor("#449D44");
                    break;
                case 2:
                    mNormalColor = Color.parseColor("#5BC0DE");
                    mPressedColor = Color.parseColor("#31B0D5");
                    break;
                case 3:
                    mNormalColor = Color.parseColor("#F0AD4E");
                    mPressedColor = Color.parseColor("#EC971F");
                    break;
                case 4:
                    mNormalColor = Color.parseColor("#D9534F");
                    mPressedColor = Color.parseColor("#C9302C");
                    break;
            }
        }

        mGradientDrawable.setColor(mNormalColor);
        mGradientDrawable.setStroke((int) mStrokeWidth, mStrokeColor);
        if (mLeftCorner > 0 || mTopCorner > 0 || mRightCorner > 0 || mBottomCorner > 0) {
            mGradientDrawable.setCornerRadii(new float[]{mLeftCorner, mLeftCorner, mTopCorner, mTopCorner, mRightCorner, mRightCorner, mBottomCorner, mBottomCorner});
        } else {
            mGradientDrawable.setCornerRadius(mCurrCorner);
        }
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent event) {
                setBackgroundDrawable(mGradientDrawable);
                return setColor(event.getAction());
            }
        });
        setBackgroundDrawable(mGradientDrawable);
    }

    private void initAttrs(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ButtonStyle);
            int color = getResources().getColor(R.color.colorPrimary);
            mNormalColor = typedArray.getColor(R.styleable.ButtonStyle_normal_color, color);
            mStrokeWidth = typedArray.getDimension(R.styleable.ButtonStyle_stroke, 0);
            mStrokeColor = typedArray.getColor(R.styleable.ButtonStyle_stroke_color, Color.TRANSPARENT);
            mPressedColor = typedArray.getColor(R.styleable.ButtonStyle_press_color, getResources().getColor(R.color.colorPrimaryDark));
            mCurrCorner = typedArray.getDimension(R.styleable.ButtonStyle_corner, getResources().getDimension(R.dimen.default_con));
            mLeftCorner = typedArray.getDimension(R.styleable.ButtonStyle_leftCorner, 0);
            mTopCorner = typedArray.getDimension(R.styleable.ButtonStyle_topCorner, 0);
            mRightCorner = typedArray.getDimension(R.styleable.ButtonStyle_rightCorner, 0);
            mBottomCorner = typedArray.getDimension(R.styleable.ButtonStyle_bottomCorner, 0);
            mType = typedArray.getInt(R.styleable.ButtonStyle_type, 0);
            typedArray.recycle();
        }
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        super.setOnClickListener(l);
        mIsTouchPass = false;
    }

    public boolean setColor(int action) {
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mGradientDrawable.setColor(mPressedColor);
                break;
            case MotionEvent.ACTION_UP:
                mGradientDrawable.setColor(mNormalColor);
                break;
            case MotionEvent.ACTION_CANCEL:
                mGradientDrawable.setColor(mNormalColor);
                break;
        }

        return mIsTouchPass;
    }

    /**
     * @return 获取按下颜色
     */
    public int getPressedColor() {
        return mPressedColor;
    }

    /**
     * @param pressedColor 按下颜色设置
     */
    public CusButton setPressedColor(int pressedColor) {
        this.mPressedColor = getResources().getColor(pressedColor);
        return this;
    }

    /**
     * @param pressedColor 设置按下颜色 例如：#ffffff
     */
    public CusButton setPressedColor(String pressedColor) {
        this.mPressedColor = Color.parseColor(pressedColor);
        return this;
    }

    /**
     * @return 获取默认颜色
     */
    public int getNormalColor() {
        return mNormalColor;
    }

    /**
     * @param normalColor 设置默认颜色
     */
    public CusButton setNormalColor(int normalColor) {
        this.mNormalColor = getResources().getColor(normalColor);
        if (mGradientDrawable != null)
            mGradientDrawable.setColor(this.mNormalColor);
        return this;
    }

    /**
     * @param normalColor 设置默认颜色 例如：#ffffff
     */
    public CusButton setNormalColor(String normalColor) {
        this.mNormalColor = Color.parseColor(normalColor);
        if (mGradientDrawable != null)
            mGradientDrawable.setColor(this.mNormalColor);
        return this;
    }

    /**
     * @return 返回当前圆角大小
     */
    public float getCurrCorner() {
        return mCurrCorner;
    }

    /**
     * @param currCorner 设置当前圆角
     */
    public CusButton setCurrCorner(float currCorner) {
        this.mCurrCorner = currCorner;
        if (mGradientDrawable != null)
            mGradientDrawable.setCornerRadius(currCorner);
        return this;
    }

    /**
     * @param currCorner 圆角设置,必须为四个参数:左上,右上,左下,右下
     * @return
     */
    public CusButton setCurrCorner(float... currCorner) {
        if (currCorner != null && currCorner.length == 4) {
            this.mLeftCorner = currCorner[0];
            this.mTopCorner = currCorner[1];
            this.mRightCorner = currCorner[2];
            this.mBottomCorner = currCorner[3];
            init();
        }
        return this;
    }


    /**
     * @return 返回边框大小
     */
    public float getStrokeWidth() {
        return mStrokeWidth;
    }

    /**
     * @param strokeWidth 设置边框大小
     */
    public CusButton setStrokeWidth(float strokeWidth) {
        this.mStrokeWidth = strokeWidth;
        if (mGradientDrawable != null)
            mGradientDrawable.setStroke((int) strokeWidth, this.mStrokeColor);
        return this;
    }

    /**
     * @return 返回边框颜色
     */
    public int getStrokeColor() {
        return mStrokeColor;
    }

    /**
     * @param strokeColor 设置边框颜色
     */
    public CusButton setStrokeColor(int strokeColor) {
        this.mStrokeColor = getResources().getColor(strokeColor);
        if (mGradientDrawable != null)
            mGradientDrawable.setStroke((int) mStrokeWidth, this.mStrokeColor);
        return this;
    }

    /**
     * @param strokeColor 设置边框颜色 例如：#ffffff
     */
    public CusButton setStrokeColor(String strokeColor) {
        this.mStrokeColor = Color.parseColor(strokeColor);
        if (mGradientDrawable != null)
            mGradientDrawable.setStroke((int) mStrokeWidth, this.mStrokeColor);
        return this;
    }
}
