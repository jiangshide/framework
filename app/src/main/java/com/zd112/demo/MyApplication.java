package com.zd112.demo;

import android.content.Context;
import android.location.Location;

import com.amap.api.location.AMapLocation;
import com.zd112.demo.utils.LocationUtils;
import com.zd112.framework.BaseApplication;
import com.zd112.framework.data.BaseData;
import com.zd112.framework.utils.LogUtils;

public class MyApplication extends BaseApplication {


    @Override
    public void newGlobalError(Context context, BaseData baseData) {

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
