package com.example.samuel.expensemanager.fragment;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codetroopers.betterpickers.numberpicker.NumberPickerBuilder;
import com.codetroopers.betterpickers.numberpicker.NumberPickerDialogFragment;
import com.example.samuel.expensemanager.ExpenseApplication;
import com.example.samuel.expensemanager.R;
import com.example.samuel.expensemanager.model.DaoSession;
import com.example.samuel.expensemanager.model.Expense;
import com.example.samuel.expensemanager.model.ExpenseDao;
import com.example.samuel.expensemanager.utils.CalUtils;
import com.example.samuel.expensemanager.utils.SPUtils;
import com.example.samuel.expensemanager.view.CircleProgress;
import com.example.samuel.expensemanager.view.CountView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SummaryFragment extends Fragment implements NumberPickerDialogFragment.NumberPickerDialogHandler {


    private static final int BUTTON_ONE_REFERENCE = 0;
    private static final int BUTTON_THREE_REFERENCE = 2;
    @Bind(R.id.tv_month_in)
    CountView mTvMonthIn;
    @Bind(R.id.ll_month_in)
    LinearLayout mLlMonthIn;
    @Bind(R.id.circlePb)
    CircleProgress mCirclePb;
    @Bind(R.id.tv_not_budget)
    TextView mTvNotBudget;
    @Bind(R.id.tv_budget_figure)
    CountView mTvBudgetFigure;
    @Bind(R.id.tv_budget_title)
    TextView mTvBudgetTitle;
    @Bind(R.id.ll_circle)
    RelativeLayout mLlCircle;
    @Bind(R.id.tv_month_out)
    CountView mTvMonthOut;
    @Bind(R.id.ll_month_out)
    LinearLayout mLlMonthOut;
    @Bind(R.id.line_chart_summary)
    LineChart mLineChart;
    @Bind(R.id.bar_chart_summary)
    BarChart mBarChart;
    private ExpenseDao mExpenseDao;
    private List<Expense> mExpenseMonth;
    private double mSumMonthOut;
    private double mSumMonthIn;
    private double mRemainBudget;//预算剩余
    private int mPercentage;
    private SQLiteDatabase db;
    private String mCurrentDate;


    public SummaryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summary, container, false);
        ButterKnife.bind(this, view);

        mCirclePb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSetBudgetDialog();
            }
        });
        return view;
    }

    private void initLineChart() {
        //设置北京网格
        mLineChart.setDrawGridBackground(false);
        // no description text
        mLineChart.setDescription("");

        mLineChart.setPressed(true);
        // enable touch gestures
        mLineChart.setTouchEnabled(true);

        // enable scaling and dragging
        mLineChart.setDragEnabled(false);
        mLineChart.setScaleEnabled(true);
        mLineChart.setDrawBorders(false);
        //设置没有数据显示描述
        mLineChart.setDescriptionTextSize(25f);
        mLineChart.setNoDataText("亲，快去开启您的记账之旅");

        // if disabled, scaling can be done on x- and y-axis separately
        mLineChart.setPinchZoom(true);

        mLineChart.setDrawGridBackground(false);
        // set an alternative background color
        // mBarChart.setBackgroundColor(Color.GRAY);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        /*MyMarkerView mv = new MyMarkerView(getContext(), R.layout.custom_marker_view);

        // set the marker to the chart
        mLineChart.setMarkerView(mv);*/

        XAxis xl = mLineChart.getXAxis();
        xl.setAvoidFirstLastClipping(true);
        xl.setDrawGridLines(false);
        xl.setLabelsToSkip(0);
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xl.setSpaceBetweenLabels(3);
        // xl.setEnabled(false);

        YAxis leftAxis = mLineChart.getAxisLeft();
        leftAxis.setStartAtZero(true);
        leftAxis.setDrawGridLines(false);
        leftAxis.setLabelCount(4, false);
        leftAxis.setDrawLabels(true);
        leftAxis.setAxisLineColor(Color.WHITE);
//        leftAxis.setEnabled(false);

        YAxis rightAxis = mLineChart.getAxisRight();
        rightAxis.setEnabled(false);
        // add data
        setData();
        mLineChart.getLegend().setEnabled(false);

        mLineChart.animateXY(1000, 800);
        // Log.e("--------", "-------------------------");
        mLineChart.invalidate();
    }

    private void setData() {

        String mLastDate = CalUtils.getLastThreeDate(7);

        //从数据库获取近七天总支出
        String sqllast = "select date,sum(figure) from EXPENSE where TYPE_FLAG=1 and  UPLOAD_FLAG in (0,1,5,8) and DATE between " + mLastDate + "  and  " + mCurrentDate + " " + "group by date order by date asc;";
        Cursor cursorSum = db.rawQuery(sqllast, null);
        ArrayList<String> dates = new ArrayList<String>();
        ArrayList<Entry> figures = new ArrayList<Entry>();
        int i = 0;
        while (cursorSum.moveToNext()) {
            dates.add(cursorSum.getString(0).substring(4, 6) + "/" + cursorSum.getString(0).substring(6));
            figures.add(new Entry(cursorSum.getFloat(1), i));
            i++;
        }
        //传入折线图数据
        LineDataSet set1 = new LineDataSet(figures, "");

        set1.setDrawCubic(true);
        set1.setLineWidth(2f);
        set1.setCircleSize(4f);
        set1.setValueTextSize(11.5f);
        set1.setDrawFilled(true);
        //取消高度提示线
        set1.setDrawHighlightIndicators(false);

        // create a data object with the datasets
        LineData data = new LineData(dates, set1);

        // set data
        mLineChart.setData(data);

    }

    private void initBarChart() {
        mBarChart.setDescription("");

//        mBarChart.setDrawBorders(true);

        // scaling can now only be done on x- and y-axis separately
        mBarChart.setPinchZoom(false);

        mBarChart.setDrawBarShadow(false);

        mBarChart.setDrawGridBackground(false);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        mBarChart.setNoDataText("亲，快去开启您的记账之旅");

        Legend l = mBarChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_INSIDE);
        /*l.setYOffset(0f);
        l.setYEntrySpace(0f);*/
        l.setTextSize(8f);

        XAxis xl = mBarChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setLabelsToSkip(0);
        xl.setDrawGridLines(false);

        mBarChart.getAxisLeft().setEnabled(false);
