package com.example.mipareader;

import android.app.Application;

import androidx.room.Room;

import com.example.mipareader.DATA.DataSet;
import com.example.mipareader.DATA.Repository.AppDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

public class MyApp extends Application {
    private static MyApp instance;

    public static MyApp getInstance() {
        return instance;
    }

    private AppDatabase database;
    private ExecutorService executor;
    private ThreadPoolExecutor threadPoolExecutor;
    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化全局对象
        database = Room.databaseBuilder(this, AppDatabase.class, "my-database")
                .fallbackToDestructiveMigration()
                .build();

        instance  = this;
    }

    public AppDatabase getDatabase() {
        return database;
    }
}
