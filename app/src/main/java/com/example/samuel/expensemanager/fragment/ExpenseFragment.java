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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.samuel.expensemanager.ExpenseApplication;
import com.example.samuel.expensemanager.R;
import com.example.samuel.expensemanager.adapter.ExpenseRecyclerViewAdapter;
import com.example.samuel.expensemanager.model.DaoSession;
import com.example.samuel.expensemanager.model.TypeInfo;
import com.example.samuel.expensemanager.model.TypeInfoDao;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExpenseFragment extends Fragment {
    @Bind(R.id.recyclerview_expense)
    RecyclerView mRecyclerviewExpense;
    @Bind(R.id.key_1)
    Button mKey1;
    @Bind(R.id.key_4)
    Button mKey4;
    @Bind(R.id.key_7)
    Button mKey7;
    @Bind(R.id.key_clear)
    Button mKeyClear;
    @Bind(R.id.key_2)
    Button mKey2;
    @Bind(R.id.key_5)
    Button mKey5;
    @Bind(R.id.key_8)
    Button mKey8;
    @Bind(R.id.key_0)
    Button mKey0;
    @Bind(R.id.key_3)
    Button mKey3;
    @Bind(R.id.key_6)
    Button mKey6;
    @Bind(R.id.key_9)
    Button mKey9;
    @Bind(R.id.key_point)
    Button mKeyPoint;
    @Bind(R.id.key_del)
    FrameLayout mKeyDel;
    @Bind(R.id.key_add)
    FrameLayout mKeyAdd;
    @Bind(R.id.key_ok)
    Button mKeyOk;
    @Bind(R.id.ll_expense_cal)
    LinearLayout mLlExpenseCal;
    @Bind(R.id.iv_expense_type)
    ImageView mIvExpenseType;
    @Bind(R.id.tv_expense_type)
    TextView mTvExpenseType;
    @Bind(R.id.tv_expense_figure)
    TextView mTvExpenseFigure;
    @Bind(R.id.rl_expense_info)
    RelativeLayout mRlExpenseInfo;
    @Bind(R.id.rl_expense_cal)
    RelativeLayout mRlExpenseCal;

    private Context mContext;
    private List<TypeInfo> mTypeInfos;
    private GridLayoutManager mGridLayoutManager;
    private boolean isShow = true;
    private Menu mMenu;
    private ExpenseRecyclerViewAdapter mRecyclerViewAdapter;
    private int[] mColorArray;


    public ExpenseFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expense, container, false);
        ButterKnife.bind(this, view);

        initData();
        initUI();
        initListener();

        return view;
    }

    private void initUI() {
        mGridLayoutManager = new GridLayoutManager(getActivity(), 5);
        mRecyclerViewAdapter = new ExpenseRecyclerViewAdapter(mTypeInfos, getActivity());

        mRecyclerviewExpense.setLayoutManager(mGridLayoutManager);
        mRecyclerviewExpense.setAdapter(mRecyclerViewAdapter);

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

                mIvExpenseType.setColorFilter(mColorArray[typeInfo.getTypeColor()]);
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
        mRecyclerviewExpense.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        DaoSession daoSession = ((ExpenseApplication) getActivity().getApplicationContext()).getDaoSession();
        TypeInfoDao typeInfoDao = daoSession.getTypeInfoDao();

        mTypeInfos = typeInfoDao.queryBuilder().where(TypeInfoDao.Properties.TypeFlag.eq(1)).list();
        mColorArray = getActivity().getResources().getIntArray(R.array.colorType);

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @OnClick({R.id.key_0, R.id.key_1, R.id.key_2,
            R.id.key_3, R.id.key_4, R.id.key_5,
            R.id.key_6, R.id.key_7, R.id.key_8, R.id.key_9})
    public void inputNumber(View view) {
        if (mTvExpenseFigure.length() > 10) {
            return;
        }
        mTvExpenseFigure.append(((Button) view).getText().toString());

    }

    @OnClick(R.id.key_clear)
    public void clearInput(View view) {
        mTvExpenseFigure.setText("￥0.00");


    }

}
