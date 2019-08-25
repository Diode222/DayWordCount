package com.erjiguan.daywordcount.fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.Nullable;

import com.erjiguan.daywordcount.R;
import com.erjiguan.daywordcount.adapter.TextTagsAdapter;
import com.moxun.tagcloudlib.view.TagCloudView;

import java.util.ArrayList;
import java.util.List;

public class WordCloudFragment extends Fragment {

    private Spinner graphicsSpinner;
    private List<String> graphicsList;
    private ArrayAdapter<String> graphicsSpinnerAdapter;

    private Spinner dataAmountSpinner;
    private List<String> dataAmountList;
    private ArrayAdapter<String> dataAmountSpinnerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wordcloud_fragment, container, false);

        graphicsSpinner = (Spinner) view.findViewById(R.id.graphics_spinner);
        graphicsList = new ArrayList<String>() {{
            add("3D词云");
            add("2D词云");
            add("柱状图");
        }};

        graphicsSpinnerAdapter = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_item, graphicsList); //后面不行这里还要改一下，用fragment继承的方式来取context
        graphicsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        graphicsSpinner.setAdapter(graphicsSpinnerAdapter);

        // 给spinner设置监听
        graphicsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // TODO
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO
            }
        });

        dataAmountSpinner = (Spinner) view.findViewById(R.id.data_amount_spinner);
        dataAmountList = new ArrayList<String>() {{
            add("密集");  //暂定
            add("适量");
            add("稀疏");
        }};

        dataAmountSpinnerAdapter = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_item, dataAmountList); //后面不行这里还要改一下，用fragment继承的方式来取context
        dataAmountSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAmountSpinner.setAdapter(dataAmountSpinnerAdapter);

        // 给spinner设置监听
        dataAmountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // TODO
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO
            }
        });

        TagCloudView tagCloudView = (TagCloudView) view.findViewById(R.id.tag_cloud);
        tagCloudView.setBackgroundColor(Color.LTGRAY);

        TextTagsAdapter tagsAdapter = new TextTagsAdapter(new String[20]);
        tagCloudView.setAdapter(tagsAdapter);

        return view;
    }
}
