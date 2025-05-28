package com.example.mipareader.DATA.Repository;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BookDao {

    @Transaction
    @Query("SELECT * FROM books WHERE id = :bookId")
    BookWithSectionsAndBookmarks getBookWithDetails(int bookId);

    @Transaction
    @Query("SELECT * FROM books")
    List<BookWithSectionsAndBookmarks> getAllBooksWithDetails();

    @Query("SELECT * FROM sections WHERE book_id = :bookId")
    List<Section> getSectionsForBook(int bookId);

    @Query("SELECT * FROM Bookmarks WHERE book_id = :bookId")
    List<Bookmark> getBookmarksForBook(int bookId);

    @Insert
    void insertBook(Book book);

    @Update
    void updataBook(Book book);
}
