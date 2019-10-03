package com.erjiguan.daywordcount.model.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.erjiguan.daywordcount.model.entity.WordFreqEntity;

import java.util.List;

@Dao
public interface WordFreqDao {
    @Insert
    void insertWord(WordFreqEntity wordEntity);

    @Delete
    void deleteWord(WordFreqEntity wordEntity);

    @Delete
    void deleteWord(List<WordFreqEntity> wordFreqEntities);

    @Delete
    void deleteWord(WordFreqEntity... wordFreqEntities);

    @Update
    void updateWord(WordFreqEntity wordEntity);

    @Query("select * from WordFreqEntity")
    List<WordFreqEntity> getWordList();

    @Query("select * from WordFreqEntity where word = :word")
    List<WordFreqEntity> getCountByWord(String word);

    @Query("select * from WordFreqEntity order by count desc limit 50")
    List<WordFreqEntity> getWordListIntensive();

    @Query("select * from WordFreqEntity order by count desc limit 30")
    List<WordFreqEntity> getWordListModerate();

    @Query("select * from WordFreqEntity order by count desc limit 20")
    List<WordFreqEntity> getWordListSparse();
}
