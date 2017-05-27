package com.inbin.watchu.recevice;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.inbin.watchu.admin.DeviceAdminManager;

/**
 * 一键锁屏
 */
public class LockNowReceiver extends BroadcastReceiver {

    public static Activity activity;
    public LockNowReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (activity != null){
            DeviceAdminManager.lockNow(activity);
        }
    }
}
