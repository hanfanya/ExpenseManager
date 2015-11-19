package com.example.samuel.expensemanager.fragment;


import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    private ExpenseRecyclerViewAdapter mRecyclerViewAdapter;


    public ExpenseFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
//        mRecyclerViewAdapter = new ExpenseRecyclerViewAdapter(mTypeInfos);

        mRecyclerViewType.setLayoutManager(mGridLayoutManager);
        mRecyclerViewType.setAdapter(mRecyclerViewAdapter);

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

        //item的点击事件
        mRecyclerViewAdapter.setOnItemClickListener(new ExpenseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.i("ExpenseFragment", "点击了 " + position);
                TypeInfo typeInfo = mTypeInfos.get(position);

                mIvExpenseType.setColorFilter(typeInfo.getTypeColor());
                mTvExpenseType.setText(typeInfo.getTypeName());
                mRecyclerViewAdapter.setSelection(position);
                mRecyclerViewAdapter.notifyDataSetChanged();
                if (!isShow) {
                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mRlExpenseCal, "translationY", mLlExpenseCal.getHeight(), 0);
                    objectAnimator.setDuration(200);
                    objectAnimator.start();
                    isShow = true;
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        //向下滚动时，隐藏计算器
        mRecyclerViewType.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 20 && isShow) {
                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mRlExpenseCal, "translationY", 0, mLlExpenseCal.getHeight());
                    objectAnimator.setDuration(200);
                    objectAnimator.start();
                    isShow = false;
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

    //从fragment中添加toolbar菜单
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_expense, menu);
        this.mMenu = menu;
        MenuItem dateItem = mMenu.findItem(R.id.action_date);
        dateItem.setTitle("2015/11/15");
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_date) {
            Log.i("ExpenseFragment", "从 ExpenseFragment 中点击");
            MenuItem dateItem = mMenu.findItem(R.id.action_date_title);
            dateItem.setTitle("hello");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
