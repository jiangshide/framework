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

    private float scaleX = 1.05F;

    public enum ACTION {
        CLICK, FLING, DAGGLE
    }

    private Context context;

    Handler handler;
    private GestureDetector flingGestureDetector;
    WheelOnItemSelectedListener wheelOnItemSelectedListener;

    // Timer mTimer;
    ScheduledExecutorService mExecutor = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> mFuture;

    private Paint paintOuterText;
    private Paint paintCenterText;
    private Paint paintIndicator;

    List<IndexString> items;

    int textSize;
    int maxTextHeight;

    int outerTextColor;

    int centerTextColor;
    int dividerColor;

    float lineSpacingMultiplier;
    boolean isLoop;

    int firstLineY;
    int secondLineY;

    int totalScrollY;
    int initPosition;
    private int selectedItem;
    int preCurrentIndex;
    int change;

    int itemsVisibleCount;

    HashMap<Integer, IndexString> drawingStrings;

    int measuredHeight;
    int measuredWidth;

    int halfCircumference;
    int radius;

    private int mOffset = 0;
    private float previousY;
    long startTime = 0;

    private Rect tempRect = new Rect();

    private int paddingLeft, paddingRight;

    /**
     * set text line space, must more than 1
     *
     * @param lineSpacingMultiplier
     */
    public void setLineSpacingMultiplier(float lineSpacingMultiplier) {
        if (lineSpacingMultiplier > 1.0f) {
            this.lineSpacingMultiplier = lineSpacingMultiplier;
        }
    }

    /**
     * set outer text color
     *
     * @param centerTextColor
     */
    public void setCenterTextColor(int centerTextColor) {
        this.centerTextColor = centerTextColor;
        paintCenterText.setColor(centerTextColor);
    }

    /**
     * set center text color
     *
     * @param outerTextColor
     */
    public void setOuterTextColor(int outerTextColor) {
        this.outerTextColor = outerTextColor;
        paintOuterText.setColor(outerTextColor);
    }

    /**
     * set divider color
     *
     * @param dividerColor
     */
    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
        paintIndicator.setColor(dividerColor);
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
        this.context = context;
        handler = new MessageHandler(this);
        flingGestureDetector = new GestureDetector(context, new MyGestureListener(this));
        flingGestureDetector.setIsLongpressEnabled(false);

        TypedArray typedArray = context.obtainStyledAttributes(attributeset, R.styleable.wheelView);
        textSize = typedArray.getInteger(R.styleable.wheelView_textSize, DEFAULT_TEXT_SIZE);
        textSize = (int) (Resources.getSystem().getDisplayMetrics().density * textSize);
        lineSpacingMultiplier = typedArray.getFloat(R.styleable.wheelView_lineSpace, DEFAULT_LINE_SPACE);
        centerTextColor = typedArray.getInteger(R.styleable.wheelView_centerTextColor, 0xff313131);
        outerTextColor = typedArray.getInteger(R.styleable.wheelView_outerTextColor, 0xffafafaf);
        dividerColor = typedArray.getInteger(R.styleable.wheelView_dividerTextColor, 0xffc5c5c5);
        itemsVisibleCount =
                typedArray.getInteger(R.styleable.wheelView_itemsVisibleCount, DEFAULT_VISIBIE_ITEMS);
        if (itemsVisibleCount % 2 == 0) {
            itemsVisibleCount = DEFAULT_VISIBIE_ITEMS;
        }
        isLoop = typedArray.getBoolean(R.styleable.wheelView_isLoop, true);
        typedArray.recycle();

