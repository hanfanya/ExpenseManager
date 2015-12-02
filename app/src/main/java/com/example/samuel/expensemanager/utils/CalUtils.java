package com.example.samuel.expensemanager.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Samuel on 15/12/1 08:49
 * Email:xuzhou40@gmail.com
 * desc:日期相关的工具类，主要是格式化日期
 */
public class CalUtils {
    public static String getCurrentDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        String formatDate = simpleDateFormat.format(new Date());
        return formatDate;
    }

    public static String getLastThreeDate(int durationDay) {
        String date = getCurrentDate();
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(4, 6));
        int day = Integer.parseInt(date.substring(6));

        String monthString;
        String dayString;

        if (day >= durationDay) {
            day = day - durationDay + 1;
        } else {
            int monthType = getMonthType(month);
            if (monthType == 1) {
                month--;
                day = 30 + day - durationDay + 1;
            } else if (monthType == 2) {
                month--;
                day = 31 + day - durationDay + 1;
            } else if (monthType == 3) {
                year--;
                month = 12;
                day = 31 + day - durationDay + 1;
            } else if (monthType == 4) {
                month--;
                if (isLeapYear(year)) {
                    day = 29 + day - durationDay + 1;
                } else {
                    day = 28 + day - durationDay + 1;
                }
            }
        }
        if (month / 10 == 0) {
            monthString = "0" + month;
        } else {
            monthString = String.valueOf(month);
        }
        if (day / 10 == 0) {
            dayString = "0" + day;
        } else {
            dayString = String.valueOf(day);
        }

        System.out.println("结束日期：" + year + "/" + month + "/" + day);
        return year + monthString + dayString;

    }


    private static int getMonthType(int month) {
        if (month == 5 || month == 7 || month == 10 || month == 12) {//上个月有30天
            return 1;
        } else if (month == 2 || month == 4 || month == 6 || month == 8 || month == 9 || month == 11) {//上个月有31天
            return 2;
        } else if (month == 1) {//上个月是去年
            return 3;
        } else if (month == 3) {//上个月是 2 月
            return 4;
        }
        return 0;
    }

    private static boolean isLeapYear(int year) {//判断平闰年
        return year % 400 == 0 || (year % 4 == 0 && year % 100 != 0);
    }
}
