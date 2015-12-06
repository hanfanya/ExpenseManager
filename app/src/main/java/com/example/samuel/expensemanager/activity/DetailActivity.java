package com.example.samuel.expensemanager.activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.CalendarView;
import android.widget.TextView;

import com.example.samuel.expensemanager.R;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

public class DetailActivity extends AppCompatActivity implements CalendarView.OnDateChangeListener {

    private CollapsingToolbarLayout mCollapseToolbar;
    private Toolbar mToolbarDetail;
    private MaterialCalendarView mCvMain;
    private TextView mTvDate;

    private void assignViews() {
        mCollapseToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
        mToolbarDetail = (Toolbar) findViewById(R.id.toolbar_detail);
        mCvMain = (MaterialCalendarView) findViewById(R.id.cv_main);
        mTvDate = (TextView) findViewById(R.id.tv_date);


        mToolbarDetail.setTitle("明细");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        assignViews();
    }

    @Override
    public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
        String dateString = year + "年" + (month + 1) + "月" + dayOfMonth + "日";
        mTvDate.setText(dateString);
        setTitle(dateString);

    }
}
