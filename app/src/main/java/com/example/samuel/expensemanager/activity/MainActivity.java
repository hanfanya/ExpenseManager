package com.example.samuel.expensemanager.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.samuel.expensemanager.ExpenseApplication;
import com.example.samuel.expensemanager.R;
import com.example.samuel.expensemanager.adapter.HomePagerAdapter;
import com.example.samuel.expensemanager.model.CloudExpense;
import com.example.samuel.expensemanager.model.DaoSession;
import com.example.samuel.expensemanager.model.Expense;
import com.example.samuel.expensemanager.model.ExpenseDao;
import com.example.samuel.expensemanager.model.MyUser;
import com.example.samuel.expensemanager.model.TypeInfo;
import com.example.samuel.expensemanager.model.TypeInfoDao;
import com.example.samuel.expensemanager.utils.SPUtils;
import com.example.samuel.expensemanager.utils.SysUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;
import java.util.Random;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.sharesdk.framework.ShareSDK;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    //支出类型
    public static final String[] typeExpense = new String[]{"早餐", "午餐", "晚餐", "夜宵", "零食", "饮料", "日用品", "话费",
            "软件", "服装", "鞋帽", "医疗", "果蔬", "影院", "数码", "房租", "护肤", "居家", "书籍", "油盐酱醋", "交通", "摄影文印",
            "娱乐", "物业", "礼物", "社交", "剁手"};
    //收入类型
    public static final String[] typeIncome = new String[]{"工资", "奖金", "彩票", "余额宝", "股票"};
    //月份
    public static final String[] monthCase = new String[]{"03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
    //日期
    public static final String[] dayCase = new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10",
            "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
            "21", "22", "23", "24", "25", "26", "27", "28", "29", "30"};
    public Context mContext;

    private Toolbar mToolbar;
    private FloatingActionButton mFabHome;
    private DrawerLayout mDrawer;
    private NavigationView mNavigationView;
    private ViewPager mViewPagerHome;
    private TabLayout mTabLayoutHome;
    private int mSum;
    private int mCount;


    private LinearLayout mLoginLayout;//头布局
    private ImageView mLoginView;//头布局中的图像
    private TextView mLoginState;//头布局中的state
    private TextView mLoginEmail;//头布局中的email
    private boolean isAutoLogin = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bmob.initialize(this, "a4542ee0d42314bd2d2804e1ca838c5d");


        assignViews();
        initUI();
        initData();
