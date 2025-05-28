package com.example.mipareader.UI.popwindow;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mipareader.UI.adapter.FontTypeAdapter;
import com.example.mipareader.Utils.IndirectClass;
import com.example.mipareader.R;

public class FontType_popwindow {
    public IndirectClass IC;
    public FontType_popwindow(IndirectClass ic ){
        IC = ic;
        View fonttype_popview = LayoutInflater.from(IC.getContext()).inflate(R.layout.fonttype_popwindow , null);
        My_PopupWindow fonttype_popwindow = new My_PopupWindow(fonttype_popview,ViewGroup.LayoutParams.MATCH_PARENT,
                IC.getNovelWindow().GetTVHieght()/3, false);
        fonttype_popwindow.setAnimationStyle(R.style.pop_animation);
        RecyclerView rv = fonttype_popview.findViewById(R.id.FontTypeDir);
        FontTypeAdapter fta = new FontTypeAdapter(IC.getNovelWindow().getFontMap());
        fta.SetOnClickListener(position -> IC.getNovelWindow().ChangeType(fta.getItem(position),true));
        rv.setAdapter(fta);
        rv.setLayoutManager(new LinearLayoutManager(IC.getContext()));
        fonttype_popwindow.showAtLocation(IC.getNovelWindow().getTV(), Gravity.BOTTOM,0,0);
    }
}
