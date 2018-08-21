package com.zd112.framework.view.refresh.interfaces;

import android.content.Context;
import android.support.annotation.NonNull;

public interface DefaultRefreshHeaderCreator {
    @NonNull
    RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout);
}
