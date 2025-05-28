package com.example.mipareader.DATA.Repository;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(
        entities = {
                Book.class,
                Section.class,
                Bookmark.class,
        },
        version = 1,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {
    // 为每个实体类定义对应的DAO接口
    public abstract BookDao bookDao();
    public abstract SectionDao sectionDao();
    public abstract BookmarkDao bookmarkDao();


//    public abstract BookDao getBookDao();
//    //public abstract void setBookDao(BookDao bookDao);
//
//    public abstract SectionDao getSectionDao();
//    //public abstract void setSectionDao(SectionDao sectionDao);
//
//    public abstract BookmarkDao getBookmarkDao();
//    //public abstract void setBookmarkDao(BookmarkDao bookmarkDao);
    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "app_database"
                            )
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}