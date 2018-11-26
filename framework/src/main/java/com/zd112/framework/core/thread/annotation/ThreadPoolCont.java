package com.zd112.framework.core.thread.annotation;

import java.util.concurrent.TimeUnit;

/**
 * @author jiangshide
 * @Created by Ender on 2018/11/23.
 * @Emal:18311271399@163.com
 */
public class ThreadPoolCont {
    private static final int MIN_POOL_SIZE = 2;
    private static final int AVAILABLE_CORE = Runtime.getRuntime().availableProcessors();
    private static final int CPU_COUNT = (AVAILABLE_CORE < MIN_POOL_SIZE) ? MIN_POOL_SIZE : AVAILABLE_CORE;

    public static final int IO_CORE_POOL_SIZE = CPU_COUNT+6;
    public static final int IO_MAXIMUM_POOL_SIZE = CPU_COUNT+6;
    public static final int IO_KEEP_ALIVE_TIME = 1;
    public static final int IO_POOL_QUEUE_SIZE = 64;

    public static final int SERIAL_EXECUTOR_TIMEOUT = 50;
    public static final TimeUnit SERIAL_EXECUTOR_TIMEOUT_UNIT = TimeUnit.SECONDS;
}
