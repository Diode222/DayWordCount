package com.erjiguan.daywordcount.controller;

import android.content.Context;

import com.erjiguan.daywordcount.global.ChatMessageTmpDBInstance;
import com.erjiguan.daywordcount.global.WordFreqDBInstance;
import com.erjiguan.daywordcount.model.ChatMessageTmpDB;
import com.erjiguan.daywordcount.model.WordFreqDB;
import com.erjiguan.daywordcount.model.entity.ChatMessageTmpEntity;
import com.erjiguan.daywordcount.model.entity.WordFreqEntity;

import java.util.ArrayList;
import java.util.List;

public class DBController {

    final public static int INTENSIVE = 0;
    final public static int MODERATE = 1;
    final public static int SPARSE = 2;

    public ArrayList<ArrayList<Object> > dataList = new ArrayList<ArrayList<Object> >();

    final private static WordFreqDB wordFreqDB = WordFreqDBInstance.wordFreqDB;

    final private static ChatMessageTmpDB chatMessageTmpDB = ChatMessageTmpDBInstance.chatMessageTmpDB;

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

    public void setWordFreqData1(final ArrayList<String> wordList) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (wordFreqDB) {  // 写数据必须将对应数据库加锁
                    for (final String wordListened: wordList) {
                        if (wordListened.length() <= 1) {
                            continue;
                        }
                        final List<WordFreqEntity> wordEntity = wordFreqDB.wordFreqDao().getCountByWord(wordListened);
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

    // TODO 输入是网络获取到的json，将json解析得到词频数据后存入WordFreqDB中
    public void setWordFreqData(final byte[] data) {
        // 每次都会直接更换本地数据库，而非在本地基础上更新
        deleteAllWordFreqData();


    }

    private void deleteAllWordFreqData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (wordFreqDB) {
                    wordFreqDB.wordFreqDao().deleteWord();
                }
            }
        }).start();
    }

    // TODO 获取本地所有的聊天数据，根据大小打包成json对象数组，返回给调用方用于发送给Odin，调用方会循环遍历发送
    //  每个json，直到所有数据发送完成。这个方法返回之后，应该把ChatMessageTmpDB中所有的数据删除掉
    public byte[][] getAllChatMessageTmpData() {

        // 删掉ChatMessageTmpDB中所有数据
        deleteAllChatMessageTmpData();
        return null;
    }

    public void setChatMessageTmpData(final String msg, final long t, final String person) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (chatMessageTmpDB) {
                    chatMessageTmpDB.chatMessageTmpDao().insertMessage(new ChatMessageTmpEntity() {{
                        this.message = msg;
                        this.time = t;
                        this.chatPerson = person;
                    }});
                }
            }
        }).start();
    }

    public void deleteAllChatMessageTmpData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (chatMessageTmpDB) {
                    chatMessageTmpDB.chatMessageTmpDao().deleteAllMessage();
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
