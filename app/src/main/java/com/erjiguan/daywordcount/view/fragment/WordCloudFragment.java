package com.erjiguan.daywordcount.view.fragment;

import android.app.Fragment;
import android.content.pm.ActivityInfo;
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
import com.erjiguan.daywordcount.controller.DBController;
import com.erjiguan.daywordcount.global.DBControllerInstance;
import com.erjiguan.daywordcount.view.view_manager.BarChartManager;
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

    private TagCloudView tagCloudView;
    private WordCloudView wordCloudView;
    private BarChart barChartView;

    final private int IN_TAG_CLOUD_VIEW = 0;
    final private int IN_WORD_CLOUD_VIEW = 1;
    final private int IN_BAR_CHART_VIEW = 2;

    private int CURRENT_VIEW = IN_TAG_CLOUD_VIEW;

    private static DBController dbController = DBControllerInstance.dbController;

    ArrayList<ArrayList<Object> > dataList;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        dataList = dbController.getWordFreqData(DBController.INTENSIVE);

        this.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final View view = inflater.inflate(R.layout.wordcloud_fragment, container, false);

        // 3D词云图
        createTagCloudView(view, dataList);

        // 2D词云图
        createWordCloudView(view, dataList);

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
                        CURRENT_VIEW = IN_TAG_CLOUD_VIEW;
                        break;
                    case 1:  // 1表示2D词云
                        tagCloudView.setVisibility(View.GONE);
                        wordCloudView.setVisibility(View.VISIBLE);
                        barChartView.setVisibility(View.GONE);
                        CURRENT_VIEW = IN_WORD_CLOUD_VIEW;
                        break;
                    case 2:  // 2表示柱状图
                        tagCloudView.setVisibility(View.GONE);
                        wordCloudView.setVisibility(View.GONE);
                        barChartView.setVisibility(View.VISIBLE);
                        CURRENT_VIEW = IN_BAR_CHART_VIEW;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
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
            public void onItemSelected(AdapterView<?> adapterView, View arg1, int i, long l) {
                switch (i) {
                    case 0:  // 0表示密集
                        dataList = dbController.getWordFreqData(DBController.INTENSIVE);
                        break;
                    case 1:  // 1表示适量
                        dataList = dbController.getWordFreqData(DBController.MODERATE);
                        break;
                    case 2:  // 2表示稀疏
                        dataList = dbController.getWordFreqData(DBController.SPARSE);
                        break;
                }

                createTagCloudView(view, dataList);
                createWordCloudView(view, dataList);
                createBarChartView(view, dataList);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        return view;
    }

    public void createTagCloudView(View view, ArrayList<ArrayList<Object> > dataList) {
        tagCloudView = (TagCloudView) view.findViewById(R.id.tag_cloud_view);
        tagCloudView.setBackgroundColor(Color.LTGRAY);
        TextTagsAdapter tagsAdapter = new TextTagsAdapter(dataList);
        tagCloudView.setAdapter(tagsAdapter);
        tagCloudView.invalidate();
    }

    public void createWordCloudView(View view, ArrayList<ArrayList<Object> > dataList) {
        wordCloudView = (WordCloudView) view.findViewById(R.id.word_cloud_view);
        wordCloudView.removeAllViews();
        wordCloudView.CleanWordCloudView();
        for (int i = 0; i < dataList.size(); i++) {
            String word = (String) dataList.get(i).get(0);
            int weight = (int) dataList.get(i).get(1);
            wordCloudView.addTextView(word, weight);
        }
        wordCloudView.invalidate();
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
        barChartView.invalidate();
    }
}
