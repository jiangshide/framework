package com.zd112.framework;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zd112.framework.listener.CusOnClickListener;
import com.zd112.framework.net.Net;
import com.zd112.framework.net.annotation.RequestType;
import com.zd112.framework.net.callback.Callback;
import com.zd112.framework.net.callback.ProgressCallback;
import com.zd112.framework.net.helper.NetInfo;
import com.zd112.framework.utils.DateUtils;
import com.zd112.framework.utils.SystemUtils;
import com.zd112.framework.utils.ViewUtils;
import com.zd112.framework.view.DialogView;
import com.zd112.framework.view.NavigationView;
import com.zd112.framework.view.refresh.RefreshView;
import com.zd112.framework.view.refresh.interfaces.OnLoadMoreListener;
import com.zd112.framework.view.refresh.interfaces.OnRefreshListener;
import com.zd112.framework.view.refresh.interfaces.RefreshLayout;

import java.util.HashMap;

public abstract class BaseActivity extends AppCompatActivity implements CusOnClickListener, Callback, OnRefreshListener, OnLoadMoreListener {

    protected FragmentManager fragmentManager;
    protected FragmentTransaction fragmentTransaction;
    protected View mView;
    public NavigationView navigationBar;
    protected RefreshView mRefreshView;
    private DateUtils dateUtils;
    protected int default_page = 1;
    protected int default_page_size = 10;
    protected boolean isRefresh;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getSupportFragmentManager();
        navigationBar = new NavigationView(this);
        if (mView == null) {
            initView(savedInstanceState);
            setListener();
//            processLogic(savedInstanceState);
        } else {
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (parent != null) {
                parent.removeView(mView);
            }
        }
        processLogic(savedInstanceState);
    }

    protected void setTitle(int leftResId, String name, int resId, String title) {
        if (mView == null) return;
        mView.findViewById(leftResId).setOnClickListener(this);
        ((TextView) mView.findViewById(resId)).setText(title);
    }

    protected RefreshView setView(@LayoutRes int layout, Object object) {
        setView(layout, object, false);
        return mRefreshView;
    }

    protected RefreshView setView(@LayoutRes int layout, Object object, boolean isRefresh) {
        setView(LayoutInflater.from(this).inflate(layout, null), object, isRefresh);
        return mRefreshView;
    }

    protected RefreshView setView(View view, Object object) {
        setView(view, object, false);
        return mRefreshView;
    }

    protected void setView(View view, Object object, boolean isRefresh) {
        mRefreshView = new RefreshView(this);
        if (isRefresh) {
            mRefreshView.setOnRefreshListener(this);
            mRefreshView.setOnLoadMoreListener(this);
            mRefreshView.addView(view);
            mView = mRefreshView;
        } else {
            mView = view;
        }
        setContentView(mView);
        ViewUtils.inject(object, mView);
        SystemUtils.setNoStatusBarFullMode(this, false);
    }

    public void setEnableMore(int resultSize) {
        if (mRefreshView == null) return;
        mRefreshView.setNoMoreData(default_page_size > resultSize);
    }

    public int getResColor(int resColor) {
        return getResources().getColor(resColor);
    }

    public String[] getResArrStr(int resArrStr) {
        return getResources().getStringArray(resArrStr);
    }

    public void push(Fragment fragment) {
        push(fragment, null);
    }

    public void push(Fragment fragment, Bundle bundle) {
        fragment.setArguments(bundle);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(NavigationView.main, fragment);
        fragmentTransaction.commit();
    }

    public DialogView loading(String msg) {
        return ((BaseApplication) getApplication()).loading(this, msg);
    }

    public DialogView loading(int layout) {
        return ((BaseApplication) getApplication()).loading(this, layout);
    }

    public DialogView loading(int layout, DialogView.DialogViewListener dialogViewListener) {
        return ((BaseApplication) getApplication()).loading(this, layout, dialogViewListener);
    }

    public void cancelLoading() {
        if (mRefreshView != null) {
            mRefreshView.finishRefresh();
            mRefreshView.finishLoadMore();
        }
        ((BaseApplication) getApplication()).cancelLoading();
    }

    public NetInfo.Builder request(String action) {
        return request(action, null, this, null, true);
    }

    public NetInfo.Builder request(String action, Class _class) {
        return request(action, null, this, _class, true);
    }

    public NetInfo.Builder request(String action, Callback callback) {
        return request(action, null, callback, null, true);
    }

    public NetInfo.Builder request(String action, Callback callback, Class _class) {
        return request(action, null, callback, _class, true);
    }

    public NetInfo.Builder request(String action, boolean isLoading) {
        return request(action, null, this, null, isLoading);
    }

    public NetInfo.Builder request(String action, Class _class, boolean isLoading) {
        return request(action, null, this, _class, isLoading);
    }

    public NetInfo.Builder request(String action, Callback callback, boolean isLoading) {
        return request(action, null, callback, null, isLoading);
    }

    public NetInfo.Builder request(String action, Callback callback, Class _class, boolean isLoading) {
        return request(action, null, callback, _class, isLoading);
    }

    public NetInfo.Builder request(String action, HashMap<String, String> params) {
        return request(action, params, this, null, true);
    }

    public NetInfo.Builder request(String action, HashMap<String, String> params, Class _class) {
        return request(action, params, this, _class, true);
    }

    public NetInfo.Builder request(String action, HashMap<String, String> params, Callback callback) {
        return request(action, params, callback, null, true);
    }

    public NetInfo.Builder request(String action, HashMap<String, String> params, Callback callback, Class _class) {
        return request(action, params, callback, _class, true);
    }

    public NetInfo.Builder request(String action, HashMap<String, String> params, boolean isLoading) {
        return request(action, params, this, null, isLoading);
    }

    public NetInfo.Builder request(String action, HashMap<String, String> params, Class _class, boolean isLoading) {
        return request(action, params, this, _class, isLoading);
    }

    public NetInfo.Builder request(String action, HashMap<String, String> params, Callback callback, Class _class, boolean isLoading) {
        return request(RequestType.GET, action, params, callback, _class, isLoading);
    }

    public NetInfo.Builder request(int requestType, String action, HashMap<String, String> params, Callback callback, Class _class, boolean isLoading) {
        if (null == params) {
            params = new HashMap<>();
        }
        params.put("page", default_page + "");
        params.put("pageSize", default_page_size + "");
        return ((BaseApplication) this.getApplication()).request(this, requestType, action, params, callback, _class, isLoading);
    }

    public Net.Builder download(String url, ProgressCallback progressCallback) {
        return ((BaseApplication) this.getApplication()).download(url, progressCallback);
    }

    public Net.Builder download(String url, ProgressCallback progressCallback, Object tag) {
        return ((BaseApplication) this.getApplication()).download(url, progressCallback, tag);
    }

    public Net.Builder download(String url, String saveFileName, ProgressCallback progressCallback, Object tag) {
        return ((BaseApplication) this.getApplication()).download(url, saveFileName, progressCallback, tag);
    }

    @Override
    public void onSuccess(NetInfo info) {
        cancelRefresh();
    }

    @Override
    public void onFailure(NetInfo info) {
        cancelRefresh();
    }

    private void cancelRefresh() {
        if (mRefreshView != null) {
            mRefreshView.finishRefresh();
            mRefreshView.finishLoadMore();
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        isRefresh = true;
        default_page = 1;
        if (mRefreshView != null) {
            mRefreshView.setNoMoreData(false);
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        isRefresh = false;
        default_page++;
    }

    protected void scrollTxt(TextView textView, String txt) {
        textView.setText(TextUtils.isEmpty(txt) ? "" : txt);
        textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        textView.setSingleLine(true);
        textView.setSelected(true);
        textView.setFocusable(true);
        textView.setFocusableInTouchMode(true);
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onClick(View v, Bundle bundle) {
    }

    public DateUtils countDown(long second) {
        return countDown(second * 1000, 1000);
    }

    protected DateUtils countDown(long millisInFuture, long countDownInterval) {
        cancelTime();
        return dateUtils = new DateUtils(millisInFuture, countDownInterval);
    }

    protected void cancelTime() {
        if (dateUtils != null) {
            dateUtils.cancel();
            dateUtils = null;
        }
    }

    protected abstract void initView(Bundle savedInstanceState);

    protected void setListener(){};

    protected abstract void processLogic(Bundle savedInstanceState);

    @Override
    protected void onDestroy() {
        cancelTime();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        new DialogView(this, R.style.DialogTheme).setOutsideClose(false).setListener(new DialogView.DialogOnClickListener() {
            @Override
            public void onDialogClick(boolean isCancel) {
                if (!isCancel) {
                    BaseActivity.super.onBackPressed();
                }
            }
        }).show();
    }
}
