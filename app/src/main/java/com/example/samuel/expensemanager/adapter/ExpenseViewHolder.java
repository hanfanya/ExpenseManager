package com.example.samuel.expensemanager.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.samuel.expensemanager.R;

/**
 * Created by Samuel on 15/11/18.
 */
public class ExpenseViewHolder extends RecyclerView.ViewHolder {
    public ImageView mIvTypeColor;
    public TextView mTvCircle;
    public TextView mTvTypeName;

    public ExpenseViewHolder(View itemView) {
        super(itemView);

        mIvTypeColor = (ImageView) itemView.findViewById(R.id.iv_type);
        mTvCircle = (TextView) itemView.findViewById(R.id.tv_circle);
        mTvTypeName = (TextView) itemView.findViewById(R.id.tv_type);

    }
}
