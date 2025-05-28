package com.example.mipareader.DATA;

import com.example.mipareader.DATA.Repository.BookRepository;
import com.example.mipareader.DATA.Repository.Bookmark;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Bookmarks implements Serializable {
    private ArrayList<Chapter> bookmarks;

    private int book_id;

    public int getBookId(){return book_id;}
    public void setBookId(int book_id){this.book_id = book_id;}

    public Bookmarks() {
        this.bookmarks = new ArrayList<>();
    }
    public Bookmarks(List<Bookmark> bookmarks){
        ArrayList<Chapter> chpaters =  bookmarks.stream()
                .map(Chapter::fromBookmark)
                .collect(Collectors.toCollection(ArrayList::new));
        setBookmarks(chpaters);
    }
    public ArrayList<Chapter> getBookmarks() {
        return bookmarks;
    }

    public void setBookmarks(ArrayList<Chapter> bookmarks) {
        this.bookmarks = bookmarks;
    }

    public void addBookmark(String text, long position) {
        for (Chapter bookmark : bookmarks) {
            if (bookmark.getPosition() == position) {
                return;
            }
        }

        Chapter chapter = new Chapter(position, text);
        BookRepository.getInstance().addBookmark(chapter, book_id);
        bookmarks.add(chapter);
    }

    public void removeBookmark(int index) {
        if (index < 0 || index >= bookmarks.size()) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + bookmarks.size());
        }
        bookmarks.remove(index);
    }

    public boolean safeRemoveBookmark(int index) {
        if (index >= 0 && index < bookmarks.size()) {
            bookmarks.remove(index);
            return true;
        }
        return false;
    }

    public static List<Bookmark> toBookmarks(Bookmarks bookmarks){
        return bookmarks.getBookmarks().stream().map(Chapter::toBookmark).collect(Collectors.toList());
    }
}