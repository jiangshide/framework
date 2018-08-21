package com.zd112.framework.net.cookie;

import okhttp3.CookieJar;

public interface ClearableCookieJar extends CookieJar {
    /**
     * Clear all the session cookies while maintaining the persisted ones.
     */
    void clearSession();

    /**
     * Clear all the cookies from persistence and from the cache.
     */
    void clear();
}
