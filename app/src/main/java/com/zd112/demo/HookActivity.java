package com.zd112.demo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * @author jiangshide
 * @Created by Ender on 2018/9/28.
 * @Emal:18311271399@163.com
 */
public class HookActivity extends Activity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
}
