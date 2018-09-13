package com.zd112.demo.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.zd112.demo.R;
import com.zd112.framework.BaseFragment;
import com.zd112.framework.utils.ImageUtils;
import com.zd112.framework.utils.ViewUtils;
import com.zd112.framework.view.CusToast;
import com.zd112.framework.view.SwitchButton;
import com.zd112.framework.view.UnLockView;

/**
 * @author jiangshide
 * @Created by Ender on 2018/8/28.
 * @Emal:18311271399@163.com
 */
public class PulishFragment extends BaseFragment implements SwitchButton.OnCheckedChangeListener, UnLockView.OnGestureDoneListener {

    @ViewUtils.ViewInject(R.id.switchDefault)
    private SwitchButton switchDefault;
    @ViewUtils.ViewInject(R.id.qrImg)
    private ImageView qrImg;
    @ViewUtils.ViewInject(R.id.unlockView)
    private UnLockView unlockView;

    private final int DEFAULT_MIN_SIZE = 4;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setView(R.layout.pubblish, this);
        showStatusBar();
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        switchDefault.setOnCheckedChangeListener(this);
        unlockView.setOnGestureDoneListener(this);
    }

    @Override
    public void onCheckedChanged(SwitchButton view, boolean isChecked) {
        CusToast.txt("isChecked:" + isChecked);
        if (isChecked) {
            ImageUtils.showQRImg(getActivity(), R.mipmap.ic_launcher, "this is the test!", qrImg);
        }
        qrImg.setVisibility(isChecked ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean isValidGesture(int pointCount) {
        if (pointCount < DEFAULT_MIN_SIZE) {
            CusToast.fixTxt(getActivity(), "长度不能小于:" + DEFAULT_MIN_SIZE);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void inputOK(UnLockView view, String psw) {

    }
}
