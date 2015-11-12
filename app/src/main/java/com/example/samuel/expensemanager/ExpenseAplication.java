package com.example.samuel.expensemanager;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.example.samuel.expensemanager.model.DaoMaster;
import com.example.samuel.expensemanager.model.DaoSession;

/**
 * Created by Samuel on 15/11/12 10:48.
 * Email:samuel40@126.com
 */
public class ExpenseAplication extends Application {
    public DaoSession mDaoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "expense-db", null);
        SQLiteDatabase database = helper.getWritableDatabase();

        DaoMaster daoMaster = new DaoMaster(database);
        mDaoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

}
