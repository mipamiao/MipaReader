package com.example.mipareader;


import android.content.Context;

public class IndirectClass {
    private Context contxt;
    private MainActivity activity;
    private NovelWindow novelWindow;

    public Context getContxt() {
        return contxt;
    }

    public void setContxt(Context contxt) {
        this.contxt = contxt;
    }

    public MainActivity getActivity() {
        return  activity;
    }

    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }
    public void setNovelWindow(NovelWindow novelWindow){this.novelWindow = novelWindow;}

    public NovelWindow getNovelWindow(){return novelWindow; }

    public IndirectClass(Context context, MainActivity activity) {
        this.setContxt(context);
        this.setActivity(activity);
    }
    public IndirectClass(Context context, MainActivity activity,NovelWindow novelWindow) {
        this.setContxt(context);
        this.setActivity(activity);
        this.setNovelWindow(novelWindow);
    }
}
