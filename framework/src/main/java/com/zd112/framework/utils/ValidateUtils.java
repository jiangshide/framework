package com.zd112.framework.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateUtils {
    public static boolean isMobile(String mobile) {
        Pattern p = Pattern.compile("^((13[0-9])|(14[5,6,7,8])|(15[^4,\\D])|(166)|(17[^29,\\D])|(18[0-9])|(19[8,9]))\\d{8}$");
        Matcher m = p.matcher(mobile);
        return m.matches();
    }

    public static String formatMobile(String mobile) {
        if (!TextUtils.isEmpty(mobile) && mobile.length() > 7) {
            return new StringBuilder().append(mobile.substring(0, 3)).append("****")
                    .append(mobile.substring(mobile.length() - 4)).toString();
        } else {
            return mobile;
        }
    }

    public static String formatBrandCard(String cardNo) {
        if (!TextUtils.isEmpty(cardNo) && cardNo.length() > 10) {
            StringBuilder stringBuilder = new StringBuilder();
            return stringBuilder.append(cardNo.substring(0, 4)).append("********")
                    .append(cardNo.substring(cardNo.length() - 4)).toString();
        } else {
            return cardNo;
        }
    }

    public static String formatIdCard(String idcard) {
        return idcard.replaceAll("(\\d{4})\\d{10}(\\w{4})", "$1*****$2");
    }

    public static String getStr(String str) {
        return TextUtils.isEmpty(str) ? "" : str;
    }

    /**
     * 过滤html标签
     *
     * @param content
     * @return
     */
    public static String stripHtml(String content) {
        if (!TextUtils.isEmpty(content)) {
            content = content.replaceAll("<p .*?>", "\r\n");
            content = content.replaceAll("<br\\s*/?>", "\r\n");
            content = content.replaceAll("\\<.*?>", "");
        }
        return content;
    }

    public static String getJson(String json) {
        if (!TextUtils.isEmpty(json) && json.contains(":")) {
            json = "{\"res\":" + json.substring(1, json.length() - 1) + "}";
            json = json.replaceAll("\\\\", "");
        }
        return json;
    }

    public static boolean checkNameChinese(String name) {
        boolean res = true;
        char[] cTemp = name.toCharArray();
        for (int i = 0; i < name.length(); i++) {
            if (!isChinese(cTemp[i])) {
                res = false;
                break;
            }
        }
        return res;
    }

    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;
    }

    public static boolean isEmpty(String str) {
        return TextUtils.isEmpty(str);
    }

    public static boolean isInteger(String str) {
        try {
            Integer.valueOf(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static boolean isLong(String str) {
        try {
            Long.valueOf(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static boolean isDouble(String str) {
        try {
            Double.valueOf(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static boolean isEmptyOrNormPsw(String pswStr){
        String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";
        Pattern p  = Pattern.compile(regex); // 验证手机号
        Matcher m = p.matcher(pswStr);
        return m.matches();
    }
}
