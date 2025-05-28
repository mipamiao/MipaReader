package com.example.mipareader.DATA;

import java.io.Serializable;
import java.util.regex.Pattern;

import com.example.mipareader.DATA.Repository.Bookmark;
import com.example.mipareader.DATA.Repository.Section;
public class Chapter implements Serializable {
    private int id;
    private long position;
    private String name;
    private static final Pattern CHAPTER_PATTERN =
            Pattern.compile("第[0123456789零一二三四五六七八九十百千万]+[节章]");

    public Chapter(long position, String name, int id ) {
        this.position = position;
        this.name = name;
        this.id = id;
    }
    public Chapter(long position, String name ) {
        this.position = position;
        this.name = name;
    }

    public int getId(){return id;}
    public void setId(int id){this.id = id;}

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static boolean isChapter(String text) {
        return CHAPTER_PATTERN.matcher(text).find();
    }

    public static Pattern getChapterPattern(){
        return CHAPTER_PATTERN;
    }

    public static Chapter fromSection(Section section){
        return new Chapter(section.getPosition(), section.getName(), section.getId());
    }
    public static Section toSection(Chapter chapter){
        return new Section(chapter.getPosition(), chapter.getName());
    }

    public static Chapter fromBookmark(Bookmark bookmark){
        return new Chapter(bookmark.getPosition(), bookmark.getName(), bookmark.getId());
    }
    public static Bookmark toBookmark(Chapter chapter){
        return new Bookmark(chapter.getPosition(), chapter.getName());
    }
}