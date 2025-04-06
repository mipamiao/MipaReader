package com.example.mipareader;

import android.view.MotionEvent;
import android.view.View;

public interface Book_OP {
    void onItemClick(int position);
    void onItemLeftSlide(int dx, int MaxScrollPos, View itemView, MotionEvent event);
    void onItemRightSlide(int dx, int MaxScrollPos, View itemView, MotionEvent event);
}
