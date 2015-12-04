package com.example.samuel.expensemanager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.samuel.expensemanager.R;
import com.example.samuel.expensemanager.model.Expense;

import java.util.List;

/**
 * Created by Samuel on 15/11/12 15:33.
 * Email:samuel40@126.com
 */
public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.MyViewHolder> {
    public OnItemClickListener mOnItemClickListener;
    private List<Expense> mExpenseList;
    private Context mContext;

    public HomeListAdapter(List<Expense> expenseList, Context context) {
        mExpenseList = expenseList;
        mContext = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_home, null);
        MyViewHolder holder = new MyViewHolder(itemLayoutView);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Expense expense = mExpenseList.get(position);

        int[] colorArray = mContext.getResources().getIntArray(R.array.colorType);
        String date = expense.getDate();

        holder.mIvType.setColorFilter(colorArray[expense.getTypeColor()]);
        holder.mTvDate.setText(date.substring(6));
        holder.mTvTypeName.setText(expense.getTypeName());
        holder.mTvFigure.setText(expense.getFigure() + "");

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(v, pos);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemLongClick(v, pos);
                    return false;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mExpenseList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        boolean onItemLongClick(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
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
        }

    }
}
