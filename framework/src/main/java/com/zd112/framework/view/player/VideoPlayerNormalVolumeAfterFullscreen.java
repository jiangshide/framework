package com.zd112.framework.view.player;

import android.content.Context;
import android.util.AttributeSet;

import com.zd112.framework.view.player.media.MediaManager;
import com.zd112.framework.view.player.media.VideoPlayerNormal;

/**
 * Created by etongdai on 2018/3/20.
 */

public class VideoPlayerNormalVolumeAfterFullscreen extends VideoPlayerNormal {
    public VideoPlayerNormalVolumeAfterFullscreen(Context context) {
        super(context);
    }

    public VideoPlayerNormalVolumeAfterFullscreen(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onPrepared() {
        super.onPrepared();
        if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
            MediaManager.instance().mediaInterface.setVolume(1f, 1f);
        } else {
            MediaManager.instance().mediaInterface.setVolume(0f, 0f);
        }
    }

    /**
     * 进入全屏模式的时候关闭静音模式
     */
    @Override
    public void startWindowFullscreen() {
        super.startWindowFullscreen();
        MediaManager.instance().mediaInterface.setVolume(1f, 1f);
    }

    /**
     * 退出全屏模式的时候开启静音模式
     */
    @Override
    public void playOnThisJzvd() {
        super.playOnThisJzvd();
        MediaManager.instance().mediaInterface.setVolume(0f, 0f);
    }
}
