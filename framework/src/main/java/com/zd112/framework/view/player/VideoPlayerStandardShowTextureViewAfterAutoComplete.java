package com.zd112.framework.view.player;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.zd112.framework.view.player.media.VideoPlayerNormal;

/**
 * Created by etongdai on 2018/3/20.
 */

public class VideoPlayerStandardShowTextureViewAfterAutoComplete extends VideoPlayerNormal {
    public VideoPlayerStandardShowTextureViewAfterAutoComplete(Context context) {
        super(context);
    }

    public VideoPlayerStandardShowTextureViewAfterAutoComplete(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onAutoCompletion() {
        super.onAutoCompletion();
        thumbImageView.setVisibility(View.GONE);
    }
}
