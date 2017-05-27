package com.inbin.watchu.managers;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.inbin.watchu.MainActivity;
import com.inbin.watchu.R;
import com.inbin.watchu.beans.PrivateBean;
import com.inbin.watchu.sharedpreferences.UserSharedPreTool;
import com.inbin.watchu.users.PrivateManager;
import com.inbin.watchu.utils.TomastShow;

/**
 * Created by InBin time: 2017/4/18.
 * Author: caizhenbin
 * Email: caizhenbins@163.com
 * PackName: com.inbin.watchu.managers
 */

public class OrderDialogManager {


    //发送短信 一键锁屏
    static AlertDialog lockDialog;
    static Button mLockSendMeaasgeLockNow;
    static Button mLockSendInternetTrue;
    static Button mLockSendInternetFalse;
    /**
     * 一键锁屏 dialog
     */
    public static void dialogSenMeaasgeLockNow(final Activity activity, final PrivateBean bean, final String id){
        //创建对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final View lockView = LayoutInflater.from(activity).inflate(R.layout.dialog_send_lock_now, null);
        //短信View
        mLockSendMeaasgeLockNow = (Button) lockView.findViewById(R.id.lock_now_send_message);
        //点击时间的添加
        mLockSendMeaasgeLockNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SmsManager.senMessage(activity, "1", activity.getContentResolver());    //发送短信"1"  一键锁屏
                lockDialog.dismiss();
            }
        });

        //网络 true
        mLockSendInternetTrue = (Button) lockView.findViewById(R.id.lock_now_send_true);
        mLockSendInternetTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrivateManager.updateSecurityPrivateBean(activity, bean,id,true, false, false );        //网络 一键锁屏 true
                lockDialog.dismiss();
            }
        });
        //网络 false
        mLockSendInternetFalse = (Button) lockView.findViewById(R.id.lock_now_send_false);              //网络 一键锁屏 false
        mLockSendInternetFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrivateManager.updateSecurityPrivateBean(activity, bean,id,false, false, false );        //网络 一键锁屏 true
                lockDialog.dismiss();
            }
        });
        //自定义布局对话框 显示
        builder.setView(lockView);
        builder.setCancelable(true);
        lockDialog = builder.show();
    }


    //发送短信 警告
    static AlertDialog warnDialog;
    static Button mWarnSendMeaasgeWarn;
    static Button mWarnSendInternetTrue;
    static Button mWarnSendInternetFalse;
    /**
     * 警告 dialog
     */
    public static void dialogSenMeaasgeWarn(final Activity activity, final PrivateBean bean, final String id){
        //创建对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final View warnView = LayoutInflater.from(activity).inflate(R.layout.dialog_send_warn, null);
        //短信View
        mWarnSendMeaasgeWarn = (Button) warnView.findViewById(R.id.warn_send_message);
        //点击时间的添加
        mWarnSendMeaasgeWarn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SmsManager.senMessage(activity, "2", activity.getContentResolver());    //发送短信"2"  警告
                warnDialog.dismiss();
            }
        });

        //网络 true
        mWarnSendInternetTrue = (Button) warnView.findViewById(R.id.warn_send_true);
        mWarnSendInternetTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrivateManager.updateSecurityPrivateBean(activity, bean,id,false, true, false );        //网络 警告 true
                warnDialog.dismiss();
            }
        });
        //网络 false
        mWarnSendInternetFalse = (Button) warnView.findViewById(R.id.warn_send_false);
        mWarnSendInternetFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrivateManager.updateSecurityPrivateBean(activity, bean,id,false, false, false );        //网络 警告 false
                warnDialog.dismiss();
            }
        });

        //自定义布局对话框 显示
        builder.setView(warnView);
        builder.setCancelable(true);
        warnDialog = builder.show();
    }

    //发送短信 一键锁屏
    static AlertDialog wipeDialog;
    static Button mSendMeaasgeWipe;
    static Button mWipeSendInternetTrue;
    static Button mWipeSendInternetFalse;
    /**
     * 数据抹除dialog
     */
    public static void dialogSenMeaasgeWipe(final Activity activity, final PrivateBean bean, final String id){
        //创建对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final View wipeView = LayoutInflater.from(activity).inflate(R.layout.dialog_send_wipe, null);
        //短信View
        mSendMeaasgeWipe = (Button) wipeView.findViewById(R.id.wipe_send_message);
        //点击时间的添加
        mSendMeaasgeWipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SmsManager.senMessage(activity, "3", activity.getContentResolver());    //发送短信"3"  数据抹除
                wipeDialog.dismiss();
            }
        });

        //网络 true
        mWipeSendInternetTrue = (Button) wipeView.findViewById(R.id.wipe_send_true);
        mWipeSendInternetTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrivateManager.updateSecurityPrivateBean(activity, bean,id,false, false, true );        //网络 数据抹除 true
                wipeDialog.dismiss();
            }
        });
        //网络 false
        mWipeSendInternetFalse = (Button) wipeView.findViewById(R.id.wipe_send_false);              //网络 数据抹除 false
        mWipeSendInternetFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrivateManager.updateSecurityPrivateBean(activity, bean,id,false, false, false);
                wipeDialog.dismiss();
            }
        });
        //自定义布局对话框 显示
        builder.setView(wipeView);
        builder.setCancelable(true);
        wipeDialog = builder.show();
    }

}
