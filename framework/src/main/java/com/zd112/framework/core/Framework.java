package com.zd112.framework.core;

/**
 * @author jiangshide
 * @Created by Ender on 2018/9/15.
 * @Emal:18311271399@163.com
 */
public class Framework {

//    static {
//        System.loadLibrary("framework");
//    }

    public static void receiver(String data){};//由底层去处理逻辑后的结果通知

    public static native String cp(String from,String to);
}
