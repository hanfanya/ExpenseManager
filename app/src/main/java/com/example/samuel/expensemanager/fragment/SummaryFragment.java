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
import com.example.samuel.expensemanager.utils.CalUtils;
import com.example.samuel.expensemanager.utils.SPUtils;
import com.example.samuel.expensemanager.view.CircleProgress;
import com.example.samuel.expensemanager.view.CountView;
import com.example.samuel.expensemanager.view.MyMarkerView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
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
    private ExpenseDao mExpenseDao;
    private List<Expense> mExpenseMonth;
    private double mSumMonthOut;
    private double mSumMonthIn;
    private double mRemainBudget;//预算剩余
    private int mPercentage;


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
        mLineChart.setDrawGridBackground(false);
        // no description text
        mLineChart.setDescription("");

        // enable touch gestures
        mLineChart.setTouchEnabled(true);

        // enable scaling and dragging
        mLineChart.setDragEnabled(true);
        mLineChart.setScaleEnabled(true);


        // if disabled, scaling can be done on x- and y-axis separately
        mLineChart.setPinchZoom(false);

        mLineChart.setDrawGridBackground(false);
        // set an alternative background color
        // mChart.setBackgroundColor(Color.GRAY);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        MyMarkerView mv = new MyMarkerView(getActivity(), R.layout.custom_marker_view);

        // set the marker to the chart
        mLineChart.setMarkerView(mv);

        XAxis xl = mLineChart.getXAxis();
        xl.setAvoidFirstLastClipping(true);
        xl.setDrawGridLines(false);
        xl.setLabelsToSkip(0);
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        // xl.setEnabled(false);

        YAxis leftAxis = mLineChart.getAxisLeft();
        leftAxis.setStartAtZero(false);
        leftAxis.setDrawGridLines(false);
        leftAxis.setLabelCount(5, false);
        leftAxis.setAxisLineColor(Color.WHITE);
        //leftAxis.setEnabled(false);

        YAxis rightAxis = mLineChart.getAxisRight();
        rightAxis.setEnabled(false);
        // add data
        setData();
        mLineChart.getLegend().setEnabled(false);

        mLineChart.animateXY(3000, 3000);
    }

    private void setData() {
        String mCurrentDate = CalUtils.getCurrentDate();
        String mLastDate = CalUtils.getLastThreeDate(7);
        SQLiteDatabase db = getContext().openOrCreateDatabase("expense-db", getContext().MODE_PRIVATE, null);
        //从数据库获取近七天总支出
        String sqllast = "select date,sum(figure) from EXPENSE where TYPE_FLAG=1 and DATE between " + mLastDate + "  and  " + mCurrentDate + " " + "group by date order by date asc;";
        Cursor cursorSum = db.rawQuery(sqllast, null);
        ArrayList<String> dates = new ArrayList<String>();
        ArrayList<Entry> figures = new ArrayList<Entry>();
        int i = 0;
        while (cursorSum.moveToNext()) {
            dates.add(cursorSum.getString(0).substring(4));
            figures.add(new Entry(cursorSum.getFloat(1), i));
            i++;
        }


        //传入折线图数据
        LineDataSet set1 = new LineDataSet(figures, "");

        set1.setDrawCubic(true);
        set1.setLineWidth(4f);
        set1.setCircleSize(4f);
        set1.setValueTextSize(8.0f);

        // create a data object with the datasets
        LineData data = new LineData(dates, set1);

        // set data
        mLineChart.setData(data);

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaoSession daoSession = ((ExpenseApplication) getActivity().getApplicationContext()).getDaoSession();
        mExpenseDao = daoSession.getExpenseDao();
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
//        mLineChart.animateXY(3000,3000);
    }

    private void setMonthInAndOut() {
        mSumMonthOut = 0;
        mSumMonthIn = 0;
        String month = CalUtils.getCurrentDate().substring(0, 6);
        System.out.println("month=" + month);
        mExpenseMonth = mExpenseDao.queryBuilder()//查询本月数据
                .where(ExpenseDao.Properties.Date.like(month + "%")).list();
        for (int i = 0; i < mExpenseMonth.size(); i++) {
            Expense expense = mExpenseMonth.get(i);
            int flag = expense.getTypeFlag();
            if (flag == 1) {
                mSumMonthOut = mSumMonthOut + expense.getFigure();//统计本月支出
            } else {
                mSumMonthIn = mSumMonthIn + expense.getFigure();//统计本月收入
            }
        }
        mTvMonthIn.showNumberWithAnimation(String.valueOf(mSumMonthIn));
        mTvMonthOut.showNumberWithAnimation(String.valueOf(mSumMonthOut));

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
                mTvBudgetFigure.showNumberWithAnimation(String.valueOf(mRemainBudget));
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
