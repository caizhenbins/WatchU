package com.inbin.watchu.admin;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.inbin.watchu.R;
import com.inbin.watchu.sharedpreferences.UserSharedPreTool;
import com.inbin.watchu.utils.TomastShow;

/**
 * Created by InBin time: 2017/4/14.
 * Author: caizhenbin
 * Email: caizhenbins@163.com
 * PackName: com.inbin.watchu.admin
 */

public class DeviceAdminManager {

    private static final int REQUEST_CODE_ENABLE_ADMIN = 0;
    private static DevicePolicyManager dpm;

    public static void getAdmin(Context activity){
        //获取设备管理员权限
        dpm = (DevicePolicyManager) activity.getSystemService(Context.DEVICE_POLICY_SERVICE);

    }

    /**
     * 一键锁屏
     */
    public static void lockNow(Context activity){

        if (dpm == null){
            getAdmin(activity);
        }
        if (isAdmin(activity) && dpm != null){
            dpm.lockNow();
            //一键锁屏之后需要重设开启密码 注意 指纹还是可以使用的
            String password = UserSharedPreTool.getLanuchPassword(activity);
            if (password != null){
                dpm.resetPassword(password,0);
            }
        }else {
            TomastShow.lowTomast(activity, "没有权限");
        }
    }
    /**
     * 抹除信息
     * @param activity
     */
    public static void wipeAllData(Context activity){
        if (dpm == null){
            getAdmin(activity);
        }
        if (isAdmin(activity) && dpm != null){
            //0 恢复出厂设置
            dpm.wipeData(0);
            Log.i("info", "清除数据");
        }
    }

    /**
     * 播放警告
     * @param activity
     */
    public static void playWarnning(Context activity){
        MediaPlayer player = MediaPlayer.create(activity, R.raw.a);
        player.setLooping(true);     //循环播放
        player.start();
    }

    /**
     * 打开用户管理权限 授权
     * @param activity
     */
    public static void openAdmin(Activity activity){
        // Launch the activity to have the user enable our admin.
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        //激活广播
        ComponentName mDeviceAdminSample = new ComponentName(activity, DeviceAdminReciver.class);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                activity.getString(R.string.add_admin_extra_app_text));
        //启动手机Administration授权界面
        activity.startActivityForResult(intent, REQUEST_CODE_ENABLE_ADMIN);
    }

    /**
     * 设备管理启动与关闭
     * @param activity
     */
    public static void settingOrCloseAdmin(Activity activity){
        ComponentName mDeviceAdminSample = new ComponentName(activity, DeviceAdminReciver.class);
        if (isAdmin(activity)){
            dpm.removeActiveAdmin(mDeviceAdminSample);    //移除设备管理器权限
            TomastShow.lowTomast(activity, "已关闭设备管理器");
        }else {
            openAdmin(activity);                         //打开管理器管理器
        }
    }

    /**
     * 检查是否为管理员
     * @param activity
     * @return
     */
    public static boolean isAdmin(Context activity){
        ComponentName mDeviceAdminSample = new ComponentName(activity, DeviceAdminReciver.class);
        if (dpm == null){
            getAdmin(activity);
        }
        if (dpm.isAdminActive(mDeviceAdminSample)){
            return true;
        }else {
            return false;
        }
    }



}
