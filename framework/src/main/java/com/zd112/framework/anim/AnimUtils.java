package com.zd112.framework.anim;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author jiangshide
 * @Created by Ender on 2018/9/4.
 * @Emal:18311271399@163.com
 */
public class AnimUtils {

    public static void animObj2(View view) {
        @SuppressLint("ObjectAnimatorBinding") ObjectAnimator animator = ObjectAnimator.ofFloat(view, "fraction", 0, 1);
        animator.setDuration(2000);
        animator.start();
    }

    public static void animObj1(ImageView imageView) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "rotationY", 0, 359);
        animator.setDuration(1000);
        animator.setRepeatCount(1);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.start();
    }

    public static void Anim(final ImageView view) {
        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 360);
        valueAnimator.setDuration(1000);
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.setRepeatCount(1);
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) valueAnimator.getAnimatedValue();
                view.setRotationY(value);

            }
        });
        valueAnimator.start();
    }

    public static void anim2(final TextView view) {
        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 2000);
        valueAnimator.setDuration(10000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) valueAnimator.getAnimatedValue();
                view.setText(String.valueOf(value));
            }
        });
        valueAnimator.start();
    }
}
