package com.erjiguan.daywordcount.service;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.erjiguan.daywordcount.global.DBControllerInstance;

import java.util.ArrayList;

import jackmego.com.jieba_android.JiebaSegmenter;
import jackmego.com.jieba_android.RequestCallback;

public class WechatListener extends AccessibilityService {
    private String inputString = "";
    private String chatPerson = "Unknown";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        CharSequence packageName = event.getPackageName();
        if (packageName.equals("com.tencent.mm") && (eventType == AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED || eventType == AccessibilityEvent.TYPE_VIEW_CLICKED || eventType == AccessibilityEvent.TYPE_VIEW_FOCUSED) && event.getContentDescription() == null) {
            if (eventType == AccessibilityEvent.TYPE_VIEW_FOCUSED && event.getText().toString().length() > 0) {
                String chatPageItemsString = event.getText().toString();
                String insideChatPageItemsString = chatPageItemsString.substring(1, chatPageItemsString.length() - 1).trim();
                String[] chatPageItems = insideChatPageItemsString.split(", ");
                if (chatPageItems.length <= 1) {
                    return;
                }
                chatPerson = chatPageItems[1];
            } else if (eventType == AccessibilityEvent.TYPE_VIEW_CLICKED && event.getText().toString().equals("[发送]")) {
                String insideInputString = inputString.substring(1, inputString.length() - 1).trim();
                if (insideInputString.length() > 0) {
                    long sendTime = System.currentTimeMillis() / 1000;
                    // TODO 将数据存储逻辑写到这里，删掉下面的本地分词逻辑。这里只存微信消息、聊天对象和时间



                    RequestCallback<ArrayList<String> > callback = new RequestCallback<ArrayList<String>>() {
                        @Override
                        public void onSuccess(ArrayList<String> result) {
                            DBControllerInstance.dbController.setWordFreqData1(result);
                        }

                        @Override
                        public void onError(String errorMsg) {
                            Log.d("Jieba", "Jieba分词失败: " + errorMsg);
                        }
                    };
                    JiebaSegmenter.getJiebaSegmenterSingleton().getDividedStringAsync(insideInputString, callback);
                }
            } else {
                inputString = event.getText().toString();
            }
        }
    }

    @Override
    public void onInterrupt() {
    }
}
