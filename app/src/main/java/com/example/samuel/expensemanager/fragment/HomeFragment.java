package com.example.samuel.expensemanager.fragment;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.samuel.expensemanager.ExpenseAplication;
import com.example.samuel.expensemanager.R;
import com.example.samuel.expensemanager.adapter.HomeListAdapter;
import com.example.samuel.expensemanager.model.DaoSession;
import com.example.samuel.expensemanager.model.Expense;
import com.example.samuel.expensemanager.model.ExpenseDao;

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

        DaoSession daoSession = ((ExpenseAplication) getActivity().getApplicationContext()).getDaoSession();
        ExpenseDao expenseDao = daoSession.getExpenseDao();

        String startDate = "2015-06-01";
        String endDate = "2015-12-31";
        List<Expense> expenseList = expenseDao.queryBuilder()
                .where(ExpenseDao.Properties.Date.between(startDate, endDate))
                .orderAsc(ExpenseDao.Properties.Date).list();
        System.out.println("======================" + expenseList.size());

        mRecyclerViewHome.setLayoutManager(new LinearLayoutManager(getActivity()));

        HomeListAdapter homeListAdapter = new HomeListAdapter(expenseList, getActivity());
        mRecyclerViewHome.setAdapter(homeListAdapter);

        return view;
    }


}
