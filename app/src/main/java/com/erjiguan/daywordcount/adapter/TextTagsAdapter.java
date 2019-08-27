package com.erjiguan.daywordcount.adapter;


import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moxun.tagcloudlib.view.TagsAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TextTagsAdapter extends TagsAdapter {

    private List<String> dataSet = new ArrayList<>();

    public TextTagsAdapter(@NonNull String... data) {
        dataSet.clear();
        Collections.addAll(dataSet, data);
    }

    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public View getView(Context context, final int position, ViewGroup parent) {  // TODO 修改这里来将词语列表放到view中
        String[] name = {"android", "java", "c", "c++", "html5", "js", "css", "javase", "javaee"};
        Random rand = new Random();
        int randNum = rand.nextInt(9);

        TextView tv = new TextView(context);
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(100, 100);
        tv.setLayoutParams(lp);
        tv.setText(name[randNum]);
        tv.setGravity(Gravity.CENTER);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  // TODO click事件
                Log.e("Click", "Tag " + position + " clicked.");
            }
        });
        return tv;
    }

    @Override
    public Object getItem(int position) {
        return dataSet.get(position);
    }

    @Override
    public int getPopularity(int position) {
        return position % 7;
    }

    @Override
    public void onThemeColorChanged(View view, int themeColor) {
        ((TextView) view).setTextColor(themeColor);
    }
}
