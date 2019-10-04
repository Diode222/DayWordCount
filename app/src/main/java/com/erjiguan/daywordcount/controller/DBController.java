package com.erjiguan.daywordcount.controller;

import android.content.Context;
import android.util.Log;

import com.erjiguan.daywordcount.ChatMessageProtos;
import com.erjiguan.daywordcount.WordFreqProtos;
import com.erjiguan.daywordcount.global.ChatMessageTmpDBInstance;
import com.erjiguan.daywordcount.global.GlobalNumber;
import com.erjiguan.daywordcount.global.WordFreqDBInstance;
import com.erjiguan.daywordcount.model.ChatMessageTmpDB;
import com.erjiguan.daywordcount.model.WordFreqDB;
import com.erjiguan.daywordcount.model.entity.ChatMessageTmpEntity;
import com.erjiguan.daywordcount.model.entity.WordFreqEntity;
import com.google.protobuf.InvalidProtocolBufferException;

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

//    public void setWordFreqData1(final ArrayList<String> wordList) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                synchronized (wordFreqDB) {  // 写数据必须将对应数据库加锁
//                    for (final String wordListened: wordList) {
//                        if (wordListened.length() <= 1) {
//                            continue;
//                        }
//                        final List<WordFreqEntity> wordEntity = wordFreqDB.wordFreqDao().getCountByWord(wordListened);
//                        if (wordEntity.size() <= 0) {
//                            WordFreqDBInstance.wordFreqDB.wordFreqDao().insertWord(new WordFreqEntity() {{
//                                this.word = wordListened;
//                                this.count = 1;
//                            }});
//                        } else {
//                            WordFreqDBInstance.wordFreqDB.wordFreqDao().updateWord(new WordFreqEntity() {{
//                                this.word = wordListened;
//                                this.count = wordEntity.get(0).count + 1;
//                            }});
//                        }
//                    }
//                }
//            }
//        }).start();
//    }

    // 输入是网络获取到的protobuf序列化数据，将proto反序列化解析得到词频数据后，存入WordFreqDB中，
    // 存之前需要删除所有本地词频数据
    public void setWordFreqData(final List<ArrayList<Object> > dataList) {
        // 每次都会直接更换本地数据库，而非在本地基础上更新
        deleteAllWordFreqData();

        for (final ArrayList<Object> data: dataList) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    synchronized (wordFreqDB) {
                        wordFreqDB.wordFreqDao().insertWord(new WordFreqEntity() {{
                            word = (String) data.get(0);
                            count = (int) data.get(1);
                        }});
                    }
                }
            }).start();
        }

    }

    private void deleteAllWordFreqData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (wordFreqDB) {
                    wordFreqDB.wordFreqDao().deleteAllWord();
                }
            }
        }).start();
    }

    // 获取本地所有的聊天数据，根据大小打包成protobuf序列化二进制数据数组，返回给调用方用于发送给Odin，调用方会循环遍历发送
    // 每个序列化二进制数据数组，直到所有数据发送完成。这个方法返回之后，应该把ChatMessageTmpDB中所有的数据删除掉
    public ArrayList<byte[]> getAllChatMessageTmpData() {
        ArrayList<byte[]> chatMessageTmpData = new ArrayList<byte[]>();

        ChatMessageProtos.ChatMessageList.Builder chatMessageListBuilder = ChatMessageProtos.ChatMessageList.newBuilder();
        ChatMessageProtos.ChatMessage.Builder chatMessageBuilder = ChatMessageProtos.ChatMessage.newBuilder();

        List<ChatMessageTmpEntity> chatMessageTmpEntities =  chatMessageTmpDB.chatMessageTmpDao().getChatMessageList();
        for (int i = 0; i < chatMessageTmpEntities.size(); i++) {
            ChatMessageTmpEntity chatMessageTmpEntity = chatMessageTmpEntities.get(i);
            chatMessageBuilder.clear();
            chatMessageBuilder.setMessage(chatMessageTmpEntity.message);
            chatMessageBuilder.setTime(chatMessageTmpEntity.time);
            chatMessageBuilder.setChatPerson(chatMessageTmpEntity.chatPerson);
            chatMessageListBuilder.addChatMessages(chatMessageBuilder);
            if ((i + 1) % GlobalNumber.MAX_SIZE_OF_MESSAGE_IN_PACKAGE == 0) {
                ChatMessageProtos.ChatMessageList chatMessageList = chatMessageListBuilder.build();
                chatMessageTmpData.add(chatMessageList.toByteArray());
            }
        }
        if (chatMessageListBuilder.getChatMessagesList().size() > 0) {
            ChatMessageProtos.ChatMessageList chatMessageList = chatMessageListBuilder.build();
            chatMessageTmpData.add(chatMessageList.toByteArray());
        }
      
        return chatMessageTmpData;
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
