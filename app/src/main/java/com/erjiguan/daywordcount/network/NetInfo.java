package com.erjiguan.daywordcount.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class NetInfo {

    public static boolean IsNetworkAvailable(Context context) {
        return isWiFiActive(context) || isNetworkAvailable(context);
    }

    private static boolean isWiFiActive(Context context) {
        WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        try {
            WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
            int ipAddress = wifiInfo == null ? 0 : wifiInfo.getIpAddress();
            if (mWifiManager.isWifiEnabled() && ipAddress != 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean isNetworkAvailable( Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        }

        NetworkInfo info = connectivity.getActiveNetworkInfo();
        if(info == null){
            return false;
        } else if (info.isAvailable()){
            return true;
        }
        return false;
    }
}
