package com.zd112.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.zd112.demo.circle.CircleFragment;
import com.zd112.demo.home.HomeFragment;
import com.zd112.demo.mine.MineFragment;
import com.zd112.demo.pulish.PulishFragment;
import com.zd112.demo.shop.ShopFragment;
import com.zd112.demo.user.AppSessionEngine;
import com.zd112.demo.user.LoginActivity;
import com.zd112.framework.BaseActivity;

public class MainActivity extends BaseActivity {

    private Fragment[] fragments = {new HomeFragment(), new CircleFragment(), new PulishFragment(), new ShopFragment(), new MineFragment()};
    private int id = 0;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setView(mNavigationBar.initView(id, new int[]{R.mipmap.tab_home, R.mipmap.tab_circle, R.mipmap.tab_publish, R.mipmap.tab_shop, R.mipmap.tab_mine}, new int[]{R.mipmap.tab_home_selected, R.mipmap.tab_circle_selected, R.mipmap.tab_publish_selected, R.mipmap.tab_shop_selected, R.mipmap.tab_mine_selected}, getResArrStr(R.array.tab_main_title), R.color.font_gray, R.color.colorPrimary, this)
                .setBgColor(getResColor(R.color.app_bg)), this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        push(fragments[id]);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        filterLogin(v.getId());
    }

    @Override
    public void onClick(View v, Bundle bundle) {
        super.onClick(v, bundle);
        filterLogin(v.getId());
    }

    private void filterLogin(int id) {
        if (AppSessionEngine.getUseId() > 0 && id == 2) {
            startActivity(new Intent(this, LoginActivity.class));
        }
        mNavigationBar.changeBarStatus(id);
        push(fragments[id]);
    }

    //    private void load() {
//        HashMap<String, String> params = new HashMap<>();
//        params.put("orderArgs", "2");
//        params.put("sort", "desc");
//        params.put("useType", "1");
//        request("investments/list", params, TransferredData.class);
//    }

//    @Override
//    public void onSuccess(NetInfo info) {
//        super.onSuccess(info);
//        TransferredData transferredData = info.getResponseObj();
//        for (TransferredData.Res.ListItem listItem : transferredData.res.lists) {
//            listItem.rate = DoubleUtil.round(((listItem.claTransClaimSumYuan - listItem.claTransSumYuan) / listItem.claTransClaimSumYuan) * 100, 2);
//        }
//        Collections.sort(transferredData.res.lists, new Comparator<TransferredData.Res.ListItem>() {
//            @Override
//            public int compare(TransferredData.Res.ListItem o1, TransferredData.Res.ListItem o2) {
//                return o1.rate < o2.rate ? 1 : o1.rate > o2.rate ? -1 : 0;
//            }
//        });
//        mainTotal.setText("总共" + transferredData.res.lists.size() + "条");
//        listView.setAdapter(new CommAdapter<TransferredData.Res.ListItem>(this, transferredData.res.lists, R.layout.comm_item) {
//            @Override
//            protected void convertView(int position, View item, TransferredData.Res.ListItem listItem) {
//                ((TextView) get(item, R.id.itemRate)).setText("剩余本息:" + listItem.claTransClaimSumYuan + " | 转让价格:" + listItem.claTransSumYuan + " | 利率:" + DoubleUtil.formatStr(listItem.iteYearRate * 100) + "% | 剩余时间:" + listItem.iteRepayInterval + listItem.iteRepayIntervalName + " | 回收率:" + DoubleUtil.formatStr(listItem.rate) + "%");
//            }
//        });
//    }
}
