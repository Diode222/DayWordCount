package com.erjiguan.daywordcount.controller;

import android.content.Context;

import com.erjiguan.daywordcount.global.WordFreqDBInstance;
import com.erjiguan.daywordcount.model.WordFreqDB;
import com.erjiguan.daywordcount.model.entity.WordFreqEntity;

import java.util.ArrayList;
import java.util.List;

public class DBController {

    final public static int INTENSIVE = 0;
    final public static int MODERATE = 1;
    final public static int SPARSE = 2;

    public ArrayList<ArrayList<Object> > dataList = new ArrayList<ArrayList<Object> >();

    private static WordFreqDB wordFreqDB = WordFreqDBInstance.wordFreqDB;

    private static DBController dbControllerInstance;

    private DBController(Context context) { }

    public static synchronized DBController getInstance(Context context) {
        if (dbControllerInstance == null) {
            dbControllerInstance = new DBController(context);
        }
        return dbControllerInstance;
    }

    public ArrayList<ArrayList<Object> > getWordFreqData(int dataAmount) {
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
                getWordFreqDataIntensive();
        }

        return dataList;
    }

    public void setWordFreqData(final ArrayList<String> wordList) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {  // 写数据必须加锁
                    for (final String wordListened: wordList) {
                        if (wordListened.length() <= 1) {
                            continue;
                        }
                        final List<WordFreqEntity> wordEntity = WordFreqDBInstance.wordFreqDB.wordFreqDao().getCountByWord(wordListened);
                        if (wordEntity.size() <= 0) {
                            WordFreqDBInstance.wordFreqDB.wordFreqDao().insertWord(new WordFreqEntity() {{
                                this.word = wordListened;
                                this.count = 1;
                            }});
                        } else {
                            WordFreqDBInstance.wordFreqDB.wordFreqDao().updateWord(new WordFreqEntity() {{
                                this.word = wordListened;
                                this.count = wordEntity.get(0).count + 1;
                            }});
                        }
                    }
                }
            }
        }).start();
    }

    // 获取密集数据
    private ArrayList<ArrayList<Object> > getWordFreqDataIntensive() {
        dataList = new ArrayList<ArrayList<Object> >();
        List<WordFreqEntity> rawData = wordFreqDB.wordFreqDao().getWordListIntensive();

        for (final WordFreqEntity entity: rawData) {
            dataList.add(new ArrayList<Object>() {{
                add(entity.word);
                add(entity.count);
            }});
        }

        return dataList;
    }

    // 获取适量数据
    private ArrayList<ArrayList<Object> > getWordFreqDataModerate() {
        dataList = new ArrayList<ArrayList<Object> >();
        List<WordFreqEntity> rawData = wordFreqDB.wordFreqDao().getWordListModerate();

        for (final WordFreqEntity entity: rawData) {
            dataList.add(new ArrayList<Object>() {{
                add(entity.word);
                add(entity.count);
            }});
        }

        return dataList;
    }

    // 获取稀疏数据
    private ArrayList<ArrayList<Object> > getWordFreqDataSparse() {
        dataList = new ArrayList<ArrayList<Object> >();
        List<WordFreqEntity> rawData = wordFreqDB.wordFreqDao().getWordListSparse();

        for (final WordFreqEntity entity: rawData) {
            dataList.add(new ArrayList<Object>() {{
                add(entity.word);
                add(entity.count);
            }});
        }

        return dataList;
    }
}
