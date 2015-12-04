package com.example.samuel.expensemanager.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.samuel.expensemanager.ExpenseApplication;
import com.example.samuel.expensemanager.R;
import com.example.samuel.expensemanager.activity.AddRecordActivity;
import com.example.samuel.expensemanager.adapter.HomeListAdapter;
import com.example.samuel.expensemanager.model.DaoSession;
import com.example.samuel.expensemanager.model.Expense;
import com.example.samuel.expensemanager.model.ExpenseDao;
import com.example.samuel.expensemanager.utils.CalUtils;
import com.example.samuel.expensemanager.view.CountView;
import com.melnykov.fab.FloatingActionButton;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Samuel on 15/12/1 23:25
 * Email:xuzhou40@gmail.com
 * desc:主页信息的展示：今日支出，本月支出，三天内支出记录
 */

public class HomeFragment extends Fragment {


    @Bind(R.id.tv_today_out)
    CountView mTvTodayOut;
    @Bind(R.id.tv_month_out)
    CountView mTvMonthOut;
    @Bind(R.id.recyclerview_home)
    RecyclerView mRecyclerviewHome;
    @Bind(R.id.fab_home)
    FloatingActionButton mFabHome;
    //    private RecyclerView mRecyclerViewHome;
//    private FloatingActionButton mFabHome;
    private HomeListAdapter mHomeListAdapter;
    private List<Expense> mExpenseList;
    private List<Expense> mExpenseMonth;
    private String mStartDate;
    private String mEndDate;
    private ExpenseDao mExpenseDao;
    private double mSumToday;
    private double mSumMonth;


    public HomeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
//        mRecyclerViewHome = (RecyclerView) view.findViewById(R.id.recyclerview_home);
//        mFabHome = (FloatingActionButton) view.findViewById(R.id.fab_home);


        initRecyclerViewList();
        //设置recyclerview布局
        mRecyclerviewHome.setLayoutManager(new LinearLayoutManager(getActivity()));

        mRecyclerviewHome.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
                .build());//设置 divider 分割线 margin(110, 55)

//      recyclerview 滑动时，FabButton 隐藏或显示
        mFabHome.attachToRecyclerView(mRecyclerviewHome);
//        fab的点击事件
        mFabHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddRecordActivity.class));
            }
        });

        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaoSession daoSession = ((ExpenseApplication) getActivity().getApplicationContext()).getDaoSession();
        mExpenseDao = daoSession.getExpenseDao();

//        显示最近 3 天的收支详情
        mStartDate = CalUtils.getLastThreeDate(3);
        mEndDate = CalUtils.getCurrentDate();
    }

    @Override
    public void onResume() {
        super.onResume();
        //增加收入/支出记录后，刷新界面
        initRecyclerViewList();
        setTodayAndMonth();
    }

    private void setTodayAndMonth() {
        mSumToday = 0;
        mSumMonth = 0;
        String month = CalUtils.getCurrentDate().substring(0, 6);
        System.out.println("month=" + month);
//        month = "%" + month + "%";
        //获取当月的记录
        mExpenseMonth = mExpenseDao.queryBuilder()
                .where(ExpenseDao.Properties.Date.like(month + "%")).list();
        for (int i = 0; i < mExpenseMonth.size(); i++) {
            Expense expense = mExpenseMonth.get(i);
            if (expense.getDate().equals(CalUtils.getCurrentDate())) {
                mSumToday = mSumToday + expense.getFigure();//统计今日支出
            }
            mSumMonth = mSumMonth + expense.getFigure();//统计本月支出
        }
        Log.i("home", mExpenseMonth.size() + "");
        mTvTodayOut.showNumberWithAnimation(String.valueOf(mSumToday));
        mTvMonthOut.showNumberWithAnimation(String.valueOf(mSumMonth));
    }

    public void initRecyclerViewList() {
        mExpenseList = mExpenseDao.queryBuilder()
                .where(ExpenseDao.Properties.Date.between(mStartDate, mEndDate))
                .orderDesc(ExpenseDao.Properties.Date).list();
        mHomeListAdapter = new HomeListAdapter(mExpenseList, getActivity());
        mRecyclerviewHome.setAdapter(mHomeListAdapter);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
