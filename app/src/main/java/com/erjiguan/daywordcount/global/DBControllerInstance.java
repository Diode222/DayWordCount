package com.erjiguan.daywordcount.global;

import android.content.Context;

import com.erjiguan.daywordcount.controller.DBController;
import com.erjiguan.daywordcount.model.WordFreqDB;

public class DBControllerInstance {

    public static DBController dbController;

    private DBControllerInstance() { }

    public static void init(Context context) {
        dbController = DBController.getInstance(context);
    }
}
