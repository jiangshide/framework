package com.zd112.demo.home.fragment;

import android.os.Bundle;
import android.widget.TextView;

import com.zd112.demo.R;
import com.zd112.framework.BaseFragment;
import com.zd112.framework.utils.ViewUtils;

/**
 * @author jiangshide
 * @Created by Ender on 2018/8/28.
 * @Emal:18311271399@163.com
 */
public class CommentFragment extends BaseFragment{

    @ViewUtils.ViewInject(R.id.itemRate)
    private TextView itemRate;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setView(R.layout.comm_item,this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        itemRate.setText("Comment");
    }
}
