package com.zd112.framework.abstracts;

/**
 * @author jiangshide
 * @Created by Ender on 2018/9/12.
 * @Emal:18311271399@163.com
 */
public abstract class Singleton<T> {
    private T mInstance;
    protected abstract T create();

    public final T get(){
        synchronized (this){
            if(mInstance == null){
                mInstance = create();
            }
            return mInstance;
        }
    }
}
