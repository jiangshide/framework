package com.zd112.framework.core;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.zd112.framework.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jiangshide
 * @Created by Ender on 2018/10/25.
 * @Emal:18311271399@163.com
 */
public class CoreService extends Service {

    private List<String> data = new ArrayList<>();
    private Messenger mClient;
    private Messenger messenger = new Messenger(new CoreHandler());

    public CoreService(){
        data.add("This is some msg from service");
    }

    class CoreHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 101:
                    Bundle bundle = msg.getData();
                    LogUtils.e("---------------service:",bundle.getString("key")," | process:",getCurrProcessName());
                    Message message = Message.obtain(null,111,1,1);
                    bundle.putString("key","from service222222");
                    message.setData(bundle);
                    mClient = msg.replyTo;
                    try {
                        mClient.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    default:
            }
        }
    }


    private String getCurrProcessName(){
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) getApplication().getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningAppProcessInfo appProcessInfo:activityManager.getRunningAppProcesses()){
            if(appProcessInfo.pid == pid){
                return appProcessInfo.processName;
            }
        }
        return null;
    }

    @Override
    public IBinder onBind(Intent intent) {
        String s = intent.getStringExtra("data");
        LogUtils.e("---------onBind~accept:",s);
        data.add(s);
        return messenger.getBinder();
    }
}
