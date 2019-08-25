package com.erjiguan.daywordcount.fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.erjiguan.daywordcount.R;
import com.erjiguan.daywordcount.adapter.TextTagsAdapter;
import com.moxun.tagcloudlib.view.TagCloudView;

public class WordCloudFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wordcloud_fragment, container, false);

        TagCloudView tagCloudView = (TagCloudView) view.findViewById(R.id.tag_cloud);
        tagCloudView.setBackgroundColor(Color.LTGRAY);

        TextTagsAdapter tagsAdapter = new TextTagsAdapter(new String[20]);
        tagCloudView.setAdapter(tagsAdapter);

        return view;
    }
}
