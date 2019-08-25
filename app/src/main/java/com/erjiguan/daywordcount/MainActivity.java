package com.erjiguan.daywordcount;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;

import android.os.Bundle;

import com.erjiguan.daywordcount.adapter.TextTagsAdapter;
import com.moxun.tagcloudlib.view.TagCloudView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TagCloudView tagCloudView = (TagCloudView) findViewById(R.id.tag_cloud);
        tagCloudView.setBackgroundColor(Color.LTGRAY);

        TextTagsAdapter tagsAdapter = new TextTagsAdapter(new String[20]);
        tagCloudView.setAdapter(tagsAdapter);
    }
}
