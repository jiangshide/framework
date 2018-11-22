package com.zd112.framework.service;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;

import com.zd112.framework.utils.LogUtils;

import java.util.List;

/**
 * @author jiangshide
 * @Created by Ender on 2018/11/15.
 * @Emal:18311271399@163.com
 */
@SuppressLint("NewApi")
public class JobScheduleService extends JobService {

    private JobScheduler mJobScheduler;
    private final long INTERVAL_TIME = 2 * 60 * 1000;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mJobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
            JobInfo.Builder builder = new JobInfo.Builder(startId++, new ComponentName(getPackageName(), JobScheduleService.class.getName()));
            builder.setPeriodic(INTERVAL_TIME);
            builder.setRequiresCharging(true);//充电场景下,默认为false
            builder.setRequiresDeviceIdle(true);//手机空闲场景下,默认为false
            builder.setPersisted(true);//设备重启之后任务是否继续,默认为false
            if (mJobScheduler.schedule(builder.build()) <= 0) {
                LogUtils.e("JobScheduleService false!");
            } else {
                LogUtils.e("JobScheduleService true!");
            }

        }
        return START_STICKY;
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        LogUtils.e("JobScheduleService started!");
        if (!isServiceRunning("")) {
            startService();
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        LogUtils.e("JobScheduleService stop!");
        if (!isServiceRunning("")) {
            startService();
        }
        return false;
    }

    private void startService() {
        LogUtils.e("startService!");
    }

    public boolean isServiceRunning(String serviceName) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfoList = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcessInfoList) {
            LogUtils.e("processName:", runningAppProcessInfo.processName);
            if (runningAppProcessInfo.processName.equals(serviceName)) {
                isRunning = true;
            }
        }
        return isRunning;
    }
}
