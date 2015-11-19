package com.example.samuel.expensemanager.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.samuel.expensemanager.R;
import com.example.samuel.expensemanager.model.TypeInfo;

import java.util.List;

/**
 * Created by Samuel on 15/11/18.
 */
public class ExpenseRecyclerViewAdapter extends RecyclerView.Adapter<ExpenseViewHolder> {
    public OnItemClickListener mOnItemClickListener;
    private List<TypeInfo> mTypeInfos;
    private Context mContext;
    private int clickTemp = -1;

    public ExpenseRecyclerViewAdapter(List<TypeInfo> typeInfos, Context context) {
        mTypeInfos = typeInfos;
        mContext = context;
    }

    public void setSelection(int position) {
        clickTemp = position;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public ExpenseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_type, null);
        ExpenseViewHolder viewHolder = new ExpenseViewHolder(layoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ExpenseViewHolder holder, int position) {
        TypeInfo typeInfo = mTypeInfos.get(position);
        int[] colorArray = mContext.getResources().getIntArray(R.array.colorType);

        holder.mIvTypeColor.setColorFilter(colorArray[typeInfo.getTypeColor()]);
        holder.mTvCircle.setText(typeInfo.getTypeName().substring(0, 1));
        holder.mTvTypeName.setText(typeInfo.getTypeName());

        if (clickTemp == position) {
            holder.itemView.setBackgroundColor(Color.parseColor("#B6B6B6"));
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView, pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemLongClick(holder.itemView, pos);
                    return false;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mTypeInfos.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }


}
