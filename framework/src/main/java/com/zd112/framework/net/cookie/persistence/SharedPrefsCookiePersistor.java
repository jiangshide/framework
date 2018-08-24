package com.zd112.framework.net.cookie.persistence;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;

public class SharedPrefsCookiePersistor implements CookiePersistor {
    private final SharedPreferences mSharedPreferences;

    public SharedPrefsCookiePersistor(Context context) {
        this(context.getSharedPreferences("CookiePersistence", Context.MODE_PRIVATE));
    }

    public SharedPrefsCookiePersistor(SharedPreferences sharedPreferences) {
        this.mSharedPreferences = sharedPreferences;
    }

    @Override
    public List<Cookie> loadAll() {
        List<Cookie> cookies = new ArrayList<>(mSharedPreferences.getAll().size());

        for (Map.Entry<String, ?> entry : mSharedPreferences.getAll().entrySet()) {
            String serializedCookie = (String) entry.getValue();
            Cookie cookie = new SerializableCookie().decode(serializedCookie);
            cookies.add(cookie);
        }
        return cookies;
    }

    @Override
    public void saveAll(Collection<Cookie> cookies) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        for (Cookie cookie : cookies) {
            if (cookie.persistent()) {
                editor.putString(createCookieKey(cookie), new SerializableCookie().encode(cookie));
            }
        }
        editor.apply();
    }

    @Override
    public void removeAll(Collection<Cookie> cookies) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        for (Cookie cookie : cookies) {
            editor.remove(createCookieKey(cookie));
        }
        editor.apply();
    }

    private static String createCookieKey(Cookie cookie) {
        return (cookie.secure() ? "https" : "http") + "://" + cookie.domain() + cookie.path() + "|" + cookie.name();
    }

    @Override
    public void clear() {
        mSharedPreferences.edit().clear().apply();
    }
}
