package com.erjiguan.daywordcount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MenuItem;

import com.erjiguan.daywordcount.fragment.WordCloudFragment;
import com.erjiguan.daywordcount.fragment.WordDicFragment;
import com.erjiguan.daywordcount.fragment.WordSoundFragment;
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
}
