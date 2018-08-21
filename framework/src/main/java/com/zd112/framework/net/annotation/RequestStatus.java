package com.zd112.framework.net.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({RequestStatus.NORMAL, RequestStatus.REFRESH, RequestStatus.MORE})
@Retention(RetentionPolicy.SOURCE)
public @interface RequestStatus {
    int NORMAL = 1;
    int REFRESH = 2;
    int MORE = 3;
}
