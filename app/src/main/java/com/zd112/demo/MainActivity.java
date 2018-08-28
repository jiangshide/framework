package com.zd112.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.zd112.demo.data.TransferredData;
import com.zd112.framework.BaseActivity;
import com.zd112.framework.apdater.CommAdapter;
import com.zd112.framework.net.helper.NetInfo;
import com.zd112.framework.utils.DoubleUtil;
import com.zd112.framework.utils.ViewUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class MainActivity extends BaseActivity {

    @ViewUtils.ViewInject(R.id.input)
    private EditText input;
    @ViewUtils.ViewInject(R.id.mainTotal)
    private TextView mainTotal;
    @ViewUtils.ViewInject(R.id.listView)
    private ListView listView;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setView(R.layout.activity_main, this, true);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        load();
    }

    public void testBtn(View view) {
        load();
    }

    private void load() {
        HashMap<String, String> params = new HashMap<>();
        params.put("orderArgs", "2");
        params.put("sort", "desc");
        params.put("useType", "1");
        request("investments/list", params, TransferredData.class);
    }

    @Override
    public void onSuccess(NetInfo info) {
        super.onSuccess(info);
        TransferredData transferredData = info.getResponseObj();
        for (TransferredData.Res.ListItem listItem : transferredData.res.lists) {
            listItem.rate = DoubleUtil.round(((listItem.claTransClaimSumYuan - listItem.claTransSumYuan) / listItem.claTransClaimSumYuan) * 100, 2);
        }
        Collections.sort(transferredData.res.lists, new Comparator<TransferredData.Res.ListItem>() {
            @Override
            public int compare(TransferredData.Res.ListItem o1, TransferredData.Res.ListItem o2) {
                return o1.rate < o2.rate ? 1 : o1.rate > o2.rate ? -1 : 0;
            }
        });
        mainTotal.setText("总共" + transferredData.res.lists.size() + "条");
        listView.setAdapter(new CommAdapter<TransferredData.Res.ListItem>(this, transferredData.res.lists, R.layout.comm_item) {
            @Override
            protected void convertView(int position, View item, TransferredData.Res.ListItem listItem) {
                ((TextView) get(item, R.id.itemRate)).setText("剩余本息:" + listItem.claTransClaimSumYuan + " | 转让价格:" + listItem.claTransSumYuan + " | 利率:" + DoubleUtil.formatStr(listItem.iteYearRate * 100) + "% | 剩余时间:" + listItem.iteRepayInterval + listItem.iteRepayIntervalName + " | 回收率:" + DoubleUtil.formatStr(listItem.rate) + "%");
            }
        });
    }
}
