package com.zd112.framework.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.lang.reflect.Field;

/**
 * @author jiangshide
 * @Created by Ender on 2018/9/12.
 * @Emal:18311271399@163.com
 */
public class ScreenUtils {

    public static void apdater(Context context, boolean isAdapter) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            apdater(context, displayMetrics.widthPixels, isAdapter);
        }
    }

    public static void apdater(Context context, int width, boolean isAdapter) {
        Resources resources = context.getResources();
        DisplayMetrics displayMetrics = getDisplayMetrics(resources);
        if (isAdapter && width > 0) {
            Point point = new Point();
            ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(point);
            resources.getDisplayMetrics().xdpi = point.x / width * 72f;
            if (displayMetrics != null) {
                displayMetrics.xdpi = point.x / width * 72f;
            }
        } else {
            resources.getDisplayMetrics().setToDefaults();
            if (displayMetrics != null) {
                displayMetrics.setToDefaults();
            }
        }
    }

    private static DisplayMetrics getDisplayMetrics(Resources resources) {
        if ("MiuiResources".equals(resources.getClass().getSimpleName()) || "XResources".equals(resources.getClass().getSimpleName())) {
            try {
                Field field = Resources.class.getDeclaredField("mTmpMetrics");
                field.setAccessible(true);
                return (DisplayMetrics) field.get(resources);
            } catch (Exception e) {
                LogUtils.e(e);
                return null;
            }
        }
        return null;
    }
}
