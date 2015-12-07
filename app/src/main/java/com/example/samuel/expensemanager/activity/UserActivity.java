package com.example.samuel.expensemanager.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.samuel.expensemanager.ExpenseApplication;
import com.example.samuel.expensemanager.R;
import com.example.samuel.expensemanager.model.CloudExpense;
import com.example.samuel.expensemanager.model.DaoSession;
import com.example.samuel.expensemanager.model.Expense;
import com.example.samuel.expensemanager.model.ExpenseDao;
import com.example.samuel.expensemanager.model.MyUser;
import com.example.samuel.expensemanager.utils.SPUtils;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;

public class UserActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView user_iv_head;
    private TextView user_tv_nickname;
    //    private Button user_bt_update;
    private TextView user_tv_checkout;
    private MyUser mBmobUser;
    //    private ImageLoader mImageLoader;
    private SharedPreferences mSharedPerfarece;
    private int mSum;
    private int mCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

//        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
//        ImageLoader.getInstance().init(configuration);

        mBmobUser = BmobUser.getCurrentUser(this, MyUser.class);

        initView();
        initLogin();
    }

    private void initLogin() {
        if (mBmobUser != null) {
            mSharedPerfarece = getSharedPreferences("user", MODE_PRIVATE);

            String nickName = mSharedPerfarece.getString(mBmobUser.getObjectId() + "_nickName", "");
            user_tv_nickname.setText(nickName);
            boolean haveDownload = SPUtils.getBoolean(UserActivity.this, "haveDownload", false);
            if (!haveDownload) {
                initDownload();
            }


        }
    }

    private void initDownload() {

        mSum = 0;
        BmobQuery<CloudExpense> query = new BmobQuery<>();
        query.addWhereEqualTo("username", "samuel");
        query.count(this, CloudExpense.class, new CountListener() {
            @Override
            public void onSuccess(int i) {
                mSum = i;
                mCount = mSum / 50 + 1;
                Log.i("bmobTest", "查询成功，总个数为：" + i);
                downloadData();
            }

            @Override
            public void onFailure(int i, String s) {
                Log.i("bmobTest", "查询失败" + s);

            }
        });
        Log.i("bmobTest", "一共需要下载 " + mCount + " 次");


    }

    private void downloadData() {
        final String username = mBmobUser.getUsername();

        DaoSession daoSession = ((ExpenseApplication) getApplicationContext()).getDaoSession();

        final ExpenseDao expenseDao = daoSession.getExpenseDao();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < mCount; i++) {
                    final BmobQuery<CloudExpense> bmobQuery = new BmobQuery<>();
                    bmobQuery.addWhereEqualTo("username", username);
                    bmobQuery.setLimit(50);
                    bmobQuery.setSkip(i * 50);
                    final int finalI = i;
                    bmobQuery.findObjects(UserActivity.this, new FindListener<CloudExpense>() {
                        @Override
                        public void onSuccess(List<CloudExpense> list) {
                            Log.i("bmobTest", "下载数据成功,数据数量：" + list.size());
                            int num = 0;
                            for (CloudExpense cloudExpense : list) {
                                Expense expense = new Expense();

                                expense.setDate(cloudExpense.getDate());
                                expense.setUploadFlag(8);
                                expense.setExpenseObjectId(cloudExpense.getObjectId());
                                expense.setFigure(cloudExpense.getFigure());
                                expense.setTypeName(cloudExpense.getTypeName());
                                expense.setTypeFlag(cloudExpense.getTypeFlag());
                                expense.setTypeColor(cloudExpense.getTypeColor());

                                expenseDao.insertOrReplace(expense);
                                num++;
                                Log.i("bmob", "成功写入第 " + (finalI * 50 + num) + " 条数据");

                            }

                        }

                        @Override
                        public void onError(int i, String s) {
                            Log.i("bmobTest", "下载数据失败,错误信息为：" + s + " 已下载数据 " + mCount);
                            SPUtils.saveInt(UserActivity.this, "hasDownloadCount", mCount);


                        }
                    });


                }
            }
        }).start();
        SPUtils.saveBoolean(UserActivity.this, "haveDownload", true);
        List<Expense> expenseList = expenseDao.queryBuilder()
                .where(ExpenseDao.Properties.UploadFlag.in(0, 1, 5, 6, 7)).list();

        Log.i("bmobTest", "本地数据库的的记录数量为：" + expenseList.size());
    }

    private void initView() {
//        mImageLoader = ImageLoader.getInstance();
//        user_iv_head = (ImageView) findViewById(R.id.user_iv_head);
        user_tv_nickname = (TextView) findViewById(R.id.user_tv_nickname);
//        user_bt_update = (Button) findViewById(R.id.user_bt_update);
        user_tv_checkout = (TextView) findViewById(R.id.user_tv_checkout);
//        user_bt_update.setOnClickListener(this);
        user_tv_checkout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.user_bt_update://修改密码
//                Intent intent = new Intent(UserActivity.this,ChangePWordActivity.class);
//

            case R.id.user_tv_checkout://退出登陆
                BmobUser currentUser = BmobUser.getCurrentUser(UserActivity.this);
                String objectId = currentUser.getObjectId();
                SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
                sharedPreferences.edit().putString(objectId + "_nickName", "").commit();


                BmobUser.logOut(UserActivity.this);
                if (null == BmobUser.getCurrentUser(UserActivity.this)) {
                    mBmobUser = null;
                    Toast.makeText(this, "退出成功", Toast.LENGTH_SHORT).show();
                    SPUtils.saveBoolean(UserActivity.this, "haveLogin", false);
                    Intent intent2 = new Intent(UserActivity.this, MainActivity.class);
                    startActivity(intent2);
                    finish();
                }
                break;
        }
    }
}
