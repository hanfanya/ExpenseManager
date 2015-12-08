package com.example.samuel.expensemanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.samuel.expensemanager.ExpenseApplication;
import com.example.samuel.expensemanager.R;
import com.example.samuel.expensemanager.model.DaoSession;
import com.example.samuel.expensemanager.model.TypeInfo;
import com.example.samuel.expensemanager.model.TypeInfoDao;

import java.util.List;
import java.util.Random;

public class AddTypeInfoActivity extends AppCompatActivity {

    public static final int REQUEST_COLOR = 0;
    private LinearLayout mLlContent;
    private LinearLayout mLlName;
    private EditText mEdtTypeName;
    private LinearLayout mLlType;
    private TextView mLableType;
    private RadioButton mRbTypeExpense;
    private RadioButton mRbTypeIncome;
    private LinearLayout mLlColor;
    private TextView mLabelColor;
    private ImageView mIvType;
    private Button mBtnCancel;
    private Button mBtnOk;
    private int[] mColorArray;
    private int mTypeColor;
    private TypeInfoDao mTypeInfoDao;
    private Toolbar mToolbar;

    private void assignViews() {
        mLlContent = (LinearLayout) findViewById(R.id.ll_content);
        mLlName = (LinearLayout) findViewById(R.id.ll_name);
        mEdtTypeName = (EditText) findViewById(R.id.edt_type_name);
        mLlType = (LinearLayout) findViewById(R.id.ll_type);
        mLableType = (TextView) findViewById(R.id.lable_type);
        mRbTypeExpense = (RadioButton) findViewById(R.id.rb_type_expense);
        mRbTypeIncome = (RadioButton) findViewById(R.id.rb_type_income);
        mLlColor = (LinearLayout) findViewById(R.id.ll_color);
        mLabelColor = (TextView) findViewById(R.id.label_color);
        mIvType = (ImageView) findViewById(R.id.iv_type);
        mBtnCancel = (Button) findViewById(R.id.btn_cancel);
        mBtnOk = (Button) findViewById(R.id.btn_ok);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_type_info);
        assignViews();

        initData();
        mIvType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddTypeInfoActivity.this, SelectColorActivity.class);
                intent.putExtra("defaultColor", mTypeColor);
                startActivityForResult(intent, REQUEST_COLOR);
            }
        });

        mBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTypeInfo();
            }
        });

        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        mToolbar.setTitle("增加类别");
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        DaoSession daoSession = ((ExpenseApplication) getApplicationContext()).getDaoSession();
        mTypeInfoDao = daoSession.getTypeInfoDao();

        Intent intent = getIntent();
        int currentType = intent.getIntExtra("currentType", 0);
        System.out.println("current====+++" + currentType);
        if (currentType == 0) {
            mRbTypeExpense.setChecked(true);
        } else {
            mRbTypeIncome.setChecked(true);
        }

        mColorArray = getResources().getIntArray(R.array.colorType);
        Random random = new Random();
        mTypeColor = random.nextInt(mColorArray.length);

        mIvType.setColorFilter(mColorArray[mTypeColor]);

    }

    private void saveTypeInfo() {
        String typeName = mEdtTypeName.getText().toString();
        if (TextUtils.isEmpty(typeName)) {
            Toast.makeText(AddTypeInfoActivity.this, "类别名称不能为空", Toast.LENGTH_SHORT).show();
            return;
        }


        List<TypeInfo> typeInfoList = mTypeInfoDao.queryBuilder()
                .where(TypeInfoDao.Properties.TypeName.eq(typeName)).list();
        if (typeInfoList.size() > 0) {
            Toast.makeText(AddTypeInfoActivity.this, "该类别已存在", Toast.LENGTH_SHORT).show();
            return;
        }
        int typeFlag = 1;
        if (mRbTypeExpense.isChecked()) {
            typeFlag = 1;
        } else {
            typeFlag = 0;
        }

        TypeInfo typeInfo = new TypeInfo();

        typeInfo.setTypeColor(mTypeColor);
        typeInfo.setTypeName(typeName);
        typeInfo.setTypeFlag(typeFlag);
        typeInfo.setFrequency(0);
        typeInfo.setUploadFlag(0);

        mTypeInfoDao.insertOrReplace(typeInfo);
        finish();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int colorPosition = data.getIntExtra("color_position", 0);
        mTypeColor = colorPosition;
        mIvType.setColorFilter(mColorArray[colorPosition]);

        System.out.println("+++++++++++" + colorPosition);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent("com.example.barry.clockdemo.ReminderActivity.onDestroy");
        sendBroadcast(intent);
    }
}
