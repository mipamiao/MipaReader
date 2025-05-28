package com.example.mipareader.UI.event;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class NovelViewTouchListener implements View.OnTouchListener {

    private final OnTouchActionListener listener;

    public NovelViewTouchListener(OnTouchActionListener listener) {
        this.listener = listener;
    }
    private final String TAG = "NovelViewTouchListener";
    private boolean onemove = false;
    private float startX = 0;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                onemove = true;
                startX = event.getX();
                Log.e(TAG, "onTouch: down" + "x:"+event.getX()+"y:"+event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaX = event.getX() - startX;
                if(onemove){
                    if (deltaX > 0) listener.onLastPage(v);
                    else if (deltaX < 0) listener.onNextPage(v);
                    onemove = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                if(startX == event.getX())listener.onClick(v,event);
                Log.e(TAG, "onTouch: up" + "x:"+event.getX()+"y:"+event.getY());
        }
        return true; // 返回 true 表示消耗了触摸事件
    }


    public interface OnTouchActionListener {
        void onLastPage(View v);
        void onNextPage(View v);
        void onClick(View v, MotionEvent event);
    }
}
