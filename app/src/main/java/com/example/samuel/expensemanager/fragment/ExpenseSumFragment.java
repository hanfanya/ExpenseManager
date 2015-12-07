package com.example.samuel.expensemanager.fragment;

import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.example.samuel.expensemanager.R;
import com.example.samuel.expensemanager.adapter.SumExpenseListViewAdapter;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * author: 刘万鹏
 * <p/>
 * 统计页面的支出部分Fragment显示
 * <p/>
 * Created by Mustard on 2015/12/1.
 */
public class ExpenseSumFragment extends Fragment implements RadioGroup.OnCheckedChangeListener, View.OnClickListener, OnChartValueSelectedListener {

    private View mView;
    private TextView mTvDate;
    private RadioGroup mRgRoot;
    private Calendar mCalendar;
    private int mCurrentYear;
    private int mCurrentMonth;
    private int mCurrentDay;

    private Button mBtnStartDate;
    private Button mBtnEndDate;
    private MyStartDate mStartDate;
    private MyEndDate mEndDate;
    private AlertDialog mBuilder;
    private Button mBtnDiy;
    private String startDate;
    private String endDate;
    private PieChart mChart;
    private ListView mListView;
    private Double SumExpense = 0.0;
    private ArrayList<String> typeNames;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_sum_expense, null);
        initUI(mView);
        initData();
        return mView;
    }

    private void initUI(View view) {
        View headerview = View.inflate(getContext(), R.layout.headview_sum_expenselist, null);
        mRgRoot = (RadioGroup) headerview.findViewById(R.id.rg_root_group);
        mTvDate = (TextView) headerview.findViewById(R.id.tv_date_ex);
        mBtnDiy = (Button) headerview.findViewById(R.id.btn_diy_ex);
        mChart = (PieChart) headerview.findViewById(R.id.chart1);
        mListView = (ListView) view.findViewById(R.id.rcv_expense_sum);
        mListView.addHeaderView(headerview);

        //初始化日历对象
        mCalendar = Calendar.getInstance();
        //先拿到当前月份
        mCurrentYear = mCalendar.get(Calendar.YEAR);
        //System.out.println("当前年份"+month);
        mCurrentMonth = mCalendar.get(Calendar.MONTH) + 1;
        mCurrentDay = mCalendar.get(Calendar.DAY_OF_MONTH);

        //设置监听事件
        mRgRoot.setOnCheckedChangeListener(this);
        //给自定义设置点击监听
        mBtnDiy.setOnClickListener(this);
        mStartDate = new MyStartDate(mCurrentYear, mCurrentMonth, mCurrentDay);
        mEndDate = new MyEndDate(mCurrentYear, mCurrentMonth, mCurrentDay);
        //给listview设置监听
        /*mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                System.out.println(position + "----------------------------");
                int Listposition = position - 1;
                Intent intent = new Intent(getActivity(), PasswordSettingActivity.class);
//                String startdate=mStartDate.startYear+mStartDate
                *//*intent.putExtra("startdate", mStartDate.getStartDate());
                intent.putExtra("enddate", mEndDate.getEndDate());

                intent.putExtra("typename", typeNames.get(Listposition));*//*
                startActivity(intent);

            }
        });*/

    }

    private void initData() {
        setMonthDate();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId) {
            case R.id.rb_month_ex://选择本月统计数据
                mBtnDiy.setBackgroundColor(Color.TRANSPARENT);
                setMonthDate();
                break;
            case R.id.rb_halfyear_ex://选择半年统计数据
                mBtnDiy.setBackgroundColor(Color.TRANSPARENT);
                setHalfDate();
                break;
            case R.id.rb_year_ex://选择一年的统计数据
                mBtnDiy.setBackgroundColor(Color.TRANSPARENT);
                setYearDate();
                break;
            default:
                break;
        }
    }

    //弹出自定义选择的dialog
    private void showDateSetDialog() {
        //填充自定义布局
        View view = View.inflate(getActivity(), R.layout.dialog_dateset_sum, null);
        mBuilder = new AlertDialog.Builder(getActivity()).setView(view).show();

        mBtnStartDate = (Button) view.findViewById(R.id.btn_start_date);
        mBtnEndDate = (Button) view.findViewById(R.id.btn_end_date);
        Button btnOk = (Button) view.findViewById(R.id.btn_ok_dialog);
        Button btnCancle = (Button) view.findViewById(R.id.btn_cancle_dialog);
        //每次选择完为button设置变化后的时间
        mBtnStartDate.setText(mStartDate.startYear + "年" + getTwoBmonth(mStartDate.startMonth) + "月" + mStartDate.startDay + "日");
        mBtnEndDate.setText(mEndDate.endYear + "年" + getTwoBmonth(mEndDate.endMonth) + "月" + mEndDate.endDay + "日");
        //设置点击监听
        mBtnStartDate.setOnClickListener(this);
        mBtnEndDate.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        btnCancle.setOnClickListener(this);
    }

    //Button的点击监听
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_date://弹出选择开始日期的日历

                /*DatePickerDialog.newInstance(new myDateSetLinstener(), mStartDate.startYear, mStartDate.startMonth - 1, mStartDate.startDay)
                        .show(getActivity().getFragmentManager(), "start");*/
                CalendarDatePickerDialogFragment mStartDatePickerDialogFragment = CalendarDatePickerDialogFragment.newInstance(new myDateSetLinstener(), mStartDate.startYear, mStartDate.startMonth - 1, mStartDate.startDay);
                mStartDatePickerDialogFragment.show(getFragmentManager(), "start");
                break;
            case R.id.btn_end_date://弹出选择结束日期的日历
                /*DatePickerDialog.newInstance(new myDateSetLinstener(), mEndDate.endYear, mEndDate.endMonth - 1, mEndDate.endDay)
                        .show(getActivity().getFragmentManager(), "end");*/
                CalendarDatePickerDialogFragment mEndDatePickerDialogFragment = CalendarDatePickerDialogFragment.newInstance(new myDateSetLinstener(), mEndDate.endYear, mEndDate.endMonth - 1, mEndDate.endDay);
                mEndDatePickerDialogFragment.show(getFragmentManager(), "end");
                break;
            //点击确定
            case R.id.btn_ok_dialog:
                //设置标题时间
                mRgRoot.clearCheck();
                String startDate = mStartDate.startYear + "年" + getTwoBmonth(mStartDate.startMonth) + "月" + getTwoBmonth(mStartDate.startDay) + "日";
                String endDate = mEndDate.endYear + "年" + getTwoBmonth(mEndDate.endMonth) + "月" + getTwoBmonth(mEndDate.endDay) + "日";
                mTvDate.setText(startDate + "~" + endDate);
                mBuilder.dismiss();
                mBtnDiy.setBackgroundColor(Color.rgb(204, 204, 204));
                setChangeDate(startDate, endDate);
                break;
            case R.id.btn_cancle_dialog:
                mBuilder.dismiss();
                break;
            //点击自定义按钮，弹出选择日期窗口
            case R.id.btn_diy_ex:
                showDateSetDialog();
                break;
            default:
                break;
        }

    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

    }

    //日历选择的回调方法

    @Override
    public void onNothingSelected() {

    }

    private void setYearDate() {
        mStartDate.startYear = mCurrentYear - 1;
        mStartDate.startMonth = mCurrentMonth;

        startDate = (mStartDate.startYear) + "年" + getTwoBmonth(mStartDate.startMonth) + "月";
        endDate = mCurrentYear + "年" + getTwoBmonth(mCurrentMonth) + "月";
        mTvDate.setText(startDate + "~" + endDate);
        setChangeDate(startDate + "01日", endDate + getTwoBmonth(mEndDate.endDay) + "日");
    }

    private void setHalfDate() {

        endDate = mCurrentYear + "年" + getTwoBmonth(mCurrentMonth) + "月";

        if (mCurrentMonth - 5 > 0) {//如果大于5月份
            mStartDate.startYear = mCurrentYear;
            mStartDate.startMonth = mCurrentMonth - 5;

            startDate = mStartDate.startYear + "年" + getTwoBmonth(mStartDate.startMonth) + "月";
            mTvDate.setText(startDate + "~" + endDate);
        } else {//如果小于5月份，开始时间为去年
            mStartDate.startYear = mCurrentYear - 1;
            mStartDate.startMonth = mCurrentMonth + 7;

            startDate = mStartDate.startYear + "年" + getTwoBmonth(mStartDate.startMonth) + "月";
            mTvDate.setText(startDate + "~" + endDate);
        }
        setChangeDate(startDate + "01日", endDate + getTwoBmonth(mEndDate.endDay) + "日");

    }

    public void setMonthDate() {
        mEndDate.endDay = mCurrentDay;
        startDate = mCurrentYear + "年" + getTwoBmonth(mCurrentMonth) + "月";
        mTvDate.setText(startDate);
        setChangeDate(startDate + "01日", mCurrentYear + "年" + getTwoBmonth(mCurrentMonth) + "月" + getTwoBmonth(mEndDate.endDay) + "日");
    }


    public String getTwoBmonth(int month) {
        String twoBmonth = month + "";
        if (month < 10) {
            twoBmonth = '0' + twoBmonth;
        }
        return twoBmonth;
    }

    private void setChangeDate(String startDate, String endDate) {
        String NumberStartDate = startDate.substring(0, 4) + startDate.substring(5, 7) + startDate.substring(8, 10);
        //System.out.println(a+"---------------------------------------");
        String NumberEndDate = endDate.substring(0, 4) + endDate.substring(5, 7) + endDate.substring(8, 10);
        //访问数据库，拿到数据
        SQLiteDatabase db = getContext().openOrCreateDatabase("expense-db", getContext().MODE_PRIVATE, null);
        //从数据库获取总支出
        String sqlSum = "select sum(figure) from EXPENSE where TYPE_FLAG=1 and  UPLOAD_FLAG in (0,1,5,8) and DATE between " + NumberStartDate + "  and  " + NumberEndDate + ";";
        Cursor cursorSum = db.rawQuery(sqlSum, null);

        if (cursorSum.moveToNext()) {
            SumExpense = cursorSum.getDouble(0);
        }
        //设置饼状图
        mChart.setUsePercentValues(false);
        mChart.setExtraOffsets(5, 5, 5, 5);
        mChart.setDescription("");
        mChart.setDragDecelerationFrictionCoef(0.96f);
        mChart.setCenterText(SumExpense.toString() + "元");
        mChart.setCenterTextSize(25.0f);
        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColorTransparent(true);
        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);
        mChart.setHoleRadius(50f);
        mChart.setTransparentCircleRadius(53f);
        mChart.setDrawCenterText(true);
        mChart.setRotationAngle(0);
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setNoDataText("");

        String sql = "select sum(figure),type_name,type_color from EXPENSE where TYPE_FLAG=1 and  UPLOAD_FLAG in (0,1,5,8) and DATE between " + NumberStartDate + " and " + NumberEndDate + "  group by type_name order by sum(figure) desc;";

        Cursor cursor = db.rawQuery(sql, null);
        setData(cursor);

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        mChart.getLegend().setEnabled(false);
    }

    private void setData(Cursor cursor) {

        //用来存放数据的集合
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        //用来存放类别的名称
        typeNames = new ArrayList<String>();
        //用来存放颜色的集合
        ArrayList<Float> eSum = new ArrayList<Float>();
        //用来存放每个类别的总金额
        ArrayList<Integer> colors = new ArrayList<Integer>();
        int i = 0;
        while (cursor.moveToNext()) {
            yVals1.add(new Entry(cursor.getFloat(0), i));
            eSum.add(cursor.getFloat(0));
            typeNames.add(cursor.getString(1));
            int[] colorArray = getContext().getResources().getIntArray(R.array.colorType);
            colors.add(colorArray[cursor.getInt(2)]);
            //System.out.println("-------------------------------------------");
            //System.out.println("Figure"+cursor.getFloat(0)+"类别"+cursor.getString(1)+"colorid"+cursor.getInt(2));
            i++;
        }
        System.out.println("-------------------------------------------");
        PieDataSet dataSet = new PieDataSet(yVals1, "支出类别");
        dataSet.setSliceSpace(2f);
        dataSet.setSelectionShift(5f);

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(typeNames, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(10f);
        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return "";
            }
        });
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
        SumExpenseListViewAdapter mViewAdapter = new SumExpenseListViewAdapter(eSum, typeNames, colors, SumExpense, getContext());
        mListView.setAdapter(mViewAdapter);
    }

    /*//日历选择的回调方法
    private class myDateSetLinstener implements DatePickerDialog.OnDateSetListener {
        //回调用户选择的日期，这里month需要+1
        @Override
        public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
            if ("start".equals(dialog.getTag())) {
                mStartDate.startYear = year;
                mStartDate.startMonth = monthOfYear + 1;
                mStartDate.startDay = dayOfMonth;
                mBtnStartDate.setText(year + "年" + getTwoBmonth(monthOfYear + 1) + "月" + dayOfMonth + "日");
            } else if ("end".equals(dialog.getTag())) {
                mEndDate.endYear = year;
                mEndDate.endMonth = monthOfYear + 1;
                mEndDate.endDay = dayOfMonth;
                mBtnEndDate.setText(year + "年" + getTwoBmonth(monthOfYear + 1) + "月" + dayOfMonth + "日");
            }

        }
    }*/

    private class myDateSetLinstener implements CalendarDatePickerDialogFragment.OnDateSetListener {
        //回调用户选择的日期，这里month需要+1
        @Override
        public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
            if ("start".equals(dialog.getTag())) {
                mStartDate.startYear = year;
                mStartDate.startMonth = monthOfYear + 1;
                mStartDate.startDay = dayOfMonth;
                mBtnStartDate.setText(year + "年" + getTwoBmonth(monthOfYear + 1) + "月" + dayOfMonth + "日");
            } else if ("end".equals(dialog.getTag())) {
                mEndDate.endYear = year;
                mEndDate.endMonth = monthOfYear + 1;
                mEndDate.endDay = dayOfMonth;
                mBtnEndDate.setText(year + "年" + getTwoBmonth(monthOfYear + 1) + "月" + dayOfMonth + "日");
            }
        }
    }

    class MyStartDate {
        private int startYear;
        private int startMonth;
        private int startDay;

        public MyStartDate(int startYear, int startMonth, int startDay) {
            this.startMonth = startMonth;
            this.startYear = startYear;
            this.startDay = startDay;
        }

        public String getStartDate() {
            String startdate = "" + startYear + getTwoBmonth(startMonth) + getTwoBmonth(startDay);
            return startdate;
        }
    }

    class MyEndDate {
        private int endYear;
        private int endMonth;
        private int endDay;

        public MyEndDate(int endYear, int endMonth, int endDay) {
            this.endYear = endYear;
            this.endMonth = endMonth;
            this.endDay = endDay;
        }

        public String getEndDate() {
            String enddate = "" + endYear + getTwoBmonth(endMonth) + getTwoBmonth(endDay);
            return enddate;
        }
    }
}
