package com.example.samuel.expensemanager.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;


/**
 * Created by Samuel on 15/12/4 09:17
 * Email:xuzhou40@gmail.com
 * desc:自定义数字显示控件，实现动态显示效果
 */

public class CountView extends TextView {
    private int duration = 800;
    private float number;

    public CountView(Context context) {
        super(context);
    }

    public CountView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CountView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void showNumberWithAnimation(String number) {
        //修改number属性，会调用setNumber方法
        Log.i("CountView", "number= " + number);
        float numberFloat = Float.parseFloat(number);
        Log.i("CountView", "numberFloat= " + numberFloat);

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "number", 0, Float.parseFloat(number));

        objectAnimator.setDuration(duration);
        //加速器，从慢到快到再到慢
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator.start();
    }

    public float getNumber() {
        return number;
    }

    public void setNumber(float number) {
        this.number = number;
        setText(String.format("%.1f", number));
    }
}
