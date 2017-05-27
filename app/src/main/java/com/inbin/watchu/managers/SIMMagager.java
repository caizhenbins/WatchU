package com.inbin.watchu.managers;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by InBin time: 2017/3/23.
 * Author: caizhenbin
 * Email: caizhenbins@163.com
 * PackName: com.inbin.watchu.managers
 */

public class SIMMagager {

    private TelephonyManager tm;

    /**
     * 获取SIM序列号
     * @param activity
     */
    public  String saveSIM(Activity activity){
        tm = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        String simSrialNum = tm.getSimSerialNumber();
        Log.i("info", "序列号为:" + simSrialNum);
        return simSrialNum;
    }


}
