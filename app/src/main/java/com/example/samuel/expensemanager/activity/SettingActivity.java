package com.example.samuel.expensemanager.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.v7.app.ActionBar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.Window;

import com.example.samuel.expensemanager.R;
import com.example.samuel.expensemanager.utils.SPUtils;
import com.example.samuel.expensemanager.utils.SysUtils;

/**
 * Created by Samuel on 15/12/3 23:07
 * Email:xuzhou40@gmail.com
 * desc:应用设置界面，修改自Android Studio的模板，
 */


public class SettingActivity extends AppCompatPreferenceActivity {
    private static Context mContext;
    private static Activity mActivity;

    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
//            String stringValue = value.toString();
            String stringValue = value.toString();
            String currentTheme = SPUtils.getString(mContext, "theme_setting", "0");

            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                String key = listPreference.getKey();

                if (key.equals("network_setting")) {
                    int index = listPreference.findIndexOfValue(stringValue);

                    preference.setSummary(
                            index >= 0
                                    ? listPreference.getEntries()[index]
                                    : null);
                } else if (key.equals("theme_setting")) {
                    int index = listPreference.findIndexOfValue(stringValue);

                    mContext.setTheme(R.style.AppThemeGreen);
//                    int index = listPreference.findIndexOfValue(stringValue);
                    preference.setSummary(
                            index >= 0
                                    ? listPreference.getEntries()[index]
                                    : null);
                    if (index == 0) {
                        if (!currentTheme.equals("0")) {
                            mActivity.finish();
                            Intent intent = mActivity.getIntent();
                            mActivity.startActivity(intent);

                            /*Intent intent = mActivity.getIntent();
                            mActivity.overridePendingTransition(0, 0);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            mActivity.finish();

                            mActivity.overridePendingTransition(0, 0);
                            mActivity.startActivity(intent);*/
                        }
//                        Toast.makeText(mContext, "你选择了 0", Toast.LENGTH_SHORT).show();

                    } else if (index == 1) {
                        if (!currentTheme.equals("1")) {
                            mActivity.finish();
                            Intent intent = mActivity.getIntent();
                            mActivity.startActivity(intent);
                        }

//                        Toast.makeText(mContext, "你选择了 1", Toast.LENGTH_SHORT).show();

                    } else if (index == 2) {
                        if (!currentTheme.equals("2")) {
                            mActivity.finish();
                            Intent intent = mActivity.getIntent();
                            mActivity.startActivity(intent);
                        }
//                        Toast.makeText(mContext, "你选择了 2", Toast.LENGTH_SHORT).show();

                    } else if (index == 3) {
                        if (!currentTheme.equals("3")) {
                            mActivity.finish();
                            Intent intent = mActivity.getIntent();
                            mActivity.startActivity(intent);
                        }
//                        Toast.makeText(mContext, "你选择了 2", Toast.LENGTH_SHORT).show();

                    } else if (index == 4) {
                        if (!currentTheme.equals("4")) {
                            mActivity.finish();
                            Intent intent = mActivity.getIntent();
                            mActivity.startActivity(intent);
                        }
//                        Toast.makeText(mContext, "你选择了 2", Toast.LENGTH_SHORT).show();

                    } else if (index == 5) {
                        if (!currentTheme.equals("5")) {
                            mActivity.finish();
                            Intent intent = mActivity.getIntent();
                            mActivity.startActivity(intent);
                        }
//                        Toast.makeText(mContext, "你选择了 2", Toast.LENGTH_SHORT).show();

                    } else if (index == 6) {
                        if (!currentTheme.equals("6")) {
                            mActivity.finish();
                            Intent intent = mActivity.getIntent();
                            mActivity.startActivity(intent);
                        }
//                        Toast.makeText(mContext, "你选择了 2", Toast.LENGTH_SHORT).show();

                    } else if (index == 7) {
                        if (!currentTheme.equals("7")) {
                            mActivity.finish();
                            Intent intent = mActivity.getIntent();
                            mActivity.startActivity(intent);
                        }
//                        Toast.makeText(mContext, "你选择了 2", Toast.LENGTH_SHORT).show();

                    } else if (index == 8) {
                        if (!currentTheme.equals("8")) {
                            mActivity.finish();
                            Intent intent = mActivity.getIntent();
                            mActivity.startActivity(intent);
                        }
//                        Toast.makeText(mContext, "你选择了 2", Toast.LENGTH_SHORT).show();

                    } else if (index == 9) {
                        if (!currentTheme.equals("9")) {
                            mActivity.finish();
                            Intent intent = mActivity.getIntent();
                            mActivity.startActivity(intent);
                        }
//                        Toast.makeText(mContext, "你选择了 2", Toast.LENGTH_SHORT).show();

                    } else if (index == 10) {
                        if (!currentTheme.equals("10")) {
                            mActivity.finish();
                            Intent intent = mActivity.getIntent();
                            mActivity.startActivity(intent);
                        }
//                        Toast.makeText(mContext, "你选择了 2", Toast.LENGTH_SHORT).show();

                    }

                }


            } else if (preference instanceof SwitchPreference) {
                SwitchPreference switchPreference = (SwitchPreference) preference;
                boolean hasPassword = SPUtils.getBoolean(mContext, "passset", false);
                if (hasPassword) {
                    switchPreference.setChecked(true);
                    preference.setSummary("已设置密码");
                } else {
                    switchPreference.setChecked(false);
                    preference.setSummary("未设置密码");
                }

            } else {

                preference.setSummary(stringValue);
            }
            return true;
        }
    };
    private static CheckBoxPreference checkBoxPreference;
    private static ListPreference mThemeSetting;


    private static void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
        SharedPreferences sharedPreferences = preference.getContext().getSharedPreferences("config", Context.MODE_PRIVATE);

        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                sharedPreferences.getString(preference.getKey(), ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_ACTION_BAR);

        super.onCreate(savedInstanceState);
        String currentTheme = SPUtils.getString(SettingActivity.this, "theme_setting", "0");

        setTheme(SysUtils.getThemeResId(SettingActivity.this));

        mContext = SettingActivity.this;
        mActivity = this;
        setupActionBar();
        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new PrefsFragment()).commit();
    }

    @Override
    protected void onResume() {

        super.onResume();


//        Preference preference = getPreferenceManager().findPreference("passset");
//        SwitchPreference switchPreference = (SwitchPreference) preference;
        boolean hasPassword = SPUtils.getBoolean(mContext, "passset", false);
        // Log.i("=================================", hasPassword + "");
        if (hasPassword) {
            checkBoxPreference.setChecked(true);
            checkBoxPreference.setSummary("已设置密码");
        } else {
            checkBoxPreference.setChecked(false);
            checkBoxPreference.setSummary("未设置密码");

        }
    }

    private void setupActionBar() {
//        String themeSetting = SPUtils.getString(SettingActivity.this, "theme_setting", "0");
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        int primaryColor = typedValue.data;


        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(primaryColor);
        }
