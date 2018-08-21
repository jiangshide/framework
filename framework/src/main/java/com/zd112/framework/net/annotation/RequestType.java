package com.zd112.framework.net.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({RequestType.POST, RequestType.GET, RequestType.PUT, RequestType.DELETE})
@Retention(RetentionPolicy.SOURCE)
public @interface RequestType {
    /**
     * POST
     */
    int POST = 1;

    /**
     * GET
     */
    int GET = 2;

    /**
     * PUT
     */
    int PUT = 3;

    /**
     * DELETE
     */
    int DELETE = 4;
}