/*        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(30f);*/

        mBarChart.getAxisRight().setEnabled(false);

        mBarChart.animateXY(800, 1000);
        setBarData();
    }

    private String getLastSixDate() {
        //拿到当前月份和年份
        //初始化日历对象
        Calendar calendar = Calendar.getInstance();
        int mCurrentYear = calendar.get(Calendar.YEAR);
        int mCurrentMonth = calendar.get(Calendar.MONTH) + 1;
        int mCurrentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int lastsixmonth;
        int lastsixyear;
        if (mCurrentMonth > 5) {
            lastsixmonth = mCurrentMonth - 5;
            lastsixyear = mCurrentYear;
        } else {
            lastsixmonth = mCurrentMonth + 7;
            lastsixyear = mCurrentYear - 1;
        }
        String lastSixDate = CalUtils.getFormatDate(lastsixyear, lastsixmonth, 1);
        return lastSixDate;

    }

    private void setBarData() {
        //X轴
        ArrayList<String> xVals = new ArrayList<String>();
        //Y轴
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
        //从数据库拿到数据
        String mlastSixDate = getLastSixDate();

        String sqlLastSixEx = "select a.[sum(figure)],b.[sum(figure)],a.[substr(date,0,7)] as mydate from (select sum(figure),substr(date,0,7) from EXPENSE where  TYPE_FLAG =1 and  UPLOAD_FLAG in (0,1,5,8) and DATE between " + mlastSixDate + " and " + mCurrentDate + " group by substr(date,0,7) order by date asc) as a left outer join (select sum(figure),substr(date,0,7) from EXPENSE where  TYPE_FLAG =0 and  UPLOAD_FLAG in (0,1,5,8) and DATE between " + mlastSixDate + " and " + mCurrentDate + " group by substr(date,0,7) order by date asc) as b on a.[substr(date,0,7)]=b.[substr(date,0,7)] " +
                "union select b.[sum(figure)],a.[sum(figure)],a.[substr(date,0,7)] as mydate from (select sum(figure),substr(date,0,7) from EXPENSE where  TYPE_FLAG =0 and  UPLOAD_FLAG in (0,1,5,8) and DATE between " + mlastSixDate + " and " + mCurrentDate + " group by substr(date,0,7) order by date asc) as a left outer join (select sum(figure),substr(date,0,7) from EXPENSE where  TYPE_FLAG =1 and  UPLOAD_FLAG in (0,1,5,8) and DATE between " + mlastSixDate + " and " + mCurrentDate + " group by substr(date,0,7) order by date asc) as b on a.[substr(date,0,7)]=b.[substr(date,0,7)] order by mydate asc;";
//        String sqlLastSixEx = "select sum(figure),substr(date,0,7) from EXPENSE where TYPE_FLAG =1 and  UPLOAD_FLAG in (0,1,5,8) and DATE between " + mlastSixDate + " and " + mCurrentDate + " group by substr(date,0,7) order by date asc;";
        Cursor cursor = db.rawQuery(sqlLastSixEx, null);
        int i = 0;
        while (cursor.moveToNext()) {
//            xVals.add(cursor.getString(1).substring(2, 4) + "/" + cursor.getString(1).substring(4));
            if (cursor.getString(0) != null) {
//                xVals.add(cursor.getString(1).substring(2, 4) + "/" + cursor.getString(1).substring(4));
                yVals1.add(new BarEntry(cursor.getFloat(0), i));

            }
//            yVals1.add(new BarEntry(cursor.getFloat(0), i));
            if (cursor.getString(1) != null) {
                yVals2.add(new BarEntry(cursor.getFloat(1), i));
            }
            xVals.add(cursor.getString(2).substring(2, 4) + "/" + cursor.getString(2).substring(4));
            System.out.println("++++++" + cursor.getString(2));
            i++;
        }

//        String sqlLastSixIn = "select sum(figure),substr(date,0,7) from EXPENSE where TYPE_FLAG =0 and  UPLOAD_FLAG in (0,1,5,8) and DATE between " + mlastSixDate + " and " + mCurrentDate + " group by substr(date,0,7) order by date asc;";
//        Cursor cursor2 = db.rawQuery(sqlLastSixIn, null);
//        i = 0;
        /*while (cursor2.moveToNext()) {
            yVals2.add(new BarEntry(cursor2.getFloat(0), i));
            i++;
        }*/
        BarDataSet set1 = new BarDataSet(yVals1, "支出");
        // set1.setColors(ColorTemplate.createColors(getApplicationContext(),
        // ColorTemplate.FRESH_COLORS));
        set1.setColor(Color.rgb(104, 241, 175));
        set1.setValueTextSize(12f);
        BarDataSet set2 = new BarDataSet(yVals2, "收入");
        set2.setColor(Color.rgb(164, 228, 251));
        set2.setValueTextSize(12f);
        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set1);
        dataSets.add(set2);
        BarData data = new BarData(xVals, dataSets);
