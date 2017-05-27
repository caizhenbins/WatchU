package com.inbin.watchu.users;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.inbin.watchu.admin.DeviceAdminManager;
import com.inbin.watchu.beans.PrivateBean;
import com.inbin.watchu.beans.UserBean;
import com.inbin.watchu.sharedpreferences.UserSharedPreTool;
import com.inbin.watchu.utils.TomastShow;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by InBin time: 2017/3/29.
 * Author: caizhenbin
 * Email: caizhenbins@163.com
 * PackName: com.inbin.watchu.users
 */

public class PrivateManager {
    private static PrivateManager instance;
    //保存位置信息成功
    public static final int SAVE_LOCALTION_SUCCESS = 0x11;
    //查询PrivateBean Id
    public static final int GET_PRIVATEBEAN_SUCCESS = 0x77;
    public static final int GET_PRIVATEBEAN_FAIL = 0x99;
    //获取本地用户的PrivateBean信息 是否决定抹除信息 发送警告 一键锁屏
    public static final int LOCK_NOW_REMOTE = 0x911;
    public static final int WIPE_DATA_REMOTE = 0x926;
    public static final int WARING__REMOTE = 0x926;
    //---------PrivateBean相关------------------

    public static PrivateManager getPrivateManager() {
        if (instance == null) {
            instance = new PrivateManager();
        }
        return instance;
    }


    /**
     * 上传当前用户的PrivateBean信息
     *
     * @param user
     * @param hanlder
     */
    public static void saveCurrentUserPrivateBean(UserBean user, final Handler hanlder) {
        PrivateBean p = new PrivateBean();
        p.setUser(user);
        p.setLatitude("0");
        p.setLongitude("0");
        p.setLocation("0");
        p.setClean(false);
        p.setPhoto(false);
        p.setWarnning(false);
        ;
        p.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Message msg = new Message();
                    msg.what = SAVE_LOCALTION_SUCCESS;
                    msg.obj = s;                            //与用户相对应的PrivateBean Id
                    hanlder.sendMessage(msg);
                }
            }
        });
    }

    /**
     * 查询当前登录用户所绑定安全手机的PrivateBean实例
     *
     * @param id
     * @param handler
     */
    public static void getSecurityPrivateBean(String id, final Handler handler) {
        BmobQuery<PrivateBean> query = new BmobQuery<>();
        query.getObject(id, new QueryListener<PrivateBean>() {
            @Override
            public void done(PrivateBean bean, BmobException e) {
                if (e == null) {
                    Log.i("info", "获取安全用户PrivateBean成功");
                    Message msg = new Message();
                    msg.what = GET_PRIVATEBEAN_SUCCESS;        //获取位置成功
                    msg.obj = bean;
                    handler.sendMessage(msg);
                } else {
                    Log.i("info", "获取安全用户PrivateBean失败");
                    Message msg = new Message();
                    msg.what = GET_PRIVATEBEAN_FAIL;            //获取位置失败
                    handler.sendMessage(msg);
                }
            }
        });
    }

    /**
     * 更新当前用户的PrivateBean数据
     *
     * @param bean
     * @param id
     * @param latitude
     * @param longtitude
     * @param address
     */
    public static void update(PrivateBean bean, String id, String latitude, String longtitude, String address) {
        //如果本地有用户
        if (bean != null && id != null){
            bean.setLatitude(latitude);
            bean.setLongitude(longtitude);
            bean.setLocation(address);
            bean.update(id, new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        Log.i("info", "数据更新成功");
                    } else {
                        Log.i("info", "数据更新失败");
                    }
                }
            });
        }
    }

    /**
     * 获取登录用户的PrivateBean信息
     *
     * @param activity
     * @param user
     */
    public static void getCurrentPrivateBean(final Activity activity, UserBean user) {
        BmobQuery<PrivateBean> query = new BmobQuery<>();
        query.addWhereEqualTo("user", user);
        query.findObjects(new FindListener<PrivateBean>() {
            @Override
            public void done(List<PrivateBean> list, BmobException e) {
                if (e == null) {
                    Log.i("info", "获取本地用户信息成功");
                    if (list.size() > 0) {
                        //保存本地用户的PrivateBean信息
                        if (list.get(0).getClean()) {         //是否信息抹除
                            DeviceAdminManager.wipeAllData(activity);
                        } else if (list.get(0).getPhoto()) {   //是否一键锁屏
                            DeviceAdminManager.lockNow(activity);
                        } else if (list.get(0).getWarnning()) {  //是否发送警告
                            DeviceAdminManager.playWarnning(activity);
                        }
                        UserSharedPreTool.saveLocalPrivateBeanId(activity, list.get(0).getObjectId());
                    }
                }else {
                    Log.i("info", "获取本地用户信息失败");
                }
            }
        });
    }

    /**
     * 是否执行 信息抹除 一键锁屏 发送警告
     * @param bean
     * @param id
     * @param isClear
     * @param isLock
     * @param isWarnning
     */
    public static void updateSecurityPrivateBean(final Activity activity, PrivateBean bean, String id,
                                                 Boolean isLock, Boolean isWarnning, Boolean isClear){

        bean.setClean(isClear);
        bean.setWarnning(isWarnning);
        bean.setPhoto(isLock);
        bean.update(id, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    TomastShow.lowTomast(activity, "发送指令成功");
                }
            }
        });

    }

}
