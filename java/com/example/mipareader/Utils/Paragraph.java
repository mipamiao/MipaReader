package com.example.mipareader.Utils;

public class Paragraph {
    private long pos;
    private String content;

    public long getPos() {
        return pos;
    }

    public void setPos(long pos) {
        this.pos = pos;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public Paragraph(long pos, String line){
        this.pos = pos;
        this.content = line;
    }
}
