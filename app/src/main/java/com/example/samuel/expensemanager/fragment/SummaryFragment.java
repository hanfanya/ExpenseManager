package com.example.samuel.expensemanager.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.samuel.expensemanager.R;
import com.example.samuel.expensemanager.view.CircleProgress;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SummaryFragment extends Fragment {


    @Bind(R.id.tv_month_in)
    TextView mTvMonthIn;
    @Bind(R.id.ll_month_in)
    LinearLayout mLlMonthIn;
    @Bind(R.id.circlePb)
    CircleProgress mCirclePb;
    @Bind(R.id.tv_not_budget)
    TextView mTvNotBudget;
    @Bind(R.id.tv_budget_figure)
    TextView mTvBudgetFigure;
    @Bind(R.id.tv_budget_title)
    TextView mTvBudgetTitle;
    @Bind(R.id.ll_circle)
    RelativeLayout mLlCircle;
    @Bind(R.id.tv_month_out)
    TextView mTvMonthOut;
    @Bind(R.id.ll_month_out)
    LinearLayout mLlMonthOut;

    public SummaryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summary, container, false);
        ButterKnife.bind(this, view);
        mCirclePb.setTargetProgress(80);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
