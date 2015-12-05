package com.example.samuel.expensemanager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.samuel.expensemanager.R;

/**
 * Created by Samuel on 15/12/5 19:39
 * Email:xuzhou40@gmail.com
 * desc:颜色选择器adapter
 */
public class SelectColorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public OnItemClickListener mOnItemClickListener;
    private int[] mColorArray;
    private Context mContext;

    public SelectColorAdapter(Context context) {
        mContext = context;
        mColorArray = mContext.getResources().getIntArray(R.array.colorType);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_color, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MyViewHolder viewHolder = (MyViewHolder) holder;
        viewHolder.mImageView.setColorFilter(mColorArray[position]);

        if (mOnItemClickListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = viewHolder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(v, pos);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mColorArray.length;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.iv_color);
        }
    }
}
