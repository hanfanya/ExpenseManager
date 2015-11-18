package com.example.samuel.expensemanager.fragment;


import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.samuel.expensemanager.R;
import com.example.samuel.expensemanager.adapter.ExpenseRecyclerViewAdapter;
import com.example.samuel.expensemanager.model.TypeInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExpenseFragment extends Fragment {
    public static final String[] typeExpense = new String[]{"早餐", "午餐", "晚餐", "夜宵", "零食", "软件", "App Store", "话费", "手机", "衣服", "书籍", "交通", "药品", "电影", "饮料", "物业", "房租",
            "早餐", "午餐", "晚餐", "夜宵", "零食", "软件", "App Store", "话费", "手机", "衣服", "书籍", "交通", "药品", "电影", "饮料", "物业", "房租",
            "早餐", "午餐", "晚餐", "夜宵", "零食", "软件", "App Store", "话费", "手机", "衣服", "书籍", "交通", "药品", "电影", "饮料", "物业", "房租"};

    private List<TypeInfo> mTypeInfos;
    private Context mContext;
    private GridLayoutManager mGridLayoutManager;
    private RelativeLayout mRlExpenseInfo;
    private LinearLayout mLlExpenseCal;
    private boolean isShow = true;
    private RelativeLayout mRlExpenseCal;
    private ImageView mIvExpenseType;
    private TextView mTvExpenseType;
    private Menu mMenu;
    private RecyclerView mRecyclerViewType;


    public ExpenseFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_expense, container, false);
        mRecyclerViewType = (RecyclerView) view.findViewById(R.id.recyclerview_expense);
        mRlExpenseInfo = (RelativeLayout) view.findViewById(R.id.rl_expense_info);
        mLlExpenseCal = (LinearLayout) view.findViewById(R.id.ll_expense_cal);
        mRlExpenseCal = (RelativeLayout) view.findViewById(R.id.rl_expense_cal);
        mIvExpenseType = (ImageView) view.findViewById(R.id.iv_expense_type);
        mTvExpenseType = (TextView) view.findViewById(R.id.tv_expense_type);

        initData();
        initUI();
        initListener();


        return view;
    }

    private void initUI() {
        mGridLayoutManager = new GridLayoutManager(getActivity(), 5);
        ExpenseRecyclerViewAdapter recyclerViewAdapter = new ExpenseRecyclerViewAdapter(mTypeInfos);

        mRecyclerViewType.setLayoutManager(mGridLayoutManager);
        mRecyclerViewType.setAdapter(recyclerViewAdapter);

    }

    private void initListener() {
        mRlExpenseInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShow) {
                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mRlExpenseCal, "translationY", 0, mLlExpenseCal.getHeight());
                    objectAnimator.setDuration(200);
                    objectAnimator.start();
                    isShow = false;
                } else {
                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mRlExpenseCal, "translationY", mLlExpenseCal.getHeight(), 0);
                    objectAnimator.setDuration(200);
                    objectAnimator.start();
                    isShow = true;
                }


            }
        });

    }

    private void initData() {
        mTypeInfos = new ArrayList<>();
        int[] colorArray = getActivity().getResources().getIntArray(R.array.rainbow);

        for (int i = 0; i < colorArray.length; i++) {
            TypeInfo typeInfo = new TypeInfo();

            typeInfo.setTypeColor(colorArray[i]);
            typeInfo.setTypeName(typeExpense[i]);

            mTypeInfos.add(typeInfo);
        }

    }

}
