/*
 * 官网地站:http://www.ShareSDK.cn
 * 技术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第一时间通过微信将版本更新内容推送给您。如果使用过程中有任何问题，也可以通过微信与我们取得联系，我们将会在24小时内给予回复）
 *
 * Copyright (c) 2013年 ShareSDK.cn. All rights reserved.
 */

package com.zd112.framework;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.zd112.framework.utils.ToastUtils;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseApplication) getApplicationContext()).mWxApi.handleIntent(getIntent(), this);
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        ((BaseApplication) getApplicationContext()).mWxApi.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
    }

    @Override
    public void onResp(BaseResp baseResp) {
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                String result = getString(R.string.share_success);
                setTitle(result);
                ToastUtils.show(this, getString(R.string.cancel));
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                ToastUtils.show(this, getString(R.string.cancel));
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                ToastUtils.show(this, getString(R.string.weixin_false));
                break;
            default:
                ToastUtils.show(this, getString(R.string.back));
                break;
        }
    }

}
