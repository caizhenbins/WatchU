package com.inbin.watchu.managers;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.inbin.watchu.sharedpreferences.UserSharedPreTool;

import org.json.JSONObject;

import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.listener.ValueEventListener;

/**
 * Created by InBin time: 2017/3/30.
 * Author: caizhenbin
 * Email: caizhenbins@163.com
 * PackName: com.inbin.watchu.managers
 */

public class SecurityManager {

    /**
     * 监听数据实时更新
     */
    public static void listener(Activity activity){
        //开始连接服务器
        BmobRealTimeData rtd = new BmobRealTimeData();
        rtd.start(new ValueEventListener() {
            @Override
            public void onConnectCompleted(Exception e) {
               if (e==null){
                   Log.i("info", "连接成功");
               }
            }
            @Override
            public void onDataChange(JSONObject jsonObject) {
                Log.i("info","<<<监听数据>>>:" + jsonObject.toString());
            }
        });
    }
}
