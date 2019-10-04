package com.erjiguan.daywordcount.network;

import android.util.Log;

import com.erjiguan.daywordcount.WordFreqProtos;
import com.erjiguan.daywordcount.controller.DBController;
import com.erjiguan.daywordcount.global.DBControllerInstance;
import com.erjiguan.daywordcount.global.GlobalNumber;
import com.erjiguan.daywordcount.global.RequestAddressInfo;
import com.erjiguan.daywordcount.utils.SubArrayList;

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

public class Api {

    public static ArrayList<ArrayList<Object> > transmissionMessageDataAndRequest(int dataAmount, String pos) {
        final ArrayList<ArrayList<Object> > responseDataList = new ArrayList<ArrayList<Object> >();

        // 用于标识当前请求/响应是同一次
        final String requestFlag = Base64.getEncoder().encodeToString(("" + dataAmount + "" + pos + "" + System.currentTimeMillis()).getBytes());

        ArrayList<byte[]> chatMessageDataList = DBControllerInstance.dbController.getAllChatMessageTmpData();

        HttpUrl url = new HttpUrl.Builder().scheme("http").host(RequestAddressInfo.IP).port(RequestAddressInfo.PORT).
                                            addPathSegment("get").
                                            addQueryParameter("part_of_speech", ""+ pos).build();
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS).readTimeout(5, TimeUnit.SECONDS).build();
        for (byte[] chatMessageData: chatMessageDataList) {
            RequestBody requestBody = FormBody.create(chatMessageData);
            Request.Builder requestBuilder = new Request.Builder();
            Request request = requestBuilder.url(url).post(requestBody).addHeader("request_flag", requestFlag).build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.d("api_error", e.toString());
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    try {
                        String responseFlag = response.header("request_flag");
                        // 当本次请求不是循环内的第一次请求时，不做响应处理
                        if (responseFlag.equals(requestFlag)) {
                            return;
                        }

                        WordFreqProtos.WordFreqList wordFreqList = WordFreqProtos.WordFreqList.parseFrom(response.body().bytes());
                        ArrayList<Object> responseWordFreq = new ArrayList<Object>();
                        for (final WordFreqProtos.WordFreq wordFreq: wordFreqList.getWordFreqsList()) {
                            responseDataList.add(new ArrayList<Object>() {{
                                add(wordFreq.getWord());
                                add(wordFreq.getCount());
                            }});
                        }
                        responseDataList.add(responseWordFreq);
                        DBControllerInstance.dbController.setWordFreqData(responseDataList);

                    } catch (IOException e) {
                        Log.d("api error", "response parse failed");
                        e.printStackTrace();
                    }
                }
            });
        }

        ArrayList<ArrayList<Object> > res = new ArrayList<ArrayList<Object> >();
        switch (dataAmount) {
            case 0:
                res = SubArrayList.subList(responseDataList, 0, GlobalNumber.DATA_AMOUNT_INTENSIVE);
            case 1:
                res = SubArrayList.subList(responseDataList, 0, GlobalNumber.DATA_AMOUNT_MODERATE);
            case 2:
                res = SubArrayList.subList(responseDataList, 0, GlobalNumber.DATA_AMOUNT_SPARSE);
            default:
                res = SubArrayList.subList(responseDataList, 0, GlobalNumber.DATA_AMOUNT_INTENSIVE);
        }

        if (res.size() > 0) {
            // 真正获取到了从服务器返回的新数据，可以将本地存的聊天数据删掉了
            DBControllerInstance.dbController.deleteAllChatMessageTmpData();
            return res;
        } else {
            return DBControllerInstance.dbController.getWordFreqData(dataAmount);
        }
    }
}
