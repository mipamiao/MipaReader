package com.example.mipareader.DATA.Repository;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import java.io.Serializable;

@Entity(tableName = "sections")
public class Section implements Serializable {


    @PrimaryKey(autoGenerate = true)
    private int id;


    @ColumnInfo(name = "position")
    private long position;


    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "book_id")
    private int bookId;

    public Section() {
    }


    @Ignore
    public Section(long position, String name) {
        this.position = position;
        this.name = name;
    }

    // Getter 和 Setter 方法
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBookId(){
        return bookId;
    }

    public void setBookId(int id){
        this.bookId = id;
    }



    @Override
    public String toString() {
        return "Section{" +
                "id=" + id +
                ", position=" + position +
                ", name='" + name + '\'' +
                '}';
    }
}