package com.example.samuel.expensemanager.fragment;


import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.datetimepicker.date.DatePickerDialog;
import com.example.samuel.expensemanager.ExpenseApplication;
import com.example.samuel.expensemanager.R;
import com.example.samuel.expensemanager.adapter.ExpenseRecyclerViewAdapter;
import com.example.samuel.expensemanager.model.DaoSession;
import com.example.samuel.expensemanager.model.Expense;
import com.example.samuel.expensemanager.model.ExpenseDao;
import com.example.samuel.expensemanager.model.TypeInfo;
import com.example.samuel.expensemanager.model.TypeInfoDao;
import com.example.samuel.expensemanager.utils.SysUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExpenseFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    @Bind(R.id.recyclerview_expense)
    RecyclerView mRecyclerviewExpense;
    @Bind(R.id.key_1)
    Button mKey1;
    @Bind(R.id.key_4)
    Button mKey4;
    @Bind(R.id.key_7)
    Button mKey7;
    @Bind(R.id.key_clear)
    Button mKeyClear;
    @Bind(R.id.key_2)
    Button mKey2;
    @Bind(R.id.key_5)
    Button mKey5;
    @Bind(R.id.key_8)
    Button mKey8;
    @Bind(R.id.key_0)
    Button mKey0;
    @Bind(R.id.key_3)
    Button mKey3;
    @Bind(R.id.key_6)
    Button mKey6;
    @Bind(R.id.key_9)
    Button mKey9;
    @Bind(R.id.key_point)
    Button mKeyPoint;
    @Bind(R.id.key_del)
    FrameLayout mKeyDel;
    @Bind(R.id.key_add)
    FrameLayout mKeyAdd;
    @Bind(R.id.key_ok)
    Button mKeyOk;
    @Bind(R.id.ll_expense_cal)
    LinearLayout mLlExpenseCal;
    @Bind(R.id.iv_expense_type)
    ImageView mIvExpenseType;
    @Bind(R.id.tv_expense_type)
    TextView mTvExpenseType;
    @Bind(R.id.tv_expense_figure)
    TextView mTvExpenseFigure;
    @Bind(R.id.rl_expense_info)
    RelativeLayout mRlExpenseInfo;
    @Bind(R.id.rl_expense_cal)
    RelativeLayout mRlExpenseCal;

    private Context mContext;
    private List<TypeInfo> mTypeInfos;
    private GridLayoutManager mGridLayoutManager;
    private boolean isShow = true;
    private Menu mMenu;
    private ExpenseRecyclerViewAdapter mRecyclerViewAdapter;
    private int[] mColorArray;
    private boolean isInt = true;
    private boolean isAdding = false;
    private boolean isFirstClickAdd = true;
    private StringBuffer mStringBufferInt;
    private StringBuffer mStringBufferDecimal;
    private double mSumNumber;
    private Calendar mCalendar;
    private DateFormat mDateFormat;
    private MenuItem mDateItem;
    private boolean isCreated = true;
    private TypeInfo mTypeInfo;
    private Expense mExpense;
    private TypeInfoDao mTypeInfoDao;
    private ExpenseDao mExpenseDao;
    private SimpleDateFormat mSimpleDateFormat;


    public ExpenseFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expense, container, false);
        ButterKnife.bind(this, view);

        initData();
        initUI();
        initListener();

        return view;
    }

    //从fragment中添加toolbar菜单
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity();

        DaoSession daoSession = ((ExpenseApplication) getActivity().getApplicationContext()).getDaoSession();
        mTypeInfoDao = daoSession.getTypeInfoDao();
        mExpenseDao = daoSession.getExpenseDao();


        mTypeInfo = new TypeInfo();
        mExpense = new Expense();

        setHasOptionsMenu(true);
        mCalendar = Calendar.getInstance();
        mDateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
        mSimpleDateFormat = new SimpleDateFormat("yyyyMMdd");

    }

    private void initUI() {
        mGridLayoutManager = new GridLayoutManager(getActivity(), 4);
        mRecyclerViewAdapter = new ExpenseRecyclerViewAdapter(mTypeInfos, getActivity());

        mRecyclerviewExpense.setLayoutManager(mGridLayoutManager);
        mRecyclerviewExpense.setAdapter(mRecyclerViewAdapter);

        mTypeInfo = mTypeInfos.get(0);
        mIvExpenseType.setColorFilter(mColorArray[mTypeInfo.getTypeColor()]);
        mTvExpenseType.setText(mTypeInfo.getTypeName());

    }

    private void initListener() {
        mRlExpenseInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShow) {
                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mRlExpenseCal, "translationY", 0, mLlExpenseCal.getHeight());
                    objectAnimator.setDuration(200);
                    objectAnimator.start();
                    isShow = false;
                } else {
                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mRlExpenseCal, "translationY", mLlExpenseCal.getHeight(), 0);
                    objectAnimator.setDuration(200);
                    objectAnimator.start();
                    isShow = true;
                }
            }
        });

        //item的点击事件
        mRecyclerViewAdapter.setOnItemClickListener(new ExpenseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.i("ExpenseFragment", "点击了 " + position);
                mTypeInfo = mTypeInfos.get(position);

                mIvExpenseType.setColorFilter(mColorArray[mTypeInfo.getTypeColor()]);
                mTvExpenseType.setText(mTypeInfo.getTypeName());
                mRecyclerViewAdapter.setSelection(position);
                mRecyclerViewAdapter.notifyDataSetChanged();

                if (!isShow) {
                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mRlExpenseCal, "translationY", mLlExpenseCal.getHeight(), 0);
                    objectAnimator.setDuration(200);
                    objectAnimator.start();
                    isShow = true;
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        mRecyclerViewAdapter.setSelection(0);
        //向下滚动时，隐藏计算器
        mRecyclerviewExpense.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 20 && isShow) {
                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mRlExpenseCal, "translationY", 0, mLlExpenseCal.getHeight());
                    objectAnimator.setDuration(200);
                    objectAnimator.start();
                    isShow = false;
                }
            }
        });

    }

    private void initData() {
        DaoSession daoSession = ((ExpenseApplication) getActivity().getApplicationContext()).getDaoSession();
        TypeInfoDao typeInfoDao = daoSession.getTypeInfoDao();

        mTypeInfos = typeInfoDao.queryBuilder().where(TypeInfoDao.Properties.TypeFlag.eq(1)).list();
        mColorArray = getActivity().getResources().getIntArray(R.array.colorType);

        mStringBufferInt = new StringBuffer();
        mStringBufferDecimal = new StringBuffer();


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_expense, menu);
        this.mMenu = menu;
        mDateItem = mMenu.findItem(R.id.action_date_title);
