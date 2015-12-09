package com.example.samuel.expensemanager.fragment;


import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.samuel.expensemanager.ExpenseApplication;
import com.example.samuel.expensemanager.R;
import com.example.samuel.expensemanager.adapter.TypeInfoListAdapter;
import com.example.samuel.expensemanager.model.DaoSession;
import com.example.samuel.expensemanager.model.Expense;
import com.example.samuel.expensemanager.model.ExpenseDao;
import com.example.samuel.expensemanager.model.TypeInfo;
import com.example.samuel.expensemanager.model.TypeInfoDao;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class IncomeTypeInfoFragment extends Fragment {


    @Bind(R.id.recyclerview_typeinfo)
    RecyclerView mRecyclerviewTypeinfo;
    private TypeInfoDao mTypeInfoDao;
    private List<TypeInfo> mTypeInfos;
    private TypeInfoListAdapter mAdapter;
    private ExpenseDao mExpenseDao;

    public IncomeTypeInfoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaoSession daoSession = ((ExpenseApplication) getActivity().getApplicationContext()).getDaoSession();
        mTypeInfoDao = daoSession.getTypeInfoDao();
        mExpenseDao = daoSession.getExpenseDao();
        mTypeInfos = mTypeInfoDao.queryBuilder().where(TypeInfoDao.Properties.TypeFlag.eq(0),
                TypeInfoDao.Properties.UploadFlag.in(0, 1, 5, 8)).list();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expense_type_info, container, false);
        ButterKnife.bind(this, view);
        initList();
        return view;
    }

    private void initList() {
        mAdapter = new TypeInfoListAdapter(mTypeInfos, getActivity());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerviewTypeinfo.setItemAnimator(new DefaultItemAnimator());
        mRecyclerviewTypeinfo.setLayoutManager(layoutManager);
        mRecyclerviewTypeinfo.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
                .color(Color.GRAY)
                .build());
        mRecyclerviewTypeinfo.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new TypeInfoListAdapter.OnItemClickListener() {
            @Override
            public boolean onItemLongClick(View view, int position) {
                showActionDialog(position);
                return false;
            }
        });
        mRecyclerviewTypeinfo.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                FloatingActionButton floatingActionButton = (FloatingActionButton) getActivity().findViewById(R.id.fab_typeinfo);
                if (dy > 0) {
                    floatingActionButton.hide();
                }
                if (dy < 0) {
                    floatingActionButton.show();

                }
            }
        });

    }

    private void showActionDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("谨慎操作");
        builder.setMessage("确定删除该类别吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteTypeRecord(position);
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    private void deleteTypeRecord(int position) {
        TypeInfo typeInfo = mTypeInfos.get(position);

        List<Expense> expenseList = mExpenseDao.queryBuilder().
                where(ExpenseDao.Properties.TypeName.eq(typeInfo.getTypeName())).list();
        for (int i = 0; i < expenseList.size(); i++) {
            Expense expense = expenseList.get(i);
            Integer expenseUploadFlag = expense.getUploadFlag();
            switch (expenseUploadFlag) {
                case 0:
                case 1:
                    mExpenseDao.delete(expense);
                    break;
                case 5:
                    expense.setUploadFlag(6);
                    mExpenseDao.update(expense);
                    break;
                case 8:
                    expense.setUploadFlag(6);
                    mExpenseDao.update(expense);
                    break;
                default:
                    break;
            }

        }
        Integer uploadFlag = typeInfo.getUploadFlag();
        switch (uploadFlag) {
            case 0:
                mTypeInfoDao.delete(typeInfo);
                break;
            case 8:
                typeInfo.setUploadFlag(6);
                mTypeInfoDao.update(typeInfo);
                break;
            default:
                break;

        }
        mTypeInfos.remove(position);
        mAdapter.notifyDataSetChanged();

        Toast.makeText(getActivity(), "该类别已删除", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
