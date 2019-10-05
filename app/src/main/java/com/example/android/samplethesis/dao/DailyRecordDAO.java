package com.example.android.samplethesis.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.android.samplethesis.model.DailyRecord;
import com.example.android.samplethesis.model.DailyRecordWithItem;

import java.util.Date;
import java.util.List;

@Dao
public interface DailyRecordDAO {
    @Insert
    public void insert(DailyRecord... dailyRecords);

    @Update
    public  void update(DailyRecord... dailyRecords);

    @Delete
        public void delete(DailyRecord... dailyRecords);

    @Query("Select * from DailyRecord")
        public List<DailyRecord> getAllRecord();

    @Query("Select DailyRecord.* , Item.* From DailyRecord , Item  " +
            "WHERE DailyRecord.itemId = Item.id Order By Date Desc")
    public List<DailyRecordWithItem> getDRecordWithItem();

    @Query("Select DailyRecord.* , Item.* From DailyRecord , Item  " +
            "WHERE DailyRecord.itemId = Item.id and DailyRecord.date = :date Order By Date Desc")
    public List<DailyRecordWithItem> getDayRecordWithItem(Date date);

    @Query("Select value from DailyRecord Where itemId = :itemId ORDER BY date DESC Limit 1" )
            public int getLastRecord(long itemId);

    @Query("Select sum(value) from DailyRecord Where itemId = :itemId and date Between :date1 and :date2 ")
        public int getMonthRecord(Date date1 , Date date2 , long itemId);

    @Query("Select sum(DailyRecord.value) from DailyRecord INNER JOIN Item ON  DailyRecord.itemId = Item.id " +
            "Where financeType = :type and date Between :date1 and :date2 ")
        public int getNWTotal(Date date1 , Date date2 , String type);

    @Query("Select sum(DailyRecord.value) From DailyRecord INNER JOIN Item ON DailyRecord.itemId = Item.id " +
            "Where item.category = :category and date Between :date1 and :date2" )
        public int getIESTotal(Date date1, Date date2, String category);

}
