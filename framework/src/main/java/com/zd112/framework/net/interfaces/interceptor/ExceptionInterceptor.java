package com.zd112.framework.net.interfaces.interceptor;

import com.zd112.framework.net.helper.NetInfo;

public interface ExceptionInterceptor {
    NetInfo intercept(NetInfo info) throws Exception;
}
