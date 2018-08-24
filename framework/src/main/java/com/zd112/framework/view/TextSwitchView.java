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
    private long mDefalutTime = 2000;
    private Rotate3dAnimation mInUp;
    private Rotate3dAnimation mOutUp;
    private String mTxt;
    private List<String> mTxtArr;
    public int mPosition;
    private boolean mIsScroll = false;
    private final int EVENT_WHAT = 1;

    private OnTextSwitchViewtListener mListener;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == EVENT_WHAT) {
                int size = mTxtArr.size() - 1;
                if (mPosition > size) {
                    mPosition = 0;
                }
                String txt = mTxtArr.get(mPosition);
                scrollTxt(txt);
                mPosition++;
            }
        }
    };

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == View.VISIBLE) {
            mIsScroll = true;
            if (mTxtArr != null && mTxtArr.size() > 0) {
                scrollTxt(mTxtArr);
            } else if (TextUtils.isEmpty(mTxt)) {
                scrollTxt(mTxt);
            } else {
                //
                mIsScroll = false;
            }
        } else {
            mIsScroll = false;
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
        this.mTxt = txt;
        this.setText(TextUtils.isEmpty(txt) ? "" : txt);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClick(TextSwitchView.this, mPosition);
                }
            }
        });
        if (mTxtArr != null && mTxtArr.size() > 0 && mIsScroll) {
            handler.sendEmptyMessageDelayed(EVENT_WHAT, mDefalutTime);
        }
        return this;
    }

    public TextSwitchView scrollTxt(List<String> arr) {
        this.mTxtArr = arr;
        if (mTxtArr != null) {
            mPosition = 0;
            handler.removeMessages(EVENT_WHAT);
            scrollTxt(mTxtArr.get(mPosition));
        }
        return this;
    }

    public TextSwitchView setListener(OnTextSwitchViewtListener listener) {
        this.mListener = listener;
        return this;
    }

    public TextSwitchView setScroll(boolean isScroll) {
        this.mIsScroll = isScroll;
        if (mTxtArr != null) {
            scrollTxt(mTxtArr);
        }
        return this;
    }

    public TextSwitchView setDelayedTime(long delayedTime) {
        this.mDefalutTime = delayedTime;
        invalidate();
        return this;
    }

    public interface OnTextSwitchViewtListener {
        void onClick(View view, int position);
    }
}
