package com.example.samuel.expensemanager.activity;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.widget.ImageButton;

import com.example.samuel.expensemanager.R;
import com.example.samuel.expensemanager.adapter.SumPagerAdapter;

/**
 * author: 刘万鹏
 * <p/>
 * 统计页面的Activity
 * 初始化统计页面
 * <p/>
 * Created by Mustard on 2015/12/1.
 */
public class SumActivity extends AppCompatActivity {

    private TabLayout mTablayoutSum;
    private Toolbar mToolbarSum;
    private ViewPager mViewpagerSum;
    private ImageButton mBtnBacksum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sum);
        assignViews();
        initUI();
    }


    private void assignViews() {
        mToolbarSum = (Toolbar) findViewById(R.id.toolbar_sum);
        mTablayoutSum = (TabLayout) findViewById(R.id.tablayout_sum);
        mViewpagerSum = (ViewPager) findViewById(R.id.viewpager_tab_sum);
    }

    private void initUI() {
        //初始化ToolBar
        mToolbarSum.setTitle("");
        setSupportActionBar(mToolbarSum);
        //设置返回父页面
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        //设置ViewPager
        SumPagerAdapter sumPagerAdapter = new SumPagerAdapter(getSupportFragmentManager());
        mViewpagerSum.setAdapter(sumPagerAdapter);
        //初始话TableLayout
        mTablayoutSum.setTabMode(TabLayout.MODE_FIXED);
        mTablayoutSum.setupWithViewPager(mViewpagerSum);
        mTablayoutSum.setTabsFromPagerAdapter(sumPagerAdapter);

    }
}
