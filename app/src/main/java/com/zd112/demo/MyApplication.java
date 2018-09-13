package com.zd112.demo;

import android.content.Context;

import com.zd112.framework.BaseApplication;
import com.zd112.framework.data.BaseData;

public class MyApplication extends BaseApplication {


    @Override
    public void newGlobalError(Context context, BaseData baseData) {
        loading(context, baseData.msg);
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        LocationUtils.INSTANCE.initLocation(getBaseContext()).start();
//        LocationUtils.INSTANCE.setLocationListener(new LocationUtils.AmapLocationListener() {
//            @Override
//            public void onLocation(AMapLocation aMapLocation, Location location) {
//                LogUtils.e("aMapLocation:", aMapLocation.toString());
//            }
//        });
    }

}
