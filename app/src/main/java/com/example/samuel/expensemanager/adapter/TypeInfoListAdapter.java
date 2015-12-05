package com.example.samuel.expensemanager.adapter;

import android.content.Context;
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
 * Created by Samuel on 15/12/5 09:27
 * Email:xuzhou40@gmail.com
 * desc:类别管理列表
 */

public class TypeInfoListAdapter extends RecyclerView.Adapter<TypeInfoListAdapter.MyViewHolder> {
    public OnItemClickListener mOnItemClickListener;
    private List<TypeInfo> mTypeInfoList;
    private Context mContext;

    public TypeInfoListAdapter(List<TypeInfo> expenseList, Context context) {
        mTypeInfoList = expenseList;
        mContext = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_typeinfo, null);
        MyViewHolder holder = new MyViewHolder(itemLayoutView);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        TypeInfo typeInfo = mTypeInfoList.get(position);

        int[] colorArray = mContext.getResources().getIntArray(R.array.colorType);

        holder.mIvType.setColorFilter(colorArray[typeInfo.getTypeColor()]);
        holder.mTvTypeName.setText(typeInfo.getTypeName());

        if (mOnItemClickListener != null) {
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
        return mTypeInfoList.size();
    }

    public interface OnItemClickListener {
        boolean onItemLongClick(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView mIvType;
        TextView mTvTypeName;


        public MyViewHolder(View itemView) {
            super(itemView);
            mIvType = (ImageView) itemView.findViewById(R.id.iv_type);
            mTvTypeName = (TextView) itemView.findViewById(R.id.tv_typename);
        }

    }
}
