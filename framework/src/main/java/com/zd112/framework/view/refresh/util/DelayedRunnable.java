package com.zd112.framework.view.refresh.util;

public class DelayedRunnable implements Runnable {
    public long mDelayMillis;
    private Runnable runnable = null;
    public DelayedRunnable(Runnable runnable, long delayMillis) {
        this.runnable = runnable;
        this.mDelayMillis = delayMillis;
    }
    @Override
    public void run() {
        try {
            if (runnable != null) {
                runnable.run();
                runnable = null;
            }
        } catch (Throwable e) {
            if (!(e instanceof NoClassDefFoundError)) {
                e.printStackTrace();
            }
        }
    }
}