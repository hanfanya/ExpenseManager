package com.example.samuel.expensemanager;

import android.content.Context;

import com.example.samuel.expensemanager.model.DaoSession;
import com.example.samuel.expensemanager.model.Expense;
import com.example.samuel.expensemanager.model.ExpenseDao;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    public static final String[] typeExpense = new String[]{"早餐", "午餐", "晚餐", "夜宵", "零食", "软件", "App Store", "话费", "手机", "衣服", "书籍", "交通", "药品", "电影", "饮料", "物业", "房租"};
    public static final String[] typeIncome = new String[]{"工资", "股票", "彩票", "股份", "余额宝", "奖金"};
    public static final String[] monthCase = new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
    public static final String[] dayCase = new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10",
            "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
            "21", "22", "23", "24", "25", "26", "27", "28", "29", "30"};
    public Context mContext;


    public void testInsert() {
        DaoSession daoSession = ((ExpenseApplication) mContext.getApplicationContext()).getDaoSession();
        ExpenseDao expenseDao = daoSession.getExpenseDao();

        Random random = new Random();
        for (int i = 0; i < 300; i++) {
            Expense expense = new Expense();

            int figure = random.nextInt(100);
            int type = random.nextInt(typeExpense.length);
            String expenseType = typeExpense[type];
            String month = monthCase[random.nextInt(monthCase.length)];
            String day = dayCase[random.nextInt(dayCase.length)];
            String date = "2015-" + month + "-" + day;

            expense.setFigure((double) figure);
            expense.setTypeFlag(1);
            expense.setTypeName(expenseType);
            expense.setDate(date);
            expense.setTypeColor(type);
            expense.setIsDeleted(0);
            expense.setIsModified(0);
            expense.setIsUploaded(0);

            expenseDao.insertOrReplace(expense);

        }

    }


}