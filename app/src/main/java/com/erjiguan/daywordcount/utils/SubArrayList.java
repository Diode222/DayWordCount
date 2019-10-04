package com.erjiguan.daywordcount.utils;

import java.util.ArrayList;

public class SubArrayList {

    public static ArrayList<ArrayList<Object> > subList(ArrayList<ArrayList<Object> > list, int startIndex, int endIndex) {
        int listSize = list.size();
        ArrayList<ArrayList<Object> > res = new ArrayList<ArrayList<Object> >();
        if (startIndex > listSize) {
            return res;
        }

        for (int i = startIndex; i < listSize && i < endIndex; i++) {
            res.add(list.get(i));
        }

        return res;
    }
}
