package com.zd112.framework.view.player;

import android.content.Context;
import android.util.AttributeSet;

import com.zd112.framework.view.player.media.VideoPlayerNormal;

/**
 * Created by etongdai on 2018/3/20.
 */

public class VideoPlayerNormalFresco extends VideoPlayerNormal {
    public VideoPlayerNormalFresco(Context context) {
        super(context);
    }

    public VideoPlayerNormalFresco(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init(Context context) {
        super.init(context);
//        bottomProgressBar = findViewById(R.id.bottom_progress);
//        titleTextView = findViewById(R.id.title);
//        backButton = findViewById(R.id.back);
////        thumbImageView = findViewById(R.id.thumb);
//        loadingProgressBar = findViewById(R.id.loading);
//        tinyBackImageView = findViewById(R.id.back_tiny);
//
////        thumbImageView.setOnClickListener(this);
//        backButton.setOnClickListener(this);
//        tinyBackImageView.setOnClickListener(this);

    }

    @Override
    public void setUp(String url, int screen, Object... objects) {
        super.setUp(url, screen, objects);
        if (objects.length == 0) return;
        titleTextView.setText(objects[0].toString());
//        if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
//            fullscreenButton.setImageResource(R.drawable.jz_shrink);
//            backButton.setVisibility(View.VISIBLE);
//            tinyBackImageView.setVisibility(View.INVISIBLE);
//        } else if (currentScreen == SCREEN_WINDOW_LIST) {
//            fullscreenButton.setImageResource(R.drawable.jz_enlarge);
//            backButton.setVisibility(View.GONE);
//            tinyBackImageView.setVisibility(View.INVISIBLE);
//        } else if (currentScreen == SCREEN_WINDOW_TINY) {
//            tinyBackImageView.setVisibility(View.VISIBLE);
//            setAllControlsVisiblity(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
//                    View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
//        }
    }

    @Override
    public int getLayoutId() {
//        return R.layout.layout_standard_fresco;
        return 0;
    }
}
