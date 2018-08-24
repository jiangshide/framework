package com.zd112.framework.net.cookie.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import okhttp3.Cookie;

class IdentifiableCookie {
    private Cookie mCookie;

    static List<IdentifiableCookie> decorateAll(Collection<Cookie> cookies) {
        List<IdentifiableCookie> identifiableCookies = new ArrayList<>(cookies.size());
        for (Cookie cookie : cookies) {
            identifiableCookies.add(new IdentifiableCookie(cookie));
        }
        return identifiableCookies;
    }

    IdentifiableCookie(Cookie cookie) {
        this.mCookie = cookie;
    }

    Cookie getCookie() {
        return mCookie;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof IdentifiableCookie)) return false;
        IdentifiableCookie that = (IdentifiableCookie) other;
        return that.mCookie.name().equals(this.mCookie.name())
                && that.mCookie.domain().equals(this.mCookie.domain())
                && that.mCookie.path().equals(this.mCookie.path())
                && that.mCookie.secure() == this.mCookie.secure()
                && that.mCookie.hostOnly() == this.mCookie.hostOnly();
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = 31 * hash + mCookie.name().hashCode();
        hash = 31 * hash + mCookie.domain().hashCode();
        hash = 31 * hash + mCookie.path().hashCode();
        hash = 31 * hash + (mCookie.secure() ? 0 : 1);
        hash = 31 * hash + (mCookie.hostOnly() ? 0 : 1);
        return hash;
    }
}
