package com.example.samuel.expensemanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.samuel.expensemanager.R;
import com.example.samuel.expensemanager.adapter.ChooseListAdapter;
import com.example.samuel.expensemanager.bean.Country;
import com.example.samuel.expensemanager.utils.PrefUtils;
import com.example.samuel.expensemanager.utils.SysUtils;

import java.util.ArrayList;

public class ChooseActivity extends AppCompatActivity {

    private ArrayList<Country> mCountryList;
    private ListView lv_choose;
    private ChooseListAdapter mChooseListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(SysUtils.getThemeResId(ChooseActivity.this));

        setContentView(R.layout.activity_choose);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        lv_choose = (ListView) findViewById(R.id.lv_choose);


        mCountryList = new ArrayList<Country>();

        // mUpdatedCountryList = new ArrayList<Country>();

        initCountryList(mCountryList);

        // mUpdatedCountryList = updateCountryListFromRate(mCountryList);

        //  mChooseListAdapter = new ChooseListAdapter(this,mUpdatedCountryList);

        mChooseListAdapter = new ChooseListAdapter(this, mCountryList);

        lv_choose.setAdapter(mChooseListAdapter);

        lv_choose.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = getIntent();
                Bundle bundleData = intent.getBundleExtra("data");

                //把 key = shownCountryX 的值改为countryList里面被点击的position
                String showPositonKey = bundleData.getString("positionInShownList");

                PrefUtils.setInt(ChooseActivity.this, showPositonKey, position);

                int replacedPosition = bundleData.getInt("replacedPosition", -1);//要被替换的位置

                //如果被换掉的是基础货币，就把换上去的设为新的基础货币
                if (bundleData.getBoolean("replacedItemIsSelected", false)) {
                    PrefUtils.setInt(ChooseActivity.this, "selectedCountry", position);
                    intent.putExtra("newSelectedCountry", position);//新的基础货币在CountryList中的位置
                }

                intent.putExtra("replacedPosition", replacedPosition);//要被替换的位置
                intent.putExtra("newPosition", position);//要换成的新index

                setResult(RESULT_OK, intent);

                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {//直接点返回键
        Intent intent = getIntent();
        Bundle bundleData = intent.getBundleExtra("data");
        int replacedPosition = bundleData.getInt("replacedPosition", -1);//要被替换的位置

        //如果被换掉的是基础货币，就不换
        if (bundleData.getBoolean("replacedItemIsSelected", false)) {//把要替换的位置作为新位置传回去
            //PrefUtils.setInt(ChooseActivity.this,"selectedCountry",replacedPosition);
            intent.putExtra("newSelectedCountry", PrefUtils.getInt(ChooseActivity.this, "isReplaced", -1));//新的基础货币在CountryList中的位置

            Log.i("onBackPressed", "换基础货币");
        }

        setResult(RESULT_OK, intent);
        finish();
        super.onBackPressed();
    }


    private ArrayList<Country> updateCountryListFromRate(ArrayList<Country> countryList) {

        //此方法暂时弃用
        ArrayList<Country> updatedCountryList = new ArrayList<Country>();

        Intent intent = getIntent();
        Bundle bundleData = intent.getBundleExtra("data");

        Country replacedCountry = (Country) bundleData.getSerializable("isReplaced");

        for (Country country : countryList) {
            if (country.getImageId() == replacedCountry.getImageId()) {
                country = replacedCountry;//把isReplaced属性拿到
            }
            updatedCountryList.add(country);
        }

        return updatedCountryList;
    }

    private void initCountryList(ArrayList<Country> countryList) {

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

            if (i == PrefUtils.getInt(this, "isReplaced", -1)) {
                country.setIsReplaced(true);
            } else {
                country.setIsReplaced(false);
            }

            countryList.add(country);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent("com.example.barry.clockdemo.ReminderActivity.onDestroy");
        sendBroadcast(intent);
    }
}
