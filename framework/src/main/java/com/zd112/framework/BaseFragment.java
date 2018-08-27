package com.zd112.framework;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zd112.framework.listener.CusOnClickListener;
import com.zd112.framework.net.annotation.RequestStatus;
import com.zd112.framework.net.annotation.RequestType;
import com.zd112.framework.net.callback.Callback;
import com.zd112.framework.net.callback.ProgressCallback;
import com.zd112.framework.net.helper.NetInfo;
import com.zd112.framework.utils.SystemUtils;
import com.zd112.framework.utils.ViewUtils;
import com.zd112.framework.view.DialogView;
import com.zd112.framework.view.refresh.RefreshView;
import com.zd112.framework.view.refresh.interfaces.OnLoadMoreListener;
import com.zd112.framework.view.refresh.interfaces.OnRefreshListener;
import com.zd112.framework.view.refresh.interfaces.RefreshLayout;

import java.util.HashMap;

public abstract class BaseFragment extends Fragment implements CusOnClickListener, Callback, OnRefreshListener, OnLoadMoreListener {
    protected View mView;
    protected Bundle mBundle;
    protected RefreshView mRefreshView;
    private boolean mIsLoadMore;
    private NetInfo.Builder mBuilder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBundle = getArguments();
        if (mView == null) {
            initView(savedInstanceState);
            setListener();
//            processLogic(savedInstanceState);
        } else {
            ViewGroup viewGroup = (ViewGroup) mView.getParent();
            if (viewGroup != null) {
                viewGroup.removeView(mView);
            }
        }
        processLogic(savedInstanceState);
        return mView;
    }

    protected RefreshView setView(@LayoutRes int layout, Object object) {
        setView(layout, object, false);
        return mRefreshView;
    }

    protected RefreshView setView(@LayoutRes int layout, Object object, boolean isRefresh) {
        if (isRefresh) {
            mRefreshView = new RefreshView(getContext());
            mRefreshView.setOnRefreshListener(this);
            mRefreshView.setOnLoadMoreListener(this);
            mRefreshView.addView(LayoutInflater.from(getContext()).inflate(layout, null));
            mView = mRefreshView;
        } else {
            mView = LayoutInflater.from(getContext()).inflate(layout, null);
        }
        ViewUtils.inject(object, mView);
        return mRefreshView;
    }

    protected void showStatusBar() {
        if (mView == null) return;
        View view = mView.findViewById(R.id.statusBar);
        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SystemUtils.getStatusBarHeight(getActivity())));
    }

    public void setEnableMore(int resultSize) {
        if (mRefreshView == null) return;
//        mRefreshView.setNoMoreData(mDefaultPageSize > resultSize);
    }

    protected int getColor(int resColor) {
        return getResources().getColor(resColor);
    }

    protected String getString(String key) {
        if (mBundle != null && mBundle.containsKey(key)) {
            return mBundle.getString(key);
        }
        return null;
    }

    public String[] getArrStr(int resArrStr) {
        return getResources().getStringArray(resArrStr);
    }

    public void push(Fragment fragment) {
        ((BaseActivity) getActivity()).push(fragment);
    }

    public DialogView loading(String msg) {
        return BaseApplication.mApplication.loading(getActivity(), msg);
    }

    public DialogView loading(int layout) {
        return BaseApplication.mApplication.loading(getActivity(), layout);
    }

    public DialogView loading(int layout, DialogView.DialogViewListener dialogViewListener) {
        return BaseApplication.mApplication.loading(getActivity(), layout, dialogViewListener);
    }

    public void request(String action) {
        this.request(action, null, this, null, true);
    }

    public void request(String action, Class _class) {
        this.request(action, null, this, _class, true);
    }

    public void request(String action, Callback callback) {
        this.request(action, null, callback, null, true);
    }

    public void request(String action, Callback callback, Class _class) {
        this.request(action, null, callback, _class, true);
    }

    public void request(String action, boolean isLoading) {
        this.request(action, null, this, null, isLoading);
    }

    public void request(String action, Class _class, boolean isLoading) {
        this.request(action, null, this, _class, isLoading);
    }

    public void request(String action, Callback callback, boolean isLoading) {
        this.request(action, null, callback, null, isLoading);
    }

    public void request(String action, Callback callback, Class _class, boolean isLoading) {
        this.request(action, null, callback, _class, isLoading);
    }

    public void request(String action, HashMap<String, String> params) {
        this.request(action, params, this, null, true);
    }

    public void request(String action, HashMap<String, String> params, Class _class) {
        this.request(action, params, this, _class, true);
    }

    public void request(String action, HashMap<String, String> params, Callback callback) {
        this.request(action, params, callback, null, true);
    }

    public void request(String action, HashMap<String, String> params, Callback callback, Class _class) {
        this.request(action, params, callback, _class, true);
    }

    public void request(String action, HashMap<String, String> params, boolean isLoading) {
        this.request(action, params, this, null, isLoading);
    }

    public void request(String action, HashMap<String, String> params, Class _class, boolean isLoading) {
        this.request(action, params, this, _class, isLoading);
    }

    public void request(String action, HashMap<String, String> params, Callback callback, Class _class, boolean isLoading) {
        this.request(RequestType.GET, action, params, callback, _class, isLoading);
    }

    public void request(int requestType, String action, HashMap<String, String> params, Callback callback, Class _class, boolean isLoading) {
        BaseApplication.mApplication.request(getActivity(), BaseApplication.mApplication.build(requestType, action, params, _class), callback, isLoading);
    }

    public void download(String url, ProgressCallback progressCallback) {
        this.download(url, progressCallback, this);
    }

    public void download(String url, ProgressCallback progressCallback, Object tag) {
        this.download(url, null, progressCallback, tag);
    }

    public void download(String url, String saveFileName, ProgressCallback progressCallback, Object tag) {
        BaseApplication.mApplication.request(BaseApplication.mApplication.build(url, saveFileName, progressCallback), tag);
    }

    @Override
    public void onSuccess(NetInfo info) {
        cancelRefresh();
        mBuilder = info.getBuild();
    }

    @Override
    public void onFailure(NetInfo info) {
        mBuilder = info.getBuild();
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
        if (mRefreshView != null) {
            mRefreshView.setNoMoreData(false);
        }
        BaseApplication.mApplication.mNetBuilder.build().doAsync(mBuilder.setStatus(RequestStatus.REFRESH).build(), this);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        mIsLoadMore = true;
        BaseApplication.mApplication.mNetBuilder.build().doAsync(mBuilder.setStatus(RequestStatus.MORE).build(), this);
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

    protected abstract void initView(Bundle savedInstanceState);

    protected void setListener() {
    }

    protected abstract void processLogic(Bundle savedInstanceState);
}