//        testBmob();

        //Gxl
        //初始化sharedsdk
        ShareSDK.initSDK(this);
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
        ImageLoader.getInstance().init(configuration);
        initlogin();

    }


    //GXL
    @Override
    protected void onResume() {
        super.onResume();
        initlogin();
    }

    //检查登陆状态
    private void initlogin() {
        //检测是否登陆
        //BmobUser user = BmobUser.getCurrentUser(this);
        MyUser user = BmobUser.getCurrentUser(this, MyUser.class);
        if (user != null) {
            //已经登陆
            //加载数据
            String userObjectId = user.getObjectId();//获得ID
            SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
            //获取数据

            String nickName = sp.getString(userObjectId + "_nickName", "");
            String nickname = (String) BmobUser.getObjectByKey(this, "nickname");
            mLoginState.setText(!"".equals(nickName) ? nickName : nickname);

            //本地
            String userImageUrl = sp.getString(userObjectId + "_imageUrl", "");
            String userimageUrl = (String) BmobUser.getObjectByKey(this, "userimageurl");

            ImageLoader imageLoader = ImageLoader.getInstance();
            if (!"".equals(userimageUrl) || ("").equals(userimageUrl)) {
                if ("" != userImageUrl) {
                    imageLoader.displayImage(userImageUrl, mLoginView);
                } else {
                    imageLoader.displayImage(userimageUrl, mLoginView);
                }
            }
        } else { //如果没有登陆
            //判断是否需要自动登陆

            if (isAutoLogin) {
                //跳转到登陆页面
                Log.i("MainActivity", "自动登录");
            } else {
                //停在本地
                Log.i("MainActivity", "没有登录");

                mLoginView.setImageResource(R.mipmap.ic_launcher);
                mLoginState.setText("您尚未登陆,点击头像跳转登陆界面");
                mLoginEmail.setText("您的email");
            }
        }
    }

    private void testBmob(boolean isClickByHand) {
        final String username = BmobUser.getCurrentUser(MainActivity.this).getUsername();
        DaoSession daoSession = ((ExpenseApplication) getApplicationContext()).getDaoSession();
        final ExpenseDao expenseDao = daoSession.getExpenseDao();
        final List<Expense> expenseList = expenseDao.queryBuilder()
                .where(ExpenseDao.Properties.UploadFlag.in(0, 1, 5, 6, 7)).list();
        System.out.println("expenseList size===" + expenseList.size());
//        SPUtils.saveBoolean(MainActivity.this, "isSame", false);
        boolean isSame = SPUtils.getBoolean(MainActivity.this, "isSame", false);
        if (!isSame) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < expenseList.size(); i++) {
                        final Expense expense = expenseList.get(i);
                        Integer uploadFlag = expense.getUploadFlag();
                        final CloudExpense cloudExpense = new CloudExpense();
                        cloudExpense.setTypeFlag(expense.getTypeFlag());
                        cloudExpense.setDate(expense.getDate());
                        cloudExpense.setFigure(expense.getFigure());
                        cloudExpense.setTypeColor(expense.getTypeColor());
                        cloudExpense.setTypeName(expense.getTypeName());
                        cloudExpense.setUsername(username);

                        if (uploadFlag == 0 || uploadFlag == 1) {//向服务器插入数据
                            cloudExpense.save(MainActivity.this, new SaveListener() {
                                @Override
                                public void onSuccess() {
                                    String objectId = cloudExpense.getObjectId();
                                    expense.setExpenseObjectId(objectId);
                                    expense.setUploadFlag(8);
                                    expenseDao.update(expense);
                                    Log.i("bmobTest", "上传成功数据,objectId = " + objectId);
                                }

                                @Override
                                public void onFailure(int i, String s) {
                                    String objectId = cloudExpense.getObjectId();

                                    SPUtils.saveBoolean(MainActivity.this, "isSame", false);
                                    Log.i("bmobTest", "上传数据失败,objectId = " + objectId + "错误信息为：" + s);

                                }
                            });
                        } else if (uploadFlag == 5) {
                            String expenseObjectId = expense.getExpenseObjectId();
                            cloudExpense.update(MainActivity.this, expenseObjectId, new UpdateListener() {
                                @Override
                                public void onSuccess() {
                                    Log.i("bmobTest", "更新数据成功,getUpdatedAt = " + cloudExpense.getUpdatedAt());
                                    expense.setUploadFlag(200);
                                    expenseDao.update(expense);
                                }

                                @Override
                                public void onFailure(int i, String s) {
                                    Log.i("bmobTest", "更新数据失败,失败信息为：" + cloudExpense.getUpdatedAt());
                                    SPUtils.saveBoolean(MainActivity.this, "isSame", false);


                                }
                            });

                        } else if (uploadFlag == 6 || uploadFlag == 7) {
                            String expenseObjectId = expense.getExpenseObjectId();
                            cloudExpense.setObjectId(expenseObjectId);
                            cloudExpense.delete(MainActivity.this, new DeleteListener() {
                                @Override
                                public void onSuccess() {
                                    Log.i("bmobTest", "删除数据成功");
//                                expense.setUploadFlag(200);
                                    expenseDao.delete(expense);
                                }

                                @Override
                                public void onFailure(int i, String s) {
                                    Log.i("bmobTest", "删除数据失败,失败信息为：" + s);
                                    SPUtils.saveBoolean(MainActivity.this, "isSame", false);

                                }
                            });

                        }
                    }
                }
            }).start();
            List<Expense> list = expenseDao.queryBuilder()
                    .where(ExpenseDao.Properties.UploadFlag.in(0, 1, 5, 6, 7)).list();
            if (list.size() == 0) {
                Log.i("bombTest", "全部上传成功============================");
                SPUtils.saveBoolean(MainActivity.this, "isSame", true);
            }
        } else if (isClickByHand) {
            Toast.makeText(MainActivity.this, "数据已经全部更新", Toast.LENGTH_SHORT).show();
            Log.i("bombTest", "数据已经全部更新");
        }


