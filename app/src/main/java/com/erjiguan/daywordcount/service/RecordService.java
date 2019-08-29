package com.erjiguan.daywordcount.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import com.erjiguan.daywordcount.MainActivity;
import com.erjiguan.daywordcount.R;
import com.erjiguan.daywordcount.broadcast.CloseRecordServiceBroadcast;

public class RecordService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent activityIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplication(), 0, activityIntent, 0);

        RemoteViews remoteViews = new RemoteViews(this.getPackageName(), R.layout.record_notification);
        Notification.Builder builder = new Notification.Builder(this.getApplicationContext(), NOTIFICATION_SERVICE).setContent(remoteViews);
        builder.setTicker("录音启动").setWhen(System.currentTimeMillis()).setSmallIcon(R.drawable.sound_recorder).setContentIntent(pendingIntent).setOngoing(true).setChannelId("record_sound");
        Notification notification = builder.build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("record_sound", "录音", NotificationManager.IMPORTANCE_HIGH);
        notificationManager.createNotificationChannel(channel);

        // 通知栏取消按钮
        Intent closeRecordIntent = new Intent(this, CloseRecordServiceBroadcast.class);
        PendingIntent closeRecordPendingIntent = PendingIntent.getBroadcast(getApplication(), 0, closeRecordIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.cancel_button, closeRecordPendingIntent);

        // 通知栏停止按钮


        // 通知栏开始暂停按钮




        startForeground(110, notification);

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d("lvyang", "service ondestroy");
    }
}
