package com.zd112.framework.net.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({DataType.typeBool, DataType.typeInt, DataType.typeLong, DataType.typefloat, DataType.typeString})
@Retention(RetentionPolicy.SOURCE)
public @interface DataType {
    int typeBool = 1;
    int typeInt = 2;
    int typeLong = 3;
    int typefloat = 4;
    int typeString = 5;
}
