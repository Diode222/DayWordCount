package com.erjiguan.daywordcount.presenter;

import java.util.ArrayList;

public class DataFormatter {

    final public static int INTENSIVE = 0;
    final public static int MODERATE = 1;
    final public static int SPARSE = 2;

    // TODO
    public static ArrayList<ArrayList<Object> > getFormatedData(int dataAmount) {
        ArrayList<ArrayList<Object> > dataList;
        switch (dataAmount) {
            case INTENSIVE:
                dataList = new ArrayList<ArrayList<Object>>();
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
                break;
            case MODERATE:
                dataList = new ArrayList<ArrayList<Object>>();
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
                break;
            case SPARSE:
                dataList = new ArrayList<>();
                break;
            default:
                // 默认为INTENSIVE
                dataList = new ArrayList<ArrayList<Object>>();
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
        }

        return dataList;
    }
}
