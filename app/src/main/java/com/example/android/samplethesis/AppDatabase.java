package com.example.android.samplethesis;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.example.android.samplethesis.dao.DailyRecordDAO;
import com.example.android.samplethesis.dao.ItemDAO;
import com.example.android.samplethesis.model.DailyRecord;
import com.example.android.samplethesis.model.Item;

@Database(entities = {Item.class, DailyRecord.class},version = 1,exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract ItemDAO getItemDAO();
    public abstract DailyRecordDAO getDailyRecordDAO();

    @Override
    public void init(@NonNull DatabaseConfiguration configuration) {
        super.init(configuration);
    }
}
