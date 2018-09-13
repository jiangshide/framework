package com.zd112.demo.ui.fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.zd112.demo.R;
import com.zd112.framework.BaseFragment;
import com.zd112.framework.utils.ImageUtils;
import com.zd112.framework.utils.ShareUtils;
import com.zd112.framework.utils.ViewUtils;
import com.zd112.framework.view.CusButton;
import com.zd112.framework.view.CusToast;
import com.zd112.framework.view.DialogView;
import com.zd112.framework.view.TagView;

import java.util.Random;

/**
 * @author jiangshide
 * @Created by Ender on 2018/8/28.
 * @Emal:18311271399@163.com
 */
public class CircleFragment extends BaseFragment implements DialogView.DialogViewListener {

    @ViewUtils.ViewInject(R.id.shareContactBbtn)
    private CusButton shareContactBbtn;
    @ViewUtils.ViewInject(R.id.shareFriendCircleBtn)
    private CusButton shareFriendCircleBtn;
    @ViewUtils.ViewInject(R.id.toastTipsBtn)
    private CusButton toastTipsBtn;
    @ViewUtils.ViewInject(R.id.dialogTipsBtn)
    private CusButton dialogTipsBtn;
    @ViewUtils.ViewInject(R.id.dialogBtn)
    private CusButton dialogBtn;
    @ViewUtils.ViewInject(R.id.dialogLoadingBtn)
    private CusButton dialogLoadingBtn;
    @ViewUtils.ViewInject(R.id.dialogCusViewBtn)
    private CusButton dialogCusViewBtn;
    @ViewUtils.ViewInject(R.id.tagViewBtn)
    private CusButton tagViewBtn;
    @ViewUtils.ViewInject(R.id.tagView)
    private TagView mTagView;

    public final static int INVITE_FRIENDS = 0;
    public final static int SHARE_FRIEND = 1;

    private String[] mDatas = new String[]{"支", "支付", "支付宝", "支付宝钱", "支付宝钱包", "支付宝钱包多点", "支付宝钱包多点", "支付宝钱包多点", "支付宝钱包多点"};
    private int[] colors = {Color.GRAY, Color.LTGRAY, Color.RED, Color.YELLOW, Color.BLUE};

    private String shareUrl = "http://api.etongdai.com/images/share/share1.png";
    private String shareContent = "----7年平台，220亿交易额，531万投资人的选择！快来领取红包，一起来赚钱吧！";
    private String shareTitle = "我在易通贷投资，推荐给你！注册送1088元红包！";
    private String sharePageUrl = "https://m.etongdai.com/static/register/invitefriend.html?friendId=MTA2NjUxNDc=";

    @Override
    protected void initView(Bundle savedInstanceState) {
        setView(R.layout.ui_circle, this);
        showStatusBar();
    }

    @Override
    protected void setListener() {
        super.setListener();
        tagViewBtn.setOnClickListener(this);
        dialogTipsBtn.setOnClickListener(this);
        toastTipsBtn.setOnClickListener(this);
        dialogBtn.setOnClickListener(this);
        dialogLoadingBtn.setOnClickListener(this);
        dialogCusViewBtn.setOnClickListener(this);
        shareContactBbtn.setOnClickListener(this);
        shareFriendCircleBtn.setOnClickListener(this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.shareContactBbtn:
                openWeiXin(INVITE_FRIENDS, shareUrl, sharePageUrl, shareTitle, shareContent);
                break;
            case R.id.shareFriendCircleBtn:
                openWeiXin(SHARE_FRIEND, shareUrl, sharePageUrl, shareTitle, shareContent);
                break;
            case R.id.toastTipsBtn:
                CusToast.txt("this is the toast!");
                break;
            case R.id.dialogTipsBtn:
                CusToast.fixTxt(getActivity(), "this is the toast!");
                break;
            case R.id.dialogLoadingBtn:
                new DialogView(getActivity(), R.style.DialogTheme, R.layout.default_loading).setOutsideClose(false).show();
                break;
            case R.id.dialogBtn:
                loading("this is the dialog!");
                break;
            case R.id.dialogCusViewBtn:
                loading(R.layout.default_dialog, this);
                break;
            case R.id.dialogSure:
                cancelLoading();
                break;
            case R.id.tagViewBtn:
                mTagView.setPadding(10, 10, 10, 10);
                Random random = new Random();
                for (int i = 0; i < mDatas.length; i++) {
                    TextView textView = new TextView(getContext());
                    textView.setText(mDatas[i]);
                    textView.setBackgroundColor(colors[random.nextInt(5)]);
                    textView.setPadding(5, 5, 5, 5);
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextSize(random.nextInt(10) + 16);
                    mTagView.addView(textView);
                }
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    public void openWeiXin(final int scene, String imgUrl, final String pageUrl, final String title, final String content) {
        new ImageUtils(imgUrl, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Bitmap bitmap = null;
                switch (msg.what) {
                    case ImageUtils.SUCCESS:
                        bitmap = (Bitmap) msg.obj;
                        break;
                    case ImageUtils.FALSE:
                        CusToast.txt(msg.toString());
                        break;
                }
                ShareUtils.shareWeixin(scene, pageUrl, title, content, bitmap);
            }
        }).start();
    }

    @Override
    public void onView(View view) {
        view.findViewById(R.id.dialogSure).setOnClickListener(this);
    }
}
