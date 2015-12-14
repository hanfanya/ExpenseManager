package com.example.samuel.expensemanager.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.samuel.expensemanager.R;
import com.example.samuel.expensemanager.utils.SPUtils;
import com.example.samuel.expensemanager.utils.SysUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PassInputActivity extends AppCompatActivity implements View.OnClickListener {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.one)
    TextView one;
    @Bind(R.id.two)
    TextView two;
    @Bind(R.id.three)
    TextView three;
    @Bind(R.id.four)
    TextView four;
    @Bind(R.id.five)
    TextView five;
    @Bind(R.id.six)
    TextView six;
    @Bind(R.id.seven)
    TextView seven;
    @Bind(R.id.eight)
    TextView eight;
    @Bind(R.id.nine)
    TextView nine;
    @Bind(R.id.zero)
    TextView zero;
    @Bind(R.id.delete)
    ImageButton delete;
    @Bind(R.id.pass1)
    TextView pass1;
    @Bind(R.id.pass2)
    TextView pass2;
    @Bind(R.id.pass3)
    TextView pass3;
    @Bind(R.id.pass4)
    TextView pass4;
    @Bind(R.id.tv_dsc)
    TextView tvDsc;
    @Bind(R.id.pas)
    LinearLayout mPas;

    private String p;
    private String password;

    /*    @Override
        public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
            super.onCreate(savedInstanceState, persistentState);
        }*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(SysUtils.getThemeResId(PassInputActivity.this));

        setContentView(R.layout.activity_password_setting);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //设置返回按钮

        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
        five.setOnClickListener(this);
        six.setOnClickListener(this);
        seven.setOnClickListener(this);
        eight.setOnClickListener(this);
        nine.setOnClickListener(this);
        zero.setOnClickListener(this);
        delete.setOnClickListener(this);
        p = "";

        toolbar.setTitle("请输入密码");
        setSupportActionBar(toolbar);

        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        int primaryColor = typedValue.data;
        mPas.setBackgroundColor(primaryColor);

        password = SPUtils.getString(this, "password", "");
    }


    @Override
    public void onClick(View v) {
        int length = p.length();
        if (length < 4) {
            switch (v.getId()) {
                case R.id.one:
                    p += "1";
                    break;
                case R.id.two:
                    p += "2";
                    break;
                case R.id.three:
                    p += "3";
                    break;
                case R.id.four:
                    p += "4";
                    break;
                case R.id.five:
                    p += "5";
                    break;
                case R.id.six:
                    p += "6";
                    break;
                case R.id.seven:
                    p += "7";
                    break;
                case R.id.eight:
                    p += "8";
                    break;
                case R.id.nine:
                    p += "9";
                    break;
                case R.id.zero:
                    p += "0";
                    break;
                case R.id.delete:
                    if (length > 0) {
                        p = p.substring(0, length - 1);
                    }
                    break;
            }
        } else if (length == 4 && v.getId() == R.id.delete) {
            p = p.substring(0, length - 1);
        }
        int lengthafter = p.length();
        switch (lengthafter) {
            case 0:
                pass1.setText("");
                pass2.setText("");
                pass3.setText("");
                pass4.setText("");
                break;
            case 1:
                pass1.setText("*");
                pass2.setText("");
                pass3.setText("");
                pass4.setText("");
                break;
            case 2:
                pass1.setText("*");
                pass2.setText("*");
                pass3.setText("");
                pass4.setText("");
                break;
            case 3:
                pass1.setText("*");
                pass2.setText("*");
                pass3.setText("*");
                pass4.setText("");
                break;
            case 4:
                pass1.setText("*");
                pass2.setText("*");
                pass3.setText("*");
                pass4.setText("*");
                //判断密码是否正确
                if (p.equals(password)) {
                    Intent intent = new Intent(PassInputActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    pass1.setText("");
                    pass2.setText("");
                    pass3.setText("");
                    pass4.setText("");
                    tvDsc.setText("密码错误,请重新输入");
                    p = "";
                    tvDsc.setTextColor(Color.RED);
                }
                break;
        }
    }
}