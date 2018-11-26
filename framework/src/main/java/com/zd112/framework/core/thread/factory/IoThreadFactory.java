package com.zd112.framework.core.thread.factory;

/**
 * @author jiangshide
 * @Created by Ender on 2018/11/23.
 * @Emal:18311271399@163.com
 */
public class IoThreadFactory extends BaseThreadFactory{
    public IoThreadFactory(){
        mThreadNamePrefix = "IOThread";
    }
}
