package com.example.mipareader;


import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;


public class setting_popwindow {
    public IndirectClass IC;

    public setting_popwindow(IndirectClass ic){

        IC = ic;
        View Setting_View  = LayoutInflater.from(IC.getContxt()).inflate(R.layout.setting_popwindow,null);
        My_PopupWindow setting_window = new My_PopupWindow(Setting_View ,ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,false);
        setting_window.setAnimationStyle(R.style.pop_animation);
        setting_window.showAtLocation(IC.getNovelWindow().tv,Gravity.BOTTOM,0,0);
        ImageView Dir_but = Setting_View.findViewById(R.id.Dir_img);
        Dir_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("tag", "onClick: " + IC.getNovelWindow().dir.CheapterList.size());
                new Dir_popwindow(IC);
            }
        });

        ImageView FontSize_but = Setting_View.findViewById(R.id.FontSize_img);
        FontSize_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FontSize_popwindow(IC);
            }
        });

        ImageView FontType_but = Setting_View.findViewById(R.id.FontType_img);
        FontType_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FontType_popwindow(IC);
            }
        });
        ImageView Bookmark_but = Setting_View.findViewById(R.id.Bookmark);
        Bookmark_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {new Bookmark_popwindow(IC); }
        });

        View top_view = LayoutInflater.from(IC.getContxt()).inflate(R.layout.setting_popupwindow_top,null);
        PopupWindow top_popup = new PopupWindow(top_view ,ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,false);
        setting_window.AddPopup(top_popup);
        top_popup.setAnimationStyle(R.style.setting_top_animation);
        top_popup.showAtLocation(IC.getNovelWindow().tv,Gravity.TOP,0,0);
        ImageView back_but = top_view.findViewById(R.id.setting_return);
        back_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ic.getNovelWindow().onBackPressed();
            }
        });
        ImageView add_bookmark_but = top_view.findViewById(R.id.setting_addbookmark);
        add_bookmark_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ic.getNovelWindow().AddBookmark();
                Toast.makeText(ic.getContxt(),"ok", Toast.LENGTH_LONG).show();
            }
        });
    }
}
