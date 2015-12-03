package com.example.samuel.expensemanager.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.samuel.expensemanager.R;

import java.util.ArrayList;

/**
 * Created by Samuel on 15/11/18.
 */
public class SumExpenseListViewAdapter extends BaseAdapter {
    private ArrayList<Float> mSum;
    private ArrayList<String> mtypenames;
    private ArrayList<Integer> mColors;
    private Context mContext;
    private Double mSumExpense;


    public SumExpenseListViewAdapter() {
    }

    public SumExpenseListViewAdapter(ArrayList<Float> eSum, ArrayList<String> xVals, ArrayList<Integer> colors, Double SumExpense, Context context) {
        mSum = eSum;
        mtypenames = xVals;
        mColors = colors;
        mContext = context;
        mSumExpense = SumExpense;
    }

    @Override
    public int getCount() {
        return mtypenames.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_sumlist_expense, null);
            holder = new MyViewHolder();
            holder.ivTypecolor = (ImageView) convertView.findViewById(R.id.iv_type_sum);
            holder.tvFigure = (TextView) convertView.findViewById(R.id.tv_figure_sum);
            holder.tvTypename = (TextView) convertView.findViewById(R.id.tv_typename_sum);
            holder.tvPercent = (TextView) convertView.findViewById(R.id.tv_percent_sum);
            convertView.setTag(holder);
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }
        holder.ivTypecolor.setColorFilter(mColors.get(position));
        holder.tvTypename.setText(mtypenames.get(position));
        holder.tvFigure.setText("￥" + mSum.get(position).toString());

        Double percent = mSum.get(position) * 100 / mSumExpense;
        int ceilPercent = (int) Math.ceil(percent);
        holder.tvPercent.setText(ceilPercent + "%");

        return convertView;
    }

    public class MyViewHolder {

        private ImageView ivTypecolor;
        private TextView tvTypename;
        private TextView tvFigure;
        private TextView tvPercent;

    }
}
