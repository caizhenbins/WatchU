package com.inbin.watchu.admin;


import android.database.ContentObserver;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.inbin.watchu.managers.SmsManager;
import com.inbin.watchu.sharedpreferences.UserSharedPreTool;

/**
 * Created by InBin time: 2017/4/14.
 * Author: caizhenbin
 * Email: caizhenbins@163.com
 * PackName: com.inbin.watchu.admin
 */

public class SmsObserver extends ContentObserver {

    private ContentResolver contentResolver;
    private Handler handler;
    private String phone;
    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public SmsObserver(Handler handler, ContentResolver contentResolver, String phone) {
        super(handler);
        this.contentResolver = contentResolver;
        this.handler = handler;
        this.phone = phone;
    }
    @Override
    public void onChange(boolean selfChange) {
         //监听短信数据库
           SmsManager.inBoxMessage(handler, contentResolver, phone);   //接收到短信
        super.onChange(selfChange);
    }
}
