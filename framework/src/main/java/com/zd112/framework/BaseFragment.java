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
import com.zd112.framework.net.Net;
import com.zd112.framework.net.annotation.RequestStatus;
import com.zd112.framework.net.annotation.RequestType;
import com.zd112.framework.net.callback.Callback;
import com.zd112.framework.net.callback.ProgressCallback;
import com.zd112.framework.net.helper.NetInfo;
import com.zd112.framework.utils.LogUtils;
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
    protected int default_page = 1;
    protected int default_page_size = 20;
    protected boolean isRefresh;

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
        mRefreshView.setNoMoreData(default_page_size > resultSize);
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
        return ((BaseApplication) getActivity().getApplication()).loading(getActivity(), msg);
    }

    public DialogView loading(int layout) {
        return ((BaseApplication) getActivity().getApplication()).loading(getActivity(), layout);
    }

    public DialogView loading(int layout, DialogView.DialogViewListener dialogViewListener) {
        return ((BaseApplication) getActivity().getApplication()).loading(getActivity(), layout, dialogViewListener);
    }

    public void cancelLoading() {
        if (mRefreshView != null) {
            mRefreshView.finishRefresh();
            mRefreshView.finishLoadMore();
        }
        ((BaseApplication) getActivity().getApplication()).cancelLoading();
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
        return ((BaseApplication) getActivity().getApplication()).request(getActivity(), requestType, action, params, callback, _class, isLoading);
    }

    public Net.Builder download(String url, ProgressCallback progressCallback) {
        return ((BaseApplication) getActivity().getApplication()).download(url, progressCallback);
    }

    public Net.Builder download(String url, ProgressCallback progressCallback, Object tag) {
        return ((BaseApplication) getActivity().getApplication()).download(url, progressCallback, tag);
    }

    public Net.Builder download(String url, String saveFileName, ProgressCallback progressCallback, Object tag) {
        return ((BaseApplication) getActivity().getApplication()).download(url, saveFileName, progressCallback, tag);
    }

    @Override
    public void onSuccess(NetInfo info) {
        cancelRefresh();
        LogUtils.e("---info:", info);
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
        HashMap<String, String> params = new HashMap<>();
        params.put("page", default_page + "");
        params.put("pageSize", default_page_size + "");
        ((BaseApplication) getActivity().getApplication()).request(params, RequestStatus.REFRESH);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        isRefresh = false;
        default_page++;
        HashMap<String, String> params = new HashMap<>();
        params.put("page", default_page + "");
        params.put("pageSize", default_page_size + "");
        ((BaseApplication) getActivity().getApplication()).request(params, RequestStatus.MORE);
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

    protected abstract void setListener();

    protected abstract void processLogic(Bundle savedInstanceState);
}
