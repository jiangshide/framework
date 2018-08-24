package com.zd112.framework.net.cookie.persistence;

import android.util.Log;

import com.zd112.framework.utils.LogUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import okhttp3.Cookie;

public class SerializableCookie implements Serializable{

    private static final long serialVersionUID = -8594045714036645534L;

    private transient Cookie mCookie;

    public String encode(Cookie cookie) {
        this.mCookie = cookie;

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = null;

        try {
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(this);
        } catch (IOException e) {
            LogUtils.d("IOException in encodeCookie", e);
            return null;
        } finally {
            if (objectOutputStream != null) {
                try {
                    // Closing a ByteArrayOutputStream has no effect, it can be used later (and is used in the return statement)
                    objectOutputStream.close();
                } catch (IOException e) {
                    LogUtils.d("Stream not closed in encodeCookie", e);
                }
            }
        }

        return byteArrayToHexString(byteArrayOutputStream.toByteArray());
    }

    /**
     * Using some super basic byte array &lt;-&gt; hex conversions so we don't
     * have to rely on any large Base64 libraries. Can be overridden if you
     * like!
     *
     * @param bytes byte array to be converted
     * @return string containing hex values
     */
    private static String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte element : bytes) {
            int v = element & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString();
    }

    public Cookie decode(String encodedCookie) {

        byte[] bytes = hexStringToByteArray(encodedCookie);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                bytes);

        Cookie cookie = null;
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            cookie = ((SerializableCookie) objectInputStream.readObject()).mCookie;
        } catch (IOException e) {
            LogUtils.d( "IOException in decodeCookie", e);
        } catch (ClassNotFoundException e) {
            LogUtils.d( "ClassNotFoundException in decodeCookie", e);
        } finally {
            if (objectInputStream != null) {
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    LogUtils.d( "Stream not closed in decodeCookie", e);
                }
            }
        }
        return cookie;
    }

    /**
     * Converts hex values from strings to byte array
     *
     * @param hexString string of hex-encoded values
     * @return decoded byte array
     */
    private static byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character
                    .digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }

    private static long NON_VALID_EXPIRES_AT = -1L;

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(mCookie.name());
        out.writeObject(mCookie.value());
        out.writeLong(mCookie.persistent() ? mCookie.expiresAt() : NON_VALID_EXPIRES_AT);
        out.writeObject(mCookie.domain());
        out.writeObject(mCookie.path());
        out.writeBoolean(mCookie.secure());
        out.writeBoolean(mCookie.httpOnly());
        out.writeBoolean(mCookie.hostOnly());
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        Cookie.Builder builder = new Cookie.Builder();

        builder.name((String) in.readObject());

        builder.value((String) in.readObject());

        long expiresAt = in.readLong();
        if (expiresAt != NON_VALID_EXPIRES_AT) {
            builder.expiresAt(expiresAt);
        }

        final String domain = (String) in.readObject();
        builder.domain(domain);

        builder.path((String) in.readObject());

        if (in.readBoolean())
            builder.secure();

        if (in.readBoolean())
            builder.httpOnly();

        if (in.readBoolean())
            builder.hostOnlyDomain(domain);

        mCookie = builder.build();
    }
}
