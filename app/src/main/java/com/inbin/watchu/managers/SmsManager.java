package com.inbin.watchu.managers;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.inbin.watchu.admin.DeviceAdminManager;
import com.inbin.watchu.sharedpreferences.UserSharedPreTool;
import com.inbin.watchu.utils.TomastShow;

import java.util.ArrayList;

/**
 * Created by InBin time: 2017/4/15.
 * Author: caizhenbin
 * Email: caizhenbins@163.com
 * PackName: com.inbin.watchu.managers
 */

public class SmsManager extends Activity{

    private static String msgId;
    //短信类型 何种操作
    public static final int LOCK_NOM = 1;
    public static final int WIPE_DATA = 3;
    public static final int WARN_ING = 2;
    private int kind;
    /**
     * 发送短信
     * @param activity
     * @param text
     */
    public static void senMessage(Activity activity, String text, ContentResolver contentResolver){
        String address = UserSharedPreTool.getUserFormLocal(activity, new ArrayList<String>()).get(4);
        android.telephony.SmsManager manager =  android.telephony.SmsManager.getDefault();
          ArrayList<String> message = manager.divideMessage(text);
        for (String msg : message){
            if (address != null){
                manager.sendTextMessage(address, null,msg,null, null);
                TomastShow.lowTomast(activity, "指令发送成功");
            }else {
                TomastShow.lowTomast(activity, "请登录或者绑定有效的安全手机号");
            }
        }
    }



    /**
     * 监听短信接受变化
     */
    public static void inBoxMessage(Handler handler, ContentResolver contentResolver, String phone){
        //监听短信数据库
        Cursor cursor = contentResolver.query(Uri.parse("content://sms/inbox"),
                new String[] { "_id", "address", "read", "body", "thread_id" },
                "read=?", new String[] { "0" }, "date desc");
        if (cursor == null){     //没有接收到
            return;
        }else {                   //接收到
            while (cursor.moveToNext()){
                //获取短信内容
                StringBuilder sb = new StringBuilder();
                //发送者 手机号
                String acceptAddress = cursor.getString(cursor.getColumnIndex("address"));
                if (acceptAddress.equals(phone)){
                    //短信内容
                     sb.append(cursor.getString(cursor.getColumnIndex("body")));
                    String body = cursor.getString(cursor.getColumnIndex("body"));
                    if (body.equals("1")){
                        Message msga = new Message();
                        msga.what = LOCK_NOM;                //一键锁屏
                        handler.sendMessage(msga);
                        break;
                    }else if (body.equals("2")){
                        Message msgb = new Message();
                        msgb.what = WARN_ING;                //发送警告
                        handler.sendMessage(msgb);
                    }else if (body.equals("3")){
                        Message msgc = new Message();
                        msgc.what = WIPE_DATA;               //抹除数据
                        handler.sendMessage(msgc);
                        break;
                    }
                }
            }
        }
        cursor.close();
    }

    /**
     //     *删除所发短信
     //     * @param cr
     //     */
    public static void deleteMeaasge(ContentResolver cr){
        Cursor cursor = cr.query(Uri.parse("content://sms/outbox/"),
                new String[] { "_id", "address", "read", "body", "thread_id" },
                "read=?", new String[] { "0" }, "date desc");
        if (cursor == null){     //发送失败
            return;
        }else {                   //发送成功
            if (cursor.moveToNext()){
                //删除所发短信
                msgId = cursor.getString(cursor.getColumnIndex("_id"));
                cr.delete(Uri.parse("content://sms/"),"_id" + cursor.getString(cursor.getColumnIndex("_id")),null);
                }
            }
        cursor.close();
        }
}
