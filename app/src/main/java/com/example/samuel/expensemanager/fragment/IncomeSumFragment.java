package com.example.samuel.expensemanager.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.samuel.expensemanager.R;

/**
 * author: 刘万鹏
 * <p/>
 * 统计页面的收入部分Fragment显示
 * <p/>
 * Created by Mustard on 2015/12/1.
 */
public class IncomeSumFragment extends Fragment {

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sum_income, null);

        return view;
    }
}
