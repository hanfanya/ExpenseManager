package com.example.samuel.expensemanager.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.samuel.expensemanager.R;
import com.example.samuel.expensemanager.model.TypeInfo;

import java.util.List;

/**
 * Created by Samuel on 15/11/18.
 */
public class ExpenseRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_type, null);
        ExpenseViewHolder viewHolder = new ExpenseViewHolder(layoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final ExpenseViewHolder viewHolder = (ExpenseViewHolder) holder;
        TypeInfo typeInfo = mTypeInfos.get(position);
        int[] colorArray = mContext.getResources().getIntArray(R.array.colorType);

        viewHolder.mIvTypeColor.setColorFilter(colorArray[typeInfo.getTypeColor()]);
        viewHolder.mTvCircle.setText(typeInfo.getTypeName().substring(0, 1));
        viewHolder.mTvTypeName.setText(typeInfo.getTypeName());

        if (clickTemp == position) {//选中时改变背景颜色
            viewHolder.itemView.setBackgroundColor(Color.parseColor("#B6B6B6"));
        } else {
            viewHolder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }

        if (mOnItemClickListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = viewHolder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(viewHolder.itemView, pos);
                }
            });

            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = viewHolder.getLayoutPosition();
                    mOnItemClickListener.onItemLongClick(viewHolder.itemView, pos);
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

    class ExpenseViewHolder extends RecyclerView.ViewHolder {
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


}
