package com.example.samuel.expensemanager.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.samuel.expensemanager.R;
import com.example.samuel.expensemanager.model.Expense;
import com.example.samuel.expensemanager.utils.CalUtils;
import com.example.samuel.expensemanager.view.SwipeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Samuel on 15/12/14 18:29
 * Email:xuzhou40@gmail.com
 * desc:
 */
public class SearchResultListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public OnItemClickListener mOnItemClickListener;
    private List<Expense> mExpenseList;
    private ArrayList<SwipeLayout> mOpendItems;

    public SearchResultListAdapter(List<Expense> expenseList) {
        mExpenseList = expenseList;
        mOpendItems = new ArrayList<SwipeLayout>();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_search, parent, false);

        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final SearchViewHolder viewHolder = (SearchViewHolder) holder;
        Expense expense = mExpenseList.get(position);
        viewHolder.mTvExpense.setText(expense.getTypeName());
        viewHolder.mTvDate.setText(CalUtils.getFormatDisplayDate(expense.getDate()));
        viewHolder.mTvFigure.setText(expense.getFigure() + "");

        final SwipeLayout swipeLayout = (SwipeLayout) viewHolder.itemView;
        swipeLayout.setSwipeLayoutListener(new SwipeLayout.OnSwipeLayoutListener() {

            @Override
            public void onStartOpen(SwipeLayout mSwipeLayout) {
                // 要去开启时,先遍历所有已打开条目, 逐个关闭

                for (SwipeLayout layout : mOpendItems) {
                    layout.close();
                }

                mOpendItems.clear();
            }

            @Override
            public void onStartClose(SwipeLayout mSwipeLayout) {

            }

            @Override
            public void onOpen(SwipeLayout mSwipeLayout) {

                // 添加进集合
                mOpendItems.add(mSwipeLayout);
            }

            @Override
            public void onDraging(SwipeLayout mSwipeLayout) {
            }

            @Override
            public void onClose(SwipeLayout mSwipeLayout) {
                // 移除集合
                mOpendItems.remove(mSwipeLayout);
            }
        });

        if (mOnItemClickListener != null) {
            viewHolder.mTvEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = viewHolder.getLayoutPosition();
                    mOnItemClickListener.onItemEditClick(v, pos);
                    swipeLayout.close();
                }
            });
            viewHolder.mTvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = viewHolder.getLayoutPosition();
                    mOnItemClickListener.onItemDeleteClick(v, pos);
                    swipeLayout.close();
//                    Log.i("SearchResultListAdapter", "onItemDeleteClick");

                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return mExpenseList.size();
    }

    public interface OnItemClickListener {
        void onItemEditClick(View view, int position);

        void onItemDeleteClick(View view, int position);
    }

    class SearchViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_delete)
        TextView mTvDelete;
        @Bind(R.id.tv_edit)
        TextView mTvEdit;
        @Bind(R.id.tv_expense)
        TextView mTvExpense;
        @Bind(R.id.tv_date)
        TextView mTvDate;
        @Bind(R.id.tv_figure)
        TextView mTvFigure;

        public SearchViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

}
