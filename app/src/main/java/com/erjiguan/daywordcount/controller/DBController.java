package com.erjiguan.daywordcount.controller;

import android.content.Context;

import com.erjiguan.daywordcount.model.WordFreqDB;
import com.erjiguan.daywordcount.model.entity.WordFreqEntity;

import java.util.ArrayList;
import java.util.List;

public class DBController {

    final public static int INTENSIVE = 0;
    final public static int MODERATE = 1;
    final public static int SPARSE = 2;

    public ArrayList<ArrayList<Object> > dataList = new ArrayList<ArrayList<Object> >();

    final private WordFreqDB wordFreqDB;

    public DBController(Context context) {
        wordFreqDB = WordFreqDB.getInstance(context);
    }

    public ArrayList<ArrayList<Object>> getWordFreqData(int dataAmount) {
        switch (dataAmount) {
            case INTENSIVE:
                getWordFreqDataIntensive();
                break;
            case MODERATE:
                getWordFreqDataModerate();
                break;
            case SPARSE:
                getWordFreqDataSparse();
                break;
            default:

        }
        return dataList;
    }

    // 获取密集数据
    private void getWordFreqDataIntensive() {
        dataList = new ArrayList<ArrayList<Object> >();
        List<WordFreqEntity> rawData = wordFreqDB.wordFreqDao().getWordListIntensive();

        for (final WordFreqEntity entity: rawData) {
            dataList.add(new ArrayList<Object>() {{
                add(entity.word);
                add(entity.count);
            }});
        }
    }

    // 获取适量数据
    private void getWordFreqDataModerate() {
        dataList = new ArrayList<ArrayList<Object> >();
        List<WordFreqEntity> rawData = wordFreqDB.wordFreqDao().getWordListModerate();

        for (final WordFreqEntity entity: rawData) {
            dataList.add(new ArrayList<Object>() {{
                add(entity.word);
                add(entity.count);
            }});
        }
    }

    // 获取稀疏数据
    private void getWordFreqDataSparse() {
        dataList = new ArrayList<ArrayList<Object> >();
        List<WordFreqEntity> rawData = wordFreqDB.wordFreqDao().getWordListSparse();

        for (final WordFreqEntity entity: rawData) {
            dataList.add(new ArrayList<Object>() {{
                add(entity.word);
                add(entity.count);
            }});
        }
    }
}
