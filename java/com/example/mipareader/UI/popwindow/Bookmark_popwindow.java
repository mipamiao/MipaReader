package com.example.mipareader.UI.popwindow;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mipareader.DATA.DataSet;
import com.example.mipareader.UI.adapter.DirAdapter;
import com.example.mipareader.Utils.IndirectClass;
import com.example.mipareader.R;
import com.example.mipareader.UI.MainActivity;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

public class Bookmark_popwindow {
    public IndirectClass IC;
    public Bookmark_popwindow(IndirectClass ic){
        IC = ic;
        View Bookmark_view = LayoutInflater.from(IC.getContext()).inflate(R.layout.dir_popwindow,null);
        FastScrollRecyclerView rv = Bookmark_view.findViewById(R.id.recycler);
        Log.e("454", "Bookmark_popwindow: "+IC.getNovelWindow().GetCheapterIndex() );
        DirAdapter da = new DirAdapter(IC.getNovelWindow().DT.getNovelBookmark().getBookmarks(), IC);
        rv.setAdapter(da);
        rv.setLayoutManager(new LinearLayoutManager(IC.getContext()));
        My_PopupWindow dir_popwindow = new My_PopupWindow(Bookmark_view, IC.getNovelWindow().GetTVWidth()/2,
                ViewGroup.LayoutParams.WRAP_CONTENT,false);
        dir_popwindow.setAnimationStyle(R.style.dir_pop_animation);
        dir_popwindow.showAtLocation(IC.getNovelWindow().getTV(), Gravity.LEFT|Gravity.TOP,0,0);
        rv.scrollToPosition(IC.getNovelWindow().GetCheapterIndex());
        da.SetOnClickListener(position -> {
            IC.getNovelWindow().ReShowChapter(da.getItem(position).getPosition());
            //DataSet.getInstance(null).save();
            dir_popwindow.request_dismiss();
        });
    }
}
