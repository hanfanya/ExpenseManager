package com.example.samuel.expensemanager.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.samuel.expensemanager.fragment.ExpenseSumFragment;
import com.example.samuel.expensemanager.fragment.IncomeSumFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * author: 刘万鹏
 * <p/>
 * 统计页面的ViewPagerAdapter
 * 在这里初始化分别表示收入和支出的Fragment
 * <p/>
 * Created by Mustard on 2015/12/1.
 */
public class SumPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> mFragments = new ArrayList<>();
    private List<String> mFragmentTitles = new ArrayList<>();

    public SumPagerAdapter(FragmentManager fm) {
        super(fm);
        initFragment();
    }

    private void initFragment() {
        addFragment(new ExpenseSumFragment(), "支出");
        addFragment(new IncomeSumFragment(), "收入");
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

    //这里用于在TabLayout中显示标题日
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitles.get(position);
    }
}
