package com.example.android.samplethesis.model;

import android.arch.persistence.room.Embedded;

import java.io.Serializable;
import java.util.Objects;

public class DailyRecordWithItem implements Serializable {
    @Embedded
    private DailyRecord dailyRecord;
    @Embedded
    private  Item item;

    public DailyRecord getDailyRecord() {
        return dailyRecord;
    }

    public void setDailyRecord(DailyRecord dailyRecord) {
        this.dailyRecord = dailyRecord;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    @Override
    public boolean equals(Object o) {
        if (this.dailyRecord.getRecordId() == ((DailyRecordWithItem)o).getDailyRecord().getRecordId()) return true;

        return false;
    }


}
