package com.zd112.framework.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.zd112.framework.R;
import com.zd112.framework.utils.DimenUtils;

import static android.graphics.Paint.Style.STROKE;

public class CusProgressbar extends View {
    private int mOutsideColor;    //进度的颜色
    private float mOutsideRadius;    //外圆半径大小
    private int mInsideColor;    //背景颜色
    private int mProgressTextColor;   //圆环内文字颜色
    private float mProgressTextSize;    //圆环内文字大小
    private float mProgressWidth;    //圆环的宽度
    private int mMaxProgress;    //最大进度
    private float mProgress;    //当前进度
    private int mDirection;    //进度从哪里开始(设置了4个值,上左下右)

    private Paint mPaint;
    private String mProgressText;     //圆环内文字
    private Rect mRect;

    enum DirectionEnum {
        LEFT(0, 180.0f),
        TOP(1, 270.0f),
        RIGHT(2, 0.0f),
        BOTTOM(3, 90.0f);

        private final int direction;
        private final float degree;

        DirectionEnum(int direction, float degree) {
            this.direction = direction;
            this.degree = degree;
        }

        public int getDirection() {
            return direction;
        }

        public float getDegree() {
            return degree;
        }

        public boolean equalsDescription(int direction) {
            return this.direction == direction;
        }

        public static DirectionEnum getDirection(int direction) {
            for (DirectionEnum enumObject : values()) {
                if (enumObject.equalsDescription(direction)) {
                    return enumObject;
                }
            }
            return RIGHT;
        }

        public static float getDegree(int direction) {
            DirectionEnum enumObject = getDirection(direction);
            if (enumObject == null) {
                return 0;
            }
            return enumObject.getDegree();
        }
    }

    public CusProgressbar(Context context) {
        this(context, null);
    }

    public CusProgressbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CusProgressbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomProgressBar, defStyleAttr, 0);
        mOutsideColor = a.getColor(R.styleable.CustomProgressBar_outside_color, ContextCompat.getColor(getContext(), R.color.colorPrimary));
        mOutsideRadius = a.getDimension(R.styleable.CustomProgressBar_outside_radius, DimenUtils.dp2px(getContext(), 60.0f));
        mInsideColor = a.getColor(R.styleable.CustomProgressBar_inside_color, ContextCompat.getColor(getContext(), R.color.inside_color));
        mProgressTextColor = a.getColor(R.styleable.CustomProgressBar_progress_text_color, ContextCompat.getColor(getContext(), R.color.colorPrimary));
        mProgressTextSize = a.getDimension(R.styleable.CustomProgressBar_progress_text_size, DimenUtils.dp2px(getContext(), 14.0f));
        mProgressWidth = a.getDimension(R.styleable.CustomProgressBar_progress_width, DimenUtils.dp2px(getContext(), 10.0f));
        mProgress = a.getFloat(R.styleable.CustomProgressBar_progress, 50.0f);
        mMaxProgress = a.getInt(R.styleable.CustomProgressBar_max_progress, 100);
        mDirection = a.getInt(R.styleable.CustomProgressBar_direction, 3);

        a.recycle();

        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int circlePoint = getWidth() / 2;
        //第一步:画背景(即内层圆)
        mPaint.setColor(mInsideColor); //设置圆的颜色
        mPaint.setStyle(STROKE); //设置空心
        mPaint.setStrokeWidth(mProgressWidth); //设置圆的宽度
        mPaint.setAntiAlias(true);  //消除锯齿
        canvas.drawCircle(circlePoint, circlePoint, mOutsideRadius, mPaint); //画出圆

        //第二步:画进度(圆弧)
        mPaint.setColor(mOutsideColor);  //设置进度的颜色
        RectF oval = new RectF(circlePoint - mOutsideRadius, circlePoint - mOutsideRadius, circlePoint + mOutsideRadius, circlePoint + mOutsideRadius);  //用于定义的圆弧的形状和大小的界限
        canvas.drawArc(oval, DirectionEnum.getDegree(mDirection), 360 * (mProgress / mMaxProgress), false, mPaint);  //根据进度画圆弧

        //第三步:画圆环内百分比文字
        mRect = new Rect();
        mPaint.setColor(mProgressTextColor);
        mPaint.setTextSize(mProgressTextSize);
        mPaint.setStrokeWidth(0);
        mProgressText = getProgressText();
        mPaint.getTextBounds(mProgressText, 0, mProgressText.length(), mRect);
        Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
        int baseline = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;  //获得文字的基准线
        canvas.drawText(mProgressText, getMeasuredWidth() / 2 - mRect.width() / 2, baseline, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width;
        int height;
        int size = MeasureSpec.getSize(widthMeasureSpec);
        int mode = MeasureSpec.getMode(widthMeasureSpec);

        if (mode == MeasureSpec.EXACTLY) {
            width = size;
        } else {
            width = (int) ((2 * mOutsideRadius) + mProgressWidth);
        }
        size = MeasureSpec.getSize(heightMeasureSpec);
        mode = MeasureSpec.getMode(heightMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            height = size;
        } else {
            height = (int) ((2 * mOutsideRadius) + mProgressWidth);
        }
        setMeasuredDimension(width, height);
    }

    //中间的进度百分比
    private String getProgressText() {
        return (int) ((mProgress / mMaxProgress) * 100) + "%";
    }

    public int getOutsideColor() {
        return mOutsideColor;
    }

    public void setOutsideColor(int outsideColor) {
        this.mOutsideColor = outsideColor;
    }

    public float getOutsideRadius() {
        return mOutsideRadius;
    }

    public void setOutsideRadius(float outsideRadius) {
        this.mOutsideRadius = outsideRadius;
    }

    public int getInsideColor() {
        return mInsideColor;
    }

    public void setInsideColor(int insideColor) {
        this.mInsideColor = insideColor;
    }

    public int getProgressTextColor() {
        return mProgressTextColor;
    }

    public void setProgressTextColor(int progressTextColor) {
        this.mProgressTextColor = progressTextColor;
    }

    public float getProgressTextSize() {
        return mProgressTextSize;
    }

    public void setProgressTextSize(float progressTextSize) {
        this.mProgressTextSize = progressTextSize;
    }

    public float getProgressWidth() {
        return mProgressWidth;
    }

    public void setProgressWidth(float progressWidth) {
        this.mProgressWidth = progressWidth;
    }

    public synchronized int getMaxProgress() {
        return mMaxProgress;
    }

    public synchronized void setMaxProgress(int maxProgress) {
        if (maxProgress < 0) {
            //此为传递非法参数异常
            throw new IllegalArgumentException("maxProgress should not be less than 0");
        }
        this.mMaxProgress = maxProgress;
    }

    public synchronized float getProgress() {
        return mProgress;
    }

    //加锁保证线程安全,能在线程中使用
    public synchronized void setProgress(int progress) {
        if (progress < 0) {
            throw new IllegalArgumentException("progress should not be less than 0");
        }
        if (progress > mMaxProgress) {
            progress = mMaxProgress;
        }
        startAnim(progress);
    }

    private void startAnim(float startProgress) {
        ValueAnimator animator = ObjectAnimator.ofFloat(0, startProgress);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                CusProgressbar.this.mProgress = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        animator.setStartDelay(500);
        animator.setDuration(2000);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
    }
}
