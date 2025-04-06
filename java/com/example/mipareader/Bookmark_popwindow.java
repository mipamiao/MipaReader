package com.example.mipareader;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.zip.Inflater;

public class Bookmark_popwindow {
    public IndirectClass IC;
    public Bookmark_popwindow(IndirectClass ic){
        IC = ic;
        View Bookmark_view = LayoutInflater.from(IC.getContxt()).inflate(R.layout.dir_popwindow,null);
        FastScrollRecyclerView rv = Bookmark_view.findViewById(R.id.recycler);
        Log.e("454", "Bookmark_popwindow: "+IC.getNovelWindow().GetCheapterIndex() );
        DirAdapter da = new DirAdapter(IC.getNovelWindow().DT.NovelBookmark.AllBookmark, IC);
        rv.setAdapter(da);
        rv.setLayoutManager(new LinearLayoutManager(IC.getContxt()));
        My_PopupWindow dir_popwindow = new My_PopupWindow(Bookmark_view, IC.getNovelWindow().GetTVWidth()/2,
                ViewGroup.LayoutParams.WRAP_CONTENT,false);
        dir_popwindow.setAnimationStyle(R.style.dir_pop_animation);
        dir_popwindow.showAtLocation(IC.getNovelWindow().tv, Gravity.LEFT|Gravity.TOP,0,0);
        rv.scrollToPosition(IC.getNovelWindow().GetCheapterIndex());
        da.SetOnClickListener(position -> {
            IC.getNovelWindow().ReShowChapter(da.getItem(position).pos);
            DataSet.Save(MainActivity.DS,IC);
            dir_popwindow.request_dismiss();
        });
    }
}