//        System.out.println("全部上传成功============================");

    }

    private void initData() {
        boolean hasInitData = SPUtils.getBoolean(this, "hasInitData", false);
        if (!hasInitData) {
            testInsertType();
            SPUtils.saveBoolean(this, "hasInitData", true);
        }
//        testInsertData();

    }

    public void testInsertType() {
        DaoSession daoSession = ((ExpenseApplication) getApplicationContext()).getDaoSession();
        TypeInfoDao typeInfoDao = daoSession.getTypeInfoDao();
        int[] colorArray = getResources().getIntArray(R.array.colorType);


        Random random = new Random();
        for (int i = 0; i < typeExpense.length; i++) {
            TypeInfo typeInfo = new TypeInfo();

            int color = random.nextInt(colorArray.length);
            String name = typeExpense[i];
            int flag = 1;
            int frequency = 0;
            int uploadFrag = 0;

            typeInfo.setTypeColor(color);
            typeInfo.setTypeName(name);
            typeInfo.setTypeFlag(flag);
            typeInfo.setFrequency(frequency);
            typeInfo.setUploadFlag(uploadFrag);

            typeInfoDao.insertOrReplace(typeInfo);
        }

        for (int i = 0; i < typeIncome.length; i++) {
            TypeInfo typeInfo = new TypeInfo();

            int color = random.nextInt(colorArray.length);
            String name = typeIncome[i];
            int flag = 0;
            int frequency = 0;
            int uploadFrag = 0;

            typeInfo.setTypeColor(color);
            typeInfo.setTypeName(name);
            typeInfo.setTypeFlag(flag);
            typeInfo.setFrequency(frequency);
            typeInfo.setUploadFlag(uploadFrag);

            typeInfoDao.insertOrReplace(typeInfo);
        }

    }

    public void testInsertData() {
        DaoSession daoSession = ((ExpenseApplication) getApplicationContext()).getDaoSession();
        ExpenseDao expenseDao = daoSession.getExpenseDao();
        TypeInfoDao typeInfoDao = daoSession.getTypeInfoDao();
        List<TypeInfo> typeInfos = typeInfoDao.loadAll();

        Random random = new Random();
        for (int i = 0; i < 300; i++) {
            Expense expense = new Expense();

            int typeNumber = random.nextInt(typeExpense.length);
            int figure = random.nextInt(100);

            int flag = 1;//支出
            int color = typeInfos.get(typeNumber).getTypeColor();
            String expenseType = typeExpense[typeNumber];
            String month = monthCase[random.nextInt(monthCase.length)];
            String day = dayCase[random.nextInt(dayCase.length)];
            String date = "2015" + month + day;

            expense.setFigure((double) figure);
            expense.setTypeFlag(flag);
            expense.setTypeName(expenseType);
            expense.setDate(date);
            expense.setTypeColor(color);
            expense.setUploadFlag(0);

            expenseDao.insertOrReplace(expense);

        }
        for (int i = 0; i < 80; i++) {
            Expense expense = new Expense();

            int typeNumber = random.nextInt(typeIncome.length);
            int figure = random.nextInt(5000);

            int flag = 0;//收入
            int color = typeInfos.get(typeNumber).getTypeColor();
            String expenseType = typeIncome[typeNumber];
            String month = monthCase[random.nextInt(monthCase.length)];
            String day = dayCase[random.nextInt(dayCase.length)];
            String date = "2015" + month + day;

            expense.setFigure((double) figure);
            expense.setTypeFlag(flag);
            expense.setTypeName(expenseType);
            expense.setDate(date);
            expense.setTypeColor(color);
            expense.setUploadFlag(0);

            expenseDao.insertOrReplace(expense);

        }

    }

    private void assignViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_home);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mViewPagerHome = (ViewPager) findViewById(R.id.viewpager_tab_home);
        mTabLayoutHome = (TabLayout) findViewById(R.id.tablayout_home);

        //GXL
        //gxl 动态加载头布局
        View headView = View.inflate(MainActivity.this, R.layout.nav_header_main, null);
        mNavigationView.addHeaderView(headView);
        mLoginLayout = (LinearLayout) headView.findViewById(R.id.ll_main_login);
        mLoginView = (ImageView) headView.findViewById(R.id.iv_main_login);
        mLoginState = (TextView) headView.findViewById(R.id.tv_main_login);
        mLoginEmail = (TextView) headView.findViewById(R.id.tv_main_email);

    }

    private void initUI() {

        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);

        //设置 drawlayout
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);

        //设置 viewpager
        mTabLayoutHome.setTabMode(TabLayout.MODE_FIXED);
        final HomePagerAdapter homePagerAdapter = new HomePagerAdapter(getSupportFragmentManager());
        mViewPagerHome.setAdapter(homePagerAdapter);
        mTabLayoutHome.setupWithViewPager(mViewPagerHome);
        mTabLayoutHome.setTabsFromPagerAdapter(homePagerAdapter);

        mViewPagerHome.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    Fragment fragment = homePagerAdapter.getItem(position);
                    fragment.onResume();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //GXL
        mLoginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (BmobUser.getCurrentUser(MainActivity.this) == null) {//跳转到登陆界面
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    //finish();
                } else {
                    Intent intent = new Intent(MainActivity.this, UserActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void testBmobDownload() {
        Bmob.initialize(this, "a4542ee0d42314bd2d2804e1ca838c5d");

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


    }

    private void downloadData() {
        Log.i("bmobTest", "一共需要下载 " + mCount + " 次");

        DaoSession daoSession = ((ExpenseApplication) getApplicationContext()).getDaoSession();

        final ExpenseDao expenseDao = daoSession.getExpenseDao();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < mCount; i++) {
                    final BmobQuery<CloudExpense> bmobQuery = new BmobQuery<>();
                    bmobQuery.addWhereEqualTo("username", "samuel");
                    bmobQuery.setLimit(50);
                    bmobQuery.setSkip(i * 50);
                    final int finalI = i;
                    bmobQuery.findObjects(MainActivity.this, new FindListener<CloudExpense>() {
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
                            SPUtils.saveInt(MainActivity.this, "hasDownloadCount", mCount);


                        }
                    });


                }
            }
        }).start();

        List<Expense> expenseList = expenseDao.queryBuilder()
                .where(ExpenseDao.Properties.UploadFlag.in(0, 1, 5, 6, 7)).list();
        Log.i("bmobTest", "本地数据库的的记录数量为：" + expenseList.size());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_upload) {
            uploadData(false, true);
            return true;
        }
        /*if (id == R.id.action_download) {
            testBmobDownload();
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    private void uploadData(boolean autoUpload, boolean isClickByHand) {
        if (isClickByHand) {
            if (!SysUtils.haveNetwork(MainActivity.this)) {
                Toast.makeText(MainActivity.this, " 不能联网，请检查网络设置", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!SysUtils.haveWifi(MainActivity.this)) {
                Toast.makeText(MainActivity.this, "你设置仅在WiFi下连接，请更改网络设置", Toast.LENGTH_SHORT).show();
                return;
            }
            boolean haveLogin = SPUtils.getBoolean(MainActivity.this, "haveLogin", false);
            if (!haveLogin) {
                Toast.makeText(MainActivity.this, " 请登录后同步", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(MainActivity.this, "正在同步，请稍后……", Toast.LENGTH_SHORT).show();
            testBmob(isClickByHand);
        }
        if (autoUpload) {
            if (SysUtils.canUpload(MainActivity.this)) {
                testBmob(false);
            }
        }


    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_detail) {
            startActivity(new Intent(this, DetailActivity.class));
        } else if (id == R.id.nav_sum) {
            startActivity(new Intent(this, SumActivity.class));
        } else if (id == R.id.nav_setting) {
            startActivity(new Intent(this, SettingActivity.class));

        } else if (id == R.id.nav_share) {

        }
        //抽屉收缩动画延迟0.5s
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(500);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDrawer.closeDrawer(GravityCompat.START);
                    }
                });
            }
        }).start();
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        return true;
    }
}
