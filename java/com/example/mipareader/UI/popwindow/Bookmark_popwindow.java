package com.example.mipareader.UI.popwindow;

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

public class Bookmark_popwindow {

    public Bookmark_popwindow(NovelWindow novelWindow){
        View Bookmark_view = LayoutInflater.from(MyApp.getInstance().getApplicationContext()).inflate(R.layout.dir_popwindow,null);
        FastScrollRecyclerView rv = Bookmark_view.findViewById(R.id.recycler);
        //Log.e("454", "Bookmark_popwindow: "+IC.getNovelWindow().GetCheapterIndex() );
        DirAdapter da = new DirAdapter(novelWindow.getPresenter().getBookmark().getBookmarks());
        rv.setAdapter(da);
        rv.setLayoutManager(new LinearLayoutManager(MyApp.getInstance().getApplicationContext()));
        My_PopupWindow dir_popwindow = new My_PopupWindow(Bookmark_view, novelWindow.getTV().getWidth()/2,
                ViewGroup.LayoutParams.WRAP_CONTENT,false);
        dir_popwindow.setAnimationStyle(R.style.dir_pop_animation);
        dir_popwindow.showAtLocation(novelWindow.getTV(), Gravity.LEFT|Gravity.TOP,0,0);
        rv.scrollToPosition(novelWindow.getPresenter().getChapterIndex());
        da.SetOnClickListener(position -> {
            novelWindow.getPresenter().reshowChapter(da.getItem(position).getPosition());
            //DataSet.getInstance(null).save();
            dir_popwindow.request_dismiss();
        });
    }
}
