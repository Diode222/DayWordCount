package com.erjiguan.daywordcount.global;

import android.content.Context;

import com.erjiguan.daywordcount.model.ChatMessageTmpDB;

public class ChatMessageTmpDBInstance {

    public static ChatMessageTmpDB chatMessageTmpDB;

    private ChatMessageTmpDBInstance() { }

    public static void init(Context context) {
        chatMessageTmpDB = ChatMessageTmpDB.getInstance(context);
    }
}
