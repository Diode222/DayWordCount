package com.erjiguan.daywordcount.global;

import android.content.Context;

import com.erjiguan.daywordcount.model.WordFreqDB;

public class WordFreqDBInstance {

    public static WordFreqDB wordFreqDB;

    private WordFreqDBInstance() { }

    public static void init(Context context) {
        wordFreqDB = WordFreqDB.getInstance(context);
    }
}
