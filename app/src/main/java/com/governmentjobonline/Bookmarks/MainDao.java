package com.governmentjobonline.Bookmarks;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface MainDao {
    //Insert
    @Insert(onConflict = REPLACE)
    void insert(MainData mainData);

    //delete
    @Delete
    void delete(MainData mainData);

    //delete all
    @Delete
    void reset(List<MainData> mainData);

    //update
    @Query("UPDATE table_name SET text = :sText WHERE ID = :sID")
    void update(int sID, String sText);

    //GetAllData
    @Query("SELECT * FROM table_name")
    List<MainData> getAll();

    @Query("SELECT * FROM table_name WHERE text = :id")
    MainData isExist(String id);




}