//        mDateItem.setTitle("2015/11/15");
        String dateFormat = mSimpleDateFormat.format(mCalendar.getTime());
        mExpense.setDate(dateFormat);

        mDateItem.setTitle(dateFormat.substring(0, 4) + "年" + dateFormat.substring(4, 6) + "月" + dateFormat.substring(6) + "日");

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_date) {//选择日期
            DatePickerDialog.newInstance(this, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                    mCalendar.get(Calendar.DAY_OF_MONTH)).show(getActivity().getFragmentManager(), "datePicker");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        mCalendar.set(year, monthOfYear, dayOfMonth);
        String dateFormat = mSimpleDateFormat.format(mCalendar.getTime());
        mExpense.setDate(dateFormat);

        mDateItem.setTitle(dateFormat.substring(0, 4) + "年" + dateFormat.substring(4, 6) + "月" + dateFormat.substring(6) + "日");
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.key_0, R.id.key_1, R.id.key_2,
            R.id.key_3, R.id.key_4, R.id.key_5,
            R.id.key_6, R.id.key_7, R.id.key_8, R.id.key_9})
    public void inputNumber(View view) {
        if (isInt) {
            if (mStringBufferInt.length() > 6) {
                return;
            }
            mStringBufferInt.append(((Button) view).getText().toString());
            if (mStringBufferInt.toString().startsWith("0")) {
                mStringBufferInt = mStringBufferInt.delete(0, 1);
                System.out.println(mStringBufferInt.toString());
                mTvExpenseFigure.setText("0.0");
                return;
            }
            mTvExpenseFigure.setText(mStringBufferInt + ".0");
        } else {
            if (mStringBufferDecimal.length() > 0) {
                return;
            }
            if (mStringBufferInt.length() == 0) {
                mStringBufferInt.append("0");
            }
            mStringBufferDecimal.append(((Button) view).getText().toString());
            /*if (mStringBufferDecimal.length() == 1) {
                mTvExpenseFigure.setText(mStringBufferInt + "." + mStringBufferDecimal + "0");
            } else {
                mTvExpenseFigure.setText(mStringBufferInt + "." + mStringBufferDecimal);
            }*/
            mTvExpenseFigure.setText(mStringBufferInt + "." + mStringBufferDecimal);

        }
    }

    @OnClick(R.id.key_point)
    public void keyPoint(View view) {
        isInt = false;
    }

    @OnClick(R.id.key_clear)
    public void clearInput(View view) {
        mTvExpenseFigure.setText("0.0");
        isInt = true;
        isAdding = false;

        mKeyOk.setText("OK");


        mStringBufferInt = new StringBuffer();
        mStringBufferDecimal = new StringBuffer();

    }

    @OnClick(R.id.key_del)
    public void keyDel(View view) {

        if (!mTvExpenseFigure.getText().toString().endsWith("0")) {
//            mStringBufferDecimal.setCharAt(0, '0');
            mTvExpenseFigure.setText(mStringBufferInt + ".0");
            return;
        }
        if (mStringBufferInt.length() == 0) {
            return;
        } else if (mStringBufferInt.length() == 1) {
            int len = mStringBufferInt.length();
            mStringBufferInt = mStringBufferInt.delete(len - 1, len);

            mTvExpenseFigure.setText("0.0");
//            mStringBufferInt = new StringBuffer();
        } else {
            int len = mStringBufferInt.length();
            mStringBufferInt = mStringBufferInt.delete(len - 1, len);
            mTvExpenseFigure.setText(mStringBufferInt + ".0");
        }


    }

    @OnClick(R.id.key_add)
    public void addNumber(View view) {
        if (isFirstClickAdd) {

            isAdding = true;
            isInt = true;
            mSumNumber = SysUtils.stringToDouble(mTvExpenseFigure.getText().toString());

            mKeyOk.setText("=");
            mTvExpenseFigure.setText("0.0");

            mStringBufferInt = new StringBuffer();
            mStringBufferDecimal = new StringBuffer();
            isFirstClickAdd = false;
        }
    }


    @OnClick(R.id.key_ok)
    public void saveRecord(View view) {
        if (mTvExpenseFigure.getText().toString().equals("0.0") && !isAdding) {
            Toast.makeText(getActivity(), "请输入支出金额", Toast.LENGTH_SHORT).show();
            return;
        }
        if (isAdding) {
            mKeyOk.setText("OK");
            double inputNumber = SysUtils.stringToDouble(mTvExpenseFigure.getText().toString());
            mSumNumber += inputNumber;

            String sum = String.valueOf(mSumNumber);
            String[] stringDigit = sum.split("\\.");
            mStringBufferInt = new StringBuffer(stringDigit[0]);
            mStringBufferDecimal = new StringBuffer(stringDigit[1]);

            mTvExpenseFigure.setText(sum);

            isAdding = false;
            isFirstClickAdd = true;
        } else {
            String numberString = mTvExpenseFigure.getText().toString();
            double inputNumber = SysUtils.stringToDouble(numberString);

            String date = mDateItem.getTitle().toString();
            String dateString = date.substring(0, 4) + date.substring(5, 7) + date.substring(8, 10);

            System.out.println("你输入的数字为： " + inputNumber);
            System.out.println("日期为：        " + dateString);
            System.out.println("选择的项目id:   " + mTypeInfo.getId());
            System.out.println("选择的项目名称:  " + mTypeInfo.getTypeName());
            System.out.println("选择的项目颜色:  " + mTypeInfo.getTypeColor());


            if (isCreated) {
                /*this.date = date;
                this.figure = figure;
                this.typeName = typeName;
                this.typeColor = typeColor;
                this.typeFlag = typeFlag;
                this.isUploaded = isUploaded;
                this.isModified = isModified;
                this.isDeleted = isDeleted;
                */

                mExpense.setFigure(inputNumber);
                mExpense.setTypeName(mTypeInfo.getTypeName());
                mExpense.setTypeColor(mTypeInfo.getTypeColor());
                mExpense.setTypeFlag(mTypeInfo.getTypeFlag());

                mExpense.setIsUploaded(0);
                mExpense.setIsModified(0);
                mExpense.setIsDeleted(0);
                mExpenseDao.insertOrReplace(mExpense);

                Integer frequency = mTypeInfo.getFrequency();
                mTypeInfo.setFrequency(++frequency);

                mTypeInfoDao.update(mTypeInfo);

                getActivity().finish();


            } else {
                // TODO: 15/11/30 修改支出记录
            }
        }
    }
}
