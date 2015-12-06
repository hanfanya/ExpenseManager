package com.example.samuel.expensemanager.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.samuel.expensemanager.R;
import com.example.samuel.expensemanager.activity.MainActivity;
import com.example.samuel.expensemanager.service.TimeService;
import com.example.samuel.expensemanager.utils.CheckServiceUtils;
import com.example.samuel.expensemanager.utils.SPUtils;

import java.util.Calendar;

/**
 * 广播接收者
 */
public class TimeChangeReceiver extends BroadcastReceiver {

    private static final int NOTIFICATION_FLAG = 1;
    private boolean isServiceRunning = false;

    public TimeChangeReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        String action = intent.getAction();
        // 判断服务是否在运行中
        isServiceRunning = CheckServiceUtils.isServiceRunning(context, "com.example.barry.clockdemo.TimeService");

        if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {// 开机完成 或者
            Intent service = new Intent(context, TimeService.class);
            context.startService(service);
            Log.i("today", "TimeChangeReceiver : ACTION_BOOT_COMPLETED : 开启服务");

        } else if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {// SD卡加载完成
            if (!isServiceRunning) {// 如果服务被关闭了，开启服务
                Log.i("today", "TimeChangeReceiver : ACTION_MEDIA_MOUNTED : 开启服务");
                Intent service = new Intent(context, TimeService.class);
                context.startService(service);
            }

        } else if (action.equals(Intent.ACTION_TIME_TICK)) {// 时间变化
            if (!isServiceRunning) {// 如果服务被关闭了，开启服务
                Log.i("today", "TimeChangeReceiver : ACTION_TIME_TICK : 开启服务");
                Intent service = new Intent(context, TimeService.class);
                context.startService(service);
            }
            Calendar c = Calendar.getInstance();
            int day = (c.get(Calendar.DAY_OF_WEEK) + 12) % 7;// 让星期一为0，星期日为6
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            if (SPUtils.getBoolean(context, "pick_status" + day, false)
                    && SPUtils.getBoolean(context, "remind_status", false)) {//是不是选中的日期
                if (hour == SPUtils.getInt(context, "remind_time_hour", -1)
                        && minute == SPUtils.getInt(context, "remind_time_minute", -1)) {// 是不是设定的时间

                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    PendingIntent pendingIntent1 = PendingIntent.getActivity(context, 0,
                            new Intent(context, MainActivity.class), 0);
                    Notification notify = new Notification.Builder(context)
                            .setSmallIcon(R.mipmap.ic_launcher)//设置通知的图标
                            .setTicker("TickerText:" + "您有新短消息，请注意查收！")// 设置在状态栏上显示的提示文字
                            .setContentTitle("寸金提醒")// 设置提醒标题
                            .setContentText("一寸光阴一寸金，快打开寸金记录收支吧")// 设置显示内容
                            .setContentIntent(pendingIntent1)// 关联PendingIntent
                            .setNumber(1)
                            .setWhen(System.currentTimeMillis())// 设置通知中的显示时间
                            .getNotification();// API16可以用build代替

                    notify.flags |= Notification.FLAG_AUTO_CANCEL; // 点击清除按钮或点击通知后会自动消失
                    manager.notify(NOTIFICATION_FLAG, notify);
                    Log.i("today", "TimeChangeReceiver : ACTION_TIME_TICK : 发送提醒");
                }
                Log.i("today", "TimeChangeReceiver : ACTION_TIME_TICK : 60s广播");
            }

        } else if (action.equals("com.example.barry.clockdemo.TimeService.onDestroy")) {// 接收到服务destory的广播，重启服务
            Intent service = new Intent(context, TimeService.class);
            context.startService(service);
            Log.i("today", "TimeChangeReceiver : 接收到服务destory广播 : 开启服务");

        } else if (action.equals("MainActivity.openReminder.repeat")) {
            if (!isServiceRunning) {
                Intent service = new Intent(context, TimeService.class);
                context.startService(service);
                Log.i("today", "TimeChangeReceiver : repeat : 开启服务");
            }
            Log.i("today", "TimeChangeReceiver : repeat : 30s广播");

        } else if (action.equals("com.example.barry.clockdemo.MainActivity.onDestroy")) {
            if (!isServiceRunning) {
                Intent service = new Intent(context, TimeService.class);
                context.startService(service);
                Log.i("today", "TimeChangeReceiver : 接收到活动destory广播 : 开启服务");
            }
            Log.i("today", "TimeChangeReceiver : 接收到活动destory广播 ");
        }
    }
}
