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

    @Relation(
            parentColumn = "id",
            entityColumn = "book_id",
            entity = Bookmark.class
    )
    public List<Bookmark> bookmarks;


    public int getTotalSections() {
        return sections != null ? sections.size() : 0;
    }

    public int getTotalBookmarks() {
        return bookmarks != null ? bookmarks.size() : 0;
    }
}