package com.example.mipareader.UI.DIYview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ReadView  extends View {

    private ArrayList<String> paragraph;
    private Typeface font;

    Paint paint;

    private int lincCount;

    public float getLineSpaceMul() {
        return lineSpaceMul;
    }

    public void setLineSpaceMul(float lineSpaceMul) {
        this.lineSpaceMul = lineSpaceMul;
    }

    private float lineSpaceMul = 1.2f;

    public ArrayList<String> getParagraph() {
        return paragraph;
    }

    public void setParagraph(ArrayList<String> paragraph) {
        this.paragraph = paragraph;
        invalidate();
    }

    public Typeface getFont() {
        return font;
    }

    public void setFont(Typeface font) {
        this.font = font;
        paint.setTypeface(this.font);
        invalidate();
    }

    public int getLincCount() {
        Log.e(TAG, "getLincCount: " + getHeight());
        int fontHeight = (int)Math.ceil(bottom-top);
        int lineHeight = (int)Math.ceil((bottom-top)*lineSpaceMul);
        lincCount = (int)(1.0f*(getHeight() - fontHeight)/(lineHeight)) + 1;
        return lincCount;
    }

    public void setLincCount(int lincCount) {
        this.lincCount = lincCount;
    }

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
        paint.setTextSize(this.fontSize);
        top = paint.getFontMetrics().top;
        bottom = paint.getFontMetrics().bottom;
        invalidate();
    }

    private float fontSize = 48;

    private float top, bottom;

    private float startX = 0;

    private final static String TAG = "readView";


    public ReadView(Context context) {
        super(context);
    }

    public ReadView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs); // 传递 AttributeSet 到初始化方法
    }

    public ReadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    // init: 11:-50.695312:13.0078125
    private void init(AttributeSet attrs){
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        font = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);
        fontSize = 48;
        startX = 0;
        paint.setTypeface(font);
        paint.setTextSize(fontSize);
        paint.setColor(Color.BLACK);
        top = paint.getFontMetrics().top;
        bottom = paint.getFontMetrics().bottom;
        lincCount = (730/((int)Math.ceil(bottom-top)));
        Log.e(TAG, "init: " + lincCount  + ":" + top + ":" + bottom);
        startX = 0;
        paragraph = new ArrayList<>();
    }
    @Override
    public void onLayout(boolean changed,int left,int top,int right,int bottom){
        super.onLayout(changed,left,top,right,bottom);
        startX = getPaddingStart();
    }


    @Override
    public void onDraw(Canvas canvas){
        float startY = 0;
        for(int i = 0; i<paragraph.size(); i++){
            startY -= top;
            canvas.drawText(paragraph.get(i),startX, startY, paint);
            startY += (bottom + (bottom- top)*(lineSpaceMul-1));
        }
    }

    public float getAvailWidth(){
        return getWidth() - getPaddingStart() - getPaddingEnd();
    }
    public Paint getPaint(){
        return paint;
    }
}
