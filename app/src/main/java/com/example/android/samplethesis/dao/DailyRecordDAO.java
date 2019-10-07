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

    @Query("Select ifnull(value,0) from DailyRecord Where itemId = :itemId ORDER BY date DESC Limit 1" )
            public int getLastRecord(long itemId);

    @Query("Select ifnull(sum(value),0) from DailyRecord Where itemId = :itemId and date Between :date1 and :date2 ")
        public int getMonthRecord(Date date1 , Date date2 , long itemId);

    @Query("Select ifnull(sum(DailyRecord.value),0) from DailyRecord INNER JOIN Item ON  DailyRecord.itemId = Item.id " +
            "Where financeType = :type and date Between :date1 and :date2 ")
        public int getNWTotal(Date date1 , Date date2 , String type);

    @Query("Select ifnull(sum(DailyRecord.value),0) from DailyRecord INNER JOIN Item ON  DailyRecord.itemId = Item.id " +
            "Where financeType = :type and date =  :date ")
    public int getDayNWTotal(Date date, String type);

    @Query("Select ifnull(sum(DailyRecord.value),0) From DailyRecord INNER JOIN Item ON DailyRecord.itemId = Item.id " +
            "Where item.category = :category and financeType !='Withdraw' and date Between :date1 and :date2 " )
        public int getIESTotal(Date date1, Date date2, String category);

    @Query("Select ifnull(sum(DailyRecord.value),0) From DailyRecord INNER JOIN Item ON DailyRecord.itemId = Item.id " +
            "Where item.id = :itemId and item.category = :category and financeType !='Withdraw'" )
    public int getSavingItemTotal(long itemId, String category);

    @Query("Select ifnull(DailyRecord.value,0) From DailyRecord INNER JOIN Item ON DailyRecord.itemId = Item.id " +
            "Where item.id = :itemId and financeType = :type ORDER BY date DESC Limit 1" )
    public int getLastItemWithdraw(long itemId, String type);

    @Query("Select ifnull(sum(DailyRecord.value),0) From DailyRecord INNER JOIN Item ON DailyRecord.itemId = Item.id " +
            "Where financeType = :type and date Between :date1 and :date2" )
    public int getWithdrawTotal(Date date1, Date date2, String type);

    @Query("Select ifnull(sum(DailyRecord.value),0) From DailyRecord INNER JOIN Item ON DailyRecord.itemId = Item.id " +
            "Where item.category = :category and financeType !='Withdraw'and strftime('%Y', date) = :year and abs(strftime('%m',date))=:i" )
    public float getIESMonthTotal(String category,int year, int i);

    @Query("Select ifnull(sum(DailyRecord.value),0) from DailyRecord INNER JOIN Item ON  DailyRecord.itemId = Item.id " +
            "Where financeType = :type and strftime('%Y', date) = :year and abs(strftime('%m',date))=:i")
    public int getNWMonthTotal(String type,int year, int i);

    @Query("Select ifnull(sum(DailyRecord.value),0) From DailyRecord INNER JOIN Item ON DailyRecord.itemId = Item.id " +
            "Where financeType = :type and strftime('%Y', date) = :year and abs(strftime('%m',date))=:i" )
    public int getWithdrawMonthTotal(String type,int year,int i);

}
