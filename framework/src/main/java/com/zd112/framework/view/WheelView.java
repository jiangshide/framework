package com.zd112.framework.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.zd112.framework.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class WheelView extends View {
    private static final int DEFAULT_TEXT_SIZE = (int) (Resources.getSystem().getDisplayMetrics().density * 15);

    private static final float DEFAULT_LINE_SPACE = 2f;

    private static final int DEFAULT_VISIBIE_ITEMS = 9;

    private float mScaleX = 1.05F;

    public enum ACTION {
        CLICK, FLING, DAGGLE
    }

    private Context mContext;

    private Handler mHandler;
    private GestureDetector mFlingGestureDetector;
    private WheelOnItemSelectedListener mWheelOnItemSelectedListener;

    // Timer mTimer;
    private ScheduledExecutorService mExecutor = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> mFuture;

    private Paint mPaintOuterText;
    private Paint mPaintCenterText;
    private Paint mPaintIndicator;

    private List<IndexString> mItems;

    private int mTextSize;
    private int mMaxTextHeight;

    private int mOuterTextColor;

    private int mCenterTextColor;
    private int mDividerColor;

    private float mLineSpacingMultiplier;
    private boolean mIsLoop;

    private int mFirstLineY;
    private int mSecondLineY;

    private int mTotalScrollY;
    private int mInitPosition;
    private int mSelectedItem;
    private int mPreCurrentIndex;
    private int mChange;

    private int mItemsVisibleCount;

    private HashMap<Integer, IndexString> mDrawingStrings;

    private int mMeasuredHeight;
    private int mMeasuredWidth;

    private int mHalfCircumference;
    private int mRadius;

    private int mOffset = 0;
    private float mPpreviousY;
    private long mStartTime = 0;

    private Rect mTempRect = new Rect();

    private int mPaddingLeft, mPaddingRight;

    /**
     * set text line space, must more than 1
     *
     * @param lineSpacingMultiplier
     */
    public void setLineSpacingMultiplier(float lineSpacingMultiplier) {
        if (lineSpacingMultiplier > 1.0f) {
            this.mLineSpacingMultiplier = lineSpacingMultiplier;
        }
    }

    /**
     * set outer text color
     *
     * @param centerTextColor
     */
    public void setCenterTextColor(int centerTextColor) {
        this.mCenterTextColor = centerTextColor;
        mPaintCenterText.setColor(centerTextColor);
    }

    /**
     * set center text color
     *
     * @param outerTextColor
     */
    public void setOuterTextColor(int outerTextColor) {
        this.mOuterTextColor = outerTextColor;
        mPaintOuterText.setColor(outerTextColor);
    }

    /**
     * set divider color
     *
     * @param dividerColor
     */
    public void setDividerColor(int dividerColor) {
        this.mDividerColor = dividerColor;
        mPaintIndicator.setColor(dividerColor);
    }

    public WheelView(Context context) {
        super(context);
        initView(context, null);
    }

    public WheelView(Context context, AttributeSet attributeset) {
        super(context, attributeset);
        initView(context, attributeset);
    }

    public WheelView(Context context, AttributeSet attributeset, int defStyleAttr) {
        super(context, attributeset, defStyleAttr);
        initView(context, attributeset);
    }

    private void initView(Context context, AttributeSet attributeset) {
        this.mContext = context;
        mHandler = new MessageHandler(this);
        mFlingGestureDetector = new GestureDetector(context, new MyGestureListener(this));
        mFlingGestureDetector.setIsLongpressEnabled(false);

        TypedArray typedArray = context.obtainStyledAttributes(attributeset, R.styleable.wheelView);
        mTextSize = typedArray.getInteger(R.styleable.wheelView_textSize, DEFAULT_TEXT_SIZE);
        mTextSize = (int) (Resources.getSystem().getDisplayMetrics().density * mTextSize);
        mLineSpacingMultiplier = typedArray.getFloat(R.styleable.wheelView_lineSpace, DEFAULT_LINE_SPACE);
        mCenterTextColor = typedArray.getInteger(R.styleable.wheelView_centerTextColor, 0xff313131);
        mOuterTextColor = typedArray.getInteger(R.styleable.wheelView_outerTextColor, 0xffafafaf);
        mDividerColor = typedArray.getInteger(R.styleable.wheelView_dividerTextColor, 0xffc5c5c5);
        mItemsVisibleCount =
                typedArray.getInteger(R.styleable.wheelView_itemsVisibleCount, DEFAULT_VISIBIE_ITEMS);
        if (mItemsVisibleCount % 2 == 0) {
            mItemsVisibleCount = DEFAULT_VISIBIE_ITEMS;
        }
        mIsLoop = typedArray.getBoolean(R.styleable.wheelView_isLoop, true);
        typedArray.recycle();

//        drawingStrings = new String[itemsVisibleCount];
        mDrawingStrings = new HashMap<>();
        mTotalScrollY = 0;
        mInitPosition = -1;

        initPaints();
    }

    /**
     * visible item count, must be odd number
     *
     * @param visibleNumber
     */
    public void setItemsVisibleCount(int visibleNumber) {
        if (visibleNumber % 2 == 0) {
            return;
        }
        if (visibleNumber != mItemsVisibleCount) {
            mItemsVisibleCount = visibleNumber;
//            drawingStrings = new String[itemsVisibleCount];
            mDrawingStrings = new HashMap<>();
        }
    }

    private void initPaints() {
        mPaintOuterText = new Paint();
        mPaintOuterText.setColor(mOuterTextColor);
        mPaintOuterText.setAntiAlias(true);
        mPaintOuterText.setTypeface(Typeface.MONOSPACE);
        mPaintOuterText.setTextSize(mTextSize);

        mPaintCenterText = new Paint();
        mPaintCenterText.setColor(mCenterTextColor);
        mPaintCenterText.setAntiAlias(true);
        mPaintCenterText.setTextScaleX(mScaleX);
        mPaintCenterText.setTypeface(Typeface.MONOSPACE);
        mPaintCenterText.setTextSize(mTextSize);

        mPaintIndicator = new Paint();
        mPaintIndicator.setColor(mDividerColor);
        mPaintIndicator.setAntiAlias(true);

    }

    private void remeasure() {
        if (mItems == null) {
            return;
        }

        mMeasuredWidth = getMeasuredWidth();

        mMeasuredHeight = getMeasuredHeight();

        if (mMeasuredWidth == 0 || mMeasuredHeight == 0) {
            return;
        }

        mPaddingLeft = getPaddingLeft();
        mPaddingRight = getPaddingRight();

        mMeasuredWidth = mMeasuredWidth - mPaddingRight;

        mPaintCenterText.getTextBounds("\u661F\u671F", 0, 2, mTempRect); // 星期
        mMaxTextHeight = mTempRect.height();
        mHalfCircumference = (int) (mMeasuredHeight * Math.PI / 2);

        mMaxTextHeight = (int) (mHalfCircumference / (mLineSpacingMultiplier * (mItemsVisibleCount - 1)));

        mRadius = mMeasuredHeight / 2;
        mFirstLineY = (int) ((mMeasuredHeight - mLineSpacingMultiplier * mMaxTextHeight) / 2.0F);
        mSecondLineY = (int) ((mMeasuredHeight + mLineSpacingMultiplier * mMaxTextHeight) / 2.0F);
        if (mInitPosition == -1) {
            if (mIsLoop) {
                mInitPosition = (mItems.size() + 1) / 2;
            } else {
                mInitPosition = 0;
            }
        }

        mPreCurrentIndex = mInitPosition;
    }

    void smoothScroll(ACTION action) {
        cancelFuture();
        if (action == ACTION.FLING || action == ACTION.DAGGLE) {
            float itemHeight = mLineSpacingMultiplier * mMaxTextHeight;
            mOffset = (int) ((mTotalScrollY % itemHeight + itemHeight) % itemHeight);
            if ((float) mOffset > itemHeight / 2.0F) {
                mOffset = (int) (itemHeight - (float) mOffset);
            } else {
                mOffset = -mOffset;
            }
        }
        mFuture =
                mExecutor.scheduleWithFixedDelay(new SmoothScrollTimerTask(this, mOffset), 0, 10, TimeUnit.MILLISECONDS);
    }

    protected final void scrollBy(float velocityY) {
        cancelFuture();
        // change this number, can change fling speed
        int velocityFling = 10;
        mFuture = mExecutor.scheduleWithFixedDelay(new InertiaTimerTask(this, velocityY), 0, velocityFling,
                TimeUnit.MILLISECONDS);
    }

    public void cancelFuture() {
        if (mFuture != null && !mFuture.isCancelled()) {
            mFuture.cancel(true);
            mFuture = null;
        }
    }

    /**
     * set not loop
     */
    public void setNotLoop() {
        mIsLoop = false;
    }

    /**
     * set text size in dp
     *
     * @param size
     */
    public final void setTextSize(float size) {
        if (size > 0.0F) {
            mTextSize = (int) (mContext.getResources().getDisplayMetrics().density * size);
            mPaintOuterText.setTextSize(mTextSize);
            mPaintCenterText.setTextSize(mTextSize);
        }
    }

    public final void setInitPosition(int initPosition) {
        if (initPosition < 0) {
            this.mInitPosition = 0;
        } else {
            if (mItems != null && mItems.size() > initPosition) {
                this.mInitPosition = initPosition;
            }
        }
    }

    public final void setListener(WheelOnItemSelectedListener wheelOnItemSelectedListener) {
        this.mWheelOnItemSelectedListener = wheelOnItemSelectedListener;
    }

    public final void setItems(List<String> items) {

        this.mItems = convertData(items);
        remeasure();
        invalidate();
    }

    public List<IndexString> convertData(List<String> items) {
        List<IndexString> data = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            data.add(new IndexString(i, items.get(i)));
        }
        return data;
    }

    public final int getSelectedItem() {
        return mSelectedItem;
    }
    //
    // protected final void scrollBy(float velocityY) {
    // Timer timer = new Timer();
    // mTimer = timer;
    // timer.schedule(new InertiaTimerTask(this, velocityY, timer), 0L, 20L);
    // }

    protected final void onItemSelected() {
        if (mWheelOnItemSelectedListener != null) {
            postDelayed(new WheelOnItemSelectedRunnable(this), 200L);
        }
    }

    /**
     * @param scaleX
     */
    public void setScaleX(float scaleX) {
        this.mScaleX = scaleX;
    }

    /**
     * set current item position
     *
     * @param position
     */
    public void setCurrentPosition(int position) {
        if (mItems == null || mItems.isEmpty()) {
            return;
        }
        int size = mItems.size();
        if (position >= 0 && position < size && position != mSelectedItem) {
            mInitPosition = position;
            mTotalScrollY = 0;
            mOffset = 0;
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mItems == null) {
            return;
        }

        mChange = (int) (mTotalScrollY / (mLineSpacingMultiplier * mMaxTextHeight));
        mPreCurrentIndex = mInitPosition + mChange % mItems.size();

        if (!mIsLoop) {
            if (mPreCurrentIndex < 0) {
                mPreCurrentIndex = 0;
            }
            if (mPreCurrentIndex > mItems.size() - 1) {
                mPreCurrentIndex = mItems.size() - 1;
            }
        } else {
            if (mPreCurrentIndex < 0) {
                mPreCurrentIndex = mItems.size() + mPreCurrentIndex;
            }
            if (mPreCurrentIndex > mItems.size() - 1) {
                mPreCurrentIndex = mPreCurrentIndex - mItems.size();
            }
        }

        int j2 = (int) (mTotalScrollY % (mLineSpacingMultiplier * mMaxTextHeight));
        // put value to drawingString
        int k1 = 0;
        while (k1 < mItemsVisibleCount) {
            int l1 = mPreCurrentIndex - (mItemsVisibleCount / 2 - k1);
            if (mIsLoop) {
                while (l1 < 0) {
                    l1 = l1 + mItems.size();
                }
                while (l1 > mItems.size() - 1) {
                    l1 = l1 - mItems.size();
                }
                mDrawingStrings.put(k1, mItems.get(l1));
            } else if (l1 < 0) {
//                drawingStrings[k1] = "";
                mDrawingStrings.put(k1, new IndexString());
            } else if (l1 > mItems.size() - 1) {
//                drawingStrings[k1] = "";
                mDrawingStrings.put(k1, new IndexString());
            } else {
                // drawingStrings[k1] = items.get(l1);
                mDrawingStrings.put(k1, mItems.get(l1));
            }
            k1++;
        }
        canvas.drawLine(mPaddingLeft, mFirstLineY, mMeasuredWidth, mFirstLineY, mPaintIndicator);
        canvas.drawLine(mPaddingLeft, mSecondLineY, mMeasuredWidth, mSecondLineY, mPaintIndicator);

        int i = 0;
        while (i < mItemsVisibleCount) {
            canvas.save();
            float itemHeight = mMaxTextHeight * mLineSpacingMultiplier;
            double radian = ((itemHeight * i - j2) * Math.PI) / mHalfCircumference;
            if (radian >= Math.PI || radian <= 0) {
                canvas.restore();
            } else {
                int translateY = (int) (mRadius - Math.cos(radian) * mRadius - (Math.sin(radian) * mMaxTextHeight) / 2D);
                canvas.translate(0.0F, translateY);
                canvas.scale(1.0F, (float) Math.sin(radian));
                if (translateY <= mFirstLineY && mMaxTextHeight + translateY >= mFirstLineY) {
                    // first divider
                    canvas.save();
                    canvas.clipRect(0, 0, mMeasuredWidth, mFirstLineY - translateY);
                    canvas.drawText(mDrawingStrings.get(i).string, getTextX(mDrawingStrings.get(i).string, mPaintOuterText, mTempRect),
                            mMaxTextHeight, mPaintOuterText);
                    canvas.restore();
                    canvas.save();
                    canvas.clipRect(0, mFirstLineY - translateY, mMeasuredWidth, (int) (itemHeight));
                    canvas.drawText(mDrawingStrings.get(i).string, getTextX(mDrawingStrings.get(i).string, mPaintCenterText, mTempRect),
                            mMaxTextHeight, mPaintCenterText);
                    canvas.restore();
                } else if (translateY <= mSecondLineY && mMaxTextHeight + translateY >= mSecondLineY) {
                    // second divider
                    canvas.save();
                    canvas.clipRect(0, 0, mMeasuredWidth, mSecondLineY - translateY);
                    canvas.drawText(mDrawingStrings.get(i).string, getTextX(mDrawingStrings.get(i).string, mPaintCenterText, mTempRect),
                            mMaxTextHeight, mPaintCenterText);
                    canvas.restore();
                    canvas.save();
                    canvas.clipRect(0, mSecondLineY - translateY, mMeasuredWidth, (int) (itemHeight));
                    canvas.drawText(mDrawingStrings.get(i).string, getTextX(mDrawingStrings.get(i).string, mPaintOuterText, mTempRect),
                            mMaxTextHeight, mPaintOuterText);
                    canvas.restore();
                } else if (translateY >= mFirstLineY && mMaxTextHeight + translateY <= mSecondLineY) {
                    // center item
                    canvas.clipRect(0, 0, mMeasuredWidth, (int) (itemHeight));
                    canvas.drawText(mDrawingStrings.get(i).string, getTextX(mDrawingStrings.get(i).string, mPaintCenterText, mTempRect),
                            mMaxTextHeight, mPaintCenterText);
                    mSelectedItem = mItems.indexOf(mDrawingStrings.get(i));
                } else {
                    // other item
                    canvas.clipRect(0, 0, mMeasuredWidth, (int) (itemHeight));
                    canvas.drawText(mDrawingStrings.get(i).string, getTextX(mDrawingStrings.get(i).string, mPaintOuterText, mTempRect),
                            mMaxTextHeight, mPaintOuterText);
                }
                canvas.restore();
            }
            i++;
        }
    }

    // text start drawing position
    private int getTextX(String a, Paint paint, Rect rect) {
        paint.getTextBounds(a, 0, a.length(), rect);
        int textWidth = rect.width();
        textWidth *= mScaleX;
        return (mMeasuredWidth - mPaddingLeft - textWidth) / 2 + mPaddingLeft;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        remeasure();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean eventConsumed = mFlingGestureDetector.onTouchEvent(event);
        float itemHeight = mLineSpacingMultiplier * mMaxTextHeight;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartTime = System.currentTimeMillis();
                cancelFuture();
                mPpreviousY = event.getRawY();
                if (getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;

            case MotionEvent.ACTION_MOVE:
                float dy = mPpreviousY - event.getRawY();
                mPpreviousY = event.getRawY();

                mTotalScrollY = (int) (mTotalScrollY + dy);

                if (!mIsLoop) {
                    float top = -mInitPosition * itemHeight;
                    float bottom = (mItems.size() - 1 - mInitPosition) * itemHeight;

                    if (mTotalScrollY < top) {
                        mTotalScrollY = (int) top;
                    } else if (mTotalScrollY > bottom) {
                        mTotalScrollY = (int) bottom;
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            default:
                if (!eventConsumed) {
                    float y = event.getY();
                    double l = Math.acos((mRadius - y) / mRadius) * mRadius;
                    int circlePosition = (int) ((l + itemHeight / 2) / itemHeight);

                    float extraOffset = (mTotalScrollY % itemHeight + itemHeight) % itemHeight;
                    mOffset = (int) ((circlePosition - mItemsVisibleCount / 2) * itemHeight - extraOffset);

                    if ((System.currentTimeMillis() - mStartTime) > 120) {
                        smoothScroll(ACTION.DAGGLE);
                    } else {
                        smoothScroll(ACTION.CLICK);
                    }
                }
                if (getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
        }

        invalidate();
        return true;
    }

    class IndexString {

        public IndexString() {
            this.string = "";
        }

        public IndexString(int index, String str) {
            this.index = index;
            this.string = str;
        }

        private String string;
        private int index;
    }

    class InertiaTimerTask implements Runnable {
        float a;
        final float velocityY;
        private WheelView wheelView;

        InertiaTimerTask(WheelView wheelView, float velocityY) {
            super();
            this.wheelView = wheelView;
            this.velocityY = velocityY;
            a = Integer.MAX_VALUE;
        }

        @Override
        public final void run() {
            if (a == Integer.MAX_VALUE) {
                if (Math.abs(velocityY) > 2000F) {
                    if (velocityY > 0.0F) {
                        a = 2000F;
                    } else {
                        a = -2000F;
                    }
                } else {
                    a = velocityY;
                }
            }
            if (Math.abs(a) >= 0.0F && Math.abs(a) <= 20F) {
                wheelView.cancelFuture();
                wheelView.mHandler.sendEmptyMessage(MessageHandler.WHAT_SMOOTH_SCROLL);
                return;
            }
            int i = (int) ((a * 10F) / 1000F);
            WheelView wheelView = this.wheelView;
            wheelView.mTotalScrollY = wheelView.mTotalScrollY - i;
            if (!wheelView.mIsLoop) {
                float itemHeight = wheelView.mLineSpacingMultiplier * wheelView.mMaxTextHeight;
                if (wheelView.mTotalScrollY <= (int) ((float) (-wheelView.mInitPosition) * itemHeight)) {
                    a = 40F;
                    wheelView.mTotalScrollY = (int) ((float) (-wheelView.mInitPosition) * itemHeight);
                } else if (wheelView.mTotalScrollY >= (int) ((float) (wheelView.mItems.size() - 1 - wheelView.mInitPosition) * itemHeight)) {
                    wheelView.mTotalScrollY = (int) ((float) (wheelView.mItems.size() - 1 - wheelView.mInitPosition) * itemHeight);
                    a = -40F;
                }
            }
            if (a < 0.0F) {
                a = a + 20F;
            } else {
                a = a - 20F;
            }
            wheelView.mHandler.sendEmptyMessage(MessageHandler.WHAT_INVALIDATE_LOOP_VIEW);
        }
    }

    class SmoothScrollTimerTask implements Runnable {
        int realTotalOffset;
        int realOffset;
        int offset;
        private WheelView wheelView;

        SmoothScrollTimerTask(WheelView wheelView, int offset) {
            this.wheelView = wheelView;
            this.offset = offset;
            realTotalOffset = Integer.MAX_VALUE;
            realOffset = 0;
        }

        @Override
        public final void run() {
            if (realTotalOffset == Integer.MAX_VALUE) {
                realTotalOffset = offset;
            }
            realOffset = (int) ((float) realTotalOffset * 0.1F);

            if (realOffset == 0) {
                if (realTotalOffset < 0) {
                    realOffset = -1;
                } else {
                    realOffset = 1;
                }
            }
            if (Math.abs(realTotalOffset) <= 0) {
                wheelView.cancelFuture();
                wheelView.mHandler.sendEmptyMessage(MessageHandler.WHAT_ITEM_SELECTED);
            } else {
                wheelView.mTotalScrollY = wheelView.mTotalScrollY + realOffset;
                wheelView.mHandler.sendEmptyMessage(MessageHandler.WHAT_INVALIDATE_LOOP_VIEW);
                realTotalOffset = realTotalOffset - realOffset;
            }
        }
    }

    class MessageHandler extends Handler {
        public static final int WHAT_INVALIDATE_LOOP_VIEW = 1000;
        public static final int WHAT_SMOOTH_SCROLL = 2000;
        public static final int WHAT_ITEM_SELECTED = 3000;

        private WheelView wheelView;

        MessageHandler(WheelView wheelView) {
            this.wheelView = wheelView;
        }

        @Override
        public final void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_INVALIDATE_LOOP_VIEW:
                    wheelView.invalidate();
                    break;

                case WHAT_SMOOTH_SCROLL:
                    wheelView.smoothScroll(ACTION.FLING);
                    break;

                case WHAT_ITEM_SELECTED:
                    wheelView.onItemSelected();
                    break;
            }
        }
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        private WheelView wheelView;

        MyGestureListener(WheelView wheelView) {
            this.wheelView = wheelView;
        }

        @Override
        public final boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            wheelView.scrollBy(velocityY);
            return true;
        }
    }

    class WheelOnItemSelectedRunnable implements Runnable {
        private WheelView wheelView;

        public WheelOnItemSelectedRunnable(WheelView wheelView) {
            this.wheelView = wheelView;
        }

        @Override
        public final void run() {
            wheelView.mWheelOnItemSelectedListener.onItemSelected(wheelView.getSelectedItem());
        }
    }

    public interface WheelOnItemSelectedListener {
        void onItemSelected(int index);
    }
}
