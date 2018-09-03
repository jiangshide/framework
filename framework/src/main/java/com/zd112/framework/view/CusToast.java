package com.zd112.framework.view;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.IntDef;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zd112.framework.BaseApplication;
import com.zd112.framework.R;

/**
 * @author jiangshide
 * @Created by Ender on 2018/9/3.
 * @Emal:18311271399@163.com
 */
public class CusToast extends Toast {

    private static DialogView mDialogView;
    private static Handler mHandler = new Handler();
    private static final int DEFAULT_DURATION = 2;

    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context The context to use.  Usually your {@link Application}
     *                or {@link Activity} object.
     */
    public CusToast(Context context) {
        super(context);
    }

    @IntDef({Toast.LENGTH_SHORT, Toast.LENGTH_LONG})
    public @interface Duration {
    }

    /**
     * Toast显示
     *
     * @param text 显示内容
     */
    public static void txt(String text) {
        txt(text, CusToast.LENGTH_SHORT);
    }

    /**
     * Toast显示
     *
     * @param resId 显示内容资源
     */
    public static void txt(int resId) {
        txt(resId, CusToast.LENGTH_SHORT);
    }

    /**
     * Toast显示:可以控制显示时间
     *
     * @param resId    显示内容资源
     * @param duration 显示时间
     */
    public static void txt(int resId, int duration) {
        txt(BaseApplication.mApplication.getString(resId), duration);
    }


    /**
     * Toast显示:可以控制显示时间
     *
     * @param text     显示内容
     * @param duration 显示时间
     */
    public static void txt(String text, @CusToast.Duration Integer duration) {
        makeText(BaseApplication.mApplication, text, duration).show();
    }

    /**
     * Toast显示:可以显示图片等控件
     *
     * @param layout 图片View
     */
    public static void view(int layout) {
        view(LayoutInflater.from(BaseApplication.mApplication).inflate(layout, null));
    }

    /**
     * Toast显示:可以显示图片等控件
     *
     * @param view 图片View
     */
    public static void view(View view) {
        view(Gravity.CENTER, 10, 10, CusToast.LENGTH_LONG, view);
    }

    /**
     * Toast显示:可以显示图片等控件
     *
     * @param layout 图片View
     */
    public static void view(int gravity, int layout) {
        view(gravity, LayoutInflater.from(BaseApplication.mApplication).inflate(layout, null));
    }

    /**
     * Toast显示:可以显示图片等控件
     *
     * @param view 图片View
     */
    public static void view(int gravity, View view) {
        view(gravity, 10, 10, CusToast.LENGTH_LONG, view);
    }

    /**
     * Toast显示:可以显示图片等控件
     *
     * @param layout 图片View
     */
    public static void view(int gravity, @CusToast.Duration Integer duration, int layout) {
        view(gravity, duration, LayoutInflater.from(BaseApplication.mApplication).inflate(layout, null));
    }

    /**
     * Toast显示:可以显示图片等控件
     *
     * @param view 图片View
     */
    public static void view(int gravity, @CusToast.Duration Integer duration, View view) {
        view(gravity, 10, 10, duration, view);
    }

    /**
     * Toast显示:可以显示图片等控件
     *
     * @param layout 图片View
     */
    public static void view(int gravity, int xOffset, int yOffset, int layout) {
        view(gravity, xOffset, yOffset, LayoutInflater.from(BaseApplication.mApplication).inflate(layout, null));
    }

    /**
     * Toast显示:可以显示图片等控件
     *
     * @param view 图片View
     */
    public static void view(int gravity, int xOffset, int yOffset, View view) {
        view(gravity, xOffset, yOffset, CusToast.LENGTH_LONG, view);
    }

    /**
     * Toast显示:可以显示图片等控件、控制显示时间
     *
     * @param duration 显示时间
     * @param layout   图片View
     */
    public static void view(int gravity, int xOffset, int yOffset, @CusToast.Duration Integer duration, int layout) {
        view(gravity, xOffset, yOffset, duration, LayoutInflater.from(BaseApplication.mApplication).inflate(layout, null));
    }

