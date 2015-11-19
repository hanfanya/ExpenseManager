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
import com.melnykov.fab.FloatingActionButton;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    private RecyclerView mRecyclerViewHome;
    private FloatingActionButton mFabHome;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mRecyclerViewHome = (RecyclerView) view.findViewById(R.id.recyclerview_home);
        mFabHome = (FloatingActionButton) view.findViewById(R.id.fab_home);

        DaoSession daoSession = ((ExpenseApplication) getActivity().getApplicationContext()).getDaoSession();
        ExpenseDao expenseDao = daoSession.getExpenseDao();

        String startDate = "2015-06-01";
        String endDate = "2015-12-31";
//        显示最近 7 天的收支详情
        List<Expense> expenseList = expenseDao.queryBuilder()
                .where(ExpenseDao.Properties.Date.between(startDate, endDate))
                .orderAsc(ExpenseDao.Properties.Date).list();

        mRecyclerViewHome.setLayoutManager(new LinearLayoutManager(getActivity()));

        HomeListAdapter homeListAdapter = new HomeListAdapter(expenseList, getActivity());
        mRecyclerViewHome.setAdapter(homeListAdapter);
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


}
