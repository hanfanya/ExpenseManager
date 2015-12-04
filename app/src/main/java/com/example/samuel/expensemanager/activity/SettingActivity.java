package com.example.samuel.expensemanager.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.example.samuel.expensemanager.R;

/**
 * Created by Samuel on 15/12/3 23:07
 * Email:xuzhou40@gmail.com
 * desc:应用设置界面，修改自Android Studio的模板，
 */


public class SettingActivity extends AppCompatPreferenceActivity {

    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else {
                preference.setSummary(stringValue);
            }
            return true;
        }
    };


    private static void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
        SharedPreferences sharedPreferences = preference.getContext().getSharedPreferences("config", Context.MODE_PRIVATE);

        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                sharedPreferences.getString(preference.getKey(), ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new PrefsFragment()).commit();
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("设置");//设置标题
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
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
