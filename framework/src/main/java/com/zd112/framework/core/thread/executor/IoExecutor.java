package com.zd112.framework.core.thread.executor;

import com.zd112.framework.core.thread.annotation.ThreadPoolParams;
import com.zd112.framework.core.thread.factory.BaseThreadFactory;
import com.zd112.framework.core.thread.factory.IoThreadFactory;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author jiangshide
 * @Created by Ender on 2018/11/23.
 * @Emal:18311271399@163.com
 */
public class IoExecutor extends BaseExecutor {

    public IoExecutor(ThreadPoolParams threadPoolParams) {
        super(threadPoolParams);
    }

    @Override
    BaseThreadFactory getBaseThreadFactory() {
        return new IoThreadFactory();
    }

    @Override
    RejectedExecutionHandler getBaseRejectExecutionHandler() {
        return new ThreadPoolExecutor.DiscardOldestPolicy();
    }
}
