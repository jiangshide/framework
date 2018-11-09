package com.zd112.framework.exception;

import android.os.Environment;
import android.os.Looper;

import com.zd112.framework.utils.LogUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

/**
 * @author jiangshide
 * @Created by Ender on 2018/10/16.
 * @Emal:18311271399@163.com
 */
public class ANRExecption extends RuntimeException{
    public ANRExecption(){
        super("application not response!");
        Thread mainThread = Looper.getMainLooper().getThread();
        StackTraceElement[] stackTraceElements = mainThread.getStackTrace();
        setStackTrace(stackTraceElements);
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0;i<stackTraceElements.length;i++){
            stringBuilder.append(stackTraceElements[i].toString()).append("\n");
        }
        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/anr/";
            String fileName = System.currentTimeMillis()+"_anr.log";
            File filePath = new File(path);
            if(!filePath.exists())filePath.mkdirs();
            File file = new File(filePath,fileName);
            if(!file.exists())file.createNewFile();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
//            bufferedWriter.write();
            bufferedWriter.close();
        }catch (Exception e){
            LogUtils.e(e);
        }
    }
}
