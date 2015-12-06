package com.example.samuel.expensemanager.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.example.samuel.expensemanager.R;
import com.example.samuel.expensemanager.fragment.DetailFragment;
import com.example.samuel.expensemanager.utils.DatesUtils;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailActivity extends AppCompatActivity implements OnDateSelectedListener,
        ViewPager.OnPageChangeListener, OnMonthChangedListener {

    private MaterialCalendarView mCvMain;
    private ViewPager vp_expanse_detail;
    private String sDate;  //上面日历选择的日期
    private ActionBar actionBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        assignViews();
        initData();
    }
    private void assignViews() {
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mCvMain = (MaterialCalendarView) findViewById(R.id.cv_main);
        vp_expanse_detail = (ViewPager) findViewById(R.id.vp_expanse_detail);

    }
    /**
     * 初始化数据
     */
    private void initData() {
        //拿到当前日期：20151203
        Date d = new Date();
        String date = format.format(d);
        /**
         *  假定2005年1月1日是5001，调用方法类，将日期转换为position
         *  定位到当前日期的position
         */
        int position = DatesUtils.getPosition(date);
        System.out.println("当前日期是： " + date);
        mCvMain.setArrowColor(getResources().getColor(R.color.white));
//        mCvMain.setLeftArrowMask(getResources().getDrawable(R.drawable.left));
//        mCvMain.setRightArrowMask(getResources().getDrawable(R.drawable.right));
        mCvMain.setDateTextAppearance(R.color.mcv_text_date_light);
        mCvMain.setSelectedDate(d); //选中当前日期
        mCvMain.setOnDateChangedListener(this); //当点选不同的日期时call到
        mCvMain.setOnMonthChangedListener(this);
        vp_expanse_detail.setAdapter(new MyAdapter(getSupportFragmentManager()));
        vp_expanse_detail.setOnPageChangeListener(this);
        vp_expanse_detail.setCurrentItem(position);
    }
    //自定义一个日期格式
    private static final SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
    //获取系统自带的日期格式化工具
    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();

    /**
     * 日历控件监听器接口需要实现的方法
     * 首先获取当前日期，然后再去数据库查询当日的数据
     */
    @Override
    public void onDateSelected(MaterialCalendarView widget, CalendarDay date, boolean selected) {
        CalendarDay select = widget.getSelectedDate();
        Date selectedDate = select.getDate();
        if (selectedDate != null) {
            sDate = DetailActivity.format.format(selectedDate);
            System.out.println("选中的日期是： " + sDate);
            int position = DatesUtils.getPosition(sDate);
            vp_expanse_detail.setCurrentItem(position);
        }
    }
    /**
     * 创建一个Date对象
     */
    public Date getDate(String date) {
        int year = Integer.parseInt(date.substring(0, 4)) - 1900;
        int month = Integer.parseInt(date.substring(4, 6)) - 1;
        int day = Integer.parseInt(date.substring(6));
        Date newDate = new Date(year, month, day);
        return newDate;
    }
    /**
     * viewpager的滑动监听
     */
    @Override
    public void onPageSelected(int position) {
        //实时显示当前页面的位置
        String date = DatesUtils.getDate(position);
        Date newDate = getDate(date);
        mCvMain.setSelectedDate(newDate);//在日历上选中该日期
        mCvMain.setCurrentDate(newDate);//在日历内部选中当前日期，切换月份
        getSupportActionBar().setTitle(date.substring(0, 4) + "/" + date.substring(4, 6)
                + "/" + date.substring(6));
        System.out.println("当前page的位置是： " + position + " ，对应的日期是：" + date);

    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        System.out.println("月份改变了！");
    }

    /**
     * viewpager的适配器
     */
    class MyAdapter extends FragmentStatePagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * 返回相应position的fragment对象
         */
        @Override
        public Fragment getItem(int position) {
            return DetailFragment.newInstance(position);
        }

        /**
         * 设置viewpager有10000页
         */
        @Override
        public int getCount() {
            return 10000;
        }
    }


}
