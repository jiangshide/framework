package com.zd112.framework.view.player;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.zd112.framework.view.player.media.VideoPlayerNormal;

/**
 * Created by etongdai on 2018/3/20.
 */

public class VideoPlayerStandardShowTitleAfterFullscreen extends VideoPlayerNormal {
    public VideoPlayerStandardShowTitleAfterFullscreen(Context context) {
        super(context);
    }

    public VideoPlayerStandardShowTitleAfterFullscreen(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setUp(String url, int screen, Object... objects) {
        super.setUp(url, screen, objects);
        if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
            titleTextView.setVisibility(View.VISIBLE);
        } else {
            titleTextView.setVisibility(View.INVISIBLE);
        }
    }
}
