package com.example.mipareader.DATA.File;

import java.util.LinkedList;

public class NovelContent {

    private String filePath;
    private int lastReadPos;
    private LinkedList<String> que;
    public NovelContent(String filePath, int lastReadPos){
        this.filePath = filePath;
        this.lastReadPos = lastReadPos;
        que = new LinkedList<>();
    }

    public String getParagraph(){
        return null;
    }
    private void fillQue(){

    }

}
