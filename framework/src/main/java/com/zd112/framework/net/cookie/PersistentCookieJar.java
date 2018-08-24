package com.zd112.framework.net.cookie;

import com.zd112.framework.net.cookie.cache.CookieCache;
import com.zd112.framework.net.cookie.persistence.CookiePersistor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

public class PersistentCookieJar implements ClearableCookieJar{
    private CookieCache mCache;
    private CookiePersistor mPersistor;

    public PersistentCookieJar(CookieCache cache, CookiePersistor persistor) {
        this.mCache = cache;
        this.mPersistor = persistor;

        this.mCache.addAll(persistor.loadAll());
    }

    @Override
    synchronized public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        mCache.addAll(cookies);
        mPersistor.saveAll(cookies);
    }

    @Override
    synchronized public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> removedCookies = new ArrayList<>();
        List<Cookie> validCookies = new ArrayList<>();

        for (Iterator<Cookie> it = mCache.iterator(); it.hasNext(); ) {
            Cookie currentCookie = it.next();

            if (isCookieExpired(currentCookie)) {
                removedCookies.add(currentCookie);
                it.remove();

            } else if (currentCookie.matches(url)) {
                validCookies.add(currentCookie);
            }
        }

        mPersistor.removeAll(removedCookies);

        return validCookies;
    }

    private static boolean isCookieExpired(Cookie cookie) {
        return cookie.expiresAt() < System.currentTimeMillis();
    }

    @Override
    public void clearSession() {
        mCache.clear();
        mCache.addAll(mPersistor.loadAll());
    }

    synchronized public void clear() {
        mCache.clear();
        mPersistor.clear();
    }
}
