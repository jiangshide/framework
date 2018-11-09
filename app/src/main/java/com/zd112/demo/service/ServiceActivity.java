package com.zd112.demo.service;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zd112.demo.R;
import com.zd112.framework.BaseActivity;
import com.zd112.framework.utils.LogUtils;
import com.zd112.framework.utils.ViewUtils;
import com.zd112.framework.view.CusToast;

/**
 * @author jiangshide
 * @Created by Ender on 2018/10/25.
 * @Emal:18311271399@163.com
 */
public class ServiceActivity extends BaseActivity {

    private Bundle bundle = new Bundle();
    private Messenger messenger = new Messenger(new MyHandler());
    private Messenger mMessenger;
    @ViewUtils.ViewInject(R.id.bindMsg)
    private Button bindMsg;
    @ViewUtils.ViewInject(R.id.sendMsg)
    private Button sendMsg;
    @ViewUtils.ViewInject(R.id.msgTxt)
    private TextView msgTxt;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtils.e("-------", "onServiceConnected: ComponentName = " + name);
            mMessenger = new Messenger(service);
            msgTxt.setText("连接成功");
            Message message = Message.obtain(null, 101);
            bundle.putString("key", "from MainActivity=11111");
            message.setData(bundle);
            message.replyTo = messenger;
            try {
                mMessenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 111:
                    int i = msg.arg1;
                    if (i == 1) {
                        LogUtils.e("Main=", currProcessName());
                        Bundle bundle = msg.getData();
                        String d = "接收到Service消息" + bundle.getString("key");
                        LogUtils.e("---------client:", d);
                        CusToast.txt(d);
                    }
            }
        }
    }

    private String currProcessName() {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) getApplication().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcessInfo : activityManager.getRunningAppProcesses()) {
            if (appProcessInfo.pid == pid) {
                return appProcessInfo.processName;
            }
        }
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setView(R.layout.service_view, this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.e("-----------------v:",v," | bundle:",bundle," | mMessenger:",mMessenger);
                sendMsg(v);
            }
        });
        bindMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.e("-----bind service");
                Intent intent = new Intent();
                ComponentName componentName = new ComponentName("com.zd112.demo", "com.zd112.demo.CoreService");
                intent.setComponent(componentName);
                intent.putExtra("data", "msg from MainActivity");
                bindService(intent, serviceConnection, BIND_AUTO_CREATE);
            }
        });
    }

    public void sendMsg(View view) {
        Message message = Message.obtain(null, 101);
        bundle.putString("key","from MainActivity button");
        message.setData(bundle);
        message.replyTo = messenger;
        try {
            mMessenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(serviceConnection);
    }
}
