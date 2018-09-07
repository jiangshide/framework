package com.zd112.framework.view.player;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.zd112.framework.view.player.media.VideoPlayerNormal;

/**
 * Created by etongdai on 2018/3/20.
 */

public class VideoPlayerStandardShowShareButtonAfterFullscreen extends VideoPlayerNormal {
    public ImageView shareButton;

    public VideoPlayerStandardShowShareButtonAfterFullscreen(Context context) {
        super(context);
    }

    public VideoPlayerStandardShowShareButtonAfterFullscreen(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init(Context context) {
        super.init(context);
//        shareButton = findViewById(R.id.share);
//        shareButton.setOnClickListener(this);

    }

    @Override
    public int getLayoutId() {
//        return R.layout.layout_standard_with_share_button;
        return 0;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
//        if (v.getId() == R.id.share) {
//            Toast.makeText(getContext(), "Whatever the icon means", Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    public void setUp(String url, int screen, Object... objects) {
        super.setUp(url, screen, objects);
        if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
            shareButton.setVisibility(View.VISIBLE);
        } else {
            shareButton.setVisibility(View.INVISIBLE);
        }
    }
}
