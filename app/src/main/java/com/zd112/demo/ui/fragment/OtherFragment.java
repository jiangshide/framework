package com.zd112.demo.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.zd112.demo.R;
import com.zd112.framework.BaseFragment;
import com.zd112.framework.apdater.CommAdapter;
import com.zd112.framework.utils.ViewUtils;

import java.util.ArrayList;

/**
 * @author jiangshide
 * @Created by Ender on 2018/9/12.
 * @Emal:18311271399@163.com
 */
public class OtherFragment extends BaseFragment {

    @ViewUtils.ViewInject(R.id.mineList)
    private ListView mineList;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setView(R.layout.ui_mine_other, this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        ArrayList list = new ArrayList();
        for (int i = 0; i < 50; i++) {
            list.add("this is the test" + i);
        }
        mineList.setAdapter(new CommAdapter<String>(getActivity(), list, R.layout.comm_item) {
            @Override
            protected void convertView(int position, View item, String s) {
                ((TextView) get(item, R.id.itemRate)).setText(s);
            }
        });
    }
}
