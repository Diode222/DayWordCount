package com.erjiguan.daywordcount.global;

public class GlobalNumber {

    // 通知的id，用于NotificationManager的notify
    final public static int NOTIFICATION_ID = 126;

    // 词云大小相关常量
    final public static int WORD_CLOUD_MAX_SIZE = 50;
    final public static int WORD_CLOUD_MIN_SIZE = 15;
    final public static int WORD_CLOUD_DEFAULT_SIZE = 50;

    // 展示数据量大小相关常量
    final public static int DATA_AMOUNT_INTENSIVE = 50;
    final public static int DATA_AMOUNT_MODERATE = 30;
    final public static int DATA_AMOUNT_SPARSE = 20;

    // 本地临时存储到达多少条微信输入消息才会在刷新时请求新数据
    final public static int MAX_MESSAGE_STORAGE_COUNT_TO_REQUEST_NEW = 10;

    // 当本地数据库存有很多聊天数据时，拆分为多个包，每个包最多包含20条数据
    final public static int MAX_SIZE_OF_MESSAGE_IN_PACKAGE = 20;
}
