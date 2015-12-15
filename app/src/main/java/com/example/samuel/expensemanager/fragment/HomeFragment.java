package com.example.samuel.expensemanager.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.samuel.expensemanager.ExpenseApplication;
import com.example.samuel.expensemanager.R;
import com.example.samuel.expensemanager.activity.AddRecordActivity;
import com.example.samuel.expensemanager.adapter.HomeListAdapter;
import com.example.samuel.expensemanager.model.DaoSession;
import com.example.samuel.expensemanager.model.Expense;
import com.example.samuel.expensemanager.model.ExpenseDao;
import com.example.samuel.expensemanager.utils.CalUtils;
import com.example.samuel.expensemanager.utils.SPUtils;
import com.example.samuel.expensemanager.view.CountView;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.dao.query.QueryBuilder;

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
//    @Bind(R.id.tv_no_expense)
//    TextView mTvNoExpense;

    //    private RecyclerView mRecyclerViewHome;
//    private FloatingActionButton mFabHome;
    private HomeListAdapter mHomeListAdapter;
    private List<Expense> mExpenseList;
    private String mStartDate;
    private String mEndDate;
    private ExpenseDao mExpenseDao;
    private TextView mTvNoExpense;


    public HomeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        mTvNoExpense = (TextView) view.findViewById(R.id.tv_no_expense);

//        initRecyclerViewList();
        //设置recyclerview布局


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
        double sumToday = 0;
        double sumMonth = 0;
        String month = CalUtils.getCurrentDate().substring(0, 6);
        System.out.println("month=" + month);
        //获取当月的记录
        List<Expense> expenseMonth = mExpenseDao.queryBuilder()
                .where(ExpenseDao.Properties.Date.like(month + "%"), ExpenseDao.Properties.TypeFlag.eq(1),
                        ExpenseDao.Properties.UploadFlag.in(0, 1, 5, 8)).list();
        System.out.println("mExpenseMonth.size()" + expenseMonth.size());
        for (int i = 0; i < expenseMonth.size(); i++) {
            Expense expense = expenseMonth.get(i);
            if (expense.getDate().equals(CalUtils.getCurrentDate())) {
                sumToday = sumToday + expense.getFigure();//统计今日支出
            }
            sumMonth = sumMonth + expense.getFigure();//统计本月支出
        }
        Log.i("home", expenseMonth.size() + "");
        mTvTodayOut.showNumberWithAnimation(String.valueOf(sumToday));
        mTvMonthOut.showNumberWithAnimation(String.valueOf(sumMonth));
    }

    public void initRecyclerViewList() {

        QueryBuilder builder = mExpenseDao.queryBuilder();
        builder.where(ExpenseDao.Properties.Date.between(mStartDate, mEndDate),
                ExpenseDao.Properties.TypeFlag.eq(1), ExpenseDao.Properties.UploadFlag.in(0, 1, 5, 8))
                .orderDesc(ExpenseDao.Properties.Time);

        mExpenseList = builder.list();


        if (mExpenseList.size() != 0) {
            mTvNoExpense.setVisibility(View.INVISIBLE);

            mRecyclerviewHome.setLayoutManager(new LinearLayoutManager(getActivity()));

            mRecyclerviewHome.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
                    .build());//设置 divider 分割线 margin(110, 55)

//      recyclerview 滑动时，FabButton 隐藏或显示

            mHomeListAdapter = new HomeListAdapter(mExpenseList, getActivity());
            mRecyclerviewHome.setAdapter(mHomeListAdapter);
            mRecyclerviewHome.setItemAnimator(new DefaultItemAnimator());//设置默认动画
            mHomeListAdapter.setOnItemClickListener(new HomeListAdapter.OnItemClickListener() {
                @Override
                public boolean onItemLongClick(View view, int position) {
                    showActionDialog(position);

                    return false;
                }
            });

            mRecyclerviewHome.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    FloatingActionButton floatingActionButton = (FloatingActionButton) getActivity().findViewById(R.id.fab_home);
                    if (dy > 0) {
                        floatingActionButton.hide();
                    }
                    if (dy < 0) {
                        floatingActionButton.show();

                    }
                }
            });
        } else {
            mTvNoExpense.setVisibility(View.VISIBLE);
        }
    }

    private void showActionDialog(final int position) {
        String[] items = {"删除记录", "编辑记录"};
        new AlertDialog.Builder(getActivity())
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                deleteRecord(position);
                                break;
                            case 1:
                                editRecord(position);
                                break;
                            default:
                                break;
                        }
                        dialog.dismiss();
                    }
                }).show();
    }

    private void deleteRecord(int position) {
        Expense expense = mExpenseList.get(position);
        Integer uploadFlag = expense.getUploadFlag();
        switch (uploadFlag) {
            case 0:
            case 1:
                mExpenseDao.delete(expense);
                break;
            case 5:
                expense.setUploadFlag(7);
                mExpenseDao.update(expense);
                break;
            case 8:
                expense.setUploadFlag(6);
                mExpenseDao.update(expense);
                break;
            default:
                break;

        }
        setTodayAndMonth();
        mExpenseList.remove(position);
        mHomeListAdapter.notifyDataSetChanged();
        Toast.makeText(getActivity(), "记录已删除", Toast.LENGTH_SHORT).show();
        SPUtils.saveBoolean(getActivity(), "isSame", false);

    }

    private void editRecord(int position) {
        Expense expense = mExpenseList.get(position);

        Long id = expense.getId();
        Integer typeFlag = expense.getTypeFlag();
        Intent intent = new Intent(getActivity(), AddRecordActivity.class);
        intent.putExtra("edit_record", id);
        intent.putExtra("type_flag", typeFlag);
        intent.putExtra("isCreated", false);
        startActivity(intent);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
