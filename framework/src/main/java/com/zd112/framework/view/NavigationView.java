package com.zd112.framework.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zd112.framework.R;
import com.zd112.framework.listener.CusOnClickListener;
import com.zd112.framework.utils.LogUtils;
import com.zd112.framework.utils.ShareParamUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jiangshide
 * @Created by Ender on 2018/8/24.
 * @Emal:18311271399@163.com
 */
public class NavigationView extends FrameLayout {

    private ImageView mImageView;
    private View mMenuBg;

    private List<FrameLayout> mMenus;
    private List<ImageView> mMenuIcons;
    private List<TextView> mMenuTitles, mMenuPorts;
    public final static int MAIN = 0x12345678;

    private int[] mSelectes;
    private int[] mSelecteds;
    private int mTxtColor;
    private int mTxtColorSelected;
    private CusOnClickListener mListener;

    private LinearLayout mMenusLayout;
    private int mNavigationHeight;
    private boolean mIsShowPortNum = true;

    public NavigationView(Context context) {
        super(context);
        mNavigationHeight = (int) getDim(R.dimen.navigation_height);
        FrameLayout content = new FrameLayout(getContext());
        content.setPadding(0, 0, 0, mNavigationHeight);
        content.setId(MAIN);
        this.addView(content, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public NavigationView initView(int index, int[] selectes, int[] selecteds, String[] titles, int txtColor, int txtColorSelected, CusOnClickListener listener) {
        this.mSelectes = selectes;
        this.mSelecteds = selecteds;
        this.mTxtColor = txtColor;
        this.mTxtColorSelected = txtColorSelected;
        this.mListener = listener;
        mMenus = new ArrayList<>();
        mMenuIcons = new ArrayList<>();
        mMenuTitles = new ArrayList<>();
        mMenuPorts = new ArrayList<>();
        mMenusLayout = new LinearLayout(getContext());
        mMenusLayout.setOrientation(LinearLayout.HORIZONTAL);
        mMenusLayout.setPadding(0, 5, 0, 5);
        for (int i = 0; i < selectes.length; i++) {
            FrameLayout menuRoot = new FrameLayout(getContext());
            LinearLayout menuLayout = new LinearLayout(getContext());
            menuRoot.setId(i);
            menuLayout.setOrientation(LinearLayout.VERTICAL);
            ImageView imageView = new ImageView(getContext());
            if (i == index) {
                imageView.setImageResource(selecteds[i]);
            } else {
                imageView.setImageResource(selectes[i]);
            }
            mMenuIcons.add(imageView);
            menuLayout.addView(imageView);

            TextView titleTxt = new TextView(getContext());
            titleTxt.setGravity(Gravity.CENTER);
            titleTxt.setTextSize(getDim(R.dimen.navigation_txt_size));
            titleTxt.setTop(10);
            if (i == index) {
                titleTxt.setTextColor(getColor(txtColorSelected));
            } else {
                titleTxt.setTextColor(getColor(txtColor));
            }
            titleTxt.setText(titles[i]);
            mMenuTitles.add(titleTxt);
            menuLayout.addView(titleTxt);

            TextView portTxt = new TextView(getContext());
            portTxt.setBackgroundResource(R.drawable.red_dot);
            portTxt.setTextColor(getColor(R.color.red));
            portTxt.setTextSize(getDim(R.dimen.navigation_port_size));
            portTxt.setTextColor(getColor(R.color.white));
            portTxt.setVisibility(GONE);
            mMenuPorts.add(portTxt);

            menuRoot.addView(menuLayout);
            menuRoot.addView(portTxt, new LinearLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));

            menuRoot.setOnClickListener(new CusOnClickListener() {
                @Override
                public void onClick(View v, Bundle bundle) {
                    if (mListener != null) {
                        mListener.onClick(v, bundle);
                    }
                }

                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onClick(v);
                    }
                }
            });
            mMenus.add(menuRoot);
            mMenusLayout.addView(menuRoot, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER | Gravity.TOP;
            params.setMargins((int) getDim(R.dimen.navigation_port), 0, 0, 0);
            portTxt.setLayoutParams(params);
        }
        mMenuBg = new View(getContext());
        mMenuBg.setBackgroundResource(R.drawable.bg_stroke_grey);
        this.addView(mMenuBg);
        mMenusLayout.setGravity(Gravity.BOTTOM);
        this.addView(mMenusLayout);
        FrameLayout.LayoutParams menuParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, mNavigationHeight);
        menuParams.gravity = Gravity.BOTTOM;
        mMenuBg.setLayoutParams(menuParams);

