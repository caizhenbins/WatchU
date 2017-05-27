package com.inbin.watchu.recevice;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.inbin.watchu.managers.SmsManager;
import com.inbin.watchu.sharedpreferences.UserSharedPreTool;
import com.inbin.watchu.utils.TomastShow;

import java.util.ArrayList;

public class SIMReceiver extends BroadcastReceiver {
    public SIMReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        TomastShow.lowTomast(context, "SIM卡发生变化");
        //如果变动了Simm卡则发短信提醒
//        SmsManager.senMessage("绑定的用户手机SIM发生改变，这是当前手机号，如果不是机主可在防盗软件中进行相应操作");
    }
}
