package com.erjiguan.daywordcount.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.erjiguan.daywordcount.model.dao.ChatMessageTmpDao;
import com.erjiguan.daywordcount.model.entity.ChatMessageTmpEntity;

@Database(entities = {ChatMessageTmpEntity.class}, version = 1, exportSchema = false)
public abstract class ChatMessageTmpDB extends RoomDatabase {
    private static final String DB_NAME = "chat_message_tmp_db";
    private static ChatMessageTmpDB dbInstance;

    public static synchronized ChatMessageTmpDB getInstance(Context context) {
        if (dbInstance == null) {
            dbInstance = Room.databaseBuilder(context.getApplicationContext(), ChatMessageTmpDB.class, DB_NAME).allowMainThreadQueries().build();
        }
        return dbInstance;
    }

    public abstract ChatMessageTmpDao chatMessageTmpDao();
}
