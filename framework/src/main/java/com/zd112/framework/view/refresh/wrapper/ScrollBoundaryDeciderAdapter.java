package com.zd112.framework.view.refresh.wrapper;

import android.graphics.PointF;
import android.view.View;

import com.zd112.framework.view.refresh.interfaces.ScrollBoundaryDecider;
import com.zd112.framework.view.refresh.util.ScrollBoundaryUtil;

@SuppressWarnings("WeakerAccess")
public class ScrollBoundaryDeciderAdapter implements ScrollBoundaryDecider {

    public PointF mActionEvent;
    public ScrollBoundaryDecider boundary;
    public boolean mEnableLoadMoreWhenContentNotFull = true;

    @Override
    public boolean canRefresh(View content) {
        if (boundary != null) {
            return boundary.canRefresh(content);
        }
        return ScrollBoundaryUtil.canRefresh(content, mActionEvent);
    }

    @Override
    public boolean canLoadMore(View content) {
        if (boundary != null) {
            return boundary.canLoadMore(content);
        }
        return ScrollBoundaryUtil.canLoadMore(content, mActionEvent, mEnableLoadMoreWhenContentNotFull);
    }
}
