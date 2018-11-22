package com.zd112.demo.msg;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.zd112.demo.R;
import com.zd112.framework.BaseActivity;
import com.zd112.framework.utils.LogUtils;
import com.zd112.framework.utils.ViewUtils;
import com.zd112.framework.view.CusImageView;

/**
 * @author jiangshide
 * @Created by Ender on 2018/9/10.
 * @Emal:18311271399@163.com
 */
public class MsgActivity extends BaseActivity {

    @ViewUtils.ViewInject(R.id.msgBtn)
    private Button msgBtn;
    @ViewUtils.ViewInject(R.id.msgImg)
    private CusImageView msgImg;
    private int flag;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setView(R.layout.msg, this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        msgBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.msgBtn:
                int measureW = msgImg.getMeasuredWidth();
                int measureH = msgImg.getMeasuredHeight();
                int w = msgImg.getWidth();
                int h = msgImg.getHeight();
                LogUtils.e("--------------measureW:", measureW, " | measureH:", measureH, " | w:", w, " | h:", h);
//                if(flag == 0){
//                }
                break;
        }
    }
}
