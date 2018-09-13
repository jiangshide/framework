package com.zd112.demo.net;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zd112.demo.R;
import com.zd112.demo.data.HomeData;
import com.zd112.framework.BaseActivity;
import com.zd112.framework.net.annotation.RequestType;
import com.zd112.framework.net.helper.NetInfo;
import com.zd112.framework.utils.ViewUtils;
import com.zd112.framework.view.DialogView;

/**
 * @author jiangshide
 * @Created by Ender on 2018/9/10.
 * @Emal:18311271399@163.com
 */
public class NetActivity extends BaseActivity {

    @ViewUtils.ViewInject(R.id.resultNet)
    private TextView resultNet;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setView(R.layout.net, this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
    }

    public void getBtn(View view) {
        request("more/ind", HomeData.class, true);
    }

    public void postBtn(View view) {
        request(RequestType.POST, "more/ind", null, null, HomeData.class, true);
    }

    public void fixUrlBtn(View view) {
        request("https://api.etongdai.com/service/more/ind ", HomeData.class, true);
    }

    @Override
    public void onSuccess(NetInfo info) {
        super.onSuccess(info);
        resultNet.setText(info.getRetDetail());
    }
}
