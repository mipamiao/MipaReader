package com.example.mipareader;


import android.util.Log;

import java.io.File;
import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class DataSet implements Serializable {
    ArrayList<Data>AllBookData;
    public static String DataSavePath = "Save.txt";
    public DataSet( IndirectClass ic){
        AllBookData = new ArrayList<Data>();
    }
    Data nowbook;
    public static boolean Save(DataSet ds , IndirectClass IC){
        Log.e("4545", "Save: " + IC.getContxt().getFilesDir().getPath()+ File.separator +DataSavePath);
        try {
            FileOutputStream fos = new FileOutputStream(new File(
                    IC.getContxt().getFilesDir().getPath()+File.separator+DataSavePath));
            ObjectOutputStream outputStream = new ObjectOutputStream(fos);
            outputStream.writeObject(ds);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public static DataSet Load(IndirectClass IC){
        DataSet ds;
        Log.e("4545", "Load: " + IC.getContxt().getFilesDir().getPath()+ File.separator +DataSavePath);
        try {
            FileInputStream fis = new FileInputStream(new File(
                    IC.getContxt().getFilesDir().getPath()+ File.separator +DataSavePath));

            ObjectInputStream inputStream = new ObjectInputStream(fis);
            ds= (DataSet) inputStream.readObject();
            inputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new DataSet(IC);
        }
        return ds;
    }
    public void AddBook(String bookname, String bookpath){
        Data book = new Data();
        book.NovleFilePath = bookpath;
        book.NovelName = bookname;
        book.StartLoad();
        nowbook = book;
        AllBookData.add(book);
        book_to_first(book);
    }
    public void book_to_first(Data book){
        book_to_first(AllBookData.indexOf(book));
    }
    public void book_to_first( int index){
        Data book = AllBookData.get(index);
        if(index == -1||index == 0)return;
        for(int i=index -1;i>=0;i--)AllBookData.set(i+1,AllBookData.get(i));
        AllBookData.set(0,book);
    }
    public float GetLoadProgress(){
        return nowbook.GetLoadProgress();
    }
}