//        data.setValueFormatter(new LargeValueFormatter());

        // add space between the dataset groups in percent of bar-width
        data.setGroupSpace(80f);

        mBarChart.setData(data);
        mBarChart.invalidate();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaoSession daoSession = ((ExpenseApplication) getActivity().getApplicationContext()).getDaoSession();
        mExpenseDao = daoSession.getExpenseDao();
        //初始化数据库
        db = getContext().openOrCreateDatabase("expense-db", getContext().MODE_PRIVATE, null);
        //初始化当前日期
        mCurrentDate = CalUtils.getCurrentDate().substring(0, 4) + "31";
    }

    private void showSetBudgetDialog() {

        NumberPickerBuilder builder = new NumberPickerBuilder()
                .setFragmentManager(getChildFragmentManager())
                .setStyleResId(R.style.BetterPickersDialogFragment_Light)
                .setDecimalVisibility(View.INVISIBLE)
                .setPlusMinusVisibility(View.INVISIBLE)
                .setTargetFragment(SummaryFragment.this)
                .setMaxNumber(1000000);

        builder.show();
    }

    @Override
    public void onDialogNumberSet(int reference, int number, double decimal, boolean isNegative, double fullNumber) throws NumberFormatException {
        SPUtils.saveString(getActivity(), "budget_figure", String.valueOf(number));
        setBudgetProgress();

        System.out.println("number" + number);
    }

    @Override
    public void onResume() {
        super.onResume();
        setMonthInAndOut();
        setBudgetProgress();
        initLineChart();
        initBarChart();
//        mLineChart.animateXY(3000,3000);
    }

    private void setMonthInAndOut() {
        mSumMonthOut = 0;
        mSumMonthIn = 0;
        String month = CalUtils.getCurrentDate().substring(0, 6);
        System.out.println("month=" + month);
        mExpenseMonth = mExpenseDao.queryBuilder()//查询本月数据
                .where(ExpenseDao.Properties.Date.like(month + "%"), ExpenseDao.Properties.UploadFlag.in(0, 1, 5, 8)).list();
        for (int i = 0; i < mExpenseMonth.size(); i++) {
            Expense expense = mExpenseMonth.get(i);
            int flag = expense.getTypeFlag();
            if (flag == 1) {
                mSumMonthOut = mSumMonthOut + expense.getFigure();//统计本月支出
            } else {
                mSumMonthIn = mSumMonthIn + expense.getFigure();//统计本月收入
            }
        }
        if (mSumMonthIn == 0) {
            mTvMonthIn.setText("0");
        } else {
            mTvMonthIn.showNumberWithAnimation(String.valueOf(mSumMonthIn));
        }
        if (mSumMonthOut == 0) {
            mTvMonthOut.setText("0");
        } else {
            mTvMonthOut.showNumberWithAnimation(String.valueOf(mSumMonthOut));
        }

    }

    private void setBudgetProgress() {
        int budgetFigure = Integer.parseInt(SPUtils.getString(getActivity(), "budget_figure", "0"));
        if (budgetFigure == 0) {//0表示没有设置预算
            mTvBudgetFigure.setVisibility(View.INVISIBLE);
            mTvBudgetTitle.setVisibility(View.INVISIBLE);
            mTvNotBudget.setVisibility(View.VISIBLE);
            mCirclePb.setTargetProgress(0);
        } else {
            mTvBudgetFigure.setVisibility(View.VISIBLE);
            mTvBudgetTitle.setVisibility(View.VISIBLE);
            mTvNotBudget.setVisibility(View.INVISIBLE);
            if (mSumMonthOut <= budgetFigure) {//预算未超出
                mRemainBudget = budgetFigure - mSumMonthOut;
                mPercentage = (int) (1.0 * mSumMonthOut / budgetFigure * 100);
                System.out.println("mPercentage" + mPercentage);

                mTvBudgetTitle.setText("预算剩余");
                if (mRemainBudget == 0) {
                    mTvBudgetFigure.setText("0");
                } else {
                    mTvBudgetFigure.showNumberWithAnimation(String.valueOf(mRemainBudget));
                }
                mCirclePb.setTargetProgress(mPercentage);//设置自定义圆形进度条的进度

            } else {//预算超出
                mRemainBudget = mSumMonthOut - budgetFigure;
                mTvBudgetTitle.setText("预算超出");
                mTvBudgetFigure.showNumberWithAnimation(String.valueOf(mRemainBudget));
                mCirclePb.setTargetProgress(100);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
