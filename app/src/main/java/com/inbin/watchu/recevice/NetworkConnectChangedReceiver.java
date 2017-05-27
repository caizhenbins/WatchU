package com.inbin.watchu.recevice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.inbin.watchu.utils.TomastShow;

public class NetworkConnectChangedReceiver extends BroadcastReceiver {


    public NetworkConnectChangedReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //获取网络服务
        ConnectivityManager coonnManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = coonnManager.getActiveNetworkInfo();
        if (!((info != null) && (info.isConnected())) ) {
            TomastShow.lowTomast(context, "请连接网络以获得更好的服务");
        }
    }
}
