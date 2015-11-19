package com.example.samuel.expensemanager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.samuel.expensemanager.R;

import java.util.List;

/**
 * Created by Samuel on 15/11/12 15:33.
 * Email:samuel40@126.com
 */
public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.MyViewHolder> {
    private List<Expense> mExpenseList;
    private Context mContext;

    public HomeListAdapter(List<Expense> expenseList, Context context) {
        mExpenseList = expenseList;
        mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_home, null);
        MyViewHolder holder = new MyViewHolder(itemLayoutView);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Expense expense = mExpenseList.get(position);

        int[] colorArray = mContext.getResources().getIntArray(R.array.colorType);
        String date = expense.getDate();

        holder.mIvType.setColorFilter(colorArray[expense.getTypeColor()]);
        holder.mTvDate.setText(date.substring(8));
//        holder.mTvDate.setText(date);
        holder.mTvTypeName.setText(expense.getTypeName());
        holder.mTvFigure.setText(expense.getFigure() + "");

    }

    @Override
    public int getItemCount() {
        return mExpenseList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mIvType;
        TextView mTvDate;
        TextView mTvTypeName;
        TextView mTvFigure;


        public MyViewHolder(View itemView) {
            super(itemView);
            mIvType = (ImageView) itemView.findViewById(R.id.iv_type);
            mTvDate = (TextView) itemView.findViewById(R.id.tv_date);
            mTvTypeName = (TextView) itemView.findViewById(R.id.tv_typename);
            mTvFigure = (TextView) itemView.findViewById(R.id.tv_figure);
            View view = itemView.findViewById(R.id.ll_item_parent);
            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            Log.i("你点击了=========", mTvDate.getText().toString());

        }
    }
}
