package com.example.mipareader;

import android.animation.ValueAnimator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class BookTouchAndClick implements Book_OP{

    IndirectClass IC;
    public BookTouchAndClick(IndirectClass ic){
        IC = ic;
    }
    @Override
    public void onItemClick(int position) {
        IC.getActivity().OpenNovelWindow(position);
    }

    @Override
    public void onItemLeftSlide(int dx, int MaxScrollPos, View itemView, MotionEvent event) {
        int left_termine = MaxScrollPos - itemView.getWidth();
        ValueAnimator animator;
        animator = ValueAnimator.ofFloat(0.1f,left_termine);
        animator.setDuration(250);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 获取当前动画的值
                float value = (float) animation.getAnimatedValue();
                itemView.scrollTo((int) value, (int)itemView.getScrollY());
/*                Log.e("TAG", "onAnimationUpdate: scrolly" +itemView.getScrollY()
                        +" scrollx:"+itemView.getScrollX());*/
            }
        });
        animator.start();
        /*Log.e("TAG", "onTouch: up " + "x:"+event.getX()+"y:"+event.getY());*/
    }

    @Override
    public void onItemRightSlide(int dx,int MaxScrollPos, View itemView,MotionEvent event) {
        int left_termine = MaxScrollPos - itemView.getWidth();
        ValueAnimator animator;
        animator = ValueAnimator.ofFloat(left_termine-0.1f,0);
        animator.setDuration(250);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 获取当前动画的值
                float value = (float) animation.getAnimatedValue();
                itemView.scrollTo((int) value, (int)itemView.getScrollY());
                /*Log.e("TAG", "onAnimationUpdate: scrolly" +itemView.getScrollY()
                        +" scrollx:"+itemView.getScrollX());*/
            }
        });
        animator.start();
        /*Log.e("TAG", "onTouch: up " + "x:"+event.getX()+"y:"+event.getY());*/
    }
}
