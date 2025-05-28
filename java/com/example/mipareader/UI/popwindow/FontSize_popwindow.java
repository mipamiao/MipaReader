package com.example.mipareader.UI.popwindow;


import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;

import android.widget.TextView;

import com.example.mipareader.Utils.IndirectClass;
import com.example.mipareader.R;

public class FontSize_popwindow {
    IndirectClass IC;
    public FontSize_popwindow(IndirectClass IC){
        View fontsize_view = LayoutInflater.from(IC.getContext()).inflate(R.layout.fontsize_popwindow,null);
        My_PopupWindow fontsize_popwindow = new My_PopupWindow(
                fontsize_view, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,false);
        fontsize_popwindow.setAnimationStyle(R.style.pop_animation);
        TextView showsize = fontsize_view.findViewById(R.id.ShowSize);
        ImageView AddBut = fontsize_view.findViewById(R.id.AddSize);
        ImageView SubBut = fontsize_view.findViewById(R.id.SubSize);
        showsize.setText(String.valueOf(IC.getNovelWindow().getTV().getTextSize()));
        AddBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IC.getNovelWindow().AddFontSize();
                showsize.setText(String.valueOf(IC.getNovelWindow().getTV().getTextSize()));
            }
        });
        SubBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IC.getNovelWindow().SubFontSize();
                showsize.setText(String.valueOf(IC.getNovelWindow().getTV().getTextSize()));
            }
        });
        fontsize_popwindow.showAtLocation(IC.getNovelWindow().getTV(), Gravity.CENTER|Gravity.BOTTOM,0,0);
    }
}
