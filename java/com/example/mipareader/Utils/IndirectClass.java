package com.example.mipareader.Utils;

import android.content.Context;

import com.example.mipareader.UI.MainActivity;
import com.example.mipareader.UI.NovelWindow;

public class IndirectClass {

    private Context context;
    private MainActivity activity;
    private NovelWindow novelWindow;

    public IndirectClass(Context context, MainActivity activity) {
        this(context, activity, null);
    }

    public IndirectClass(Context context, MainActivity activity, NovelWindow novelWindow) {
        this.context = context;
        this.activity = activity;
        this.novelWindow = novelWindow;
    }
    private IndirectClass(){};

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public MainActivity getActivity() {
        return activity;
    }

    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }

    public NovelWindow getNovelWindow() {
        return novelWindow;
    }

    public void setNovelWindow(NovelWindow novelWindow) {
        this.novelWindow = novelWindow;
    }



    private static  class IndirectClassHolder{
        private static final IndirectClass instance = new IndirectClass();
    }
    public  static IndirectClass getInstance(){
        return IndirectClassHolder.instance;
    }
}