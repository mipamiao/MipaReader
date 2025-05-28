package com.example.mipareader.UI.DIYview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.example.mipareader.R;

public class MyCircleProgress extends View {

    private static final String TAG = "MyCircleProgress";

    private final Paint _paint;
    private final RectF _rectF;
    private final Rect _rect;
    private int _current = 1, _max = 100;
    //圆弧（也可以说是圆环）的宽度
    private final float _arcWidth = 30;
    //控件的宽度
    private float _width;

    public MyCircleProgress(Context context) {
        this(context, null);
    }

    public MyCircleProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyCircleProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        _paint = new Paint();
        _paint.setAntiAlias(true);
        _rectF = new RectF();
        _rect = new Rect();
    }

    public void SetCurrent(int _current) {
        Log.i(TAG, "当前值：" + _current + "，最大值：" + _max);
        this._current = _current;
        invalidate();
    }

    public void SetMax(int _max) {
        this._max = _max;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //getMeasuredWidth获取的是view的原始大小，也就是xml中配置或者代码中设置的大小
        //getWidth获取的是view最终显示的大小，这个大小不一定等于原始大小
        _width = getMeasuredWidth();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制圆形
        _paint.setStyle(Paint.Style.STROKE);

        _paint.setStrokeWidth(_arcWidth);
        _paint.setColor(Color.GRAY);

        float bigCircleRadius = _width / 2;

        float smallCircleRadius = bigCircleRadius - _arcWidth;

        canvas.drawCircle(bigCircleRadius, bigCircleRadius, smallCircleRadius, _paint);
        _paint.setColor(ContextCompat.getColor( getContext(), R.color.spring_green));
        _rectF.set(_arcWidth, _arcWidth, _width - _arcWidth, _width - _arcWidth);

        canvas.drawArc(_rectF, 90, (float) (_current * 360) / _max, false, _paint);

        String txt = _current * 100 / _max + "%";
        _paint.setStrokeWidth(0);
        _paint.setTextSize(40);
        _paint.getTextBounds(txt, 0, txt.length(), _rect);
        _paint.setColor(ContextCompat.getColor( getContext(),R.color.spring_green));

        canvas.drawText(txt, bigCircleRadius - (float) _rect.width() / 2, bigCircleRadius + (float) _rect.height() / 2, _paint);
    }

}