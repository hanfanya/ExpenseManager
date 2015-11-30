package com.example.samuel.expensemanager.utils;

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

    public static boolean hasNetwork() {

        return true;
    }
}
