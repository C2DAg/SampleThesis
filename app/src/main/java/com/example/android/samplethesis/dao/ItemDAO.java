package com.example.android.samplethesis.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import com.example.android.samplethesis.model.Item;

import java.util.List;

@Dao
public interface ItemDAO {
    @Insert
    public void insert(Item... items);

    @Update
    public void update(Item... items);

    @Delete
    public void delete(Item... items);

    @Query("Select * from Item")
    public List<Item> getAllItem();

    @Query("Select id from Item Where icon = :image ")
    public int getItemId(int image);

    @Query("Select * from Item Where category = :category ")
    public List<Item> getItemByCat(String category);




}
