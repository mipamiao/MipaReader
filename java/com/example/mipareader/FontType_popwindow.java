package com.example.mipareader;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FontType_popwindow {
    public IndirectClass IC;
    public FontType_popwindow(IndirectClass ic ){
        IC = ic;
        View fonttype_popview = LayoutInflater.from(IC.getContxt()).inflate(R.layout.fonttype_popwindow , null);
        My_PopupWindow fonttype_popwindow = new My_PopupWindow(fonttype_popview,ViewGroup.LayoutParams.MATCH_PARENT,
                IC.getNovelWindow().GetTVHieght()/3, false);
        fonttype_popwindow.setAnimationStyle(R.style.pop_animation);
        RecyclerView rv = fonttype_popview.findViewById(R.id.FontTypeDir);
        FontTypeAdapter fta = new FontTypeAdapter(IC.getNovelWindow().FontMap);
        fta.SetOnClickListener(position -> IC.getNovelWindow().ChangeType(fta.getItem(position),true));
        rv.setAdapter(fta);
        rv.setLayoutManager(new LinearLayoutManager(IC.getContxt()));
        fonttype_popwindow.showAtLocation(IC.getNovelWindow().tv, Gravity.BOTTOM,0,0);
    }
}
