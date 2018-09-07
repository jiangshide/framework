package com.zd112.framework.view.player.media.custom;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.Surface;

import com.zd112.framework.view.player.media.MediaInterface;
import com.zd112.framework.view.player.media.MediaManager;
import com.zd112.framework.view.player.media.VideoPlayerManager;

/**
 * Created by etongdai on 2018/3/20.
 */

public class PlayerAssertFolder extends MediaInterface implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener, MediaPlayer.OnVideoSizeChangedListener {
    public MediaPlayer mediaPlayer;

    @Override
    public void start() {
        mediaPlayer.start();
    }

    @Override
    public void prepare() {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnPreparedListener(PlayerAssertFolder.this);
            mediaPlayer.setOnCompletionListener(PlayerAssertFolder.this);
            mediaPlayer.setOnBufferingUpdateListener(PlayerAssertFolder.this);
            mediaPlayer.setScreenOnWhilePlaying(true);
            mediaPlayer.setOnSeekCompleteListener(PlayerAssertFolder.this);
            mediaPlayer.setOnErrorListener(PlayerAssertFolder.this);
            mediaPlayer.setOnInfoListener(PlayerAssertFolder.this);
            mediaPlayer.setOnVideoSizeChangedListener(PlayerAssertFolder.this);

            AssetFileDescriptor assetFileDescriptor = (AssetFileDescriptor) currentDataSource;
            mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());

            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pause() {
        mediaPlayer.pause();
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public void seekTo(long time) {
        mediaPlayer.seekTo((int) time);
    }

    @Override
    public void release() {
        if (mediaPlayer != null)
            mediaPlayer.release();
    }

    @Override
    public long getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    @Override
    public long getDuration() {
        return mediaPlayer.getDuration();
    }

    @Override
    public void setSurface(Surface surface) {
        mediaPlayer.setSurface(surface);
    }

    @Override
    public void setVolume(float leftVolume, float rightVolume) {
        mediaPlayer.setVolume(leftVolume, rightVolume);
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
        if (currentDataSource.toString().toLowerCase().contains("mp3")) {
            MediaManager.instance().mainThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (VideoPlayerManager.getCurrentJzvd() != null) {
                        VideoPlayerManager.getCurrentJzvd().onPrepared();
                    }
                }
            });
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        MediaManager.instance().mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (VideoPlayerManager.getCurrentJzvd() != null) {
                    VideoPlayerManager.getCurrentJzvd().onAutoCompletion();
                }
            }
        });
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, final int percent) {
        MediaManager.instance().mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (VideoPlayerManager.getCurrentJzvd() != null) {
                    VideoPlayerManager.getCurrentJzvd().setBufferProgress(percent);
                }
            }
        });
    }

    @Override
    public void onSeekComplete(MediaPlayer mediaPlayer) {
        MediaManager.instance().mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (VideoPlayerManager.getCurrentJzvd() != null) {
                    VideoPlayerManager.getCurrentJzvd().onSeekComplete();
                }
            }
        });
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, final int what, final int extra) {
        MediaManager.instance().mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (VideoPlayerManager.getCurrentJzvd() != null) {
                    VideoPlayerManager.getCurrentJzvd().onError(what, extra);
                }
            }
        });
        return true;
    }

    @Override
    public boolean onInfo(MediaPlayer mediaPlayer, final int what, final int extra) {
        MediaManager.instance().mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (VideoPlayerManager.getCurrentJzvd() != null) {
                    if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                        VideoPlayerManager.getCurrentJzvd().onPrepared();
                    } else {
                        VideoPlayerManager.getCurrentJzvd().onInfo(what, extra);
                    }
                }
            }
        });
        return false;
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int width, int height) {
        MediaManager.instance().currentVideoWidth = width;
        MediaManager.instance().currentVideoHeight = height;
        MediaManager.instance().mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (VideoPlayerManager.getCurrentJzvd() != null) {
                    VideoPlayerManager.getCurrentJzvd().onVideoSizeChanged();
                }
            }
        });
    }
}
