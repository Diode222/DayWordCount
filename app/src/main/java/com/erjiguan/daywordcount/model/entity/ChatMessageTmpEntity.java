package com.erjiguan.daywordcount.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ChatMessageTmpEntity {
    @PrimaryKey(autoGenerate = true)
    public int messageId;

    @ColumnInfo(name = "message", typeAffinity = ColumnInfo.TEXT)
    @NonNull
    public String message;

    @ColumnInfo(name = "time", typeAffinity = ColumnInfo.INTEGER)
    public long time;

    @ColumnInfo(name = "chat_person", typeAffinity = ColumnInfo.TEXT)
    public String chatPerson;
}
