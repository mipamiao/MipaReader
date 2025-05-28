package com.example.mipareader.DATA.Repository;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.Ignore;

import com.example.mipareader.DATA.Bookmarks;
import com.example.mipareader.DATA.Dir;

import java.io.Serializable;

@Entity(tableName = "books")

public class Book implements Serializable {


    @PrimaryKey(autoGenerate = true)
    private int id;


    @ColumnInfo(name = "last_read_pos")
    private long lastReadPos;


    @ColumnInfo(name = "last_chapter_name")
    private String lastReadChapterName;


    @ColumnInfo(name = "file_path", index = true)
    private String novelFilePath;


    @ColumnInfo(name = "name")
    private String novelName;


    @ColumnInfo(name = "font_type")
    private String fontType;


    @ColumnInfo(name = "font_size")
    private float fontSize;


    @ColumnInfo(name = "encode_type")
    private String encodeType;


    @ColumnInfo(name = "read_time_minutes")
    private float readTimeMinutes;

    @ColumnInfo(name = "read_progress")
    private float readProgress;





    public Book() {
        // 初始化默认值
        this.fontSize = 14.0f;
        this.readProgress = 0.0f;
        this.readTimeMinutes = 0.0f;
    }


    @Ignore
    public Book(int id, long lastReadPos, String lastReadChapterName,
                String novelFilePath, String novelName, String fontType,
                float fontSize, String encodeType, float readTimeMinutes,
                float readProgress, Dir novelDir, Bookmarks novelBookmarks) {
        this.id = id;
        this.lastReadPos = lastReadPos;
        this.lastReadChapterName = lastReadChapterName;
        this.novelFilePath = novelFilePath;
        this.novelName = novelName;
        this.fontType = fontType;
        this.fontSize = fontSize;
        this.encodeType = encodeType;
        this.readTimeMinutes = readTimeMinutes;
        this.readProgress = readProgress;


    }

    // 以下是所有属性的完整getter和setter方法

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getLastReadPos() {
        return lastReadPos;
    }

    public void setLastReadPos(long lastReadPos) {
        this.lastReadPos = lastReadPos;
    }

    public String getLastReadChapterName() {
        return lastReadChapterName;
    }

    public void setLastReadChapterName(String lastReadChapterName) {
        this.lastReadChapterName = lastReadChapterName;
    }

    public String getNovelFilePath() {
        return novelFilePath;
    }

    public void setNovelFilePath(String novelFilePath) {
        this.novelFilePath = novelFilePath;
    }

    public String getNovelName() {
        return novelName;
    }

    public void setNovelName(String novelName) {
        this.novelName = novelName;
    }

    public String getFontType() {
        return fontType;
    }

    public void setFontType(String fontType) {
        this.fontType = fontType;
    }

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    public String getEncodeType() {
        return encodeType;
    }

    public void setEncodeType(String encodeType) {
        this.encodeType = encodeType;
    }

    public float getReadTimeMinutes() {
        return readTimeMinutes;
    }

    public void setReadTimeMinutes(float readTimeMinutes) {
        this.readTimeMinutes = readTimeMinutes;
    }

    public float getReadProgress() {
        return readProgress;
    }

    public void setReadProgress(float readProgress) {
        this.readProgress = readProgress;
    }




    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", lastReadPos=" + lastReadPos +
                ", lastReadChapterName='" + lastReadChapterName + '\'' +
                ", novelFilePath='" + novelFilePath + '\'' +
                ", novelName='" + novelName + '\'' +
                ", fontType='" + fontType + '\'' +
                ", fontSize=" + fontSize +
                ", encodeType='" + encodeType + '\'' +
                ", readTimeMinutes=" + readTimeMinutes +
                ", readProgress=" + readProgress +
                '}';
    }
}