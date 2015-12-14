package com.example.samuel.expensemanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.samuel.expensemanager.R;
import com.example.samuel.expensemanager.adapter.AddRecordPagerAdapter;
import com.example.samuel.expensemanager.fragment.ExpenseFragment;
import com.example.samuel.expensemanager.fragment.IncomeFragment;
import com.example.samuel.expensemanager.utils.SysUtils;

public class AddRecordActivity extends AppCompatActivity {
    private Toolbar mToolbarAddRecord;
    private TabLayout mTablayoutAddRecord;
    private ViewPager mViewpagerTabAddRecord;
    private AddRecordPagerAdapter mPagerAdapter;
    private boolean mIsCreated;
    private int mTypeFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(SysUtils.getThemeResId(AddRecordActivity.this));
        setContentView(R.layout.activity_add_record);


//        System.out.println(expense.getDate());
//        System.out.println(expense.getFigure());
//        System.out.println(expense.getTypeName());

        assignViews();
        initUI();
    }

    private void assignViews() {
        mToolbarAddRecord = (Toolbar) findViewById(R.id.toolbar_add_record);
        mTablayoutAddRecord = (TabLayout) findViewById(R.id.tablayout_add_record);
        mViewpagerTabAddRecord = (ViewPager) findViewById(R.id.viewpager_tab_add_record);
    }


    private void initFragment() {//初始化Fragment
        Intent intent = getIntent();
        mIsCreated = intent.getBooleanExtra("isCreated", true);
        long recordId = intent.getLongExtra("edit_record", 0);
        mTypeFlag = intent.getIntExtra("type_flag", 0);

        Bundle bundle = new Bundle();
        bundle.putBoolean("isCreated", mIsCreated);
        bundle.putLong("edit_record", recordId);
        bundle.putInt("type_flag", mTypeFlag);

        mPagerAdapter.addFragment(ExpenseFragment.newInstance(bundle), "支出");
        mPagerAdapter.addFragment(IncomeFragment.newInstance(bundle), "收入");


    }

    //ViewPager 切换支出／收入界面
    private void initUI() {
        mToolbarAddRecord.setTitle("");
        setSupportActionBar(mToolbarAddRecord);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mTablayoutAddRecord.setTabMode(TabLayout.MODE_FIXED);//设置tab的模式

        mPagerAdapter = new AddRecordPagerAdapter(getSupportFragmentManager());
        initFragment();
        mViewpagerTabAddRecord.setAdapter(mPagerAdapter);

        mTablayoutAddRecord.setupWithViewPager(mViewpagerTabAddRecord);
        mTablayoutAddRecord.setTabsFromPagerAdapter(mPagerAdapter);


        if (mTypeFlag == 1) {
            mViewpagerTabAddRecord.setCurrentItem(0);
        } else {
            mViewpagerTabAddRecord.setCurrentItem(1);
        }
        if (mIsCreated) {
            mViewpagerTabAddRecord.setCurrentItem(0);
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent("com.example.barry.clockdemo.ReminderActivity.onDestroy");
        sendBroadcast(intent);
    }

}
