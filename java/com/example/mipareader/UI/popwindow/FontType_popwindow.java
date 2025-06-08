package com.example.mipareader.UI.popwindow;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mipareader.MyApp;
import com.example.mipareader.UI.NovelWindow;
import com.example.mipareader.UI.adapter.FontTypeAdapter;
import com.example.mipareader.R;

public class FontType_popwindow {

    public FontType_popwindow(NovelWindow novelWindow){
        View fonttype_popview = LayoutInflater.from(MyApp.getInstance().getApplicationContext()).inflate(R.layout.fonttype_popwindow , null);
        My_PopupWindow fonttype_popwindow = new My_PopupWindow(fonttype_popview,ViewGroup.LayoutParams.MATCH_PARENT,
                novelWindow.getTV().getHeight()/3, false);
        fonttype_popwindow.setAnimationStyle(R.style.pop_animation);
        RecyclerView rv = fonttype_popview.findViewById(R.id.FontTypeDir);
        FontTypeAdapter fta = new FontTypeAdapter(novelWindow.getPresenter().getTypefaceSet().getFontMap());
        fta.SetOnClickListener(position -> novelWindow.getPresenter().changeTypeface(fta.getItem(position),true));
        rv.setAdapter(fta);
        rv.setLayoutManager(new LinearLayoutManager(MyApp.getInstance().getApplicationContext()));
        fonttype_popwindow.showAtLocation(novelWindow.getTV(), Gravity.BOTTOM,0,0);
    }
}
