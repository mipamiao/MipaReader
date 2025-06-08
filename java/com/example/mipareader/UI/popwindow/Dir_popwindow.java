package com.example.mipareader.UI.popwindow;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;


import com.example.mipareader.MyApp;
import com.example.mipareader.UI.NovelWindow;
import com.example.mipareader.UI.adapter.DirAdapter;
import com.example.mipareader.R;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;


public class Dir_popwindow {

    public Dir_popwindow(NovelWindow novelWindow){

        View DirView = LayoutInflater.from(MyApp.getInstance().getApplicationContext()).inflate(R.layout.dir_popwindow,null);
        FastScrollRecyclerView rv = DirView.findViewById(R.id.recycler);
        Log.e("454", "Dir_popwindow: " );

        DirAdapter da = new DirAdapter(novelWindow.getPresenter().getDir().getChapterList());
        rv.setAdapter(da);
        rv.setLayoutManager(new LinearLayoutManager(MyApp.getInstance().getApplicationContext()));
        My_PopupWindow dir_popwindow = new My_PopupWindow(DirView, novelWindow.getTV().getWidth()/2,
         ViewGroup.LayoutParams.WRAP_CONTENT,false);
        dir_popwindow.setAnimationStyle(R.style.dir_pop_animation);
        dir_popwindow.showAtLocation(novelWindow.getTV(), Gravity.LEFT,0,0);
        rv.scrollToPosition(novelWindow.getPresenter().getChapterIndex());
        da.SetOnClickListener(position -> {
            novelWindow.getPresenter().showPage(da.getItem(position).getPosition());
           // DataSet.getInstance(null).save();
            dir_popwindow.request_dismiss();
        });
    }
}
