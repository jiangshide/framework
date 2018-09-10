package com.zd112.framework.net.status;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.zd112.framework.utils.SystemUtils;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * 检测当前环境的网络状态
 * 静态注册：
 * <receiver android:name="core.networkstate.NetworkStateReceiver" >
 * <intent-filter>
 * <action android:name="android.net.conn.CONNECTIVITY_CHANGE" />
 * <action android:name="android.gzcpc.conn.CONNECTIVITY_CHANGE" />
 * </intent-filter>
 * </receiver>
 * 动态注册：
 * registerNetworkStateReceive/unRegisterNetworkStateReceiver
 * 权限：
 * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
 * <uses-permission android:name="android.permission.CHANGE_NETWORK_STATE" />
 * <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
 */
public class NetworkStateReceiver extends BroadcastReceiver {

    private static BroadcastReceiver broadcastReceiver;
    private static ArrayList<NetworkStateListener> networkStateListenerList = new ArrayList<NetworkStateListener>();
    private final static String ANDROID_NET_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    private final static String ANDROID_NET_CHANGE_ACTION_SUPL = "android.net.conn.CONNECTIVITY_CHANGE_SUPL";
    private static LinkedList<NetInfo> list = new LinkedList<>();


    public static BroadcastReceiver getBroadcastReceiver() {
        return broadcastReceiver == null ? broadcastReceiver = new NetworkStateReceiver() : broadcastReceiver;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (broadcastReceiver == null)
            broadcastReceiver = NetworkStateReceiver.this;
        if (intent.getAction() == null)
            return;
        if (intent.getAction().equalsIgnoreCase(SystemUtils.getSDKVersion() < 24 ? ANDROID_NET_CHANGE_ACTION : ANDROID_NET_CHANGE_ACTION_SUPL)) {
            boolean isNetworkAvailable = SystemUtils.isNetworkAvailable(context);
            String networkType = SystemUtils.getNetworkType(context);
            NetInfo netInfo = new NetInfo(isNetworkAvailable, networkType);
            if (!list.isEmpty()) {
                if (!list.getLast().equal(isNetworkAvailable, networkType)) {
                    list.add(netInfo);
                    notifyListener(isNetworkAvailable, netInfo);
                }
            } else {
                list.add(netInfo);
                notifyListener(isNetworkAvailable, netInfo);
            }
        }
    }

    /**
     * 通知网络状态监听器
     */
    private void notifyListener(boolean isNetworkAvailable, NetInfo netInfo) {
        for (int i = 0; i < networkStateListenerList.size(); i++) {
            final NetworkStateListener listener = networkStateListenerList.get(i);
            if (null != listener) {
                listener.onNetworkState(isNetworkAvailable, netInfo);
            }
        }
    }

    /**
     * 添加网络状态监听
     */
    public static void addNetworkStateListener(NetworkStateListener observer) {
        if (null == networkStateListenerList) {
            networkStateListenerList = new ArrayList<NetworkStateListener>();
        }
        networkStateListenerList.add(observer);
    }

    /**
     * 移除网络状态监听
     */
    public static void removeNetworkStateListener(NetworkStateListener observer) {
        if (null != networkStateListenerList) {
            networkStateListenerList.remove(observer);
        }
    }


    /**
     * 注册网络状态广播
     */
    public static void registerNetworkStateReceiver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(SystemUtils.getSDKVersion() < 24 ? ANDROID_NET_CHANGE_ACTION : ANDROID_NET_CHANGE_ACTION_SUPL);
        context.getApplicationContext().registerReceiver(getBroadcastReceiver(), filter);
    }


    /**
     * 注销网络状态广播
     */
    public static void unRegisterNetworkStateReceiver(Context context) {
        if (null != broadcastReceiver) {
            try {
                context.getApplicationContext().unregisterReceiver(broadcastReceiver);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