    /**
     * Toast显示:可以显示图片等控件、控制显示时间
     *
     * @param duration 显示时间
     * @param view     图片View
     */
    public static void view(int gravity, int xOffset, int yOffset, @CusToast.Duration Integer duration, View view) {
        CusToast cusToast = new CusToast(BaseApplication.mApplication);
        cusToast.setGravity(gravity, xOffset, yOffset);
        cusToast.setDuration(duration);
        cusToast.setView(view);
        cusToast.show();
    }

    public static void fixTxt(Context context, int resTxt) {
        fixTxt(context, BaseApplication.mApplication.getString(resTxt), DEFAULT_DURATION);
    }

    public static void fixTxt(Context context, int resTxt, int time) {
        fixTxt(context, BaseApplication.mApplication.getString(resTxt), time);
    }

    public static void fixTxt(Context context, String txt) {
        fixTxt(context, -1, txt, DEFAULT_DURATION);
    }

    public static void fixTxt(Context context, String txt, int time) {
        fixTxt(context, -1, txt, time);
    }

    public static void fixTxt(Context context, int resImg, String txt) {
        fixTxt(context, resImg, txt, R.style.bottomAnim, DEFAULT_DURATION);
    }

    public static void fixTxt(Context context, int resImg, String txt, int time) {
        fixTxt(context, resImg, txt, R.style.bottomAnim, time);
    }

    public static void fixTxt(Context context, int resImg, String txt, int anim, int time) {
        fixTxt(context, resImg, txt, Gravity.BOTTOM, anim, time);
    }

    public static void fixTxt(Context context, int resImg, String txt, int gravity, int anim, int time) {
        fixTxt(context, resImg, txt, R.style.DialogThemeAlpha, gravity, anim, time);
    }

    public static void fixTxt(Context context, int resImg, String txt, int theme, int gravity, int anim, int time) {
        fixView(context, LayoutInflater.from(BaseApplication.mApplication).inflate(R.layout.toast, null), resImg, txt, theme, gravity, anim, time, null);
    }

    public static void fixView(Context context, int layout) {
        fixView(context, LayoutInflater.from(BaseApplication.mApplication).inflate(layout, null));
    }

    public static void fixView(Context context, View view) {
        fixView(context, view, R.style.DialogThemeAlpha);
    }

    public static void fixView(Context context, View view, int theme) {
        fixView(context, view, theme, Gravity.BOTTOM);
    }

    public static void fixView(Context context, View view, int theme, int gravity) {
        fixView(context, view, theme, gravity, R.style.bottomAnim);
    }

    public static void fixView(Context context, View view, int theme, int gravity, int anim) {
        fixView(context, view, -1, null, theme, gravity, anim, DEFAULT_DURATION, null);
    }

    public static void fixView(Context context, View view, int theme, int gravity, int anim, DialogView.DialogViewListener dialogViewListener) {
        fixView(context, view, -1, null, theme, gravity, anim, DEFAULT_DURATION, dialogViewListener);
    }

    public static void fixView(Context context, View view, int theme, int gravity, int anim, int time, DialogView.DialogViewListener dialogViewListener) {
        fixView(context, view, -1, null, theme, gravity, anim, time, dialogViewListener);
    }

    public static void fixView(Context context, View view, final int resImg, final String txt, int theme, int gravity, int anim, int time, DialogView.DialogViewListener dialogViewListener) {
        cancelLoading();
        mDialogView = new DialogView(context, theme, R.layout.toast, new DialogView.DialogViewListener() {
            @Override
            public void onView(View view) {
                if (resImg > 0) {
                    ((ImageView) view.findViewById(R.id.toastIcon)).setImageResource(resImg);
                }
                if (!TextUtils.isEmpty(txt)) {
                    ((TextView) view.findViewById(R.id.toastTxt)).setText(txt);
                }
            }
        }).setOutsideClose(false).setGravity(gravity).setAnim(anim);
        mDialogView.show();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                cancelLoading();
            }
        }, time * 1000);
    }

    public static void cancelLoading() {
        if (mDialogView != null) {
            mDialogView.dismiss();
            mDialogView = null;
        }
    }
}
