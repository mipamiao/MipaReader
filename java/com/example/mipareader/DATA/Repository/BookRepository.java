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
import com.example.mipareader.Utils.FileUtils;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;

public class BookRepository {

    public static  final String TAG = "BookRepository";

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
    public Data loadBook(int bookId){
        AppDatabase db = MyApp.getInstance().getDatabase();
        BookDao dao = db.bookDao();
        return Data.fromBookAndInf(dao.getBookWithDetails(bookId));
    }
    public void addBook(Data data){
        AppDatabase db = MyApp.getInstance().getDatabase();
        BookDao bookDao = db.bookDao();
        db.runInTransaction(new Runnable() {
            @Override
            public void run() {
                BookWithSectionsAndBookmarks bookAndInf = Data.toBookAndInf(data);

                int book_id = (int)bookDao.insertBook(bookAndInf.book);
                for(Section section:bookAndInf.sections)
                    section.setBookId(book_id);
                for(Bookmark bookmark:bookAndInf.bookmarks)
                    bookmark.setBookId(book_id);

                data.setId(book_id);
                data.getNovelDir().setBookId(book_id);
                data.getNovelBookmark().setBookId(book_id);

                long [] sectionsIds =  db.sectionDao().insertSections(bookAndInf.sections);
                long [] bookmarksIds = db.bookmarkDao().insertBookmarks(bookAndInf.bookmarks);

                for(int i = 0; i<sectionsIds.length;i++)
                    data.getNovelDir().getChapterList().get(i).setId(bookAndInf.sections.get(i).getId());
                for(int i = 0; i<bookmarksIds.length;i++)
                    data.getNovelBookmark().getBookmarks().get(i).setId(bookAndInf.bookmarks.get(i).getId());
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

    public void uploadBookFile(Data book,String OnlyCode){
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

       MultipartBody.Part part = FileUtils.createFilePart(new File(book.getNovelFilePath()), "NovelFile");
        RequestBody code = RequestBody.create(
                OnlyCode,
                okhttp3.MediaType.parse("text/plain")
        );
        Call<ResponseBody> call = apiService.uploadFile(part, code);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    System.out.println("Upload success!");
                } else {
                    System.out.println("Upload error: " + response.errorBody());
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("Upload failed: " + t.getMessage());
            }
        });
    }
    public boolean downloadBookFile(String onlyCode, String savePath){
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ResponseBody> call = apiService.downloadFile(onlyCode);

        try {
            // 同步执行请求
            Response<ResponseBody> response = call.execute();

            if (response.isSuccessful()) {
                File file = new File(savePath);
                File parent = file.getParentFile();
                if (parent != null && !parent.exists()) {
                    parent.mkdirs();
                }
                InputStream inputStream = response.body().byteStream();
                FileOutputStream outputStream = new FileOutputStream(file);

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.close();
                inputStream.close();
                System.out.println("File saved to: " + savePath);
                return true;
            } else {
                System.out.println("downloadBookFile error: " + response.code());
                return false;
            }
        } catch (IOException e) {
            System.out.println("downloadBookFile failed: " + e.getMessage());
            return false;
        }
    }
    public void uploadBookInf(Data book, String OnlyCode){
        book.setOnlyCode(OnlyCode);
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ResponseBody> call = apiService.uploadInf(book);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "onResponse: uploadBookInf" );
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i(TAG, "onFailure: uploadBookInf" );
            }
        });
    }
    public Data downloadInf(String OnlyCode){
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Data> call = apiService.downloadInf(OnlyCode);
        Data book = null;
        try {
            Response<Data> response = call.execute();
            if (response.isSuccessful()) {
                book = response.body();
                return book;
            } else {
                Log.e(TAG, "downloadInf:  Wroooooooooooong");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return book;
    }
}
