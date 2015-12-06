package com.example.samuel.expensemanager.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.samuel.expensemanager.R;
import com.example.samuel.expensemanager.bean.Country;
import com.example.samuel.expensemanager.utils.PrefUtils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * 汇率ListView的Adapter
 * <p/>
 * Created by Allen_C on 2015/12/1.
 */
public class CurrencyListAdapter extends BaseAdapter {

    Activity mActivity;//所在的Activity
    ArrayList<Country> mShownCountryList;//要显示的item
    Country mSelectedItem;//选为原始货币的item
    boolean mIsCalculating;
    boolean mIsPointLast;
    String mPointString;
    private HashMap<String, Float> rateMap = new HashMap<String, Float>();

    //创建的时候把要显示的数据list传进来
    public CurrencyListAdapter(Activity activity, ArrayList<Country> countryList, boolean isCalculating,
                               boolean isPointLast, String pointString) {
        super();
        mActivity = activity;

        mShownCountryList = countryList;

        mIsCalculating = isCalculating;

        mIsPointLast = isPointLast;

        mPointString = pointString;

        initRateMap();
    }

    @Override
    public int getCount() {
        return mShownCountryList.size();
    }

    @Override
    public Country getItem(int position) {
        return mShownCountryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        refreshData();//每次刷新都要更新哪一个是mSelectedItem,以便获取汇率判断

        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mActivity, R.layout.item_currency_list, null);
            holder = new ViewHolder();
            holder.iv_cur_country = (ImageView) convertView.findViewById(R.id.iv_cur_country);
            holder.tv_cur_currency = (TextView) convertView.findViewById(R.id.tv_cur_currency);
            holder.et_cur_money = (EditText) convertView.findViewById(R.id.et_cur_money);
            holder.tv_cur_unit = (TextView) convertView.findViewById(R.id.tv_cur_unit);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //禁止弹出系统输入法输入框
        /*mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Class<EditText> cls = EditText.class;
        Method setSoftInputShownOnFocus;
        try {
            setSoftInputShownOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
            setSoftInputShownOnFocus.setAccessible(true);
            setSoftInputShownOnFocus.invoke( holder.et_cur_money, false);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }*/


        Country item = getItem(position);

        holder.iv_cur_country.setImageResource(item.getImageId());
        holder.tv_cur_currency.setText(item.getCurrency());
        holder.tv_cur_unit.setText(item.getUnit());


        //要在本地提供一套默认汇率

        if (getRateFromSP(item) == 0) {//如果更新未结束，直接从默认汇率拿，避免显示为0.0
            holder.et_cur_money.setHint(getDefaultRate(item) * 100 + "");
        } else {
            holder.et_cur_money.setHint(getRateFromSP(item) * 100 + "");
        }

        if (item.isSelected()) {
            convertView.setBackgroundColor(Color.WHITE);
            //convertView.setBackgroundColor(Color.parseColor("#1565C0"));
        } else {
            convertView.setBackgroundColor(Color.TRANSPARENT);
            //convertView.setBackgroundColor(Color.parseColor("#0D47A1"));
        }

        if (mIsCalculating) {//如果正在计算，就setText
            //holder.et_cur_money.setTextColor(Color.WHITE);
            if (mIsPointLast) {//如果最后一个是小数点"."

                if (item.isSelected()) {//基础货币显示传进来的mPointString
                    String moneyString = mPointString;

                    NumberFormat currency = NumberFormat.getCurrencyInstance();   //建立货币格式化引用
                    BigDecimal bigLoanAmount = new BigDecimal(moneyString);
                    currency.setMaximumFractionDigits(3);//只有三位小数
                    moneyString = currency.format(bigLoanAmount);
                    moneyString = moneyString.substring(1, moneyString.length() - 2);
                    Log.i("moneyString,substring", moneyString);
                    holder.et_cur_money.setText(moneyString);

                } else {//其他仍然显示汇率转换后的数字
                    if (getRateFromSP(item) == 0) {//如果更新未结束，直接从默认汇率拿，避免显示为0.0
                        String moneyString = getDefaultRate(item) * item.getAmount() + "";

                        NumberFormat currency = NumberFormat.getCurrencyInstance();    //建立货币格式化引用
                        BigDecimal bigLoanAmount = new BigDecimal(moneyString);
                        currency.setMaximumFractionDigits(3);//只有三位小数
                        moneyString = currency.format(bigLoanAmount);

                        moneyString = moneyString.substring(1, moneyString.length());
                        Log.i("moneyString,substring", moneyString);
                        holder.et_cur_money.setText(moneyString);
                    } else {
                        String moneyString = getRateFromSP(item) * item.getAmount() + "";

                        NumberFormat currency = NumberFormat.getCurrencyInstance();    //建立货币格式化引用
                        BigDecimal bigLoanAmount = new BigDecimal(moneyString);
                        currency.setMaximumFractionDigits(3);//只有三位小数
                        moneyString = currency.format(bigLoanAmount);

                        moneyString = moneyString.substring(1, moneyString.length());
                        Log.i("moneyString,substring", moneyString);
                        holder.et_cur_money.setText(moneyString);
                    }
                }
            } else {//如果最后一个不是小数点"."，正常显示

                if (getRateFromSP(item) == 0) { //如果更新未结束，直接从默认汇率拿，避免显示为0.0

                    String moneyString = getDefaultRate(item) * item.getAmount() + "";
                    Log.i("moneyString,before", moneyString);

                    NumberFormat currency = NumberFormat.getCurrencyInstance();//建立货币格式化引用
                    BigDecimal bigLoanAmount = new BigDecimal(moneyString);

                    currency.setMaximumFractionDigits(3);//只有三位小数
                    moneyString = currency.format(bigLoanAmount);

                    Log.i("moneyString,after", moneyString);

                    moneyString = moneyString.substring(1, moneyString.length());

                    Log.i("moneyString,substring", moneyString);
                    holder.et_cur_money.setText(moneyString);
                } else {
                    String moneyString = getRateFromSP(item) * item.getAmount() + "";
                    Log.i("moneyString,before", moneyString);
                    NumberFormat currency = NumberFormat.getCurrencyInstance();    //建立货币格式化引用
                    BigDecimal bigLoanAmount = new BigDecimal(moneyString);
                    currency.setMaximumFractionDigits(3);//只有三位小数
                    moneyString = currency.format(bigLoanAmount);

                    Log.i("moneyString,after", moneyString);


                    moneyString = moneyString.substring(1, moneyString.length());


                    Log.i("moneyString,substring", moneyString);

                    holder.et_cur_money.setText(moneyString);
                }
            }
        }

