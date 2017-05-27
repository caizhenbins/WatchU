package com.inbin.watchu.recevice;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.media.MediaPlayer;
import android.telephony.SmsMessage;

import com.inbin.watchu.MainActivity;
import com.inbin.watchu.R;
import com.inbin.watchu.admin.DeviceAdminManager;
import com.inbin.watchu.sharedpreferences.UserSharedPreTool;
import com.inbin.watchu.utils.TomastShow;

import java.util.ArrayList;

public class SMSReceiver extends BroadcastReceiver {

    private String securityPhone;
    @Override
    public void onReceive(final Context context, Intent intent) {

        //手机设备管理器权限获取
        //安全手机号
        securityPhone = UserSharedPreTool.getUserFormLocal(context, new ArrayList<String>()).get(4);
        Object[] objs = (Object[]) intent.getExtras().get("pdus");
        if (securityPhone != null ){
            for(Object obj: objs) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
                String sender = smsMessage.getOriginatingAddress();
                try {
                    String body = smsMessage.getMessageBody();
                    TomastShow.lowTomast(context, body);
                    if (sender.contains(securityPhone)){
                        if (body.equals("1") ){
                            DeviceAdminManager.lockNow(context);          //一键锁屏
                        }else if (body.equals("2")){
                            DeviceAdminManager.playWarnning(context);       //播放警告
                        }else if (body.equals("3")){
                            DeviceAdminManager.wipeAllData(context);
                        }
                    }
                }catch (Exception e){
                }
            }
        }
    }
}
