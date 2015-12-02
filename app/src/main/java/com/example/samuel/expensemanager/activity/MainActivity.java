package com.example.samuel.expensemanager.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.samuel.expensemanager.ExpenseApplication;
import com.example.samuel.expensemanager.R;
import com.example.samuel.expensemanager.adapter.HomePagerAdapter;
import com.example.samuel.expensemanager.model.DaoSession;
import com.example.samuel.expensemanager.model.Expense;
import com.example.samuel.expensemanager.model.ExpenseDao;
import com.example.samuel.expensemanager.model.TypeInfo;
import com.example.samuel.expensemanager.model.TypeInfoDao;
import com.example.samuel.expensemanager.utils.SPUtils;

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    //支出类型
    public static final String[] typeExpense = new String[]{"早餐", "午餐", "晚餐", "夜宵", "零食", "饮料", "日用品", "话费",
            "软件", "服装", "鞋帽", "医疗", "果蔬", "影院", "数码", "房租", "护肤", "居家", "书籍", "油盐酱醋", "交通", "摄影文印",
            "娱乐", "物业", "礼物", "社交", "剁手"};
    //收入类型
    public static final String[] typeIncome = new String[]{"工资", "奖金", "彩票", "余额宝", "股票"};
    //月份
    public static final String[] monthCase = new String[]{"03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
    //日期
    public static final String[] dayCase = new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10",
            "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
            "21", "22", "23", "24", "25", "26", "27", "28", "29", "30"};
    public Context mContext;

    private Toolbar mToolbar;
    private FloatingActionButton mFabHome;
    private DrawerLayout mDrawer;
    private NavigationView mNavigationView;
    private ViewPager mViewPagerHome;
    private TabLayout mTabLayoutHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        assignViews();
        initUI();
        initData();


    }

    private void initData() {
        boolean hasInitData = SPUtils.getBoolean(this, "hasInitData", false);
        if (!hasInitData) {
            testInsertType();
            SPUtils.saveBoolean(this, "hasInitData", true);
        }
        testInsertData();

    }

    public void testInsertType() {
        DaoSession daoSession = ((ExpenseApplication) getApplicationContext()).getDaoSession();
        TypeInfoDao typeInfoDao = daoSession.getTypeInfoDao();
        int[] colorArray = getResources().getIntArray(R.array.colorType);


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
        DaoSession daoSession = ((ExpenseApplication) getApplicationContext()).getDaoSession();
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
            String date = "2015" + month + day;

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
    }

    private void assignViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_home);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mViewPagerHome = (ViewPager) findViewById(R.id.viewpager_tab_home);
        mTabLayoutHome = (TabLayout) findViewById(R.id.tablayout_home);
    }

    private void initUI() {

        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);

        //设置 drawlayout
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);

        //设置 viewpager
        mTabLayoutHome.setTabMode(TabLayout.MODE_FIXED);
        HomePagerAdapter homePagerAdapter = new HomePagerAdapter(getSupportFragmentManager());
        mViewPagerHome.setAdapter(homePagerAdapter);
        mTabLayoutHome.setupWithViewPager(mViewPagerHome);
        mTabLayoutHome.setTabsFromPagerAdapter(homePagerAdapter);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_slideshow) {
            // Handle the camera action
        } else if (id == R.id.nav_detail) {
            startActivity(new Intent(this, DetailActivity.class));
        } else if (id == R.id.nav_sum) {
            startActivity(new Intent(this, SumActivity.class));
        } else if (id == R.id.nav_setting) {
            startActivity(new Intent(this, SettingActivity.class));

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
