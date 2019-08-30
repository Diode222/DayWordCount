package com.erjiguan.daywordcount.service;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.erjiguan.daywordcount.controller.DBController;
import com.erjiguan.daywordcount.global.DBControllerInstance;

import java.util.ArrayList;

import jackmego.com.jieba_android.JiebaSegmenter;
import jackmego.com.jieba_android.RequestCallback;

public class WechatListener extends AccessibilityService {
    private String inputString = "";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        CharSequence packageName = event.getPackageName();
        if (packageName.equals("com.tencent.mm") && (eventType == AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED || eventType == AccessibilityEvent.TYPE_VIEW_CLICKED) && event.getContentDescription() == null) {
            if (eventType == AccessibilityEvent.TYPE_VIEW_CLICKED && event.getText().toString().equals("[发送]")) {
                String insideInputString = inputString.substring(1, inputString.length() - 1).trim();
                if (insideInputString.length() > 0) {
                    RequestCallback<ArrayList<String> > callback = new RequestCallback<ArrayList<String>>() {
                        @Override
                        public void onSuccess(ArrayList<String> result) {
                            DBControllerInstance.dbController.setWordFreqData(result);
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
