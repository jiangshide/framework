package com.zd112.framework.net.cookie.cache;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import okhttp3.Cookie;

public class SetCookieCache implements CookieCache {
    private Set<IdentifiableCookie> mCookies;

    public SetCookieCache() {
        mCookies = new HashSet<>();
    }

    @Override
    public void addAll(Collection<Cookie> newCookies) {
        for (IdentifiableCookie cookie : IdentifiableCookie.decorateAll(newCookies)) {
            this.mCookies.remove(cookie);
            this.mCookies.add(cookie);
        }
    }

    @Override
    public void clear() {
        mCookies.clear();
    }

    @Override
    public Iterator<Cookie> iterator() {
        return new SetCookieCacheIterator();
    }

    private class SetCookieCacheIterator implements Iterator<Cookie> {

        private Iterator<IdentifiableCookie> iterator;

        public SetCookieCacheIterator() {
            iterator = mCookies.iterator();
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public Cookie next() {
            return iterator.next().getCookie();
        }

        @Override
        public void remove() {
            iterator.remove();
        }
    }
}
