package com.zd112.framework.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.zd112.framework.BaseApplication;
import com.zd112.framework.R;

public class ShareUtils {

    public static void shareWeixin(int scene, String url, String title, String description, Bitmap bitmap) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = description;
        msg.setThumbImage(bitmap);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = scene;
        BaseApplication.mApplication.mWxApi.sendReq(req);
    }

    public static void openWeiXin(Context context) {
        openWeiXin(context, null);
    }

    public static void openWeiXin(Context context, String content) {
        if (BaseApplication.mApplication.mWxApi == null) {
            BaseApplication.mApplication.loading(context, context.getString(R.string.weixin_init)).setOnlySure();
            return;
        }
        if (!BaseApplication.mApplication.mWxApi.isWXAppInstalled()) {
            BaseApplication.mApplication.loading(context, context.getString(R.string.weixin_install)).setOnlySure();
            return;
        } else if (BaseApplication.mApplication.mWxApi.getWXAppSupportAPI() == 0) {
            BaseApplication.mApplication.loading(context, context.getString(R.string.no_support_device)).setOnlySure();
            return;
        } else {
            if (!TextUtils.isEmpty(content)) {
                ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                assert clipboardManager != null;
                clipboardManager.setPrimaryClip(ClipData.newPlainText(null, content));
                if (clipboardManager.hasPrimaryClip()) {
                    clipboardManager.getPrimaryClip().getItemAt(0).getText();
                }
            }
            BaseApplication.mApplication.mWxApi.openWXApp();
        }
    }

}
