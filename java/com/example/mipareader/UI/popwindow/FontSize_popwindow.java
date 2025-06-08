package com.example.mipareader.UI.popwindow;


import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;

import android.widget.TextView;

import com.example.mipareader.MyApp;
import com.example.mipareader.UI.NovelWindow;
import com.example.mipareader.R;

public class FontSize_popwindow {
    public FontSize_popwindow(NovelWindow novelWindow){
        View fontsize_view = LayoutInflater.from(MyApp.getInstance().getApplicationContext()).inflate(R.layout.fontsize_popwindow,null);
        My_PopupWindow fontsize_popwindow = new My_PopupWindow(
                fontsize_view, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,false);
        fontsize_popwindow.setAnimationStyle(R.style.pop_animation);
        TextView showsize = fontsize_view.findViewById(R.id.ShowSize);
        ImageView AddBut = fontsize_view.findViewById(R.id.AddSize);
        ImageView SubBut = fontsize_view.findViewById(R.id.SubSize);
        showsize.setText(String.valueOf(novelWindow.getTV().getFontSize()));
        AddBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                novelWindow.getPresenter().addFont();
                showsize.setText(String.valueOf(novelWindow.getTV().getFontSize()));
            }
        });
        SubBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                novelWindow.getPresenter().subFont();
                showsize.setText(String.valueOf(novelWindow.getTV().getFontSize()));
            }
        });
        fontsize_popwindow.showAtLocation(novelWindow.getTV(), Gravity.CENTER|Gravity.BOTTOM,0,0);
    }
}
