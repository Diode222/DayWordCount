package com.erjiguan.daywordcount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.erjiguan.daywordcount.global.DBControllerInstance;
import com.erjiguan.daywordcount.global.GlobalNumber;
import com.erjiguan.daywordcount.global.WordFreqDBInstance;
import com.erjiguan.daywordcount.view.fragment.WordCloudFragment;
import com.erjiguan.daywordcount.view.fragment.WordDicFragment;
import com.erjiguan.daywordcount.view.fragment.WordSoundFragment;
import com.erjiguan.diodemenupopup.DiodeMenuPopup;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import jackmego.com.jieba_android.JiebaSegmenter;

public class MainActivity extends AppCompatActivity {

    private WordCloudFragment wordCloudFragment;
    private WordSoundFragment wordSoundFragment;
    private WordDicFragment wordDicFragment;
    private NotificationManager notificationManager;
    private CloseListenServiceBroadcast closeListenServiceBroadcast;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.word_cloud:
                    showNav(R.id.word_cloud);
                    return true;
                case R.id.word_sound:
                    showNav(R.id.word_sound);
                    return true;
                case R.id.word_dic:
                    showNav(R.id.word_dic);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resourceInit();
        uiInit();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.nav_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Button settingButton = (Button) findViewById(R.id.title_setting_button);
        View view = findViewById(R.id.popup_image_style);
        recordSoundPopup(view);

        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO
            }
        });

        closeListenServiceBroadcast = new CloseListenServiceBroadcast(this);
        closeListenServiceBroadcast.registerAction("com.notification.intent.action.ButtonClick");
    }

    @Override
    protected void onDestroy() {
        notificationManager.cancel(GlobalNumber.NOTIFICATION_ID);
        unregisterReceiver(closeListenServiceBroadcast);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }

    class CloseListenServiceBroadcast extends BroadcastReceiver {
        Context context;

        public CloseListenServiceBroadcast(Context c) {
            context = c;
        }

        public void registerAction(String action) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(action);

            context.registerReceiver(this, filter);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            closeDialog();
        }
    }

    private void resourceInit() {
        WordFreqDBInstance.init(this);  // 必须先定义WordFreqDB再定义DBController，DBController依赖WordFreqDB
        DBControllerInstance.init(this);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("record_sound", "录音", NotificationManager.IMPORTANCE_HIGH);
        notificationManager.createNotificationChannel(channel);
    }

    private void uiInit(){
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);  // 设置不支持旋转屏幕

        wordCloudFragment = new WordCloudFragment();
        wordSoundFragment = new WordSoundFragment();
        wordDicFragment = new WordDicFragment();
        FragmentTransaction beginTransaction=getFragmentManager().beginTransaction();
        beginTransaction.add(R.id.content, wordCloudFragment).add(R.id.content, wordSoundFragment).add(R.id.content, wordDicFragment);//开启一个事务将fragment动态加载到组件
        beginTransaction.hide(wordCloudFragment).hide(wordSoundFragment).hide(wordDicFragment);//隐藏fragment
        beginTransaction.addToBackStack(null);//返回到上一个显示的fragment
        beginTransaction.commit();//每一个事务最后操作必须是commit（），否则看不见效果

        showNav(R.id.word_cloud);
    }

    private void showNav(int navid){
        FragmentTransaction beginTransaction = getFragmentManager().beginTransaction();
        switch (navid){
            case R.id.word_cloud:
                beginTransaction.hide(wordSoundFragment).hide(wordDicFragment);
                beginTransaction.show(wordCloudFragment);
                beginTransaction.addToBackStack(null);
                beginTransaction.commit();
                break;
            case R.id.word_sound:
                beginTransaction.hide(wordCloudFragment).hide(wordDicFragment);
                beginTransaction.show(wordSoundFragment);
                beginTransaction.addToBackStack(null);
                beginTransaction.commit();
                break;
            case R.id.word_dic:
                beginTransaction.hide(wordCloudFragment).hide(wordSoundFragment);
                beginTransaction.show(wordDicFragment);
                beginTransaction.addToBackStack(null);
                beginTransaction.commit();
                break;
        }
    }

    private void recordSoundPopup(View view) {
        final DiodeMenuPopup recordPopup = new DiodeMenuPopup(this, view);
        recordPopup.getMenuInflater().inflate(R.menu.record_sound_menu, recordPopup.getMenu());

        final Window window = getWindow();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        recordPopup.show();
                        recordPopup.dimBackground(1.0f, 0.5f, 200, window);
                    }
                });

                recordPopup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.start_listen:
                                // 异步初始化jieba分词
                                JiebaSegmenter.init(getApplicationContext());
                                createNotificationAndOpenAccesibilitySetting();
                                break;
                            case R.id.stop_listen:
                                closeDialog();
                                break;
                        }

                        return true;
                    }
                });

                recordPopup.setOnDismissListener(new PopupMenu.OnDismissListener() {
                    @Override
                    public void onDismiss(PopupMenu menu) {
                        recordPopup.dimBackground(0.5f, 1.0f, 200, window);
                    }
                });
            }
        });
    }

    private void createNotificationAndOpenAccesibilitySetting() {
        int accessibilityEnabled = 0;
        try {
            accessibilityEnabled = Settings.Secure.getInt(getApplicationContext().getContentResolver(), android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setComponent(new ComponentName(this, MainActivity.class));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.record_notification);
        Notification.Builder builder = new Notification.Builder(getApplicationContext(), NOTIFICATION_SERVICE).setContent(remoteViews);
        builder.setTicker("录音启动").setWhen(System.currentTimeMillis()).setSmallIcon(R.drawable.sound_recorder).setContentIntent(contentIntent).setOngoing(true).setChannelId("record_sound");
        Notification notification = builder.build();

        // 通知栏取消按钮
        Intent closeListenIntent = new Intent("com.notification.intent.action.ButtonClick");
        PendingIntent closeListenPendingIntent = PendingIntent.getBroadcast(getApplication(), 0, closeListenIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.cancel_button, closeListenPendingIntent);

        notificationManager.notify(GlobalNumber.NOTIFICATION_ID, notification);

        if (accessibilityEnabled == 0) {
            Intent startListenIntent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(startListenIntent);
        } else {
            Toast.makeText(this, "微信输入监听已经开启", Toast.LENGTH_SHORT).show();
        }
    }

    private void closeDialog() {
        int accessibilityEnabled = 0;
        try {
            accessibilityEnabled = Settings.Secure.getInt(getApplicationContext().getContentResolver(), android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        if (accessibilityEnabled == 1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
            builder.setTitle("关闭方式");
            builder.setIcon(R.mipmap.ic_launcher_round);
            builder.setCancelable(true);
            builder.setPositiveButton("关闭通知栏和辅助服务", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    notificationManager.cancel(GlobalNumber.NOTIFICATION_ID);
                    Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    Toast.makeText(getApplication(), "设置关闭DayWordCount辅助服务", Toast.LENGTH_LONG).show();
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("仅关闭通知栏", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    notificationManager.cancel(GlobalNumber.NOTIFICATION_ID);
                }
            });
            builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            AlertDialog alertDialog = builder.create();
            if (Settings.canDrawOverlays(this)) {
                alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
                alertDialog.show();
            } else {
                Intent canDrawOverlaysIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                startActivity(canDrawOverlaysIntent);
            }
        } else {
            Toast.makeText(getApplicationContext(), "当前未开启监听", Toast.LENGTH_LONG).show();
            notificationManager.cancel(GlobalNumber.NOTIFICATION_ID);
        }
    }
}
