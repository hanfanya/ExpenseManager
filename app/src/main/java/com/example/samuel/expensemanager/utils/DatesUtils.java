package com.example.samuel.expensemanager.utils;

/**
 * Created by WSY on 2015-12-03-0003.
 */
public class DatesUtils {
    public static int yearStart = 0;
    public static int monthStart = 0;


    /**
     * 获取当前日期对应的position
     * 假定2015年1月1号对应的是5001
     */
    public static int getPosition(String date) {
        int position = 0;

        String year = date.substring(0, 4);
        String month = date.substring(4, 6);
        String day = date.substring(6);
//        System.out.println(year + " : " + month + " : " + day);
        int years = Integer.parseInt(year);
        int months = Integer.parseInt(month);
        int days = Integer.parseInt(day);
        switch (years) {
            case 2015:
                yearStart = 5000;
                break;
            case 2016:
                yearStart = 5365;
                break;
            case 2017:
                yearStart = 5731;
                break;
            case 2018:
                yearStart = 6096;
                break;
            case 2019:
                yearStart = 6461;
                break;
            case 2020:
                yearStart = 6826;
                break;
        }

        switch (months) {
            case 1:
                monthStart = yearStart;
                break;
            case 2:
                monthStart = yearStart + 31;
                break;
            case 3:
                if (years % 4 == 0) {    //如果是闰年
                    monthStart = yearStart + 31 + 29;
                } else {
                    monthStart = yearStart + 31 + 28;
                }
                break;
            case 4:
                if (years % 4 == 0) {    //如果是闰年
                    monthStart = yearStart + 31 + 29 + 31;
                } else {
                    monthStart = yearStart + 31 + 28 + 31;
                }
                break;
            case 5:
                if (years % 4 == 0) {    //如果是闰年
                    monthStart = yearStart + 31 + 29 + 31 + 30;
                } else {
                    monthStart = yearStart + 31 + 28 + 31 + 30;
                }
                break;
            case 6:
                if (years % 4 == 0) {    //如果是闰年
                    monthStart = yearStart + 31 + 29 + 31 + 30 + 31;
                } else {
                    monthStart = yearStart + 31 + 28 + 31 + 30 + 31;
                }
                break;
            case 7:
                if (years % 4 == 0) {    //如果是闰年
                    monthStart = yearStart + 31 + 29 + 31 + 30 + 31 + 30;
                } else {
                    monthStart = yearStart + 31 + 28 + 31 + 30 + 31 + 30;
                }
                break;
            case 8:
                if (years % 4 == 0) {    //如果是闰年
                    monthStart = yearStart + 31 + 29 + 31 + 30 + 31 + 30 + 31;
                } else {
                    monthStart = yearStart + 31 + 28 + 31 + 30 + 31 + 30 + 31;
                }
                break;
            case 9:
                if (years % 4 == 0) {    //如果是闰年
                    monthStart = yearStart + 31 + 29 + 31 + 30 + 31 + 30 + 31 + 31;
                } else {
                    monthStart = yearStart + 31 + 28 + 31 + 30 + 31 + 30 + 31 + 31;
                }
                break;
            case 10:
                if (years % 4 == 0) {    //如果是闰年
                    monthStart = yearStart + 31 + 29 + 31 + 30 + 31 + 30 + 31 + 31 + 30;
                } else {
                    monthStart = yearStart + 31 + 28 + 31 + 30 + 31 + 30 + 31 + 31 + 30;
                }
                break;
            case 11:
                if (years % 4 == 0) {    //如果是闰年
                    monthStart = yearStart + 31 + 29 + 31 + 30 + 31 + 30 + 31 + 31 + 30 + 31;
                } else {
                    monthStart = yearStart + 31 + 28 + 31 + 30 + 31 + 30 + 31 + 31 + 30 + 31;
                }
                break;
            case 12:
                if (years % 4 == 0) {    //如果是闰年
                    monthStart = yearStart + 31 + 29 + 31 + 30 + 31 + 30 + 31 + 31 + 30 + 31 + 30;
                } else {
                    monthStart = yearStart + 31 + 28 + 31 + 30 + 31 + 30 + 31 + 31 + 30 + 31 + 30;
                }
                break;
        }

        position = monthStart + days;

        return position;
    }

