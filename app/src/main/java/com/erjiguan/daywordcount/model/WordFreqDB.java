package com.erjiguan.daywordcount.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.erjiguan.daywordcount.model.dao.WordFreqDao;
import com.erjiguan.daywordcount.model.entity.WordFreqEntity;

@Database(entities = {WordFreqEntity.class}, version = 1, exportSchema = false)
public abstract class WordFreqDB extends RoomDatabase {
    private static final String DB_NAME = "word_frequence_db";
    private static WordFreqDB dbInstance;

    public static synchronized WordFreqDB getInstance(Context context) {
        if (dbInstance == null) {
            dbInstance = Room.databaseBuilder(context.getApplicationContext(), WordFreqDB.class, DB_NAME).build();
        }
        return dbInstance;
    }

    public abstract WordFreqDao wordFreqDao();
}
