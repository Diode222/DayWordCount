package com.erjiguan.daywordcount.model.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.erjiguan.daywordcount.model.entity.ChatMessageTmpEntity;

import java.util.List;

@Dao
public interface ChatMessageTmpDao {
    @Insert
    void insertMessage(ChatMessageTmpEntity msgEntity);

    @Insert
    void insertMessageList(List<ChatMessageTmpEntity> msgEntity);

    @Insert
    void insertMessageList(ChatMessageTmpEntity... msgEntity);

    @Delete
    void deleteMessage(ChatMessageTmpEntity msgEntity);

    @Delete
    void deleteAllMessage(List<ChatMessageTmpEntity> mesgEntities);

    @Delete
    void deleteAllMessage(ChatMessageTmpEntity... mesgEntities);

    @Query("select * from ChatMessageTmpEntity")
    List<ChatMessageTmpEntity> getChatMessageList();
}