    /**
     * 获取position对应的日期
     */
    public static String getDate(int position) {
        String date = "";
        int yearStart = 0;
        if (position > 5000 && position <= 5365) {
            date = "2015";
            yearStart = 5000;
        } else if (position > 5365 && position <= 5731) {
            date = "2016";
            yearStart = 5365;
        } else if (position > 5731 && position <= 6096) {
            date = "2017";
            yearStart = 5731;
        } else if (position > 6096 && position <= 6461) {
            date = "2018";
            yearStart = 6096;
        } else if (position > 6461 && position <= 6826) {
            date = "2019";
            yearStart = 6461;
        } else if (position > 6826) {
            date = "2020";
            yearStart = 6826;
        }
        int days = position - yearStart; //剩余的天数
        if ((Integer.parseInt(date)) % 4 == 0) { //闰年
            if (days <= 31) {
                date = date + "01" + days;
            } else if (days > 31 && days <= (31 + 29)) {
                date = date + "02" + (days - 31);
            } else if (days > (31 + 29) && days <= (31 + 29 + 31)) {
                date = date + "03" + (days - 31 - 29);
            } else if (days > (31 + 29 + 31) && days <= (31 + 29 + 31 + 30)) {
                date = date + "04" + (days - 31 - 29 - 31);
            } else if (days > (31 + 29 + 31 + 30) && days <= (31 + 29 + 31 + 30 + 31)) {
                date = date + "05" + (days - 31 - 29 - 31 - 30);
            } else if (days > (31 + 29 + 31 + 30 + 31) && days <= (31 + 29 + 31 + 30 + 31 + 30)) {
                date = date + "06" + (days - 31 - 29 - 31 - 30 - 31);
            } else if (days > (31 + 29 + 31 + 30 + 31 + 30) && days <= (31 + 29 + 31 + 30 + 31 + 30 + 31)) {
                date = date + "07" + (days - 31 - 29 - 31 - 30 - 31 - 30);
            } else if (days > (31 + 29 + 31 + 30 + 31 + 30 + 31) && days <= (31 + 29 + 31 + 30 + 31 + 30 + 31 + 31)) {
                date = date + "08" + (days - 31 - 29 - 31 - 30 - 31 - 30 - 31);
            } else if (days > (31 + 29 + 31 + 30 + 31 + 30 + 31 + 31) && days <= (31 + 29 + 31 + 30 + 31 + 30 + 31 + 31 + 30)) {
                date = date + "09" + (days - 31 - 29 - 31 - 30 - 31 - 30 - 31 - 31);
            } else if (days > (31 + 29 + 31 + 30 + 31 + 30 + 31 + 31 + 30) && days <= (31 + 29 + 31 + 30 + 31 + 30 + 31 + 31 + 30 + 31)) {
                date = date + "10" + (days - 31 - 29 - 31 - 30 - 31 - 30 - 31 - 31 - 30);
            } else if (days > (31 + 29 + 31 + 30 + 31 + 30 + 31 + 31 + 30 + 31) && days <= (31 + 29 + 31 + 30 + 31 + 30 + 31 + 31 + 30 + 31 + 30)) {
                date = date + "11" + (days - 31 - 29 - 31 - 30 - 31 - 30 - 31 - 31 - 30 - 31);
            } else if (days <= (31 + 29 + 31 + 30 + 31 + 30 + 31 + 31 + 30 + 31 + 30 + 31)) {
                date = date + "12" + (days - 31 - 29 - 31 - 30 - 31 - 30 - 31 - 31 - 30 - 31 - 30);
            }
        } else {
            if (days <= 31) {
                date = date + "01" + days;
            } else if (days > 31 && days <= (31 + 28)) {
                date = date + "02" + (days - 31);
            } else if (days > (31 + 28) && days <= (31 + 28 + 31)) {
                date = date + "03" + (days - 31 - 28);
            } else if (days > (31 + 28 + 31) && days <= (31 + 28 + 31 + 30)) {
                date = date + "04" + (days - 31 - 28 - 31);
            } else if (days > (31 + 28 + 31 + 30) && days <= (31 + 28 + 31 + 30 + 31)) {
                date = date + "05" + (days - 31 - 28 - 31 - 30);
            } else if (days > (31 + 28 + 31 + 30 + 31) && days <= (31 + 28 + 31 + 30 + 31 + 30)) {
                date = date + "06" + (days - 31 - 28 - 31 - 30 - 31);
            } else if (days > (31 + 28 + 31 + 30 + 31 + 30) && days <= (31 + 28 + 31 + 30 + 31 + 30 + 31)) {
                date = date + "07" + (days - 31 - 28 - 31 - 30 - 31 - 30);
            } else if (days > (31 + 28 + 31 + 30 + 31 + 30 + 31) && days <= (31 + 28 + 31 + 30 + 31 + 30 + 31 + 31)) {
                date = date + "08" + (days - 31 - 28 - 31 - 30 - 31 - 30 - 31);
            } else if (days > (31 + 28 + 31 + 30 + 31 + 30 + 31 + 31) && days <= (31 + 28 + 31 + 30 + 31 + 30 + 31 + 31 + 30)) {
                date = date + "09" + (days - 31 - 28 - 31 - 30 - 31 - 30 - 31 - 31);
            } else if (days > (31 + 28 + 31 + 30 + 31 + 30 + 31 + 31 + 30) && days <= (31 + 28 + 31 + 30 + 31 + 30 + 31 + 31 + 30 + 31)) {
                date = date + "10" + (days - 31 - 28 - 31 - 30 - 31 - 30 - 31 - 31 - 30);
            } else if (days > (31 + 28 + 31 + 30 + 31 + 30 + 31 + 31 + 30 + 31) && days <= (31 + 28 + 31 + 30 + 31 + 30 + 31 + 31 + 30 + 31 + 30)) {
                date = date + "11" + (days - 31 - 28 - 31 - 30 - 31 - 30 - 31 - 31 - 30 - 31);
            } else if (days <= (31 + 28 + 31 + 30 + 31 + 30 + 31 + 31 + 30 + 31 + 30 + 31)) {
                date = date + "12" + (days - 31 - 28 - 31 - 30 - 31 - 30 - 31 - 31 - 30 - 31 - 30);
            }
        }
        if (date.length() < 8) {
            //长度<8需要补0
            String d = date.substring(6);
            String f = date.substring(0, 6);
            date = f + "0" + d;
        }
        return date;
    }

}
