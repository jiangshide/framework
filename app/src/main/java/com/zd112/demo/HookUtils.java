package com.zd112.demo;

import android.content.Context;

import com.zd112.framework.utils.LogUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author jiangshide
 * @Created by Ender on 2018/9/28.
 * @Emal:18311271399@163.com
 */
public class HookUtils {
    private Class<?> mProxyActivity;
    private Context mContext;

    public HookUtils(Class<?> proxyActivity, Context context) {
        this.mProxyActivity = proxyActivity;
        this.mContext = context;
    }

    public void hookAms() {
        try {
            Class<?> ActivityManagerNativeClass = Class.forName("android.app.ActivityManagerNative");
            Field gDefaultField = ActivityManagerNativeClass.getDeclaredField("gDefault");
            gDefaultField.setAccessible(true);
            Object gDefaultFieldValue = gDefaultField.get(null);

            Class<?> singleClass = Class.forName("android.util.Singleton");
            Field instanceField = singleClass.getDeclaredField("mInstance");
            instanceField.setAccessible(true);
            Object iActivityManagerObject = instanceField.get(gDefaultFieldValue);

            AmsInvocationHandler amsInvocationHandler = new AmsInvocationHandler(iActivityManagerObject);
            Class<?> iActivityManagerIntercept = Class.forName("android.app.IActivityManager");
            Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[]{iActivityManagerIntercept}, amsInvocationHandler);
            instanceField.set(gDefaultFieldValue, proxy);
        } catch (Exception e) {
            LogUtils.e(e);
            e.printStackTrace();
        }
    }

    private class AmsInvocationHandler implements InvocationHandler {

        private Object mIActivityManagerObject;

        private AmsInvocationHandler(Object iActivityManagerObject) {
            this.mIActivityManagerObject = iActivityManagerObject;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
            String methodName = method.getName();
            LogUtils.e("--------------methodName:", methodName);
            if (methodName.contains("startActivity")) {
                LogUtils.e("-------------startActivitying...");
            }
            return method.invoke(mIActivityManagerObject, args);
        }
    }
}