        return convertView;
    }

    private float getRateFromSP(Country country) {//从本地获取汇率
        float rate = 1;//如果被点击的就是mSelectedItem，汇率就是1

        if (country != mSelectedItem) {//两边关于基础货币的数据不统一

            float rate1 = PrefUtils.getFloat(mActivity, mSelectedItem.getUnit() + "/" + "美元", 0);

            if (mSelectedItem.getUnit().equals("美元")) {
                rate1 = 1;
            }

            float rate2 = PrefUtils.getFloat(mActivity, "美元/" + country.getUnit(), 0);

            if (country.getUnit().equals("美元")) {
                rate2 = 1;
            }

            rate = rate1 * rate2;

            Log.i("getRateFromSP", "rate = ~~~~~" + rate);
        }
        return rate;
    }

    private void initRateMap() {
        rateMap.put("美元/日元", Float.parseFloat("123.115"));
        rateMap.put("美元/加拿大元", Float.parseFloat("1.3361"));
        rateMap.put("美元/人民币", Float.parseFloat("6.4025"));
        rateMap.put("美元/澳门币", Float.parseFloat("7.9826"));
        rateMap.put("美元/澳大利亚元", Float.parseFloat("1.3624"));
        rateMap.put("美元/港币", Float.parseFloat("7.7501"));
        rateMap.put("美元/台币", Float.parseFloat("32.737"));
        rateMap.put("美元/欧元", Float.parseFloat("0.9188"));
        rateMap.put("美元/英镑", Float.parseFloat("0.6617"));

        rateMap.put("欧元/美元", Float.parseFloat("1.088376"));
        rateMap.put("日元/美元", Float.parseFloat("0.008122"));
        rateMap.put("英镑/美元", Float.parseFloat("1.511259"));
        rateMap.put("港币/美元", Float.parseFloat("0.129031"));
        rateMap.put("澳门币/美元", Float.parseFloat("0.125272"));
        rateMap.put("台币/美元", Float.parseFloat("0.030546"));
        rateMap.put("人民币/美元", Float.parseFloat("0.156189"));
        rateMap.put("加拿大元/美元", Float.parseFloat("0.748447"));
        rateMap.put("澳大利亚元/美元", Float.parseFloat("0.733999"));
    }

    private float getDefaultRate(Country country) {//获取默认汇率

        float rate = 1;//如果被点击的就是mSelectedItem，汇率就是1
        if (country != mSelectedItem) {

            float rate1;
            if (mSelectedItem.getUnit().equals("美元")) {
                rate1 = 1;
            } else {
                rate1 = rateMap.get(mSelectedItem.getUnit() + "/美元");
            }

            float rate2;
            if (country.getUnit().equals("美元")) {
                rate2 = 1;
            } else {
                rate2 = rateMap.get("美元/" + country.getUnit());
            }

            rate = rate1 * rate2;

            Log.i("getDefaultRate", "rate = ~~~~~" + rate);
        }
        return rate;

    }

    private void refreshData() {//每次刷新都要更新哪一个是mSelectedItem
        for (Country country : mShownCountryList) {
            if (country.isSelected()) {
                mSelectedItem = country;
                return;
            }
        }
    }

    static class ViewHolder {
        ImageView iv_cur_country;
        TextView tv_cur_currency;
        EditText et_cur_money;
        TextView tv_cur_unit;
    }
}
