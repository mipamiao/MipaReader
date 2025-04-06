package com.example.mipareader;
import android.animation.ValueAnimator;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import androidx.annotation.NonNull;

public class setting_pop_test {
    public IndirectClass IC;
    public static boolean HaveSettingWindow = false;
    public static PopupWindow window = null;
    public setting_pop_test(IndirectClass ic){
        if(HaveSettingWindow){
            HaveSettingWindow = false;
            CloseSettingWindow();
            return;
        }
        IC = ic;
        View Setting_View  = LayoutInflater.from(IC.getContxt()).inflate(R.layout.setting_popwindow,null);
        PopupWindow test = new PopupWindow(Setting_View, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,true);
        window = test;
        HaveSettingWindow = true;
        test.setFocusable(false);
        test.setAttachedInDecor(true);
        test.setAnimationStyle(R.style.pop_animation);
        test.showAtLocation(IC.getNovelWindow().tv,Gravity.BOTTOM,0,0);
        Setting_View.measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED);
        int bottom = Setting_View.getMeasuredHeight();
        Log.e("TAG", "setting_popwindow: "+ bottom);
        //Setting_View.setTranslationY(bottom);
        ValueAnimator va = ValueAnimator.ofInt( bottom, 0);
        va.setDuration(500);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                //Setting_View.setTranslationY(value);
            }
        });
        Setting_View.requestFocus();
        va.start();
        ImageView Dir_but = Setting_View.findViewById(R.id.Dir_img);
        Dir_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("tag", "onClick: " + IC.getNovelWindow().dir.CheapterList.size() );
                new Dir_popwindow(IC);
                test.dismiss();
            }
        });

        ImageView FontSize_but = Setting_View.findViewById(R.id.FontSize_img);
        FontSize_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FontSize_popwindow(IC);
                test.dismiss();
            }
        });

        ImageView FontType_but = Setting_View.findViewById(R.id.FontType_img);
        FontType_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FontType_popwindow(IC);
                test.dismiss();
            }
        });
    }
    public  static void CloseSettingWindow(){
        if(window != null)window.dismiss();
        HaveSettingWindow = false;
    }
}

