package com.example.mipareader.DATA;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.mipareader.DATA.Repository.AppDatabase;
import com.example.mipareader.DATA.Repository.BookDao;
import com.example.mipareader.DATA.Repository.BookRepository;
import com.example.mipareader.DATA.Repository.BookWithSectionsAndBookmarks;
import com.example.mipareader.MyApp;
import com.example.mipareader.Utils.IndirectClass;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.example.mipareader.DATA.Data;
public class DataSet implements Serializable {
    private ArrayList<Data> allBookData;
    private  Data currentBook;
    private final String filePath;
    private static final String DATA_SAVE_PATH = "Save.txt";


    public DataSet( ) {
        this.filePath = MyApp.getInstance().getApplicationContext().getFilesDir().getPath();
        load();
    }

    // Getter and Setter methods
    public ArrayList<Data> getAllBookData() {
        return allBookData;
    }

    public void setAllBookData(ArrayList<Data> allBookData) {
        this.allBookData = allBookData;
    }

    public Data getCurrentBook() {
        return currentBook;
    }

    public void setCurrentBook(Data currentBook) {
        this.currentBook = currentBook;
    }

    public static String getDataSavePath() {
        return DATA_SAVE_PATH;
    }

//    public  boolean save( ) {
//
//        Log.e("4545", "Save: " + filePath + File.separator + DATA_SAVE_PATH);
//        try {
//            FileOutputStream fos = new FileOutputStream(new File(
//                    filePath + File.separator + DATA_SAVE_PATH));
//            ObjectOutputStream outputStream = new ObjectOutputStream(fos);
//            outputStream.writeObject(this);
//            outputStream.close();
//            return true;
//        } catch (IOException e) {
//            Log.e("DataSet", "Save failed", e);
//            return false;
//        }
//    }

    private void  load() {

        allBookData = BookRepository.getInstance().loadAllBook();

//        DataSet dataSet;
//        Log.e("4545", "Load: " + filePath + File.separator + DATA_SAVE_PATH);
//        try {
//            FileInputStream fis = new FileInputStream(new File(
//                    filePath + File.separator + DATA_SAVE_PATH));
//            ObjectInputStream inputStream = new ObjectInputStream(fis);
//            dataSet = (DataSet) inputStream.readObject();
//            inputStream.close();
//        } catch (IOException | ClassNotFoundException e) {
//            Log.e("DataSet", "Load failed", e);
//            return new DataSet(filePath);
//        }

    }
    public void updateBookExceptDirAndBookmark(int index){
        BookRepository.getInstance().updataBookExceptDirAndBookmark(allBookData.get(index));
    }



    public void addBook(String bookName, String bookPath) {
        Data book = new Data();
        book.setNovelFilePath(bookPath);
        book.setNovelName(bookName);
        book.startLoad();
        this.currentBook = book;
        this.allBookData.add(book);
        BookRepository.getInstance().addBook(book);
        moveBookToFirst(book);
    }

    public void moveBookToFirst(Data book) {
        moveBookToFirst(this.allBookData.indexOf(book));
    }

    public void moveBookToFirst(int index) {
        if (index == -1 || index == 0) return;

        Data book = this.allBookData.get(index);
        for (int i = index - 1; i >= 0; i--) {
            this.allBookData.set(i + 1, this.allBookData.get(i));
        }
        this.allBookData.set(0, book);
    }

    public float getLoadProgress() {
        return this.currentBook != null ? this.currentBook.getLoadProgress() : 0;
    }


    private static volatile   DataSet ptr ;
    public static DataSet getInstance(String filePath){
        if(ptr == null){
            synchronized (DataSet.class){
                if(ptr == null){
                    ptr = new DataSet();
                }
            }
        }
        return ptr;
    }
}