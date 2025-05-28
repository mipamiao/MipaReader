package com.example.mipareader.DATA.Repository;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class BookWithSectionsAndBookmarks {
    @Embedded
    public Book book;

    @Relation(
            parentColumn = "id",
            entityColumn = "book_id",
            entity = Section.class
    )
    public List<Section> sections;

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    public List<Bookmark> getBookmarks() {
        return bookmarks;
    }

    public void setBookmarks(List<Bookmark> bookmarks) {
        this.bookmarks = bookmarks;
    }

    @Relation(
            parentColumn = "id",
            entityColumn = "book_id",
            entity = Bookmark.class
    )
    public List<Bookmark> bookmarks;

    public BookWithSectionsAndBookmarks(){}



    public int getTotalSections() {
        return sections != null ? sections.size() : 0;
    }

    public int getTotalBookmarks() {
        return bookmarks != null ? bookmarks.size() : 0;
    }
}