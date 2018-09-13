package com.zd112.demo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.zd112.demo.apdater.AdapterActivtiy;
import com.zd112.demo.author.AuthorActivity;
import com.zd112.demo.errorlog.ErrorLogActivity;
import com.zd112.demo.event.EventActivity;
import com.zd112.demo.html5.Html5Activity;
import com.zd112.demo.msg.MsgActivity;
import com.zd112.demo.net.NetActivity;
import com.zd112.demo.ui.UIActivity;
import com.zd112.demo.upgrade.UpgradeActivity;
import com.zd112.framework.BaseActivity;
import com.zd112.framework.BaseApplication;
import com.zd112.framework.utils.SystemUtils;
import com.zd112.framework.utils.ViewUtils;
import com.zd112.framework.view.DialogView;

public class MainActivity extends BaseActivity {

    @ViewUtils.ViewInject(R.id.mainL)
    private LinearLayout mMainL;

    private Class _class[] = {UIActivity.class, NetActivity.class, MsgActivity.class, EventActivity.class, AuthorActivity.class, AdapterActivtiy.class, UpgradeActivity.class, ErrorLogActivity.class, Html5Activity.class};

    @Override
    protected void initView(Bundle savedInstanceState) {
        setView(R.layout.main, this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        mMainL.removeAllViews();
        mMainL.setPadding(0, SystemUtils.getStatusBarHeight(this), 0, 0);
        for (int i = 0; i < getResArrStr(R.array.main_btn).length; i++) {
            Button button = new Button(this);
            button.setText(getResArrStr(R.array.main_btn)[i]);
            button.setId(i);
            button.setOnClickListener(this);
            mMainL.addView(button, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }

    @Override
    protected void showDialog(DialogView dialogView) {
        super.showDialog(dialogView);
//        dialogView.setSure("back").setCancel("cancel");
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        startActivity(new Intent(this, _class[v.getId()]));
    }
}
