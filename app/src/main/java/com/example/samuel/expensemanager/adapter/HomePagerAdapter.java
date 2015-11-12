package com.example.samuel.expensemanager.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.samuel.expensemanager.fragment.HomeFragment;
import com.example.samuel.expensemanager.fragment.SummaryFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Samuel on 15/11/11 22:56.
 * Email:samuel40@126.com
 */
public class HomePagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> mFragments = new ArrayList<>();
    private List<String> mFragmentTitles = new ArrayList<>();

    public HomePagerAdapter(FragmentManager fm) {
        super(fm);
        initFragment();
    }

    private void initFragment() {
        addFragment(new HomeFragment(), "首页");
        addFragment(new SummaryFragment(), "汇总");
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
