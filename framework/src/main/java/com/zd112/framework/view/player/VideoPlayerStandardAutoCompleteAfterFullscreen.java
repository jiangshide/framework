package com.zd112.framework.view.player;

import android.content.Context;
import android.media.AudioManager;
import android.util.AttributeSet;
import android.view.WindowManager;

import com.zd112.framework.utils.LogUtils;
import com.zd112.framework.view.player.media.MediaManager;
import com.zd112.framework.view.player.media.Utils;
import com.zd112.framework.view.player.media.VideoPlayerNormal;

/**
 * Created by etongdai on 2018/3/20.
 */

public class VideoPlayerStandardAutoCompleteAfterFullscreen extends VideoPlayerNormal {
    public VideoPlayerStandardAutoCompleteAfterFullscreen(Context context) {
        super(context);
    }

    public VideoPlayerStandardAutoCompleteAfterFullscreen(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void startVideo() {
        if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
            LogUtils.d("startVideo [" + this.hashCode() + "] ");
            initTextureView();
            addTextureView();
            AudioManager mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
            mAudioManager.requestAudioFocus(onAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
            Utils.scanForActivity(getContext()).getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            MediaManager.setDataSource(dataSourceObjects);
            MediaManager.setCurrentDataSource(Utils.getCurrentFromDataSource(dataSourceObjects, currentUrlMapIndex));
            MediaManager.instance().positionInList = positionInList;
            onStatePreparing();
        } else {
            super.startVideo();
        }
    }

    @Override
    public void onAutoCompletion() {
        if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
            onStateAutoComplete();
        } else {
            super.onAutoCompletion();
        }

    }
}
