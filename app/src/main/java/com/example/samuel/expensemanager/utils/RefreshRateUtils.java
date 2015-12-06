package com.example.samuel.expensemanager.utils;

import android.content.Context;
import android.util.Log;

import com.example.samuel.expensemanager.bean.Rate;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.HashMap;

/**
 * 更新汇率的工具类
 * <p/>
 * Created by Allen_C on 2015/12/3.
 */
public class RefreshRateUtils {
    static boolean success = false;
    private static Context mCxt;
    private static HashMap<String, Double> rateMap = new HashMap<String, Double>();

    RefreshRateUtils() {
        initRateMap();
    }

    private static void initRateMap() {
        rateMap.put("美元/日元", 123.115);
        rateMap.put("美元/加拿大元", 1.3361);
        rateMap.put("美元/人民币", 6.4025);
        rateMap.put("美元/澳门币", 7.9826);
        rateMap.put("美元/澳大利亚元", 1.3624);
        rateMap.put("美元/港币", 7.7501);
        rateMap.put("美元/台币", 32.737);
        rateMap.put("美元/欧元", 0.9188);
        rateMap.put("美元/英镑", 0.6617);

        rateMap.put("美元/泰铢", 35.9115);
        rateMap.put("美元/利比里亚元", 84.66);
        rateMap.put("美元/印度卢比", 66.6485);
        rateMap.put("美元/比特币", 0.0025);
        rateMap.put("美元/巴西里尔", 3.7531);
        rateMap.put("美元/黄金", 9.0E-4);
        rateMap.put("美元/朝鲜元", 900.0);
        rateMap.put("美元/越南盾", 22480.0);
        rateMap.put("美元/以色列谢克尔", 3.8176);
        rateMap.put("美元/阿根廷披索", 9.6898);

        rateMap.put("美元/白银", 0.0689);
        rateMap.put("美元/柬埔寨瑞尔", 4059.6);
        rateMap.put("美元/瑞士法郎", 0.9965);
        rateMap.put("美元/丹麦克朗", 6.8582);
        rateMap.put("美元/伊朗里亚尔", 30095.0);
        rateMap.put("美元/韩国元", 1161.395);
        rateMap.put("美元/泰铢", 35.9115);
        rateMap.put("美元/利比里亚元", 84.66);


        rateMap.put("欧元/美元", 1.088376);
        rateMap.put("日元/美元", 0.008122);
        rateMap.put("英镑/美元", 1.511259);
        rateMap.put("港币/美元", 0.129031);
        rateMap.put("澳门币/美元", 0.125272);
        rateMap.put("台币/美元", 0.030546);
        rateMap.put("人民币/美元", 0.156189);
        rateMap.put("加拿大元/美元", 0.748447);
        rateMap.put("澳大利亚元/美元", 0.733999);


        rateMap.put("利比里亚元/美元", 0.011812);
        rateMap.put("阿根廷披索/美元", 0.103201);
        rateMap.put("瑞士法郎/美元", 1.003512);
        rateMap.put("白银/美元", 14.513788);
        rateMap.put("印度卢比/美元", 0.015004);
        rateMap.put("越南盾/美元", 4.4E-5);
        rateMap.put("韩国元/美元", 8.61E-4);
        rateMap.put("以色列谢克尔/美元", 0.261945);
        rateMap.put("伊朗里亚尔/美元", 3.3E-5);
        rateMap.put("黄金/美元", 1.003512);

        rateMap.put("巴西里尔/美元", 14.513788);
        rateMap.put("比特币/美元", 0.015004);
        rateMap.put("泰铢/美元", 4.4E-5);
        rateMap.put("朝鲜元/美元", 8.61E-4);
        rateMap.put("丹麦克朗/美元", 0.261945);
        rateMap.put("柬埔寨瑞尔/美元", 3.3E-5);

    }

    public static double getDefaultRate(String key, double defValue) {
        initRateMap();
        if (rateMap.get(key) > 0) {
            return rateMap.get(key);
        } else {
            return defValue;
        }
    }

    public static boolean getRateStringFromNet(Context cxt, String scur, String tcur) {
        mCxt = cxt;
        String requestUrl = "http://api.k780.com:88/?app=finance.rate&" +
                "scur=" + scur + "&tcur=" + tcur
                + "&appkey=16647&sign=6778c3a31bc89471075510779678b627&format=json";

        String backUpRequestUrl = "http://api.k780.com:88/?app=finance.rate&" +
                "scur=" + scur + "&tcur=" + tcur
                + "&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4";

        RequestParams params = new RequestParams(requestUrl);
        params.setSslSocketFactory(null); // 设置ssl


        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Rate rateBean = gson.fromJson(result, Rate.class);
                String rateString = rateBean.result.rate;
                float rate = Float.parseFloat(rateString);
                String ratenm = rateBean.result.ratenm;

                Log.i("onSuccess", "rate" + rate);

                PrefUtils.setFloat(mCxt, ratenm, rate);

                success = true;
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (isOnCallback) {
                    Log.e("getRateStringFromNet", "onError() returned: " + ex, ex);
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
        return success;
    }


}
