package com.example.android.samplethesis.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.Date;

@Entity(
        foreignKeys = @ForeignKey( entity = Item.class,
                                    parentColumns = "id",
                                    childColumns = "itemId"))

public class DailyRecord implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long recordId;
    @ColumnInfo(index = true)
    private long itemId;
    private Date date;
    private double value;
    private String financeType;
    @Nullable
    private String memo;
    @Ignore
    public DailyRecord(long recordId, long itemId, Date date, double value, String financeType, String memo) {
        this.recordId = recordId;
        this.itemId = itemId;
        this.date = date;
        this.value = value;
        this.financeType = financeType;
        this.memo = memo;
    }

    public DailyRecord(long itemId, Date date, double value, String financeType, String memo) {
        this.itemId = itemId;
        this.date = date;
        this.value = value;
        this.financeType = financeType;
        this.memo = memo;
    }

    public long getRecordId() {
        return recordId;
    }

    public void setRecordId(long recordId) {
        this.recordId = recordId;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getFinanceType() {
        return financeType;
    }

    public void setFinanceType(String financeType) {
        this.financeType = financeType;
    }

    @Nullable
    public String getMemo() {
        return memo;
    }

    public void setMemo(@Nullable String memo) {
        this.memo = memo;
    }
}

