package com.zd112.framework.view.player.media;

/**
 * Created by etongdai on 2018/3/20.
 */

public class VideoPlayerManager {
    public static VideoPlayer FIRST_FLOOR_JZVD;
    public static VideoPlayer SECOND_FLOOR_JZVD;

    public static VideoPlayer getFirstFloor() {
        return FIRST_FLOOR_JZVD;
    }

    public static void setFirstFloor(VideoPlayer videoPlayer) {
        FIRST_FLOOR_JZVD = videoPlayer;
    }

    public static VideoPlayer getSecondFloor() {
        return SECOND_FLOOR_JZVD;
    }

    public static void setSecondFloor(VideoPlayer videoPlayer) {
        SECOND_FLOOR_JZVD = videoPlayer;
    }

    public static VideoPlayer getCurrentJzvd() {
        if (getSecondFloor() != null) {
            return getSecondFloor();
        }
        return getFirstFloor();
    }

    public static void completeAll() {
        if (SECOND_FLOOR_JZVD != null) {
            SECOND_FLOOR_JZVD.onCompletion();
            SECOND_FLOOR_JZVD = null;
        }
        if (FIRST_FLOOR_JZVD != null) {
            FIRST_FLOOR_JZVD.onCompletion();
            FIRST_FLOOR_JZVD = null;
        }
    }
}
