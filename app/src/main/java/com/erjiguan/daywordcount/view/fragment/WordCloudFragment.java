package com.erjiguan.daywordcount.view.fragment;

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
import com.erjiguan.daywordcount.utils.BarChartManager;
import com.erjiguan.wordcloudviewlib.WordCloudView;
import com.github.mikephil.charting.charts.BarChart;
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

    private BarChart barChartView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wordcloud_fragment, container, false);

        // TODO 删掉。临时用的测试数据
        ArrayList<ArrayList<Object> > dataList = new ArrayList<ArrayList<Object>>();
        dataList.add(new ArrayList<Object>() {{
            add("android");
            add(50);
        }});
        dataList.add(new ArrayList<Object>() {{
            add("java");
            add(45);
        }});
        dataList.add(new ArrayList<Object>() {{
            add("ojjective-c");
            add(40);
        }});
        dataList.add(new ArrayList<Object>() {{
            add("golang");
            add(35);
        }});
        dataList.add(new ArrayList<Object>() {{
            add("python");
            add(30);
        }});
        dataList.add(new ArrayList<Object>() {{
            add("c++");
            add(25);
        }});
        dataList.add(new ArrayList<Object>() {{
            add("html5");
            add(20);
        }});
        dataList.add(new ArrayList<Object>() {{
            add("css");
            add(15);
        }});
        dataList.add(new ArrayList<Object>() {{
            add("javascript");
            add(10);
        }});
        dataList.add(new ArrayList<Object>() {{
            add("android");
            add(8);
        }});
        dataList.add(new ArrayList<Object>() {{
            add("iOS");
            add(5);
        }});
        dataList.add(new ArrayList<Object>() {{
            add("machine learning");
            add(4);
        }});
        dataList.add(new ArrayList<Object>() {{
            add("hadoop");
            add(4);
        }});
        dataList.add(new ArrayList<Object>() {{
            add("android");
            add(4);
        }});
        dataList.add(new ArrayList<Object>() {{
            add("android");
            add(4);
        }});
        dataList.add(new ArrayList<Object>() {{
            add("android");
            add(3);
        }});
        dataList.add(new ArrayList<Object>() {{
            add("android");
            add(2);
        }});
        dataList.add(new ArrayList<Object>() {{
            add("android");
            add(1);
        }});
        dataList.add(new ArrayList<Object>() {{
            add("android");
            add(1);
        }});
        dataList.add(new ArrayList<Object>() {{
            add("android");
            add(1);
        }});

        final TagCloudView tagCloudView = (TagCloudView) view.findViewById(R.id.tag_cloud_view);
        tagCloudView.setBackgroundColor(Color.LTGRAY);
        TextTagsAdapter tagsAdapter = new TextTagsAdapter(new String[20]);
        tagCloudView.setAdapter(tagsAdapter);

        final WordCloudView wordCloudView = (WordCloudView) view.findViewById(R.id.word_cloud_view);
        // TODO 这里需要去改一下原来的WordCloudView，用一个更方便的方法来输入数据（二维List）
        // "android", "java", "c", "c++", "html5", "js", "css", "javase", "javaee"
        wordCloudView.addTextView("android", 50);
        wordCloudView.addTextView("java", 45);
        wordCloudView.addTextView("objective-c", 40);
        wordCloudView.addTextView("c++", 35);
        wordCloudView.addTextView("html5", 30);
        wordCloudView.addTextView("js", 25);
        wordCloudView.addTextView("css", 20);
        wordCloudView.addTextView("javase", 15);
        wordCloudView.addTextView("javaee", 10);

        // 柱状图
        createBarChartView(view, dataList);

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
                switch (i) {
                    case 0:  // 0表示3D词云
                        tagCloudView.setVisibility(View.VISIBLE);
                        wordCloudView.setVisibility(View.GONE);
                        barChartView.setVisibility(View.GONE);

                        break;
                    case 1:  // 1表示2D词云
                        tagCloudView.setVisibility(View.GONE);
                        wordCloudView.setVisibility(View.VISIBLE);
                        barChartView.setVisibility(View.GONE);

                        break;

                    case 2:  // 2表示柱状图
                        tagCloudView.setVisibility(View.GONE);
                        wordCloudView.setVisibility(View.GONE);
                        barChartView.setVisibility(View.VISIBLE);

                        break;
                }
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

        return view;
    }

    public void createTagCloudView(View view, ArrayList<ArrayList<Object> > dataList) {

    }

    public void createWordCloudView(View view, ArrayList<ArrayList<Object> > dataList) {

    }

    public void createBarChartView(View view, ArrayList<ArrayList<Object> > dataList) {
        barChartView = view.findViewById(R.id.bar_chart_view);
        BarChartManager barChartManager = new BarChartManager(barChartView);

        ArrayList<String> xVals = new ArrayList<>();
        ArrayList<Integer> yVals = new ArrayList<>();

        for (int i = 0; i < dataList.size(); i++) {
            String word = (String) dataList.get(i).get(0);
            int count = (int) dataList.get(i).get(1);

            xVals.add(word);
            yVals.add(count);
        }

        barChartManager.showBarChart(xVals, yVals, "词频统计", Color.BLUE);
    }
}
