package com.example.samuel.expensemanager;

import android.app.Application;
import android.content.Context;
import android.test.ApplicationTestCase;

import com.example.samuel.expensemanager.model.DaoSession;
import com.example.samuel.expensemanager.model.Expense;
import com.example.samuel.expensemanager.model.ExpenseDao;
import com.example.samuel.expensemanager.model.TypeInfo;
import com.example.samuel.expensemanager.model.TypeInfoDao;

import java.util.List;
import java.util.Random;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public static final String[] typeExpense = new String[]{"早餐", "午餐", "晚餐", "夜宵", "零食", "饮料", "日用品", "话费",
            "软件", "服装", "鞋帽", "医疗", "果蔬", "影院", "数码", "房租", "护肤", "居家", "书籍", "油盐酱醋", "交通", "摄影文印",
            "娱乐", "物业", "礼物", "社交", "剁手"};

    public static final String[] typeIncome = new String[]{"工资", "奖金", "彩票", "余额宝", "股票"};
    public static final String[] monthCase = new String[]{"03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
    public static final String[] dayCase = new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10",
            "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
            "21", "22", "23", "24", "25", "26", "27", "28", "29", "30"};
    public Context mContext;

    public ApplicationTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mContext = getContext();

    }

    public void testInsertType() {
        DaoSession daoSession = ((ExpenseApplication) mContext.getApplicationContext()).getDaoSession();
        TypeInfoDao typeInfoDao = daoSession.getTypeInfoDao();
        int[] colorArray = mContext.getResources().getIntArray(R.array.colorType);


        Random random = new Random();
        for (int i = 0; i < typeExpense.length; i++) {
            TypeInfo typeInfo = new TypeInfo();

            int color = random.nextInt(colorArray.length);
            String name = typeExpense[i];
            int flag = 1;
            int frequency = 0;
            int isUploaded = 0;
            int isModified = 0;
            int isDeleted = 0;

            typeInfo.setTypeColor(color);
            typeInfo.setTypeName(name);
            typeInfo.setTypeFlag(flag);
            typeInfo.setFrequency(frequency);
            typeInfo.setIsUploaded(isUploaded);
            typeInfo.setIsModified(isModified);
            typeInfo.setIsDeleted(isDeleted);

            typeInfoDao.insertOrReplace(typeInfo);
        }

    }

    public void testInsertData() {
        DaoSession daoSession = ((ExpenseApplication) mContext.getApplicationContext()).getDaoSession();
        ExpenseDao expenseDao = daoSession.getExpenseDao();
        TypeInfoDao typeInfoDao = daoSession.getTypeInfoDao();
        List<TypeInfo> typeInfos = typeInfoDao.loadAll();

        Random random = new Random();
        for (int i = 0; i < 300; i++) {
            Expense expense = new Expense();

            int typeNumber = random.nextInt(typeInfos.size());
            int figure = random.nextInt(100);

            int flag = 1;//支出
            int color = typeInfos.get(typeNumber).getTypeColor();
            String expenseType = typeInfos.get(typeNumber).getTypeName();
            String month = monthCase[random.nextInt(monthCase.length)];
            String day = dayCase[random.nextInt(dayCase.length)];
            String date = "2015-" + month + "-" + day;

            expense.setFigure((double) figure);
            expense.setTypeFlag(flag);
            expense.setTypeName(expenseType);
            expense.setDate(date);
            expense.setTypeColor(color);
            expense.setIsDeleted(0);
            expense.setIsModified(0);
            expense.setIsUploaded(0);

            expenseDao.insertOrReplace(expense);

        }
       /* for (int i = 0; i < 20; i++) {
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

        }*/
    }

    public void testQuery() {
        DaoSession daoSession = ((ExpenseApplication) mContext.getApplicationContext()).getDaoSession();
        ExpenseDao expenseDao = daoSession.getExpenseDao();

        String startDate = "2015-01-01";
        String endDate = "2015-12-31";
        List<Expense> expenseList = expenseDao.queryBuilder()
                .where(ExpenseDao.Properties.Date.between(startDate, endDate))
                .orderAsc().list();

        System.out.println("=================" + expenseList.size());
    }

}