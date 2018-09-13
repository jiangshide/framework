package com.zd112.demo.ui.fragment.home;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.zd112.demo.R;
import com.zd112.demo.ui.fragment.home.data.HomeData;
import com.zd112.demo.utils.Constant;
import com.zd112.framework.BaseFragment;
import com.zd112.framework.apdater.CommAdapter;
import com.zd112.framework.net.helper.NetInfo;
import com.zd112.framework.utils.LogUtils;
import com.zd112.framework.utils.SystemUtils;
import com.zd112.framework.utils.ViewUtils;
import com.zd112.framework.view.BannerView;
import com.zd112.framework.view.CusToast;
import com.zd112.framework.view.player.media.VideoPlayer;
import com.zd112.framework.view.player.media.VideoPlayerNormal;

import java.util.ArrayList;

/**
 * @author jiangshide
 * @Created by Ender on 2018/8/28.
 * @Emal:18311271399@163.com
 */
public class HomeFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    @ViewUtils.ViewInject(R.id.homeBanner)
    private BannerView mHomeBanner;
    @ViewUtils.ViewInject(R.id.homeList)
    private ListView homeList;

    private CommAdapter<HomeData> circleListDataCommAdapter;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setView(R.layout.home, this, true);
        SystemUtils.setNoStatusBarFullMode(getActivity(), true);
        request("more/ind", HomeData.class, true);
    }

    @Override
    protected void setListener() {
        super.setListener();
        homeList.setOnItemClickListener(this);
        homeList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                CusToast.txt("scrollState:", scrollState);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                VideoPlayer.onScrollAutoTiny(view, firstVisibleItem, visibleItemCount, totalItemCount);
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        homeList.setAdapter(circleListDataCommAdapter = new CommAdapter<HomeData>(getActivity(), getTempData(), R.layout.home_list_item) {
            @Override
            protected void convertView(int position, View item, HomeData circleListData) {
                VideoPlayerNormal circleListViewItemVideo = get(item, R.id.circleListViewItemVideo);
                circleListViewItemVideo.setUp(circleListData.url, VideoPlayer.SCREEN_WINDOW_LIST, circleListData.name);
                Glide.with(item.getContext()).load(circleListData.thumb).into(circleListViewItemVideo.thumbImageView);
                circleListViewItemVideo.positionInList = position;
            }
        });
    }

    private ArrayList<HomeData> getTempData() {
        ArrayList<HomeData> listData = new ArrayList<>();
        for (int i = 0; i < Constant.videoUrls[0].length; i++) {
            HomeData circleListData = new HomeData();
            circleListData.name = Constant.videoTitles[0][i];
            circleListData.url = Constant.videoUrls[0][i];
            circleListData.thumb = Constant.videoThumbs[0][i];
            listData.add(circleListData);
        }
        return listData;
    }

    @Override
    public void onSuccess(NetInfo info) {
        super.onSuccess(info);
        LogUtils.e("---info:", info.getRetDetail());
        HomeData homeData = info.getResponseObj();
        circleListDataCommAdapter.addList(getTempData(), info.getStatus());
        ArrayList<String> bannerUrls = new ArrayList<>();
        for (HomeData.Res.PicList picList : homeData.res.picLists) {
            bannerUrls.add(picList.picUrl);
        }
        mHomeBanner.setSource(bannerUrls);
    }

    @Override
    public void onPause() {
        super.onPause();
        VideoPlayer.releaseAllVideos();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
