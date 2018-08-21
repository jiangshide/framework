package com.zd112.framework.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.zd112.framework.BaseApplication;
import com.zd112.framework.net.annotation.DataType;

import java.util.HashMap;
import java.util.Map;

public enum ShareParamUtils {

    INSTANCE;

    public boolean status;

    private static SharedPreferences.Editor getEdit() {
        return BaseApplication.application.getSharedPreferences(BaseApplication.application.getPackageName(), Context.MODE_PRIVATE).edit();
    }

    private static SharedPreferences getSharedPreferences() {
        return BaseApplication.application.getSharedPreferences(BaseApplication.application.getPackageName(), Context.MODE_PRIVATE);
    }

    public synchronized ShareParamUtils putBoolean(String key, boolean value) {
        status = getEdit().putBoolean(key, value).commit();
        return this;
    }

    public synchronized ShareParamUtils putFloat(String key, float value) {
        status = getEdit().putFloat(key, value).commit();
        return this;
    }

    public synchronized ShareParamUtils putInt(String key, int value) {
        status = getEdit().putInt(key, value).commit();
        return this;
    }

    public synchronized ShareParamUtils putLong(String key, long value) {
        status = getEdit().putLong(key, value).commit();
        return this;
    }

    public synchronized ShareParamUtils putString(String key, String value) {
        status = getEdit().putString(key, value).commit();
        return this;
    }

    public static boolean getBoolean(String key) {
        return getSharedPreferences().getBoolean(key, false);
    }

    public static float getFloat(String key) {
        return getSharedPreferences().getFloat(key, 0);
    }

    public static int getInt(String key) {
        return getSharedPreferences().getInt(key, 0);
    }

    public static long getLong(String key) {
        return getSharedPreferences().getLong(key, 0);
    }

    public static String getString(String key) {
        return getSharedPreferences().getString(key, null);
    }

    public static boolean clear() {
        return getEdit().clear().commit();
    }

    public static synchronized boolean clear(String key) {
        return getEdit().remove(key).commit();
    }

    public static synchronized void clear(String... keys) {
        for (String key : keys) {
            getEdit().remove(key).commit();
        }
    }

    public static synchronized void saveClear(HashMap<String, Integer> hashMap) {
        if (hashMap == null) return;
        for (Map.Entry<String, Integer> entry : hashMap.entrySet()) {
            String key = entry.getKey();
            switch (entry.getValue()) {
                case DataType.typeBool:
                    boolean booleanValue = getBoolean(key);
                    clear(key);
                    INSTANCE.putBoolean(key, booleanValue);
                    break;
                case DataType.typeInt:
                    int intValue = getInt(key);
                    clear(key);
                    INSTANCE.putInt(key, intValue);
                    break;
                case DataType.typeLong:
                    long longValue = getLong(key);
                    clear(key);
                    INSTANCE.putLong(key, longValue);
                    break;
                case DataType.typefloat:
                    float floatValue = getFloat(key);
                    clear(key);
                    INSTANCE.putFloat(key, floatValue);
                    break;
                case DataType.typeString:
                    String stringValue = getString(key);
                    clear(key);
                    INSTANCE.putString(key, stringValue);
                    break;
                default:
                    LogUtils.e("type:", key);
                    break;
            }
        }
    }
}
