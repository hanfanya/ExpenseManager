package com.example.samuel.expensemanager.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

import java.util.List;

public class CheckServiceUtils {
    //判断服务是否在运行状态
    public static boolean isServiceRunning(Context context, String servicename) {

        boolean runningFlag = false;

        ActivityManager activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);

        List<RunningServiceInfo> runningServiceInfos = activityManager.getRunningServices(10000);

        for (RunningServiceInfo runningServiceInfo : runningServiceInfos) {

            String className = runningServiceInfo.service.getClassName();
            if (className.equals(servicename)) {
                //表示service正在运行
                runningFlag = true;
                break;
            }
        }

        return runningFlag;

    }
}
