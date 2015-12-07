package com.example.samuel.expensemanager.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.math.BigDecimal;

/**
 * Created by Samuel on 15/11/25.
 */
public class SysUtils {
    public static double stringToDouble(String string) {
        System.out.println(string);
        String[] numberString = string.split("\\.");
        int numberInt = Integer.parseInt(numberString[0]);
        int decimalInt = Integer.parseInt(numberString[1]);
        double numberDouble = numberInt + decimalInt * 1.0 / 10;

        BigDecimal bigDecimal = new BigDecimal(numberDouble);
        numberDouble = bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();

        return numberDouble;
    }

    public static boolean haveNetwork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean canUpload(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean haveWifi = networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
        boolean haveMobile = networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
        if (networkInfo != null && networkInfo.isConnected()) {
//            int networkSetting = SPUtils.getInt(context, "network_setting", 0);
            String network_setting = SPUtils.getString(context, "network_setting", "0");
            int networkSetting = Integer.parseInt(network_setting);
            if (networkSetting == 1 && haveWifi) {
                return true;
            } else if (networkSetting == 2 && haveNetwork(context)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean haveWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean haveWifi = networkInfo.getType() == ConnectivityManager.TYPE_WIFI;

        if (networkInfo != null && networkInfo.isConnected()) {
//            int networkSetting = SPUtils.getInt(context, "network_setting", 0);
            String network_setting = SPUtils.getString(context, "network_setting", "0");
            int networkSetting = Integer.parseInt(network_setting);
            if (networkSetting == 1 && !haveWifi) {
                return false;
            } else {
                return true;
            }

        } else {
            return false;
        }
    }


    public static boolean hasLogin() {

        return false;
    }

}
