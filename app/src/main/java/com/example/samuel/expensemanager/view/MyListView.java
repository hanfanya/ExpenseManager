package com.example.samuel.expensemanager.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created by WSY on 2015-12-05-0005.
 */
public class MyListView extends ListView {
    public MyListView(Context context) {
        super(context);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private int startX;
    private int startY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        System.out.println("可见的第一个位置： " + getFirstVisiblePosition());
        System.out.println("可见的最后一个位置：" + getLastVisiblePosition());
        /**
         * 那么这里就需要判断下触摸事件的各种状态了 down和move
         */
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                /**
                 * 刚刚按下的话，肯定是不希望父控件拦截的。。否则后面的操作全被拦截了。 并且，只要后面的条件不成立。就是不拦截的。由vp来处理事件。
                 */
                getParent().requestDisallowInterceptTouchEvent(true);
                // 然后记录下：初始坐标
                startX = (int) ev.getRawX();
                startY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int endX = (int) ev.getRawX();
                int endY = (int) ev.getRawY();

                // 如果dx>dy，表示左右滑动
                if (Math.abs(endX - startX) > Math.abs(endY - startY)) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                } else {//下拉
                    if (endY - startY > 0) {
                        if (getFirstVisiblePosition() == 0) {
                            getParent().requestDisallowInterceptTouchEvent(false);
                        } else {
                            getParent().requestDisallowInterceptTouchEvent(true);
                        }
                    } else {//上拉
                        if (getLastVisiblePosition() < 10) {
                            //数据显示还很少的时候，需要父控件操作上拉
                            getParent().requestDisallowInterceptTouchEvent(false);
                        } else {
                            getParent().requestDisallowInterceptTouchEvent(true);
                        }
                    }
                }
                break;
        }

        return super.dispatchTouchEvent(ev);
    }

}
