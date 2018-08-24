package com.zd112.framework.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;

import java.util.List;

/**
 * Created by etongdai on 2018/3/7.
 */

public enum LocationUtils implements AMapLocationListener {

    INSTANCE;

    private Context mContext;
    public AMapLocationClient mAMapLocationClient;
    public AMapLocation mAmapLocation;
    private AmapLocationListener mLocationListener;
    private LocationManager mLocationManager;

    public LocationUtils initLocation(final Context context) {
        this.mContext = context;
        //初始化定位
        mAMapLocationClient = new AMapLocationClient(context);
        //设置定位回调监听
        mAMapLocationClient.setLocationListener(this);
        return INSTANCE;
    }

    public LocationUtils start() {
        if (mAMapLocationClient != null) {
            mAMapLocationClient.startLocation();
        }
        getLocation();
        return INSTANCE;
    }

    public LocationUtils stop() {
        if (mAMapLocationClient != null) {
            mAMapLocationClient.stopLocation();
        }
        removeLocationUpdatesListener();
        return INSTANCE;
    }

    public void setLocationListener(AmapLocationListener locationListener) {
        this.mLocationListener = locationListener;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                this.mAmapLocation = aMapLocation;
                if (mLocationListener != null) {
                    mLocationListener.onLocation(aMapLocation, null);
                }
            } else {
                LogUtils.e("location~errorCode:", aMapLocation.getErrorCode(), " | errorInfo:", aMapLocation.getErrorInfo());
            }
        } else {
            LogUtils.e("location false，aMapLocation is ", aMapLocation);
        }
    }

    private void getLocation() {
        String locationProvider;
        //1.获取位置管理器
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        //2.获取位置提供器，GPS或是NetWork
        List<String> providers = mLocationManager.getProviders(true);
        if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是网络定位
            LogUtils.d("如果是网络定位");
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS定位
            LogUtils.d("如果是GPS定位");
            locationProvider = LocationManager.GPS_PROVIDER;
        } else {
            LogUtils.d("没有可用的位置提供器");
            return;
        }
        // 需要检查权限,否则编译报错,想抽取成方法都不行,还是会报错。只能这样重复 code 了。
        if (Build.VERSION.SDK_INT >= 23 &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //3.获取上次的位置，一般第一次运行，此值为null
        Location location = mLocationManager.getLastKnownLocation(locationProvider);
        if (location != null) {
            setLocation(location);
        }
        // 监视地理位置变化，第二个和第三个参数分别为更新的最短时间minTime和最短距离minDistace
        mLocationManager.requestLocationUpdates(locationProvider, 0, 0, locationListener);
    }

    private void setLocation(Location location) {
        if (mLocationListener != null) {
            mLocationListener.onLocation(null, location);
        }
    }

    // 移除定位监听
    public void removeLocationUpdatesListener() {
        if (mLocationManager != null) {
            mLocationManager.removeUpdates(locationListener);
        }
    }

    /**
     * LocationListern监听器
     * 参数：地理位置提供器、监听位置变化的时间间隔、位置变化的距离间隔、LocationListener监听器
     */

    LocationListener locationListener = new LocationListener() {

        /**
         * 当某个位置提供者的状态发生改变时
         */
        @Override
        public void onStatusChanged(String provider, int status, Bundle arg2) {
        }

        /**
         * 某个设备打开时
         */
        @Override
        public void onProviderEnabled(String provider) {

        }

        /**
         * 某个设备关闭时
         */
        @Override
        public void onProviderDisabled(String provider) {

        }

        /**
         * 手机位置发生变动
         */
        @Override
        public void onLocationChanged(Location location) {
            location.getAccuracy();//精确度
            setLocation(location);

        }
    };

    public interface AmapLocationListener {
        void onLocation(AMapLocation aMapLocation, Location location);
    }
}
