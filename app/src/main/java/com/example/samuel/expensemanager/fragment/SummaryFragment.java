package com.example.samuel.expensemanager.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SummaryFragment extends Fragment implements NumberPickerDialogFragment.NumberPickerDialogHandler {


    private static final int BUTTON_ONE_REFERENCE = 0;
    private static final int BUTTON_THREE_REFERENCE = 2;
    @Bind(R.id.tv_month_in)
    TextView mTvMonthIn;
    @Bind(R.id.ll_month_in)
    LinearLayout mLlMonthIn;
    @Bind(R.id.circlePb)
    CircleProgress mCirclePb;
    @Bind(R.id.tv_not_budget)
    TextView mTvNotBudget;
    @Bind(R.id.tv_budget_figure)
    TextView mTvBudgetFigure;
    @Bind(R.id.tv_budget_title)
    TextView mTvBudgetTitle;
    @Bind(R.id.ll_circle)
    RelativeLayout mLlCircle;
    @Bind(R.id.tv_month_out)
    TextView mTvMonthOut;
    @Bind(R.id.ll_month_out)
    LinearLayout mLlMonthOut;
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
                .setTargetFragment(SummaryFragment.this);

        builder.show();
    }

    @Override
    public void onDialogNumberSet(int reference, int number, double decimal, boolean isNegative, double fullNumber) throws NumberFormatException {
        SPUtils.saveInt(getActivity(), "budget_figure", number);
        setBudgetProgress();

        System.out.println("number" + number);
    }

    @Override
    public void onResume() {
        super.onResume();
        setMonthInAndOut();
        setBudgetProgress();
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
        Log.i("home", mExpenseMonth.size() + "");
        mTvMonthIn.setText("" + mSumMonthIn);
        mTvMonthOut.setText("" + mSumMonthOut);

    }

    private void setBudgetProgress() {
        int budgetFigure = SPUtils.getInt(getActivity(), "budget_figure", 0);
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
                mTvBudgetFigure.setText(mRemainBudget + "");
                mCirclePb.setTargetProgress(mPercentage);//设置自定义圆形进度条的进度

            } else {//预算超出
                mRemainBudget = mSumMonthOut - budgetFigure;
                mTvBudgetTitle.setText("预算超出");
                mTvBudgetFigure.setText(mRemainBudget + "");

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
