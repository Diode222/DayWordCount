package com.erjiguan.daywordcount.view.fragment;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.erjiguan.daywordcount.R;
import com.erjiguan.daywordcount.WordFreqProtos;
import com.erjiguan.daywordcount.adapter.TextTagsAdapter;
import com.erjiguan.daywordcount.controller.DBController;
import com.erjiguan.daywordcount.global.DBControllerInstance;
import com.erjiguan.daywordcount.global.GlobalNumber;
import com.erjiguan.daywordcount.global.RequestAddressInfo;
import com.erjiguan.daywordcount.network.NetInfo;
import com.erjiguan.daywordcount.utils.SubArrayList;
import com.erjiguan.daywordcount.view.view_manager.BarChartManager;
import com.erjiguan.wordcloudviewlib.WordCloudView;
import com.github.mikephil.charting.charts.BarChart;
import com.moxun.tagcloudlib.view.TagCloudView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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

    final private int INTENSIVE_DATA = 0;
    final private int MODERATE_DATA = 1;
    final private int SPARSE_DATA = 2;

    private int CURRENT_DATA_AMOUNT = INTENSIVE_DATA;

    private static DBController dbController = DBControllerInstance.dbController;

    ArrayList<ArrayList<Object> > dataList = new ArrayList<ArrayList<Object> >();

    SwipeRefreshLayout swipeRefreshLayout;

    Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.wordcloud_fragment, container, false);
        initSwipeFresh(view);
        updateData(getActivity(), DBController.INTENSIVE, "noun", view);

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
                SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.word_cloud_fragment_swipe_fresh);
                swipeRefreshLayout.setRefreshing(true);
                switch (i) {
                    case 0:  // 0表示密集
                        dataList = dbController.getWordFreqData(DBController.INTENSIVE);
                        CURRENT_DATA_AMOUNT = INTENSIVE_DATA;
                        break;
                    case 1:  // 1表示适量
                        dataList = dbController.getWordFreqData(DBController.MODERATE);
                        CURRENT_DATA_AMOUNT = MODERATE_DATA;
                        break;
                    case 2:  // 2表示稀疏
                        dataList = dbController.getWordFreqData(DBController.SPARSE);
                        CURRENT_DATA_AMOUNT = SPARSE_DATA;
                        break;
                }

                createTagCloudView(view, dataList);
                createWordCloudView(view, dataList);
                createBarChartView(view, dataList);

                swipeRefreshLayout.setRefreshing(false);
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
        // 对词云中词云大小进行量化
        if (dataList.size() <= 1) {
            for (int i = 0; i < dataList.size(); i++) {
                String word = (String) dataList.get(i).get(0);
                int quantificationWeight = GlobalNumber.WORD_CLOUD_DEFAULT_SIZE;
                wordCloudView.addTextView(word, quantificationWeight);
            }
        } else if (dataList.get(0).get(1) == dataList.get(dataList.size() - 1).get(1)) {
            for (int i = 0; i < dataList.size(); i++) {
                String word = (String) dataList.get(i).get(0);
                int quantificationWeight = (int) (GlobalNumber.WORD_CLOUD_DEFAULT_SIZE / 2.0f);
                wordCloudView.addTextView(word, quantificationWeight);
            }
        } else {
            int maxCount = (int) dataList.get(0).get(1);
            int minCount = (int) dataList.get(dataList.size() - 1).get(1);
            for (int i = 0; i < dataList.size(); i++) {
                String word = (String) dataList.get(i).get(0);
                int quantificationWeight = getQuantificationWeight((int) dataList.get(i).get(1), maxCount, minCount);
                wordCloudView.addTextView(word, quantificationWeight);
            }
        }
        wordCloudView.invalidate();
    }

    public int getQuantificationWeight(int count, int maxCount, int minCount) {
        return (int) ((float) (GlobalNumber.WORD_CLOUD_MAX_SIZE - GlobalNumber.WORD_CLOUD_MIN_SIZE) / (float) (maxCount - minCount) * (float) (count - minCount) + (float) GlobalNumber.WORD_CLOUD_MIN_SIZE);
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

    public void initSwipeFresh(final View view) {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.word_cloud_fragment_swipe_fresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                switch (CURRENT_DATA_AMOUNT) {
                    case INTENSIVE_DATA:
                        updateData(getActivity(), DBController.INTENSIVE, "noun", view);
                        break;
                    case MODERATE_DATA:
                        updateData(getActivity(), DBController.MODERATE, "noun", view);
                        break;
                    case SPARSE_DATA:
                        updateData(getActivity(), DBController.SPARSE, "noun", view);
                        break;
                    default:
                        updateData(getActivity(), DBController.INTENSIVE, "noun", view);
                }
            }
        });
    }

    private void updateData(Context context, int dataAmount, String pos, final View view) {
        if (NetInfo.IsNetworkAvailable(context)) {
            transmissionMessageDataAndRequest(dataAmount, pos, view);
        } else {
            swipeRefreshLayout.setRefreshing(false);
            dataList = dbController.getWordFreqData(dataAmount);
        }
    }

    private void transmissionMessageDataAndRequest(final int dataAmount, String pos, final View view) {
        final ArrayList<ArrayList<Object> > responseDataList = new ArrayList<ArrayList<Object> >();

        ArrayList<byte[]> chatMessageDataList = DBControllerInstance.dbController.getAllChatMessageTmpData();

        HttpUrl url = new HttpUrl.Builder().scheme("http").
                host(RequestAddressInfo.IP).
                port(RequestAddressInfo.PORT).
                addPathSegment("get").
                addQueryParameter("part_of_speech", ""+ pos).build();
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS).readTimeout(5, TimeUnit.SECONDS).build();

        Request request = (new Request.Builder()).url(url).get().build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("api_error", e.toString());
                e.printStackTrace();
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    WordFreqProtos.WordFreqList wordFreqList = WordFreqProtos.WordFreqList.parseFrom(response.body().bytes());
                    for (final WordFreqProtos.WordFreq wordFreq: wordFreqList.getWordFreqsList()) {
                        synchronized (this) {
                            responseDataList.add(new ArrayList<Object>() {{
                                add(wordFreq.getWord());
                                add(wordFreq.getCount());
                            }});
                        }
                    }
                    DBControllerInstance.dbController.setWordFreqData(responseDataList);

                    switch (dataAmount) {
                        case DBController.INTENSIVE:
                            dataList = SubArrayList.subList(responseDataList, 0, GlobalNumber.DATA_AMOUNT_INTENSIVE);
                            break;
                        case DBController.MODERATE:
                            dataList = SubArrayList.subList(responseDataList, 0, GlobalNumber.DATA_AMOUNT_MODERATE);
                            break;
                        case DBController.SPARSE:
                            dataList = SubArrayList.subList(responseDataList, 0, GlobalNumber.DATA_AMOUNT_SPARSE);
                            break;
                        default:
                            dataList = SubArrayList.subList(responseDataList, 0, GlobalNumber.DATA_AMOUNT_INTENSIVE);
                    }

                    if (dataList.size() > 0) {
                        DBControllerInstance.dbController.deleteAllChatMessageTmpData();
                    } else {
                        dataList = DBControllerInstance.dbController.getWordFreqData(dataAmount);
                    }

                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            // 3D词云图
                            createTagCloudView(view, dataList);

                            // 2D词云图
                            createWordCloudView(view, dataList);

                            // 柱状图
                            createBarChartView(view, dataList);
                        }
                    });
                } catch (IOException e) {
                    Log.d("api error", "response parse failed");
                    e.printStackTrace();
                } finally {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });
                }
            }
        });

        for (byte[] chatMessageData: chatMessageDataList) {
            RequestBody requestBody = FormBody.create(chatMessageData);
            Request.Builder requestBuilder = new Request.Builder();
            request = requestBuilder.url(url).post(requestBody).build();
            call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.d("api_error", e.toString());
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.code() == 200) {
                        DBControllerInstance.dbController.deleteAllChatMessageTmpData();
                    }
                }
            });
        }
    }
}
