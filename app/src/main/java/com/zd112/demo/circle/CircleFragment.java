package com.zd112.demo.circle;

import android.os.Bundle;

import com.zd112.demo.R;
import com.zd112.framework.BaseApplication;
import com.zd112.framework.BaseFragment;
import com.zd112.framework.net.interfaces.OnWebSocketListener;

/**
 * @author jiangshide
 * @Created by Ender on 2018/8/28.
 * @Emal:18311271399@163.com
 */
public class CircleFragment extends BaseFragment{

    @Override
    protected void initView(Bundle savedInstanceState) {
        setView(R.layout.circle,this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
    }
}
