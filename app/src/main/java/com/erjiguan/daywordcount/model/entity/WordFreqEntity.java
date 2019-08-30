package com.erjiguan.daywordcount.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class WordFreqEntity {
    @PrimaryKey
    @ColumnInfo(name = "word", typeAffinity = ColumnInfo.TEXT)
    @NonNull
    public String word;

    @ColumnInfo(name = "count", typeAffinity = ColumnInfo.INTEGER)
    public int count;
}
