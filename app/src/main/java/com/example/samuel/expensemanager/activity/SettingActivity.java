package com.example.samuel.expensemanager.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.MenuItem;

import com.example.samuel.expensemanager.R;
import com.example.samuel.expensemanager.utils.SPUtils;

/**
 * Created by Samuel on 15/12/3 23:07
 * Email:xuzhou40@gmail.com
 * desc:应用设置界面，修改自Android Studio的模板，
 */


public class SettingActivity extends AppCompatPreferenceActivity {
    private static Context mContext;

    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
//            String stringValue = value.toString();
            String stringValue = value.toString();

            Log.i("++++", stringValue);

            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

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


    private static void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
        SharedPreferences sharedPreferences = preference.getContext().getSharedPreferences("config", Context.MODE_PRIVATE);

        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                sharedPreferences.getString(preference.getKey(), ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = SettingActivity.this;
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
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("设置");//设置标题
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent("com.example.barry.clockdemo.ReminderActivity.onDestroy");
        sendBroadcast(intent);
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

            Preference preference = getPreferenceScreen().findPreference("passset");
            checkBoxPreference = (CheckBoxPreference) preference;


        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
//                startActivity(new Intent(getActivity(), MainActivity.class));
                System.out.println("点击了back------");
                getActivity().finish();
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }
}
