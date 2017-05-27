package com.inbin.watchu.sharedpreferences;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.inbin.watchu.activities.RegistActivity;
import com.inbin.watchu.beans.UserBean;

import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * Created by InBin time: 2017/3/26.
 * Author: caizhenbin
 * Email: caizhenbins@163.com
 * PackName: com.inbin.watchu.tools
 */

public class UserSharedPreTool {


    public static final String USER_SP = "currentUser";    //用户信息
    public static final String USER = "username";
    public static final String USER_ID = "usernid";
    public static final String PASSWORD = "password";
    public static final String OLD_PASSWORD = "oldpassword";
    public static final String SAFE_PHONE = "safephone";
    public static final String SIM = "sim";
    public static final String DEV = "dev";
    //PrivateBean Id
    public static final String PRIVATEBEAN = "privateBean";    //位置信息
    public static final String USER_LOCAL_ID ="localId";

    //------设备信息------------------
    public static final String DEV_MAIN = "dev";
    public static final String DEV_SIM = "devSim";
    public static final String SIM_CURRENT = "currentsim";
    //安全用户信息
    public static final String SECURITY_USER = "securityuser";
    public static final String SECURITY_ID = "securityid";

    //启动密码相关
    public static final String LANUCH_PASSWORD = "lanuchpassword";
    public static final String INTER_PASSWORD = "password";

    /**
     * 保存用户信息在本地
     *
     * @param actiovity
     * @param usernbame
     * @param password
     * @param oldpassword
     */
    public static void saveUser(Activity actiovity, String usernbame,String id, String password, String oldpassword,
                                String safephone, String dev, String sim) {
        SharedPreferences user = actiovity.getSharedPreferences(USER_SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = user.edit();
        editor.putString(USER, usernbame);
        editor.putString(USER_ID, id);
        editor.putString(PASSWORD, password);
        editor.putString(OLD_PASSWORD, oldpassword);
        editor.putString(SAFE_PHONE, safephone);
        editor.putString(DEV, dev);
        editor.putString(SIM, sim);
        editor.commit();
    }

    /**
     * 返回用户信息
     *
     * @param userList
     * @return
     */
    public static List<String> getUserFormLocal(Context activity, List<String> userList) {
        SharedPreferences user = activity.getSharedPreferences(USER_SP, Context.MODE_PRIVATE);
        String username = user.getString(USER, null);                 //用户
        userList.add(username);
        String id = user.getString(USER_ID, null);                 //用户Id
        userList.add(id);
        String password = user.getString(PASSWORD, null);             //密码
        userList.add(password);
        String oldpassword = user.getString(OLD_PASSWORD, null);     //旧密码
        userList.add(oldpassword);
        String safephone = user.getString(SAFE_PHONE, null);         //安全手机
        userList.add(safephone);
        String dev = user.getString(DEV, null);                       //Dev
        userList.add(dev);
        String sim = user.getString(SIM, null);                       //SIM
        userList.add(sim);
        return userList;
    }

    /**
     * 保存用户到本地
     * @param activity
     */
    public static void saveUserToLocal(Activity activity){
        TelephonyManager tm = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        UserBean currentUser = BmobUser.getCurrentUser(UserBean.class);
        String id = currentUser.getObjectId();
        String name = currentUser.getUsername();     //用户名
        String password = currentUser.getOldpassword();    //密码
        String oldpassword = currentUser.getOldpassword();  //旧密码 与密码保持一致
        String securityPhone = currentUser.getSecurityPhone();   //安全密码
        String sim = currentUser.getUsername();                                        //sim卡手机号
        String dev = tm.getSimSerialNumber();                                        //设备信息
        UserSharedPreTool.saveUser(activity, name,  id, password, oldpassword, securityPhone, dev, sim);
    }


    //-----------------PrivateBeanManaget-----------------

    /**
     * 保存位置信息
     * @param actiovity
     * @param id
     */
    public static void saveLocalPrivateBeanId(Activity actiovity, String id){
        SharedPreferences user = actiovity.getSharedPreferences(PRIVATEBEAN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = user.edit();
        editor.putString(USER_LOCAL_ID, id);
        if (!editor.commit()){
            editor.putString(USER_LOCAL_ID, "1");    //如果保存不成功便会保存
        }
    }

    /**
     * 获取位置Id
     * @param activity
     */
    public static String getLocalPrivateBeanId(Activity activity){
        SharedPreferences user = activity.getSharedPreferences(PRIVATEBEAN, Context.MODE_PRIVATE);
        String id = user.getString(USER_LOCAL_ID, "1");
        return id;
    }


    //-------------------安全用户相关------------------

    /**
     * 在修改安全手机号的时候保存安全手机的PrivateBean信息
     * @param activity
     * @param id
     */
    public static void saveSecurityUserId(Activity activity, String id){
        SharedPreferences user = activity.getSharedPreferences(SECURITY_USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = user.edit();
        editor.putString(SECURITY_ID, id);
        Log.i("info", "保全安全用户Id--------------");
        editor.commit();
    }

    /**
     * 获取安全用户的PrivateBean Id
     * @param activity
     */
    public static String getSEcurityUserId(Activity activity){
        SharedPreferences user = activity.getSharedPreferences(SECURITY_USER, Context.MODE_PRIVATE);
        //如何没有则会返回null
        String id = user.getString(SECURITY_ID,null);
        return id;
    }

    //------------安全密码相关---------------

    /**
     * 保存启动密码
     * @param activity
     * @param password
     */
    public static void saveLancuhPassword(Activity activity, String password){
        SharedPreferences user = activity.getSharedPreferences(LANUCH_PASSWORD, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = user.edit();
        editor.putString(INTER_PASSWORD, password);
        editor.commit();
    }

    /**
     * 获取安全密码
     * @param activity
     * @return
     */
    public static String getLanuchPassword(Context activity){
        SharedPreferences user = activity.getSharedPreferences(LANUCH_PASSWORD, Context.MODE_PRIVATE);
        //如何没有则会返回本用户的id
        String password = user.getString(INTER_PASSWORD, null);
        return password;
    }


    //------------保存本地的设备信息 SIM标识-----------
    public static void saveLocalDev(Activity activity){
        TelephonyManager tm = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        SharedPreferences user = activity.getSharedPreferences(DEV_MAIN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = user.edit();
        String sim = tm.getLine1Number();
        String dev = tm.getSimSerialNumber();
        editor.putString(DEV_SIM, sim);
        editor.putString(SIM_CURRENT, dev);
        editor.commit();
    }

    public static void getLocalDev(Activity activity){
        //先获取登录用户的sim信息
        TelephonyManager tm = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        SharedPreferences user = activity.getSharedPreferences(DEV_MAIN, Context.MODE_PRIVATE);
        String sim = user.getString(DEV_SIM, "1");
        String dev = user.getString(SIM_CURRENT, "1");
        Log.i("info", "sim :" + sim + "   dev: " + dev);

    }
}
