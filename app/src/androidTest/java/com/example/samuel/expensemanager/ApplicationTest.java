package com.example.samuel.expensemanager;

import android.app.Application;
import android.content.Context;
import android.test.ApplicationTestCase;

import com.example.samuel.expensemanager.model.DaoSession;
import com.example.samuel.expensemanager.model.Expense;
import com.example.samuel.expensemanager.model.ExpenseDao;

import java.util.Random;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public static final String[] typeExpense = new String[]{"早餐", "午餐", "晚餐", "夜宵", "零食", "软件", "App Store", "话费"};
    public static final String[] typeIncome = new String[]{"工资", "股票", "彩票", "股份", "余额宝", "奖金"};
    public static final int[] monthCase = new int[]{7, 8, 9, 10, 11};
    public Context mContext;

    public ApplicationTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mContext = getContext();

    }

    public void testInsert() {
        DaoSession daoSession = ((ExpenseAplication) mContext.getApplicationContext()).getDaoSession();
        ExpenseDao expenseDao = daoSession.getExpenseDao();

        Random random = new Random();
        for (int i = 0; i < 300; i++) {
            Expense expense = new Expense();

            int figure = random.nextInt(100);
            String expenseType = typeExpense[random.nextInt(typeExpense.length)];
            int month = monthCase[random.nextInt(monthCase.length)];
            int day = random.nextInt(30) + 1;
            int color = random.nextInt(40);
            String date = "2015-" + month + "-" + day;

            expense.setFigure((double) figure);
            expense.setTypeFlag(1);
            expense.setTypeName(expenseType);
            expense.setDate(date);
            expense.setTypeColor(color);
            expense.setIsDeleted(0);
            expense.setIsModified(0);
            expense.setIsUploaded(0);

            expenseDao.insertOrReplace(expense);

        }
        for (int i = 0; i < 20; i++) {
            Expense expense = new Expense();

            int figure = random.nextInt(5000);
            String incomeType = typeIncome[random.nextInt(typeIncome.length)];
            int month = monthCase[random.nextInt(monthCase.length)];
            int day = random.nextInt(30) + 1;
            int color = random.nextInt(40);
            String date = "2015-" + month + "-" + day;

            expense.setFigure((double) figure);
            expense.setTypeFlag(0);
            expense.setTypeName(incomeType);
            expense.setDate(date);
            expense.setTypeColor(color);
            expense.setIsDeleted(0);
            expense.setIsModified(0);
            expense.setIsUploaded(0);

            expenseDao.insertOrReplace(expense);

        }
    }

}