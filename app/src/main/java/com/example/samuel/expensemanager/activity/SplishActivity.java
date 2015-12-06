package com.example.samuel.expensemanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.samuel.expensemanager.R;
import com.example.samuel.expensemanager.utils.SPUtils;

public class SplishActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splish);
        boolean passset = SPUtils.getBoolean(this, "passset", false);
        if (passset) {
            startActivity(new Intent(this, PassInputActivity.class));
            finish();
        } else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}
