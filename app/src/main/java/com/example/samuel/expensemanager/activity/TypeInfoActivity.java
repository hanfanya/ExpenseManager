package com.example.samuel.expensemanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.samuel.expensemanager.R;
import com.example.samuel.expensemanager.adapter.AddRecordPagerAdapter;
import com.example.samuel.expensemanager.fragment.ExpenseTypeInfoFragment;
import com.example.samuel.expensemanager.fragment.IncomeTypeInfoFragment;

public class TypeInfoActivity extends AppCompatActivity {
    private Toolbar mToolbarAddRecord;
    private TabLayout mTablayoutAddRecord;
    private ViewPager mViewpagerTabAddRecord;
    private AddRecordPagerAdapter mPagerAdapter;
    private FloatingActionButton mFloatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_info);

        assignViews();
        initUI();

    }

    private void assignViews() {
        mToolbarAddRecord = (Toolbar) findViewById(R.id.toolbar_add_record);
        mTablayoutAddRecord = (TabLayout) findViewById(R.id.tablayout_add_record);
        mViewpagerTabAddRecord = (ViewPager) findViewById(R.id.viewpager_tab_add_record);
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
    }


    private void initFragment() {//初始化Fragment
        mPagerAdapter.addFragment(new ExpenseTypeInfoFragment(), "支出");
        mPagerAdapter.addFragment(new IncomeTypeInfoFragment(), "收入");
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

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = mViewpagerTabAddRecord.getCurrentItem();
                Intent intent = new Intent(TypeInfoActivity.this, AddTypeInfoActivity.class);
                intent.putExtra("currentType", currentItem);
                startActivity(intent);
            }
        });


    }
}
