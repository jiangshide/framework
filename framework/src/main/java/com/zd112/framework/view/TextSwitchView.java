package com.zd112.framework.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.zd112.framework.R;

import java.util.List;

public class TextSwitchView extends TextSwitcher implements ViewSwitcher.ViewFactory {
    private Context mContext;
    private long defalutTime = 2000;
    private Rotate3dAnimation mInUp;
    private Rotate3dAnimation mOutUp;
    private String txt;
    private List<String> txtArr;
    public int position;
    private boolean isScroll = false;
    private int EVENT_WHAT = 1;

    private OnTextSwitchViewtListener listener;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == EVENT_WHAT) {
                int size = txtArr.size() - 1;
                if (position > size) {
                    position = 0;
                }
                String txt = txtArr.get(position);
                scrollTxt(txt);
                position++;
            }
        }
    };

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == View.VISIBLE) {
            isScroll = true;
            if (txtArr != null && txtArr.size() > 0) {
                scrollTxt(txtArr);
            } else if (TextUtils.isEmpty(txt)) {
                scrollTxt(txt);
            } else {
                //
                isScroll = false;
            }
        } else {
            isScroll = false;
        }
    }

    public TextSwitchView(Context context) {
        this(context, null);
    }

    public TextSwitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();

    }

    private void init() {
        setFactory(this);
        setInAnimation(showTextAnimation());
        setOutAnimation(dismessTextAnimation());
    }

    private Rotate3dAnimation createAnim(boolean turnIn, boolean turnUp) {
        Rotate3dAnimation rotation = new Rotate3dAnimation(turnIn, turnUp);
        rotation.setDuration(1200);//执行动画的时间
        rotation.setFillAfter(false);//是否保持动画完毕之后的状态
        rotation.setInterpolator(new AccelerateInterpolator());//设置加速模式
        return rotation;
    }


    public View makeView() {
        TextView textView = new TextView(getContext());
        textView.setTextSize(12);
        textView.setSingleLine(true);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.font_grey));
        textView.setLineSpacing(20, 1);
        if (Build.MODEL.equals("YQ601") || Build.MODEL.equals("A0001")) {
            textView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 45));
//            textView.setPadding(0, 0, 0, 0);
        }
        return textView;

    }

    class Rotate3dAnimation extends Animation {
        private float mCenterX;
        private float mCenterY;
        private final boolean mTurnIn;
        private final boolean mTurnUp;
        private Camera mCamera;

        public Rotate3dAnimation(boolean turnIn, boolean turnUp) {
            mTurnIn = turnIn;
            mTurnUp = turnUp;
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            mCamera = new Camera();
            mCenterY = getHeight();
            mCenterX = getWidth();
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            final float centerX = mCenterX;
            final float centerY = mCenterY;
            final Camera camera = mCamera;
            final int derection = mTurnUp ? 1 : -1;
            final Matrix matrix = t.getMatrix();
            camera.save();
            if (mTurnIn) {
                camera.translate(0.0f, derection * mCenterY * (interpolatedTime - 1.0f), 0.0f);
            } else {
                camera.translate(0.0f, derection * mCenterY * (interpolatedTime), 0.0f);
            }
            camera.getMatrix(matrix);
            camera.restore();

            matrix.preTranslate(-centerX, -centerY);
            matrix.postTranslate(centerX, centerY);
        }
    }

    public AnimationSet showTextAnimation() {
        mInUp = createAnim(true, true);
        AnimationSet animationSet = new AnimationSet(true);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(mInUp);
        animationSet.setDuration(500);
        return animationSet;
    }

    public AnimationSet dismessTextAnimation() {
        mOutUp = createAnim(false, true);
        AnimationSet animationSet = new AnimationSet(true);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(mOutUp);
        animationSet.setDuration(500);
        return animationSet;
    }

    public TextSwitchView scrollTxt(String txt) {
        this.txt = txt;
        this.setText(TextUtils.isEmpty(txt) ? "" : txt);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(TextSwitchView.this, position);
                }
            }
        });
        if (txtArr != null && txtArr.size() > 0 && isScroll) {
            handler.sendEmptyMessageDelayed(EVENT_WHAT, defalutTime);
        }
        return this;
    }

    public TextSwitchView scrollTxt(List<String> arr) {
        this.txtArr = arr;
        if (txtArr != null) {
            position = 0;
            handler.removeMessages(EVENT_WHAT);
            scrollTxt(txtArr.get(position));
        }
        return this;
    }

    public TextSwitchView setListener(OnTextSwitchViewtListener listener) {
        this.listener = listener;
        return this;
    }

    public TextSwitchView setScroll(boolean isScroll) {
        this.isScroll = isScroll;
        if (txtArr != null) {
            scrollTxt(txtArr);
        }
        return this;
    }

    public TextSwitchView setDelayedTime(long delayedTime) {
        this.defalutTime = delayedTime;
        invalidate();
        return this;
    }

    public interface OnTextSwitchViewtListener {
        void onClick(View view, int position);
    }
}