//        window.requestFeature(Window.FEATURE_ACTION_BAR);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(primaryColor));
        actionBar.setTitle("设置");//设置标题
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setElevation(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent("com.example.barry.clockdemo.ReminderActivity.onDestroy");
        sendBroadcast(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(mContext, MainActivity.class));
    }

    public static class PrefsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            getPreferenceManager().setSharedPreferencesName("config");
            addPreferencesFromResource(R.xml.preferences);
            setHasOptionsMenu(true);

            bindPreferenceSummaryToValue(findPreference("budget_figure"));
            bindPreferenceSummaryToValue(findPreference("network_setting"));
            bindPreferenceSummaryToValue(findPreference("theme_setting"));

            Preference preference = getPreferenceScreen().findPreference("passset");
            checkBoxPreference = (CheckBoxPreference) preference;

            /*Preference preference1 = getPreferenceScreen().findPreference("theme_setting");
            mThemeSetting = (ListPreference) preference1;


            mThemeSetting.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    String value = (String) newValue;
//                    int indexOfValue = mThemeSetting.findIndexOfValue(value);
//                    mThemeSetting.setSummary(mThemeSetting.getEntries()[indexOfValue]);

                    switch (value) {
                        case "0":
                            Toast.makeText(mContext, "你选择了 0", Toast.LENGTH_SHORT).show();
//                            mContext.setTheme(R.style.AppTheme);
                            break;
                        case "1":
                            Toast.makeText(mContext, "你选择了 1", Toast.LENGTH_SHORT).show();

//                            mContext.setTheme(R.style.AppThemeGreen);

                            break;
                        case "2":
                            Toast.makeText(mContext, "你选择了 2", Toast.LENGTH_SHORT).show();

//                            mContext.setTheme(R.style.AppThemePurple);

                            break;
                    }
                    return false;
                }
            });*/


        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
//                startActivity(new Intent(getActivity(), MainActivity.class));
                System.out.println("点击了back------");
                getActivity().finish();
                startActivity(new Intent(mContext, MainActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

    }
}
