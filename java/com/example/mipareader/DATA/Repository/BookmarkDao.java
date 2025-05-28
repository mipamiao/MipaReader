package com.example.mipareader.DATA.Repository;

import androidx.room.Dao;
import androidx.room.Insert;

import java.util.List;

@Dao
public interface BookmarkDao {

    @Insert
    void insertBookmark(Bookmark bookmark);
    @Insert
    void insertBookmarks(List<Bookmark> bookmarks);
}
