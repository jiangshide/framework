package com.zd112.framework.view.refresh.wrapper;

import android.annotation.SuppressLint;
import android.view.View;

import com.zd112.framework.view.refresh.component.InternalAbstract;
import com.zd112.framework.view.refresh.interfaces.RefreshFooter;

@SuppressLint("ViewConstructor")
public class RefreshFooterWrapper extends InternalAbstract implements RefreshFooter{

    public RefreshFooterWrapper(View wrapper) {
        super(wrapper);
    }

    @Override
    public boolean setNoMoreData(boolean noMoreData) {
        return mWrapperView instanceof RefreshFooter && ((RefreshFooter) mWrapperView).setNoMoreData(noMoreData);
    }
}
