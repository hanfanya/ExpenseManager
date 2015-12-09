package com.example.samuel.expensemanager.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Samuel on 15/12/2 19:30
 * Email:xuzhou40@gmail.com
 * desc:自定义圆形进度条
 */
public class CircleProgress extends View {
    private RectF mRectF;
    private Paint mBackPaint;
    private Paint mFrontPaint;
    private int mProgress = 0;
    private int mTargetProgress;
    private int mMax = 100;


    public CircleProgress(Context context) {
        super(context);
        initPaint();//初始化画笔
    }

    public CircleProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();

    }

    public CircleProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        mRectF = new RectF();

        mBackPaint = new Paint();
        mBackPaint.setColor(Color.GRAY);
        mBackPaint.setAntiAlias(true);

        mFrontPaint = new Paint();
        mFrontPaint.setColor(Color.parseColor("#8BC34A"));
        mFrontPaint.setAntiAlias(true);

    }

    public void setTargetProgress(int targetProgress) {
        mTargetProgress = targetProgress;//设置目标值
        mProgress = 0;
        if (mTargetProgress >= 100) {
            mFrontPaint.setColor(Color.parseColor("#FF9800"));

        } else if (mTargetProgress >= 80) {
            mFrontPaint.setColor(Color.parseColor("#FFC107"));

        } else if (mTargetProgress >= 60) {
            mFrontPaint.setColor(Color.parseColor("#00BCD4"));
        } else if (mTargetProgress >= 40) {
            mFrontPaint.setColor(Color.parseColor("#4CAF50"));
        } else if (mTargetProgress >= 20) {
            mFrontPaint.setColor(Color.parseColor("#009688"));
        } else {
            mFrontPaint.setColor(Color.parseColor("#8BC34A"));
        }
        invalidate();

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mRectF.set(0, 0, MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(widthMeasureSpec));
        setMeasuredDimension(widthMeasureSpec, widthMeasureSpec);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Log.e("XXXXXZZZZZZ","onDraw");

        float yHeight = ((float) getProgress() / getMax() * getHeight());
        float radius = getWidth() / 2f;
        float angle = (float) (Math.acos((radius - yHeight) / radius) * 180 / Math.PI);

        canvas.drawArc(mRectF, 90, 360, false, mBackPaint);//画背景的圆
        canvas.drawArc(mRectF, 90 - angle, angle * 2, false, mFrontPaint);//画上方的圆
        // Log.e("XXXXXZZZZZZ", "mProgress"+mProgress+"==="+"mTargetProgress="+mTargetProgress);

        if (mProgress < mTargetProgress) {
            mProgress = mProgress + 1;
            invalidate();
        }
    }

    public int getProgress() {
        return mProgress;
    }

    public void setProgress(int progress) {
        this.mProgress = progress;
        if (this.mProgress > getMax()) {
            this.mProgress %= getMax();
        }
        invalidate();
    }

    public int getMax() {
        return mMax;
    }
}

