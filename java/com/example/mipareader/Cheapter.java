package com.example.mipareader;

import java.io.Serializable;
import java.util.regex.Pattern;

public class Cheapter implements Serializable {
    public long pos;
    public String name;
    public static Pattern CheapterPattern = Pattern.compile("第[0123456789零一二三四五六七八九十百千万]+[节章]");
    public Cheapter(long position , String str){
        pos = position;
        name = str;
    }
    public static boolean isCheapter(String in){
        return CheapterPattern.matcher(in).find();
    }
}
