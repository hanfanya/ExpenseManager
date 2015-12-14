package com.example.samuel.expensemanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.samuel.expensemanager.ExpenseApplication;
import com.example.samuel.expensemanager.R;
import com.example.samuel.expensemanager.adapter.SearchResultListAdapter;
import com.example.samuel.expensemanager.model.DaoSession;
import com.example.samuel.expensemanager.model.Expense;
import com.example.samuel.expensemanager.model.ExpenseDao;
import com.example.samuel.expensemanager.utils.SPUtils;
import com.example.samuel.expensemanager.utils.SysUtils;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.dao.query.QueryBuilder;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    @Bind(R.id.toolbar_search)
    Toolbar mToolbarSearch;
    @Bind(R.id.recyclerview_search)
    RecyclerView mRecyclerviewSearch;
    @Bind(R.id.tv_no_result)
    TextView mTvNoResult;
    private ExpenseDao mExpenseDao;
    private List<Expense> mExpenseList;
    private SearchResultListAdapter mListAdapter;
    private String mQuery;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(SysUtils.getThemeResId(SearchActivity.this));

        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        DaoSession daoSession = ((ExpenseApplication) getApplicationContext()).getDaoSession();
        mExpenseDao = daoSession.getExpenseDao();
        mExpenseList = new ArrayList<>();

        initUI();
        initList();

    }

    private void initUI() {
        setSupportActionBar(mToolbarSearch);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initList() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mListAdapter = new SearchResultListAdapter(mExpenseList);
        mRecyclerviewSearch.setLayoutManager(layoutManager);
        mRecyclerviewSearch.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .build());
        mRecyclerviewSearch.setItemAnimator(new DefaultItemAnimator());//设置默认动画
        mRecyclerviewSearch.setAdapter(mListAdapter);

        mListAdapter.setOnItemClickListener(new SearchResultListAdapter.OnItemClickListener() {
            @Override
            public void onItemEditClick(View view, int position) {
                editRecord(position);
            }

            @Override
            public void onItemDeleteClick(View view, int position) {
                deleteRecord(position);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        mSearchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
//        searchView.setSubmitButtonEnabled(true);
        mSearchView.setIconified(false);
        mSearchView.setOnQueryTextListener(this);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mQuery = mSearchView.getQuery().toString();
        mExpenseList.clear();
//        Toast.makeText(SearchActivity.this, "hello " + newText, Toast.LENGTH_SHORT).show();
        searchExpense(newText);

        return false;
    }

    private void searchExpense(String queryText) {
        if (!TextUtils.isEmpty(queryText)) {
            QueryBuilder builder = mExpenseDao.queryBuilder();
            builder.where(ExpenseDao.Properties.TypeName.like("%" + queryText + "%"),
                    ExpenseDao.Properties.UploadFlag.in(0, 1, 5, 8))
                    .orderDesc(ExpenseDao.Properties.Time);

            mExpenseList = builder.list();
            if (mExpenseList.size() == 0) {
                mRecyclerviewSearch.setVisibility(View.INVISIBLE);
                mTvNoResult.setVisibility(View.VISIBLE);
            } else {
                mRecyclerviewSearch.setVisibility(View.VISIBLE);
                mTvNoResult.setVisibility(View.INVISIBLE);
            }
            initList();
            /*mListAdapter = new SearchResultListAdapter(mExpenseList);
            mRecyclerviewSearch.setAdapter(mListAdapter);*/
        } else {
            mRecyclerviewSearch.setVisibility(View.INVISIBLE);
            mTvNoResult.setVisibility(View.VISIBLE);
        }


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

        mExpenseList.remove(position);
        mListAdapter.notifyDataSetChanged();
        Toast.makeText(this, "记录已删除", Toast.LENGTH_SHORT).show();
        SPUtils.saveBoolean(this, "isSame", false);

    }

    private void editRecord(int position) {
        Expense expense = mExpenseList.get(position);

        Long id = expense.getId();
        Integer typeFlag = expense.getTypeFlag();
        Intent intent = new Intent(this, AddRecordActivity.class);
        intent.putExtra("edit_record", id);
        intent.putExtra("type_flag", typeFlag);
        intent.putExtra("isCreated", false);
        startActivity(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(mQuery)) {
            onQueryTextChange(mQuery);
        }

    }
}
