package com.erjiguan.daywordcount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.erjiguan.daywordcount.service.RecordService;
import com.erjiguan.daywordcount.view.fragment.WordCloudFragment;
import com.erjiguan.daywordcount.view.fragment.WordDicFragment;
import com.erjiguan.daywordcount.view.fragment.WordSoundFragment;
import com.erjiguan.diodemenupopup.DiodeMenuPopup;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private WordCloudFragment wordCloudFragment;
    private WordSoundFragment wordSoundFragment;
    private WordDicFragment wordDicFragment;
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
        init();
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


    }

    private void init(){
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
                            case R.id.start_record:
                                Intent recordServiceIntent = new Intent(MainActivity.this, RecordService.class);
                                startService(recordServiceIntent);
                                break;
                            case R.id.record_setting:
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
}
