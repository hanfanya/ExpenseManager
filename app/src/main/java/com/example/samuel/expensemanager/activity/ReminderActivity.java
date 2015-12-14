package com.example.samuel.expensemanager.activity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.example.samuel.expensemanager.R;
import com.example.samuel.expensemanager.receiver.TimeChangeReceiver;
import com.example.samuel.expensemanager.service.TimeService;
import com.example.samuel.expensemanager.utils.CheckServiceUtils;
import com.example.samuel.expensemanager.utils.SPUtils;
import com.example.samuel.expensemanager.utils.SysUtils;
import com.example.samuel.expensemanager.view.ClickView;
import com.kyleduo.switchbutton.SwitchButton;

import org.joda.time.DateTime;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ReminderActivity extends AppCompatActivity implements View.OnClickListener,
        RadialTimePickerDialogFragment.OnTimeSetListener {

    // AlertDialog的选项
    public static final String[] DATE = {"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"};
    // 记录
    public static final String[] pick_day = {"一", "二", "三", "四", "五", "六", "日"};
    private static final String FRAG_TAG_TIME_PICKER = "timePickerDialogFragment";
    // 储存用户选择的日期，用来显示在TextView里面
    public static StringBuilder mSelectDayBuilder;
    // 选择日期的AlertDialog 选择状态
    public static boolean[] pick = {false, false, false, false, false, false, false};
    // sp中用户选择的日期和时间
    private static String mSelectDay;// 选择的日期
    private static int mSelectHour;// 选择的小时
    private static int mSelectMinute;// 选择的分钟
    private static boolean status;// 定时提醒是否开启标记
    @Bind(R.id.toolbar_home)
    Toolbar toolbarHome;
    @Bind(R.id.sb_use_checked)
    SwitchButton mUseChecked;// 开启定时提醒按钮
    @Bind(R.id.cv_day)
    ClickView mDay;// 选择日期按钮
    @Bind(R.id.cv_time)
    ClickView mTime;// 选择时间按钮
    private boolean isServiceRunning = false;// 服务是否运行的标志
    private boolean mHasDialogFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(SysUtils.getThemeResId(ReminderActivity.this));

        setContentView(R.layout.activity_reminder);
        ButterKnife.bind(this);

        toolbarHome.setTitle("记账提醒");
        setSupportActionBar(toolbarHome);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        if (savedInstanceState == null) {// 如果savedInstanceState为空，说明之前的activity没有保存信息
            mHasDialogFrame = findViewById(R.id.frame) != null;// 如果可以找到FrameLayout,说明dialog有可以填充的布局
        }

        initData();

        mUseChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    openReminder();
                } else {
                    closeReminder();
                }
            }
        });

    }

    // 初始化
    private void initData() {
        // 初始化，默认是不提醒，如果开启提醒则默认每天的20:30发送提醒
        status = SPUtils.getBoolean(ReminderActivity.this, "remind_status", false);
        mSelectDay = SPUtils.getString(ReminderActivity.this, "pick_day", "一 二 三 四 五 六 日 ");
        mSelectHour = SPUtils.getInt(ReminderActivity.this, "remind_time_hour", 20);
        mSelectMinute = SPUtils.getInt(ReminderActivity.this, "remind_time_minute", 30);

        mDay.setTitleText("设置提醒周期");
        mTime.setTitleText("设置提醒时间");

        mDay.setOnClickListener(ReminderActivity.this);
        mTime.setOnClickListener(ReminderActivity.this);
    }


    // 开启一个30秒循环的广播
    private void setAlarm() {
        // 指定启动TimeChangeReceiver对象
        Intent intent = new Intent(ReminderActivity.this, TimeChangeReceiver.class);
        intent.setAction("ReminderActivity.openReminder.repeat");
        PendingIntent sender = PendingIntent.getBroadcast(ReminderActivity.this, 0, intent, 0);

        // 开始时间
        long startTime = SystemClock.elapsedRealtime();

        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        // 开启一个10秒循环的广播
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, startTime, 30 * 1000, sender);
        Log.i("today", "ReminderActivity : openReminder : 开启30s广播");
    }

    // 把30秒的广播关掉
    private void cancelAlarm() {
        Intent intent = new Intent(ReminderActivity.this, TimeChangeReceiver.class);
        intent.setAction("ReminderActivity.openReminder.repeat");
        PendingIntent sender = PendingIntent.getBroadcast(ReminderActivity.this, 0, intent, 0);
        // 取消广播
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.cancel(sender);
        Log.i("today", "ReminderActivity : closeReminder : 关闭30s广播");
    }

    /**
     * 设置点击响应事件
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.cv_day:// 设置提醒周期
                Log.i("test", "onClick mDay");
                selectDay();
                break;
            case R.id.cv_time:// 设置提醒时间
                Log.i("test", "onClick mTime");
                selectTime();
                break;
            default:
                break;
        }
    }

    /**
     * 开启定时提醒
     */
    private void openReminder() {
        status = true;
        SPUtils.saveBoolean(ReminderActivity.this, "remind_status", true);
        mDay.setClickable(status);
        mTime.setClickable(status);
        Log.i("test", "onCheckedChanged" + status);

        Intent timeService = new Intent(ReminderActivity.this, TimeService.class);
        startService(timeService);
        Log.i("today", "ReminderActivity : openReminder : 开启服务");
    }

    /**
     * 关闭定时提醒
     */
    private void closeReminder() {
        status = false;
        SPUtils.saveBoolean(ReminderActivity.this, "remind_status", status);
        mDay.setClickable(status);
        mTime.setClickable(status);
        Log.i("test", "onCheckedChanged" + status);

        Intent timeService = new Intent(ReminderActivity.this, TimeService.class);
        stopService(timeService);
        Log.i("today", "ReminderActivity : closeReminder : 关闭服务");

        cancelAlarm();

    }

    /**
     * 选择提醒日期
     */
    private void selectDay() {
        //拿到选择状态
        for (int i = 0; i < 7; i++) {
            pick[i] = SPUtils.getBoolean(ReminderActivity.this, "pick_status" + i, true);
        }
        mSelectDayBuilder = new StringBuilder();
        new AlertDialog.Builder(ReminderActivity.this)
                .setTitle("请选择提醒日期")
                .setMultiChoiceItems(DATE,
                        pick, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                pick[which] = isChecked;// 获取勾选状态
                            }
                        })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 写入配置文件
                        for (int i = 0; i < 7; i++) {
                            SPUtils.saveBoolean(ReminderActivity.this, "pick_status" + i, pick[i]);
                            if (pick[i]) {
                                mSelectDayBuilder.append(pick_day[i] + " ");
                            }
                        }
                        // 把选择的日期写入配置文件
                        SPUtils.saveString(ReminderActivity.this, "pick_day", mSelectDayBuilder.toString());
                        // 显示选择的日期
                        mDay.setTextViewText(SPUtils.getString(ReminderActivity.this, "pick_day"
                                , "一 二 三 四 五 六 日 "));
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    /**
     * 选择提醒时间
     */
    private void selectTime() {
        DateTime now = DateTime.now();
        RadialTimePickerDialogFragment timePickerDialog = RadialTimePickerDialogFragment
                .newInstance(ReminderActivity.this,
                        now.getHourOfDay(),
                        now.getMinuteOfHour(),
                        DateFormat.is24HourFormat(ReminderActivity.this));

        if (mHasDialogFrame) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            ft.add(R.id.frame, timePickerDialog, FRAG_TAG_TIME_PICKER)// 把timePickerDialog添加到frameLayout中
                    .commit();
        } else {
            timePickerDialog.show(getSupportFragmentManager(), FRAG_TAG_TIME_PICKER);
        }
    }

    // 获取选择的时间
    @Override
    public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
        String text = null;
        if (hourOfDay < 10) {
            if (minute < 10) {
                text = "您选择了：" + "0" + hourOfDay + "时" + "0" + minute + "分";
            } else {
                text = "您选择了：" + "0" + hourOfDay + "时" + minute + "分";
            }
        } else if (hourOfDay >= 10) {
            if (minute < 10) {
                text = "您选择了：" + hourOfDay + "时" + "0" + minute + "分";
            } else {
                text = "您选择了：" + hourOfDay + "时" + minute + "分";
            }
        }

        Toast.makeText(ReminderActivity.this, text, Toast.LENGTH_SHORT).show();
        // 写入配置文件
        SPUtils.saveInt(ReminderActivity.this, "remind_time_hour", hourOfDay);
        SPUtils.saveInt(ReminderActivity.this, "remind_time_minute", minute);
        if (hourOfDay < 10) {
            if (minute < 10) {
                mTime.setTextViewText("0" + hourOfDay + " : " + "0" + minute);
            } else {
                mTime.setTextViewText("0" + hourOfDay + " : " + minute);
            }
        } else if (hourOfDay >= 10) {
            if (minute < 10) {
                mTime.setTextViewText(hourOfDay + " : " + "0" + minute);
            } else {
                mTime.setTextViewText(hourOfDay + " : " + minute);
            }
        }
    }

    @Override
    protected void onDestroy() {
        Log.i("today", "ReminderActivity : onDestroy");
        // 如果开启了提醒，在此处再发出一个自定义广播，去开启服务
        Intent intent = new Intent("com.example.barry.clockdemo.ReminderActivity.onDestroy");
        sendBroadcast(intent);
        Log.i("today", "ReminderActivity : onDestroy : 活动被杀死了");

        restartService();

        super.onDestroy();
    }

    /**
     * 如果开启了提醒，但是服务没有运行，通过这个方法重启服务
     */
    private void restartService() {
        if (status) {
            isServiceRunning = CheckServiceUtils.isServiceRunning(ReminderActivity.this,
                    "com.example.barry.clockdemo.TimeService");
            if (!isServiceRunning) {// 如果服务被关闭了，开启服务
                Intent service = new Intent(ReminderActivity.this, TimeService.class);
                ReminderActivity.this.startService(service);
                Log.i("today", "ReminderActivity : 开启服务");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("today", "ReminderActivity : onResume");
        //回显三个数据
        mUseChecked.setCheckedImmediately(status ? true : false);// 开关
        mDay.setTextViewText(mSelectDay);//日期
        String text = null;
        if (mSelectHour < 10) {
            if (mSelectMinute < 10) {
                text = "0" + mSelectHour + "时" + "0" + mSelectMinute + "分";
            } else {
                text = "0" + mSelectHour + "时" + mSelectMinute + "分";
            }
        } else if (mSelectHour >= 10) {
            if (mSelectMinute < 10) {
                text = mSelectHour + "时" + "0" + mSelectMinute + "分";
            } else {
                text = mSelectHour + "时" + mSelectMinute + "分";
            }
        }
        mTime.setTextViewText(text);// 时间
        mDay.setClickable(status);
        mTime.setClickable(status);
        setAlarm();
        restartService();

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("today", "ReminderActivity : onStart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("today", "ReminderActivity : onPause");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("today", "ReminderActivity : onRestart");
    }

}
