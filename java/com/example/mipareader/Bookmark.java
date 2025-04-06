package com.example.mipareader;

import java.io.Serializable;
import java.util.ArrayList;

public class Bookmark implements Serializable {
    public ArrayList<Cheapter> AllBookmark;
    public Bookmark(){
        AllBookmark = new ArrayList<>();
    }
    public void AddBookmark(String text,long pos){
        for (int i=0;i<AllBookmark.size();i++)
            if(AllBookmark.get(i).pos==pos)return;
        AllBookmark.add(new Cheapter(pos,text));
    }
    public void DelBookmark(int index){
        if (AllBookmark.size()<=index)return;
        AllBookmark.remove(index);
    }
}
