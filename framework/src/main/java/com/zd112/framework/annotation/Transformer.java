package com.zd112.framework.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author jiangshide
 * @Created by Ender on 2018/8/29.
 * @Emal:18311271399@163.com
 */
@IntDef({Transformer.ACCORDION, Transformer.BACKGROUD_TO_FOREGROUND, Transformer.CUBEL_IN, Transformer.CUBE_OUT, Transformer.DEFAULT, Transformer.DEPTH_PAGE, Transformer.DRAWER, Transformer.FLIP_HORIZONTAL, Transformer.FLIP_VERTICAL, Transformer.FOREGROUND_TO_BACKGROUND, Transformer.ROTATE_DOWN, Transformer.ROTATE_UP, Transformer.SCALE_IN_OUT, Transformer.STACK, Transformer.TABLE, Transformer.VERTICAL, Transformer.ZOOM_IN, Transformer.ZOOM_OUT_SLIDE, Transformer.ZOOM_OUT})
@Retention(RetentionPolicy.SOURCE)
public @interface Transformer {

    int ACCORDION = 0;
    int BACKGROUD_TO_FOREGROUND = 1;
    int CUBEL_IN = 2;
    int CUBE_OUT = 3;
    int DEFAULT = 4;
    int DEPTH_PAGE = 5;
    int DRAWER = 6;
    int FLIP_HORIZONTAL = 7;
    int FLIP_VERTICAL = 8;
    int FOREGROUND_TO_BACKGROUND = 9;
    int ROTATE_DOWN = 10;
    int ROTATE_UP = 11;
    int SCALE_IN_OUT = 12;
    int STACK = 13;
    int TABLE = 14;
    int VERTICAL = 15;
    int ZOOM_IN = 16;
    int ZOOM_OUT_SLIDE = 17;
    int ZOOM_OUT = 18;
}
