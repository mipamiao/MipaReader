package com.example.mipareader.UI.popwindow;


import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.mipareader.MyApp;
import com.example.mipareader.UI.NovelWindow;
import com.example.mipareader.R;


public class setting_popwindow {

    public setting_popwindow(NovelWindow novelWindow){

        View Setting_View  = LayoutInflater.from(MyApp.getInstance().getApplicationContext()).inflate(R.layout.setting_popwindow,null);
        My_PopupWindow setting_window = new My_PopupWindow(Setting_View ,ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,false);
        setting_window.setAnimationStyle(R.style.pop_animation);
        setting_window.showAtLocation(novelWindow.getTV(),Gravity.BOTTOM,0,0);
        ImageView Dir_but = Setting_View.findViewById(R.id.Dir_img);
        Dir_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("tag", "onClick: ");
                new Dir_popwindow(novelWindow);
            }
        });

        ImageView FontSize_but = Setting_View.findViewById(R.id.FontSize_img);
        FontSize_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FontSize_popwindow(novelWindow);
            }
        });

        ImageView FontType_but = Setting_View.findViewById(R.id.FontType_img);
        FontType_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FontType_popwindow(novelWindow);
            }
        });
        ImageView Bookmark_but = Setting_View.findViewById(R.id.Bookmark);
        Bookmark_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {new Bookmark_popwindow(novelWindow); }
        });

        View top_view = LayoutInflater.from(MyApp.getInstance().getApplicationContext()).inflate(R.layout.setting_popupwindow_top,null);
        PopupWindow top_popup = new PopupWindow(top_view ,ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,false);
        setting_window.AddPopup(top_popup);
        top_popup.setAnimationStyle(R.style.setting_top_animation);
        top_popup.showAtLocation(novelWindow.getTV(),Gravity.TOP,0,0);
        ImageView back_but = top_view.findViewById(R.id.setting_return);
        back_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                novelWindow.onBackPressed();
            }
        });
        ImageView add_bookmark_but = top_view.findViewById(R.id.setting_addbookmark);
        add_bookmark_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               novelWindow.getPresenter().addBookmark();
                Toast.makeText(MyApp.getInstance().getApplicationContext(),"ok", Toast.LENGTH_LONG).show();
            }
        });
    }
}
