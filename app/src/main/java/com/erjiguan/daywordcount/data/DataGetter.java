package com.erjiguan.daywordcount.data;

import android.content.Context;

import com.erjiguan.daywordcount.controller.DBController;
import com.erjiguan.daywordcount.network.Api;
import com.erjiguan.daywordcount.network.NetInfo;

import java.util.ArrayList;

public class DataGetter {

    public static ArrayList<ArrayList<Object> > get(Context context, int dataAmount, String pos, DBController dbController) {
        ArrayList<ArrayList<Object> > dataList = new ArrayList<ArrayList<Object> >();
        if (NetInfo.IsNetworkAvailable(context)) {
            return Api.transmissionMessageDataAndRequest(dataAmount, pos);
        } else {
            return dbController.getWordFreqData(dataAmount);
        }
    }
}
