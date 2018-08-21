package com.zd112.framework.utils;

import android.content.Context;

import com.zd112.framework.R;
import com.zd112.framework.view.DialogView;

public class DialogUtils {

    private Context mContext;
    private boolean mIsFull = true;

    public DialogUtils(Context context) {
        this.mContext = context;
    }

    public DialogUtils(Context context, boolean isFull) {
        this.mContext = context;
        this.mIsFull = isFull;
    }

    protected DialogView mDialogView;

    public DialogView loading(String msg) {
        cancelLoading();
        mDialogView = new DialogView(mContext, R.style.DialogTheme).setOutsideClose(false).setContent(msg);
        mDialogView.show();
        return mDialogView;
    }

    public DialogView loading(int layout) {
        cancelLoading();
        mDialogView = new DialogView(mContext, R.style.DialogTheme, layout).setOutsideClose(false);
        mDialogView.show();
        return mDialogView;
    }

    public DialogView loading(int layout, DialogView.DialogViewListener dialogViewListener) {
        cancelLoading();
        mDialogView = new DialogView(mContext, R.style.DialogTheme, layout, dialogViewListener).setOutsideClose(mIsFull);
        mDialogView.show();
        return mDialogView;
    }

    public void cancelLoading() {
        if (mDialogView != null) {
            mDialogView.dismiss();
            mDialogView = null;
        }
    }
}
