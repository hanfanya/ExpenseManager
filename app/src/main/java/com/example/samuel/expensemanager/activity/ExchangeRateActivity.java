package com.example.samuel.expensemanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.samuel.expensemanager.R;
import com.example.samuel.expensemanager.adapter.CurrencyListAdapter;
import com.example.samuel.expensemanager.bean.Country;
import com.example.samuel.expensemanager.utils.PrefUtils;
import com.example.samuel.expensemanager.utils.RefreshRateUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class ExchangeRateActivity extends AppCompatActivity {
    public static final int RATE_REQUEST = 100;
    @Bind(R.id.key_0)
    Button mKey_0;
    @Bind(R.id.key_1)
    Button mKey_1;
    @Bind(R.id.key_2)
    Button mKey_2;
    @Bind(R.id.key_3)
    Button mKey_3;
    @Bind(R.id.key_4)
    Button mKey_4;
    @Bind(R.id.key_5)
    Button mKey_5;
    @Bind(R.id.key_6)
    Button mKey_6;
    @Bind(R.id.key_7)
    Button mKey_7;
    @Bind(R.id.key_8)
    Button mKey_8;
    @Bind(R.id.key_9)
    Button mKey_9;
    @Bind(R.id.key_point)
    Button mKey_Piont;
    @Bind(R.id.key_clear)
    //FrameLayout mKey_Clear;
            //ImageButton mKey_Clear;
            Button mKey_Clear;
    StringBuffer numStringBuffer;
    private CurrencyListAdapter mCurrencyListAdapter;//币种信息adapter
    private ListView lv_currency;//显示币种信息的listView
    private ArrayList<Country> mCountryList;//原始数据
    private ArrayList<Country> mShownCountryList;//要显示的数据
    private ArrayList<Integer> indexInCountryList = new ArrayList<Integer>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_rate);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_home);
        toolbar.setTitle("汇率换算");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        lv_currency = (ListView) findViewById(R.id.lv_currency);

        initListView();//初始化listView

        numStringBuffer = new StringBuffer();
    }

    private void initListView() {
        mCountryList = new ArrayList<Country>();
        initCountryList(mCountryList);//初始化原始数据

        initRate();//初始化汇率，通过原始数据来确定要查询的汇率

        // initShownCountryList();//初始化要显示的数据
        updateShownCountryList();

        mCurrencyListAdapter = new CurrencyListAdapter(this, mShownCountryList, false, false, null);

        lv_currency.setAdapter(mCurrencyListAdapter);

        lv_currency.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //点击某个item，把该item设为初始货币，其他设为目标货币
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mShownCountryList.get(position).setIsSelected(true);

                Country clickedCountry = mShownCountryList.get(position);

                mShownCountryList = changeSelectedCountry(clickedCountry, mShownCountryList);

                //点击之后，在SP记录基础货币变成了mCountryList里面的哪一个
                int posionInCountryList = getPosition(clickedCountry, mCountryList);
                PrefUtils.setInt(ExchangeRateActivity.this, "selectedCountry", posionInCountryList);

                //改变mCountryList里面的基础货币
                mCountryList = changeSelectedCountry(clickedCountry, mCountryList);

                mCurrencyListAdapter.notifyDataSetChanged();
                Log.i("onItemClick", "click~!!!!!!!!!");
            }
        });


        /**
         * 长按切换币种
         * 本地数据要改，内存数据要改
         * */
        lv_currency.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                //记下在mShownCountryList的位置
                bundle.putString("positionInShownList", "shownCountry" + position);

                bundle.putInt("replacedPosition", position);//在mShownCountryList的位置,用于之后返回更新index

                Log.i("onItemLongClick", "shownCountry" + position);

                Country longClickedItem = mShownCountryList.get(position);
                for (Country country : mCountryList) {
                    if (country == longClickedItem) {
                        country.setIsReplaced(true);
                        //记下在mCountryList的位置（本地改）
                        PrefUtils.setInt(ExchangeRateActivity.this, "isReplaced", mCountryList.indexOf(country));

                        // bundle.putSerializable("isReplaced", country);
                        if (country.isSelected()) {//如果被长按的是基础货币，就把它清除，让替换的货币变成新的基础货币
                            country.setIsSelected(false);//（内存改）
                            bundle.putBoolean("replacedItemIsSelected", true);
                        }
                    } else {
                        country.setIsReplaced(false);
                    }
                }

                Intent intent = new Intent(ExchangeRateActivity.this, ChooseActivity.class);
                intent.putExtra("data", bundle);


                startActivityForResult(intent, RATE_REQUEST);

                return true;
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //刷新页面 通知adapter
        Log.i("onActivityResult", "刷新页面 通知adapter");

        int newSelectedCountryIndex = data.getIntExtra("newSelectedCountry", -1);
        if (newSelectedCountryIndex >= 0) {
            //换新的基础货币，（内存改）
            mCountryList.get(newSelectedCountryIndex).setIsSelected(true);
            //数据跟ChooseActivity同步
            Country newSelectedCountry = mCountryList.get(newSelectedCountryIndex);
            mCountryList = changeSelectedCountry(newSelectedCountry, mCountryList);
        }

        int which = data.getIntExtra("replacedPosition", -1);
        int index = data.getIntExtra("newPosition", -1);
        if (which >= 0 && index >= 0) {
            updateIndexList(which, index);
        }

        if (numStringBuffer.length() != 0) {
            numStringBuffer.delete(0, numStringBuffer.length());
            Log.i("onActivityResult", "deleteBuffer");
        }


        updateShownCountryList();//更新数据
        //initShownCountryList();

        mCurrencyListAdapter = new CurrencyListAdapter(this, mShownCountryList, false, false, null);

        lv_currency.setAdapter(mCurrencyListAdapter);

        //mCurrencyListAdapter.notifyDataSetChanged();//通知adapter刷新
        Log.i("onActivityResult", "mCurrencyListAdapter.notifyDataSetChanged();");

    }


    private int getPosition(Country clickedCountry, ArrayList<Country> countryArrayList) {
        int position = -1;
        position = countryArrayList.indexOf(clickedCountry);
        return position;
    }

    private ArrayList<Country> changeSelectedCountry(Country clickedCountry, ArrayList<Country> countryArrayList) {

        //单击某个item之后，要改变基础货币，就要刷新countryArrayList
        ArrayList<Country> changedSelectedCountry = new ArrayList<Country>();

        for (Country country : countryArrayList) {
            if (country != clickedCountry) {
                country.setIsSelected(false);//把之前的基础货币清除
            }
            changedSelectedCountry.add(country);
        }
        return changedSelectedCountry;
    }


    private void initCountryList(ArrayList<Country> countryList) {//初始化原始数据

        int[] imageIdList = new int[]{R.drawable.cny, R.drawable.usd, R.drawable.hkd, R.drawable.eur, R.drawable.jpy,
                R.drawable.gbp, R.drawable.twd, R.drawable.mop, R.drawable.aud, R.drawable.cad,
                R.drawable.myr, R.drawable.nzd, R.drawable.rub, R.drawable.sgd,
                R.drawable.ars, R.drawable.brl, R.drawable.btc, R.drawable.chf, R.drawable.dkk, R.drawable.ils, R.drawable.inr,
                R.drawable.irr, R.drawable.khr, R.drawable.kpw, R.drawable.krw, R.drawable.lrd, R.drawable.thb, R.drawable.vnd,
                R.drawable.xag, R.drawable.xau};

        String[] currencyList = new String[]{"CNY", "USD", "HKD", "EUR", "JPY", "GBP", "TWD", "MOP", "AUD", "CAD",
                "MYR", "NZD", "RUB", "SGD",
                "ARS", "BRL", "BTC", "CHF", "DKK", "ILS", "INR", "IRR", "KHR", "KPW", "KRW", "LRD", "THB", "VND", "XAG", "XAU"};

        String[] unitList = new String[]{"人民币", "美元", "港币", "欧元", "日元", "英镑", "台币", "澳门币", "澳大利亚元", "加拿大元",
                "马来西亚林吉特", "新西兰元", "俄罗斯卢布", "新加坡元",
                "阿根廷披索", "巴西里尔", "比特币", "瑞士法郎", "丹麦克朗", "以色列谢克尔", "印度卢比", "伊朗里亚尔",
                "柬埔寨瑞尔", "朝鲜元", "韩国元", "利比里亚元", "泰铢", "越南盾", "白银", "黄金"};

        for (int i = 0; i < 10; i++) {
            Country country = new Country();
            country.setImageId(imageIdList[i]);
            country.setCurrency(currencyList[i]);
            country.setUnit(unitList[i]);
            country.setAmount(100);

            if (!PrefUtils.getBoolean(this, "countryIsInited", false)) {//要恢复默认状态可以修改这个flag的值为false
                if (i < 4) {//第一次初始化,默认显示前四个
                    country.setIsShown(true);

                    PrefUtils.setInt(this, "shownCountry" + i, i);//记住要显示的是哪一个item

                    indexInCountryList.add(i);
                } else {
                    country.setIsShown(false);
                }
                if (i == 0) {//第一次初始化,默认第一个为初始货币
                    country.setIsSelected(true);
                    PrefUtils.setInt(this, "selectedCountry", 0);//记住要基础货币是哪一个item
                } else {
                    country.setIsSelected(false);
                }
            } else {//不是第一次初始化，从SP里面获取

                indexInCountryList.add(PrefUtils.getInt(this, "shownCountry0", -1));
                indexInCountryList.add(PrefUtils.getInt(this, "shownCountry1", -1));
                indexInCountryList.add(PrefUtils.getInt(this, "shownCountry2", -1));
                indexInCountryList.add(PrefUtils.getInt(this, "shownCountry3", -1));

                if (i == PrefUtils.getInt(this, "shownCountry0", -1) || i == PrefUtils.getInt(this, "shownCountry1", -1)
                        || i == PrefUtils.getInt(this, "shownCountry2", -1) || i == PrefUtils.getInt(this, "shownCountry3", -1)) {//第一次初始化
                    country.setIsShown(true);

                } else {
                    country.setIsShown(false);
                }

                if (i == PrefUtils.getInt(this, "selectedCountry", -1)) {
                    country.setIsSelected(true);
                } else {
                    country.setIsSelected(false);
                }
            }
            countryList.add(country);
        }
        PrefUtils.setBoolean(this, "countryIsInited", true);
    }

    private void updateIndexList(int which, int index) {//在choose点击了哪一个位置，index变成了什么
        ArrayList<Integer> updateIndexList = new ArrayList<Integer>();

        for (int i = 0; i < 4; i++) {
            if (i == which) {//把对应位置换成新的index
                updateIndexList.add(index);
            } else {
                updateIndexList.add(indexInCountryList.get(i));
            }
        }

        indexInCountryList = updateIndexList;

    }

    public void updateShownCountryList() {

        if (mShownCountryList == null) {
            mShownCountryList = new ArrayList<Country>();
        } else {
            Log.i("initShownCountryList", "mShownCountryList.clear();");
            mShownCountryList.clear();
        }

        Country item0 = mCountryList.get(indexInCountryList.get(0));
        Country item1 = mCountryList.get(indexInCountryList.get(1));
        Country item2 = mCountryList.get(indexInCountryList.get(2));
        Country item3 = mCountryList.get(indexInCountryList.get(3));

        mShownCountryList.add(item0);
        mShownCountryList.add(item1);
        mShownCountryList.add(item2);
        mShownCountryList.add(item3);

        Log.i("initShownCountryList", mShownCountryList.get(0).getUnit());
        Log.i("initShownCountryList", mShownCountryList.get(1).getUnit());
        Log.i("initShownCountryList", mShownCountryList.get(2).getUnit());
        Log.i("initShownCountryList", mShownCountryList.get(3).getUnit());
    }


    public void initRate() {//把汇率保存到SharedPreference里面
        //初始化汇率，通过原始数据来确定要查询的汇率
        for (Country country : mCountryList) {
            if (!country.getCurrency().equals("USD")) {
                RefreshRateUtils.getRateStringFromNet(this, "USD", country.getCurrency());
                RefreshRateUtils.getRateStringFromNet(this, country.getCurrency(), "USD");
            }
        }
    }

    private ArrayList<Country> updateShownCountryAmount(double newAmount) {
        ArrayList<Country> mCalculatingShownCountryList = new ArrayList<Country>();
        for (Country country : mShownCountryList) {
            country.setAmount(newAmount);
            mCalculatingShownCountryList.add(country);
        }
        return mCalculatingShownCountryList;

    }


    @OnClick({R.id.key_0, R.id.key_1, R.id.key_2,
            R.id.key_3, R.id.key_4, R.id.key_5,
            R.id.key_6, R.id.key_7, R.id.key_8, R.id.key_9})
    public void inputNumber(View v) {

        boolean done = false;

        String numString = numStringBuffer.toString();
        if (numString.contains(".")) {
            Log.i("numString.contain", "true");
            //String intSubstring = numString.substring(0, numString.indexOf(".")-1);
            String decimalSubstring = numString.substring(numString.indexOf(".") + 1, numString.length());

            if (decimalSubstring.length() >= 3) {//小数部分
                Toast.makeText(this, "超出长度范围", Toast.LENGTH_SHORT).show();
                done = true;
            }
        } else {
            if (numString.length() > 9) {//整数部分
                Toast.makeText(this, "超出长度范围", Toast.LENGTH_SHORT).show();
                done = true;
            }
        }

        if (!done) {
            if (numStringBuffer.toString().startsWith("0")) {
                numStringBuffer.delete(0, 1);
            }
            switch (v.getId()) {
                case R.id.key_0:
                    numStringBuffer.append("0");
                    break;
                case R.id.key_1:
                    numStringBuffer.append("1");
                    break;
                case R.id.key_2:
                    numStringBuffer.append("2");
                    break;
                case R.id.key_3:
                    numStringBuffer.append("3");
                    break;
                case R.id.key_4:
                    numStringBuffer.append("4");
                    break;
                case R.id.key_5:
                    numStringBuffer.append("5");
                    break;
                case R.id.key_6:
                    numStringBuffer.append("6");
                    break;
                case R.id.key_7:
                    numStringBuffer.append("7");
                    break;
                case R.id.key_8:
                    numStringBuffer.append("8");
                    break;
                case R.id.key_9:
                    numStringBuffer.append("9");
                    break;
                default:
                    break;
            }
            double num = Double.valueOf(numStringBuffer.toString());

            mShownCountryList = updateShownCountryAmount(num);

            mCurrencyListAdapter = new CurrencyListAdapter(this, mShownCountryList, true, false, null);

            lv_currency.setAdapter(mCurrencyListAdapter);
        }

        Log.i("inputNumber", "done~");
    }

    @OnClick(R.id.key_point)
    public void inputPiont(View v) {
        if (numStringBuffer.toString().contains(".")) {
            return;
        }

        if (numStringBuffer.length() == 0) {
            numStringBuffer.append("0.");
        } else {
            numStringBuffer.append(".");
        }

        String pointString = numStringBuffer.toString();
        mCurrencyListAdapter = new CurrencyListAdapter(this, mShownCountryList, true, true, pointString);

        lv_currency.setAdapter(mCurrencyListAdapter);
    }

    @OnClick(R.id.key_clear)
    public void backspace(View v) {
        if (numStringBuffer.length() == 0) {
            return;
        }

        if (numStringBuffer.length() > 1) {
            numStringBuffer.deleteCharAt(numStringBuffer.length() - 1);//删除最后一个

            if (numStringBuffer.lastIndexOf("0") == numStringBuffer.length() - 1) {
                //删除了之后，最后一个是"."的话
                String pointString = numStringBuffer.toString();
                mCurrencyListAdapter = new CurrencyListAdapter(this, mShownCountryList, true, true, pointString);

                lv_currency.setAdapter(mCurrencyListAdapter);

                return;
            }

            double num = Double.valueOf(numStringBuffer.toString());

            mShownCountryList = updateShownCountryAmount(num);

            mCurrencyListAdapter = new CurrencyListAdapter(this, mShownCountryList, true, false, null);

            lv_currency.setAdapter(mCurrencyListAdapter);
        } else {
            if (numStringBuffer.length() == 1) {
                numStringBuffer.deleteCharAt(numStringBuffer.length() - 1);//删除最后一个
            }
            updateShownCountryList();//重置

            mCurrencyListAdapter = new CurrencyListAdapter(this, mShownCountryList, false, false, null);

            lv_currency.setAdapter(mCurrencyListAdapter);
        }

    }

    @OnLongClick(R.id.key_clear)
    public boolean clear(View v) {
        if (numStringBuffer.length() != 0) {

            numStringBuffer.delete(0, numStringBuffer.length());

            updateShownCountryList();//重置

            mCurrencyListAdapter = new CurrencyListAdapter(this, mShownCountryList, false, false, null);

            lv_currency.setAdapter(mCurrencyListAdapter);

        }
        Log.i("onLongClick", "Click~!");
        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rate, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            initRate();
            Toast.makeText(this, "汇率已刷新", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.action_createshortcut) {
            createShortCut();
            Toast.makeText(this, "快捷方式已创建", Toast.LENGTH_SHORT).show();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void createShortCut() {
        Intent intent = new Intent(getApplicationContext(), ExchangeRateActivity.class);

        intent.setAction("com.example.samuel.expensemanager.exchangerate");
        intent.addCategory(Intent.CATEGORY_DEFAULT);

        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        // 不允许重建
        shortcut.putExtra("duplicate", false);
        // 设置名字
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, "汇率转换");
        // 设置图标
        Intent.ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.exchange_rate_icon);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
        // 设置意图和快捷方式关联程序
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);

        sendBroadcast(shortcut);

        Log.i("createShortCut", "done");
    }
}
