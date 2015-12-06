package com.example.samuel.expensemanager.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.samuel.expensemanager.R;

/**
 * Created by Barry on 2015/12/5 0005.
 */
public class ClickView extends RelativeLayout {

    private TextView mTime;
    private TextView mTitle;


    public ClickView(Context context) {
        super(context);

    }

    public ClickView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 将自定义控件的布局渲染成View
        View view = View.inflate(getContext(), R.layout.custom_view, this);
        mTitle = (TextView) findViewById(R.id.tv_remind_title);// 标题
        mTime = (TextView) findViewById(R.id.tv_remind_time);// 内容
    }

    public ClickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }


    // 设置TextView的显示内容
    public void setTitleText(String text) {
        mTitle.setText(text);
    }

    //
    public void setTextViewText(String text) {
        mTime.setText(text);
    }

}
