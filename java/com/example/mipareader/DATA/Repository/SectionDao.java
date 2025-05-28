package com.example.mipareader.DATA.Repository;

import androidx.room.Dao;
import androidx.room.Insert;

import java.util.List;

@Dao
public interface SectionDao {

    @Insert
    void insertSections(List<Section> sectionList);
}
