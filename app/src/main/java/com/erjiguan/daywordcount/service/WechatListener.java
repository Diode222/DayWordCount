package com.erjiguan.daywordcount.service;

import android.accessibilityservice.AccessibilityService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

public class WechatListener extends AccessibilityService {
    private String InputString = "";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        CharSequence packageName = event.getPackageName();
        if (packageName.equals("com.tencent.mm") && (eventType == AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED || eventType == AccessibilityEvent.TYPE_VIEW_CLICKED)) {
            if (eventType == AccessibilityEvent.TYPE_VIEW_CLICKED && event.getText().toString().equals("[发送]")) {
                Log.d("lvyang", InputString);
            } else {
                InputString = event.getText().toString();
            }
        }
    }

    @Override
    public void onInterrupt() {
    }
}
