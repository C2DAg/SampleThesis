package com.example.android.samplethesis.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity
public class Item implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true)
    @NonNull private long id;
    private String name;
    private String category;
    private String defaultType;
    private int icon;

    @Ignore
    public Item(String name, String category, String defaultType, int icon) {
        this.name = name;
        this.category = category;
        this.defaultType = defaultType;
        this.icon = icon;
    }

    public Item(Long id, String name, String category, String defaultType, int icon) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.defaultType = defaultType;
        this.icon = icon;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDefaultType() {
        return defaultType;
    }

    public void setDefaultType(String defaultType) {
        this.defaultType = defaultType;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", defaultType='" + defaultType + '\'' +
                ", icon=" + icon +
                '}';
    }
}