        mImageView = new ImageView(getContext());
        mImageView.setScaleType(ImageView.ScaleType.MATRIX);
        this.addView(mImageView, new FrameLayout.LayoutParams(160, 160));
        invalidate();
        return this;
    }

    public ImageView showIcon(String url) {
        LogUtils.e("url:", url);
        if (mImageView != null) {
            mImageView.setVisibility(View.VISIBLE);
            Glide.with(getContext()).load(url).into(mImageView);
            ShareParamUtils.INSTANCE.putString("url", url);
        }
        return mImageView;
    }

    public ImageView scrollScan(int width, int height) {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width, height);
        layoutParams.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        mImageView.setLayoutParams(layoutParams);
        return mImageView;
    }

    public NavigationView isHideIcon(boolean isHide) {
        if (mImageView != null) {
            if (isHide) {
                mImageView.setVisibility(View.GONE);
            } else {
                String url = ShareParamUtils.getString("url");
                LogUtils.e("url1:", url);
                showIcon(url);
                mImageView.setVisibility(View.VISIBLE);
            }
        }
        return this;
    }

    public NavigationView setIcon(int icon) {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        mImageView.setLayoutParams(layoutParams);
        mImageView.setImageResource(icon);
        return this;
    }

    public NavigationView setMenuBgColor(int color) {
        mMenuBg.setBackgroundColor(color);
        return this;
    }

    public NavigationView setBgResource(int resIcon) {
        mMenusLayout.setBackgroundResource(resIcon);
        return this;
    }

    public NavigationView setBgColor(int resColor) {
        this.setBackgroundColor(resColor);
        return this;
    }

    public NavigationView setTxtSize(int size) {
        for (TextView textView : mMenuTitles) {
            textView.setTextSize(size);
        }
        return this;
    }

    public NavigationView setTxtSize(int size, int index) {
        if (mMenuTitles != null && index >= 0 && mMenuTitles.size() > index) {
            mMenuTitles.get(index).setTextSize(size);
        }
        return this;
    }

    public NavigationView selectedTxtSize(int size) {
        int lenth = mMenuTitles.size();
        for (int i = 0; i < lenth; i++) {
            if (size == i) {
                mMenuTitles.get(i).setTextSize(size);
            } else {
                mMenuTitles.get(i).setTextSize(getDim(R.dimen.navigation_txt_size));
            }
        }
        invalidate();
        return this;
    }

    public NavigationView showPort(int id, int count) {
        if (count < 10) {
            mMenuPorts.get(id).setPadding(15, 5, 15, 5);
        }
        mMenuPorts.get(id).setVisibility(VISIBLE);
        mMenuPorts.get(id).setText(count > 99 ? "99+" : count + "");
        if (!mIsShowPortNum) {
            mMenuPorts.get(id).setPadding(10, 0, 10, 0);
            mMenuPorts.get(id).setTextColor(getColor(R.color.red));
        }
        return this;
    }

    private int getColor(int color) {
        return ContextCompat.getColor(getContext(), color);
    }

    private float getDim(int dim) {
        return getResources().getDimension(dim);
    }

    public NavigationView changeBarListener(int id) {
        return changeBarListener(id, null);
    }

    public NavigationView changeBarListener(int id, Bundle bundle) {
        if (bundle == null) {
            if (mListener != null && mMenus != null) {
                mListener.onClick(mMenus.get(id));
            }
        } else {
            if (mListener != null && mMenus != null) {
                mListener.onClick(mMenus.get(id), bundle);
            }
        }
        return this;
    }

    public void changeBarStatus(int id) {
        for (int j = 0; j < mMenuIcons.size(); j++) {
            if (j == id) {
                mMenuIcons.get(j).setImageResource(mSelecteds[j]);
                mMenuTitles.get(j).setTextColor(getColor(mTxtColorSelected));
            } else {
                mMenuIcons.get(j).setImageResource(mSelectes[j]);
                mMenuTitles.get(j).setTextColor(getColor(mTxtColor));
            }
        }
    }
}
