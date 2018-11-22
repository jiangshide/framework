package com.zd112.framework.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zd112.framework.utils.LogUtils;
import com.zd112.framework.view.CusToast;

/**
 * @author jiangshide
 * @Created by Ender on 2018/11/15.
 * @Emal:18311271399@163.com
 */
public class DeamonsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        CusToast.txt("action:"+action);
        LogUtils.e("-------------content:",context," | action:",action);
    }
}
