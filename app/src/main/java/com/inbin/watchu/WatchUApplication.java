package com.inbin.watchu;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.baidu.mapapi.SDKInitializer;
import com.inbin.watchu.managers.SecurityManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;


/**
 * Created by InBin time: 2017/3/18.
 * Author: caizhenbin
 * Email: caizhenbins@163.com
 * PackName: com.inbin.watchu
 */

public class WatchUApplication extends Application implements View.OnClickListener {


    @Override
    public void onCreate() {
        super.onCreate();
        //初始化百度地图
        SDKInitializer.initialize(getApplicationContext());
        //初始化Bmob
        initBmob();
    }

    /**
     * 初始化Bmob
     */
    public void initBmob(){
        Bmob.initialize(this,"e301c93539072120a30af311b0221eef");

        //如有需要可设置请求超时连接、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        //第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        BmobConfig config =new BmobConfig.Builder(this)
        ////设置appkey
        .setApplicationId("e301c93539072120a30af311b0221eef")
        ////请求超时时间（单位为秒）：默认15s
        .setConnectTimeout(30)
        ////文件分片上传时每片的大小（单位字节），默认512*1024
        .setUploadBlockSize(1024*1024)
        ////文件的过期时间(单位为秒)：默认1800s
        .setFileExpiration(2500)
        .build();
        Bmob.initialize(config);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case 0:
                Log.i("info", "设置密码");
                break;
            default:break;
        }
    }

//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base); MultiDex.install(this);
//
//    }

    /**
     * 获取当前运行的进程名
     * @return
     */
//    public static String getMyProcessName() {
//        try {
//            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
//            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
//            String processName = mBufferedReader.readLine().trim();
//            mBufferedReader.close();
//            return processName;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
}
