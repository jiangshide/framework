package com.zd112.demo.ui.fragment;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.MainThread;
import android.support.annotation.WorkerThread;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.zd112.demo.HookActivity;
import com.zd112.demo.HookUtils;
import com.zd112.demo.R;
import com.zd112.demo.data.HomeData;
import com.zd112.demo.utils.Constant;
import com.zd112.framework.BaseFragment;
import com.zd112.framework.apdater.CommAdapter;
import com.zd112.framework.net.helper.NetInfo;
import com.zd112.framework.utils.LogUtils;
import com.zd112.framework.utils.SystemUtils;
import com.zd112.framework.utils.ViewUtils;
import com.zd112.framework.view.BannerView;
import com.zd112.framework.view.CusListView;
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
    private CusListView homeList;

    private CommAdapter<HomeData> circleListDataCommAdapter;
    private Intent batteryStatus;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setView(R.layout.ui_home, this, true);
        request("more/ind", HomeData.class, true);
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        batteryStatus = getActivity().registerReceiver(null,intentFilter);
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
        mHomeBanner.setOnBannerItemClickListener(new BannerView.OnBannerItemClickListener() {
            @Override
            public void onItemClick(int position) {
                LogUtils.e("-----------status:",batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS,-1));
                int cpuCount = Runtime.getRuntime().availableProcessors();
                int CORE_POOL_SIZE = Math.max(2, Math.min(cpuCount - 1, 4));
                LogUtils.e("-------------------cpuCount:",cpuCount," | CORE_POOL_SIZE:",CORE_POOL_SIZE);
//                ClassLoader classLoader = getActivity().getClass().getClassLoader();
//                while(classLoader != null){
//                    LogUtils.e("-------------load:",classLoader.toString());
//                    classLoader = classLoader.getParent();
//                }
                test();
                if(position == 1){
                    shouldRunOnWorkerThread();
                }else{
                    runOnMainThread();
                }
            }
        });
    }

    public void test(){
        String userAgent = getDefaultUserAgent();
        LogUtils.e("--------------userAgent:",userAgent);
        System.setProperty("http.agent",userAgent);
    }

    private String getDefaultUserAgent(){
        StringBuilder stringBuilder = new StringBuilder(64);
        stringBuilder.append("Dalvik/");
        stringBuilder.append(System.getProperty("java.vm.version"));
        stringBuilder.append(" (Linux; U; Android ");

        String version = Build.VERSION.RELEASE;
        stringBuilder.append(version.length()>0?version:"1.0");
        if("REL".equals(Build.VERSION.CODENAME)){
            String model = Build.MODEL;
            if(model.length() > 0){
                stringBuilder.append("; ");
                stringBuilder.append(model);
            }
        }
        String id = Build.ID;
        if(id.length() >0){
            stringBuilder.append(" Build/");
            stringBuilder.append(id);
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    @WorkerThread
    private void shouldRunOnWorkerThread(){
        LogUtils.e("-----------------shouldRunOnWorkerThread");
    }

    @MainThread
    private void runOnMainThread(){
        LogUtils.e("----------------runOnMainThread");
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        homeList.setAdapter(circleListDataCommAdapter = new CommAdapter<HomeData>(getActivity(), getTempData(), R.layout.ui_home_list_item) {
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
