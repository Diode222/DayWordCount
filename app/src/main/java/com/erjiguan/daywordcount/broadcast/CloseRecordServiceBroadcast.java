package com.erjiguan.daywordcount.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.erjiguan.daywordcount.service.RecordService;

public class CloseRecordServiceBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent recordServiceIntent = new Intent(context, RecordService.class);
        context.stopService(recordServiceIntent);
    }
}
