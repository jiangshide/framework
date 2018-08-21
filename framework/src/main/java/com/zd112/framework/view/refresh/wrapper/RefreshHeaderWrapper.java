package com.zd112.framework.view.refresh.wrapper;

import android.annotation.SuppressLint;
import android.view.View;

import com.zd112.framework.view.refresh.component.InternalAbstract;
import com.zd112.framework.view.refresh.interfaces.RefreshHeader;

@SuppressLint("ViewConstructor")
public class RefreshHeaderWrapper extends InternalAbstract implements RefreshHeader{

    public RefreshHeaderWrapper(View wrapper) {
        super(wrapper);
    }

}
