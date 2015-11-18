package com.example.samuel.expensemanager.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.samuel.expensemanager.R;
import com.example.samuel.expensemanager.adapter.AddRecordPagerAdapter;

public class AddRecordActivity extends AppCompatActivity {
    private Toolbar mToolbarAddRecord;
    private TabLayout mTablayoutAddRecord;
    private ViewPager mViewpagerTabAddRecord;

    private void assignViews() {
        mToolbarAddRecord = (Toolbar) findViewById(R.id.toolbar_add_record);
        mTablayoutAddRecord = (TabLayout) findViewById(R.id.tablayout_add_record);
        mViewpagerTabAddRecord = (ViewPager) findViewById(R.id.viewpager_tab_add_record);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);
        assignViews();
        initUI();
    }

    //ViewPager 切换支出／收入界面
    private void initUI() {
        mToolbarAddRecord.setTitle("");
        setSupportActionBar(mToolbarAddRecord);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mTablayoutAddRecord.setTabMode(TabLayout.MODE_FIXED);//设置tab的模式

        AddRecordPagerAdapter pagerAdapter = new AddRecordPagerAdapter(getSupportFragmentManager());
        mViewpagerTabAddRecord.setAdapter(pagerAdapter);

        mTablayoutAddRecord.setupWithViewPager(mViewpagerTabAddRecord);
        mTablayoutAddRecord.setTabsFromPagerAdapter(pagerAdapter);


    }

}