//        drawingStrings = new String[itemsVisibleCount];
        drawingStrings = new HashMap<>();
        totalScrollY = 0;
        initPosition = -1;

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
        if (visibleNumber != itemsVisibleCount) {
            itemsVisibleCount = visibleNumber;
//            drawingStrings = new String[itemsVisibleCount];
            drawingStrings = new HashMap<>();
        }
    }

    private void initPaints() {
        paintOuterText = new Paint();
        paintOuterText.setColor(outerTextColor);
        paintOuterText.setAntiAlias(true);
        paintOuterText.setTypeface(Typeface.MONOSPACE);
        paintOuterText.setTextSize(textSize);

        paintCenterText = new Paint();
        paintCenterText.setColor(centerTextColor);
        paintCenterText.setAntiAlias(true);
        paintCenterText.setTextScaleX(scaleX);
        paintCenterText.setTypeface(Typeface.MONOSPACE);
        paintCenterText.setTextSize(textSize);

        paintIndicator = new Paint();
        paintIndicator.setColor(dividerColor);
        paintIndicator.setAntiAlias(true);

    }

    private void remeasure() {
        if (items == null) {
            return;
        }

        measuredWidth = getMeasuredWidth();

        measuredHeight = getMeasuredHeight();

        if (measuredWidth == 0 || measuredHeight == 0) {
            return;
        }

        paddingLeft = getPaddingLeft();
        paddingRight = getPaddingRight();

        measuredWidth = measuredWidth - paddingRight;

        paintCenterText.getTextBounds("\u661F\u671F", 0, 2, tempRect); // 星期
        maxTextHeight = tempRect.height();
        halfCircumference = (int) (measuredHeight * Math.PI / 2);

        maxTextHeight = (int) (halfCircumference / (lineSpacingMultiplier * (itemsVisibleCount - 1)));

        radius = measuredHeight / 2;
        firstLineY = (int) ((measuredHeight - lineSpacingMultiplier * maxTextHeight) / 2.0F);
        secondLineY = (int) ((measuredHeight + lineSpacingMultiplier * maxTextHeight) / 2.0F);
        if (initPosition == -1) {
            if (isLoop) {
                initPosition = (items.size() + 1) / 2;
            } else {
                initPosition = 0;
            }
        }

        preCurrentIndex = initPosition;
    }

    void smoothScroll(ACTION action) {
        cancelFuture();
        if (action == ACTION.FLING || action == ACTION.DAGGLE) {
            float itemHeight = lineSpacingMultiplier * maxTextHeight;
            mOffset = (int) ((totalScrollY % itemHeight + itemHeight) % itemHeight);
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
        isLoop = false;
    }

    /**
     * set text size in dp
     *
     * @param size
     */
    public final void setTextSize(float size) {
        if (size > 0.0F) {
            textSize = (int) (context.getResources().getDisplayMetrics().density * size);
            paintOuterText.setTextSize(textSize);
            paintCenterText.setTextSize(textSize);
        }
    }

    public final void setInitPosition(int initPosition) {
        if (initPosition < 0) {
            this.initPosition = 0;
        } else {
            if (items != null && items.size() > initPosition) {
                this.initPosition = initPosition;
            }
        }
    }

    public final void setListener(WheelOnItemSelectedListener wheelOnItemSelectedListener) {
        this.wheelOnItemSelectedListener = wheelOnItemSelectedListener;
    }

    public final void setItems(List<String> items) {

        this.items = convertData(items);
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
        return selectedItem;
    }
    //
    // protected final void scrollBy(float velocityY) {
    // Timer timer = new Timer();
    // mTimer = timer;
    // timer.schedule(new InertiaTimerTask(this, velocityY, timer), 0L, 20L);
    // }

    protected final void onItemSelected() {
        if (wheelOnItemSelectedListener != null) {
            postDelayed(new WheelOnItemSelectedRunnable(this), 200L);
        }
    }

    /**
     * @param scaleX
     */
    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
    }

    /**
     * set current item position
     *
     * @param position
     */
    public void setCurrentPosition(int position) {
        if (items == null || items.isEmpty()) {
            return;
        }
        int size = items.size();
        if (position >= 0 && position < size && position != selectedItem) {
            initPosition = position;
            totalScrollY = 0;
            mOffset = 0;
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (items == null) {
            return;
        }

        change = (int) (totalScrollY / (lineSpacingMultiplier * maxTextHeight));
        preCurrentIndex = initPosition + change % items.size();

        if (!isLoop) {
            if (preCurrentIndex < 0) {
                preCurrentIndex = 0;
            }
            if (preCurrentIndex > items.size() - 1) {
                preCurrentIndex = items.size() - 1;
            }
        } else {
            if (preCurrentIndex < 0) {
                preCurrentIndex = items.size() + preCurrentIndex;
            }
            if (preCurrentIndex > items.size() - 1) {
                preCurrentIndex = preCurrentIndex - items.size();
            }
        }

        int j2 = (int) (totalScrollY % (lineSpacingMultiplier * maxTextHeight));
        // put value to drawingString
        int k1 = 0;
        while (k1 < itemsVisibleCount) {
            int l1 = preCurrentIndex - (itemsVisibleCount / 2 - k1);
            if (isLoop) {
                while (l1 < 0) {
                    l1 = l1 + items.size();
                }
                while (l1 > items.size() - 1) {
                    l1 = l1 - items.size();
                }
                drawingStrings.put(k1, items.get(l1));
            } else if (l1 < 0) {
//                drawingStrings[k1] = "";
                drawingStrings.put(k1, new IndexString());
            } else if (l1 > items.size() - 1) {
//                drawingStrings[k1] = "";
                drawingStrings.put(k1, new IndexString());
            } else {
                // drawingStrings[k1] = items.get(l1);
                drawingStrings.put(k1, items.get(l1));
            }
            k1++;
        }
        canvas.drawLine(paddingLeft, firstLineY, measuredWidth, firstLineY, paintIndicator);
        canvas.drawLine(paddingLeft, secondLineY, measuredWidth, secondLineY, paintIndicator);

        int i = 0;
        while (i < itemsVisibleCount) {
            canvas.save();
            float itemHeight = maxTextHeight * lineSpacingMultiplier;
            double radian = ((itemHeight * i - j2) * Math.PI) / halfCircumference;
            if (radian >= Math.PI || radian <= 0) {
                canvas.restore();
            } else {
                int translateY = (int) (radius - Math.cos(radian) * radius - (Math.sin(radian) * maxTextHeight) / 2D);
                canvas.translate(0.0F, translateY);
                canvas.scale(1.0F, (float) Math.sin(radian));
                if (translateY <= firstLineY && maxTextHeight + translateY >= firstLineY) {
                    // first divider
                    canvas.save();
                    canvas.clipRect(0, 0, measuredWidth, firstLineY - translateY);
                    canvas.drawText(drawingStrings.get(i).string, getTextX(drawingStrings.get(i).string, paintOuterText, tempRect),
                            maxTextHeight, paintOuterText);
                    canvas.restore();
                    canvas.save();
                    canvas.clipRect(0, firstLineY - translateY, measuredWidth, (int) (itemHeight));
                    canvas.drawText(drawingStrings.get(i).string, getTextX(drawingStrings.get(i).string, paintCenterText, tempRect),
                            maxTextHeight, paintCenterText);
                    canvas.restore();
                } else if (translateY <= secondLineY && maxTextHeight + translateY >= secondLineY) {
                    // second divider
                    canvas.save();
                    canvas.clipRect(0, 0, measuredWidth, secondLineY - translateY);
                    canvas.drawText(drawingStrings.get(i).string, getTextX(drawingStrings.get(i).string, paintCenterText, tempRect),
                            maxTextHeight, paintCenterText);
                    canvas.restore();
                    canvas.save();
                    canvas.clipRect(0, secondLineY - translateY, measuredWidth, (int) (itemHeight));
                    canvas.drawText(drawingStrings.get(i).string, getTextX(drawingStrings.get(i).string, paintOuterText, tempRect),
                            maxTextHeight, paintOuterText);
                    canvas.restore();
                } else if (translateY >= firstLineY && maxTextHeight + translateY <= secondLineY) {
                    // center item
                    canvas.clipRect(0, 0, measuredWidth, (int) (itemHeight));
                    canvas.drawText(drawingStrings.get(i).string, getTextX(drawingStrings.get(i).string, paintCenterText, tempRect),
                            maxTextHeight, paintCenterText);
                    selectedItem = items.indexOf(drawingStrings.get(i));
                } else {
                    // other item
                    canvas.clipRect(0, 0, measuredWidth, (int) (itemHeight));
                    canvas.drawText(drawingStrings.get(i).string, getTextX(drawingStrings.get(i).string, paintOuterText, tempRect),
                            maxTextHeight, paintOuterText);
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
        textWidth *= scaleX;
        return (measuredWidth - paddingLeft - textWidth) / 2 + paddingLeft;
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
        boolean eventConsumed = flingGestureDetector.onTouchEvent(event);
        float itemHeight = lineSpacingMultiplier * maxTextHeight;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startTime = System.currentTimeMillis();
                cancelFuture();
                previousY = event.getRawY();
                if (getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;

            case MotionEvent.ACTION_MOVE:
                float dy = previousY - event.getRawY();
                previousY = event.getRawY();

                totalScrollY = (int) (totalScrollY + dy);

                if (!isLoop) {
                    float top = -initPosition * itemHeight;
                    float bottom = (items.size() - 1 - initPosition) * itemHeight;

                    if (totalScrollY < top) {
                        totalScrollY = (int) top;
                    } else if (totalScrollY > bottom) {
                        totalScrollY = (int) bottom;
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            default:
                if (!eventConsumed) {
                    float y = event.getY();
                    double l = Math.acos((radius - y) / radius) * radius;
                    int circlePosition = (int) ((l + itemHeight / 2) / itemHeight);

                    float extraOffset = (totalScrollY % itemHeight + itemHeight) % itemHeight;
                    mOffset = (int) ((circlePosition - itemsVisibleCount / 2) * itemHeight - extraOffset);

                    if ((System.currentTimeMillis() - startTime) > 120) {
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
                wheelView.handler.sendEmptyMessage(MessageHandler.WHAT_SMOOTH_SCROLL);
                return;
            }
            int i = (int) ((a * 10F) / 1000F);
            WheelView wheelView = this.wheelView;
            wheelView.totalScrollY = wheelView.totalScrollY - i;
            if (!wheelView.isLoop) {
                float itemHeight = wheelView.lineSpacingMultiplier * wheelView.maxTextHeight;
                if (wheelView.totalScrollY <= (int) ((float) (-wheelView.initPosition) * itemHeight)) {
                    a = 40F;
                    wheelView.totalScrollY = (int) ((float) (-wheelView.initPosition) * itemHeight);
                } else if (wheelView.totalScrollY >= (int) ((float) (wheelView.items.size() - 1 - wheelView.initPosition) * itemHeight)) {
                    wheelView.totalScrollY = (int) ((float) (wheelView.items.size() - 1 - wheelView.initPosition) * itemHeight);
                    a = -40F;
                }
            }
            if (a < 0.0F) {
                a = a + 20F;
            } else {
                a = a - 20F;
            }
            wheelView.handler.sendEmptyMessage(MessageHandler.WHAT_INVALIDATE_LOOP_VIEW);
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
                wheelView.handler.sendEmptyMessage(MessageHandler.WHAT_ITEM_SELECTED);
            } else {
                wheelView.totalScrollY = wheelView.totalScrollY + realOffset;
                wheelView.handler.sendEmptyMessage(MessageHandler.WHAT_INVALIDATE_LOOP_VIEW);
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
            wheelView.wheelOnItemSelectedListener.onItemSelected(wheelView.getSelectedItem());
        }
    }

    public interface WheelOnItemSelectedListener {
        void onItemSelected(int index);
    }
}
