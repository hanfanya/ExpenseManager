package com.example.samuel.expensemanager.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.samuel.expensemanager.receiver.TimeChangeReceiver;

/**
 * 服务用来注册广播接收者
 */
public class TimeService extends Service {
    public TimeService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //注册广播接收器
        TimeChangeReceiver mReceiver = new TimeChangeReceiver();
        IntentFilter filter = new IntentFilter();
        // ACTION_TIME_TICK可以检测系统时间变化，每分钟广播一次，只能动态注册
        filter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(mReceiver, filter);
        Log.i("today", "TimeService :onStartCommand : 注册广播接收者 : ACTION_TIME_TICK");

        return START_STICKY;// 返回START_STICKY
    }

    @Override
    public void onDestroy() {// 服务被销毁的时候，发送广播，重启服务
        Log.i("today", "TimeService :onDestroy : 服务被杀死了");
        Intent intent = new Intent("com.example.barry.clockdemo.TimeService.onDestroy");
        sendBroadcast(intent);
        Log.i("today", "TimeService :onDestroy : 服务被杀死了 : 发送自定义广播");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
