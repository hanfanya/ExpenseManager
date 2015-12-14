package com.example.samuel.expensemanager.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.samuel.expensemanager.R;
import com.example.samuel.expensemanager.utils.SysUtils;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(SysUtils.getThemeResId(AboutActivity.this));

        setContentView(R.layout.activity_about);
    }
}
