package com.example.samuel.expensemanager.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.samuel.expensemanager.R;
import com.example.samuel.expensemanager.utils.SPUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;
import cn.sharesdk.framework.ShareSDK;

public class SplashActivity extends AppCompatActivity {

    private static final int ENTER_MAIN = 1;
    private static final int INPUT_PASSWORD = 2;
    //    @Bind(R.id.tv_app_name)
//    TextView mTvAppName;
    @Bind(R.id.tv_version_name)
    TextView mTvVersionName;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ENTER_MAIN:
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                    break;
                case INPUT_PASSWORD:
                    startActivity(new Intent(SplashActivity.this, PassInputActivity.class));
                    finish();
                    break;
                default:
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);

        ShareSDK.initSDK(this);
        Bmob.initialize(this, "a4542ee0d42314bd2d2804e1ca838c5d");


        mTvVersionName.setText(getVersionName());


    }

    @Override
    protected void onResume() {
        super.onResume();

        /*boolean passset = SPUtils.getBoolean(this, "passset", false);
        if (passset) {
            startActivity(new Intent(this, PassInputActivity.class));
            finish();

        } else {
            try {
                Thread.sleep(1000);
//                startActivity(new Intent(SplashActivity.this, MainActivity.class));
//                finish();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
        final boolean passset = SPUtils.getBoolean(this, "passset", false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (passset) {

                    Message msg = handler.obtainMessage();
                    msg.what = INPUT_PASSWORD;
                    handler.sendMessage(msg);
                } else {
                    Message msg = handler.obtainMessage();
                    msg.what = ENTER_MAIN;
                    handler.sendMessage(msg);

                }
            }
        }).start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent("com.example.barry.clockdemo.ReminderActivity.onDestroy");
        sendBroadcast(intent);
    }

    private String getVersionName() {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);// 获取包的信息

            int versionCode = packageInfo.versionCode;
            String versionName = packageInfo.versionName;
            return versionName;
        } catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();
        }

        return "";
    }
}
