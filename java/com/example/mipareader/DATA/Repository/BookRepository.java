package com.example.mipareader.DATA.Repository;


import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.mipareader.DATA.Chapter;
import com.example.mipareader.DATA.Data;
import com.example.mipareader.DATA.Net.ApiClient;
import com.example.mipareader.DATA.Net.ApiService;
import com.example.mipareader.MyApp;
import com.example.mipareader.Utils.DatabaseExecutor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookRepository {

    private BookRepository(){};


    private static BookRepository ptr;
    public static BookRepository getInstance(){
        if (ptr == null){
            synchronized (BookRepository.class){
                if (ptr == null)
                    ptr = new BookRepository();
            }
        }
        return ptr;
    }

    public ArrayList<Data> loadAllBook(){
        AppDatabase db = MyApp.getInstance().getDatabase();
        BookDao dao = db.bookDao();
        List<BookWithSectionsAndBookmarks> bookAndInfs =  dao.getAllBooksWithDetails();
        return bookAndInfs.stream()
                .map(Data::fromBookAndInf)
                .collect(Collectors.toCollection(ArrayList::new));
    }
    public void addBook(Data data){
        AppDatabase db = MyApp.getInstance().getDatabase();
        BookDao bookDao = db.bookDao();

        DatabaseExecutor.getInstance().getDiskIOExecutor().execute(new Runnable() {
            @Override
            public void run() {
                db.runInTransaction(new Runnable() {
                    @Override
                    public void run() {
                        BookWithSectionsAndBookmarks bookAndInf = Data.toBookAndInf(data);
                        bookDao.insertBook(bookAndInf.book);
                        int book_id = bookAndInf.book.getId();
                        for(Section section:bookAndInf.sections)
                            section.setBookId(bookAndInf.book.getId());
                        for(Bookmark bookmark:bookAndInf.bookmarks)
                            bookmark.setBookId(bookAndInf.book.getId());

                        data.setId(bookAndInf.book.getId());
                        data.getNovelDir().setBookId(book_id);
                        data.getNovelBookmark().setBookId(book_id);

                        db.sectionDao().insertSections(bookAndInf.sections);
                        db.bookmarkDao().insertBookmarks(bookAndInf.bookmarks);

                        for(int i = 0; i<bookAndInf.sections.size();i++)
                            data.getNovelDir().getChapterList().get(i).setId(bookAndInf.sections.get(i).getId());
                        for(int i = 0; i<bookAndInf.bookmarks.size();i++)
                            data.getNovelBookmark().getBookmarks().get(i).setId(bookAndInf.bookmarks.get(i).getId());

                    }
                });
            }
        });



    }

    public void addBookmark(Chapter chapter, int book_id){
        AppDatabase db = MyApp.getInstance().getDatabase();
        Bookmark bookmark = Chapter.toBookmark(chapter);
        DatabaseExecutor.getInstance().getDiskIOExecutor().execute(new Runnable() {
            @Override
            public void run() {
                bookmark.setBookId(book_id);
                db.bookmarkDao().insertBookmark(bookmark);
                chapter.setId(bookmark.getId());
            }
        });
    }

    public void updataBookExceptDirAndBookmark(Data data){
        AppDatabase db = MyApp.getInstance().getDatabase();
        DatabaseExecutor.getInstance().getDiskIOExecutor().execute(new Runnable() {
            @Override
            public void run() {
                db.bookDao().updataBook(Data.toBook(data));
            }
        });

    }

    public void uploadToCloud(ArrayList<Data>allBooks){
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        AllBooksForNet  allBooksForNet = new AllBooksForNet();
        List<BookWithSectionsAndBookmarks> bookAndInfs = allBooks.stream().map(Data::toBookAndInf).collect(Collectors.toList());
        allBooksForNet.setAllBookData( bookAndInfs);
        Call<Void> call = apiService.postBooks(allBooksForNet);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    //AllBooksForNet allBooksForNet1 = response.body();
                    Log.i("uploadToCloud","okk");
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("uploadToCloud", "Error: " + errorBody);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("uploadToCloud", "Failure: " + t.getMessage());
            }
        });
    }

    public void downloadFromCloud(int id){
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<AllBooksForNet> call = apiService.getBooks(id);
        call.enqueue(new Callback<AllBooksForNet>() {
            @Override
            public void onResponse(Call<AllBooksForNet> call, Response<AllBooksForNet> response) {
                if (response.isSuccessful()) {
                    AllBooksForNet allBooksForNet1 = response.body();
                    Log.i("downloadFromCloud","okk");
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("downloadFromCloud", "Error: " + errorBody);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<AllBooksForNet> call, Throwable t) {
                Log.e("downloadFromCloud", "Failure: " + t.getMessage());
            }
        });
    }
}
