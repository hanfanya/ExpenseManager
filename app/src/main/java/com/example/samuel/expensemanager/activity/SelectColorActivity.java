package com.example.samuel.expensemanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.samuel.expensemanager.R;
import com.example.samuel.expensemanager.adapter.SelectColorAdapter;
import com.example.samuel.expensemanager.utils.SysUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SelectColorActivity extends AppCompatActivity {


    @Bind(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    private int[] mColorArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(SysUtils.getThemeResId(SelectColorActivity.this));

        setContentView(R.layout.activity_select_color);
        ButterKnife.bind(this);

        mToolbar.setTitle("请选择类别颜色");
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        initRecyclerview();

        mColorArray = getResources().getIntArray(R.array.colorType);

        Intent intent = getIntent();
        int defaultColor = intent.getIntExtra("defaultColor", 0);
        intent.putExtra("color_position", defaultColor);
        setResult(1, intent);


    }

    private void initRecyclerview() {
        SelectColorAdapter adapter = new SelectColorAdapter(this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 4);
        mRecyclerview.setLayoutManager(layoutManager);
        mRecyclerview.setAdapter(adapter);
        adapter.setOnItemClickListener(new SelectColorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                intent.putExtra("color_position", position);
                setResult(1, intent);
                finish();
            }
        });
    }

    /*@Override
    public void onBackPressed() {
    }*/
}
