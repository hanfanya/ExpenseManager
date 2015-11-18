package com.example.samuel.expensemanager.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.samuel.expensemanager.fragment.ExpenseFragment;
import com.example.samuel.expensemanager.fragment.IncomeFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Samuel on 15/11/18.
 */
public class AddRecordPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> mFragments = new ArrayList<>();
    private List<String> mFragmentTitles = new ArrayList<>();

    public AddRecordPagerAdapter(FragmentManager fm) {
        super(fm);
        initFragment();
    }

    private void initFragment() {
        addFragment(new ExpenseFragment(), "支出");
        addFragment(new IncomeFragment(), "收入");
    }

    public void addFragment(Fragment fragment, String title) {
        mFragments.add(fragment);
        mFragmentTitles.add(title);
    }


    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitles.get(position);
    }
}
