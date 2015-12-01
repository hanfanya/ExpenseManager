package com.example.samuel.expensemanager.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.example.samuel.expensemanager.utils.DateUtils;
import com.melnykov.fab.FloatingActionButton;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    private RecyclerView mRecyclerViewHome;
    private FloatingActionButton mFabHome;
    private HomeListAdapter mHomeListAdapter;
    private List<Expense> mExpenseList;
    private String mStartDate;
    private String mEndDate;
    private ExpenseDao mExpenseDao;

    public HomeFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        //增加收入/支出记录后，刷新界面
        initRecyclerViewList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mRecyclerViewHome = (RecyclerView) view.findViewById(R.id.recyclerview_home);
        mFabHome = (FloatingActionButton) view.findViewById(R.id.fab_home);


        initRecyclerViewList();
        //设置recyclerview布局
        mRecyclerViewHome.setLayoutManager(new LinearLayoutManager(getActivity()));

        mRecyclerViewHome.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
                .margin(110, 55).build());//设置 divider 分割线

//      recyclerview 滑动时，FabButton 隐藏或显示
        mFabHome.attachToRecyclerView(mRecyclerViewHome);
//        fab的点击事件
        mFabHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddRecordActivity.class));
            }
        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaoSession daoSession = ((ExpenseApplication) getActivity().getApplicationContext()).getDaoSession();
        mExpenseDao = daoSession.getExpenseDao();

//        显示最近 3 天的收支详情
        mStartDate = DateUtils.getLastThreeDate();
        mEndDate = DateUtils.getCurrentDate();
    }

    public void initRecyclerViewList() {
        mExpenseList = mExpenseDao.queryBuilder()
                .where(ExpenseDao.Properties.Date.between(mStartDate, mEndDate))
                .orderDesc(ExpenseDao.Properties.Date).list();
        mHomeListAdapter = new HomeListAdapter(mExpenseList, getActivity());
        mRecyclerViewHome.setAdapter(mHomeListAdapter);

    }
}
