package com.zd112.demo.home.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.zd112.demo.R;
import com.zd112.framework.BaseFragment;
import com.zd112.framework.apdater.CommAdapter;
import com.zd112.framework.utils.ViewUtils;
import com.zd112.framework.view.CusListView;
import com.zd112.framework.view.CusToast;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jiangshide
 * @Created by Ender on 2018/8/28.
 * @Emal:18311271399@163.com
 */
public class CommentFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    @ViewUtils.ViewInject(R.id.itemRate)
    private TextView itemRate;
    @ViewUtils.ViewInject(R.id.homeListView)
    private CusListView homeListView;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setView(R.layout.comm_item, this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        itemRate.setText("Comment");
        List<String> arrStr = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            arrStr.add("this is the title" + i);
        }
        homeListView.setAdapter(new CommAdapter<String>(getActivity(), arrStr, R.layout.home_list_item) {
            @Override
            protected void convertView(int position, View item, String s) {
                ((TextView) get(item, R.id.listItem)).setText(s);
            }
        });
        homeListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CusToast.txt(id + "position:" + position);
    }
}
