package com.zd112.demo.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.zd112.demo.R;
import com.zd112.framework.BaseFragment;
import com.zd112.framework.utils.ViewUtils;
import com.zd112.framework.view.WheelView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jiangshide
 * @Created by Ender on 2018/8/28.
 * @Emal:18311271399@163.com
 */
public class ShopFragment extends BaseFragment {

    @ViewUtils.ViewInject(R.id.wheelViewL)
    private LinearLayout wheelViewL;
    private LinearLayout linearLayout;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setView(R.layout.ui_shop, this);
        showStatusBar();
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        wheelViewL.removeAllViews();
        for (int i = 0; i < 3; i++) {
            Button button = new Button(getActivity());
            button.setText("wheelView例子" + i);
            button.setId(i);
            button.setOnClickListener(this);
            wheelViewL.addView(button, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }

    private void showWheel(int size) {
        if (linearLayout != null) {
            wheelViewL.removeView(linearLayout);
        }
        linearLayout = new LinearLayout(getActivity());
        for (int i = 0; i < size; i++) {
            WheelView wheelView = new WheelView(getActivity());
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            wheelView.setTextSize(i * 3 + 10);
            linearLayout.addView(wheelView, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
            wheelView.setItems(getList());
        }
        wheelViewL.addView(linearLayout);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onClick(View v) {
        showWheel(v.getId() + 1);
    }

    private List<String> getList() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add("测试" + i);
        }
        return list;
    }
}
