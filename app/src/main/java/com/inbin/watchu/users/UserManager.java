package com.inbin.watchu.users;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.baidu.platform.comapi.map.B;
import com.inbin.watchu.activities.UpdateSecurityActivity;
import com.inbin.watchu.beans.PrivateBean;
import com.inbin.watchu.beans.UserBean;
import com.inbin.watchu.sharedpreferences.UserSharedPreTool;
import com.inbin.watchu.utils.TomastShow;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by InBin time: 2017/3/28.
 * Author: caizhenbin
 * Email: caizhenbins@163.com
 * PackName: com.inbin.watchu.users
 */

public class UserManager {

    //保存位置信息成功
    public static final int SAVE_LOCALTION_SUCCESS = 0x11;
    //登录
    public static final int REGIST_SUCCESS = 0x22;
    public static final int REGIST_FAIL = 0x33;
    //重设密码
    public static final int RESET_PASSWORD_SUCCESS = 0x44;
    public static final int RESET_PASSWORD_FSAIL = 0x55;
    //登录
    public static final int LOGIN_SUCCESS = 0x35;
    public static final int LOGIN_FAIL = 0x67;
    //修改安全密码'
    public static final int UPDATE_SECURITY_SUCCESS = 0x81;
    public static final int UPDATE_SECURITY_FAIL = 0x83;
    //查询用户
    public static final int QUERY_USER_SUCCESS = 0x62;
    public static final int QUERY_USER_FAIL = 0x63;
    public static final int QUERY_USER_NONE = 0x53;


    //--------------------注册相关-----------------
    /**
     * 注册  name是手机号
     * @param user
     * @param name
     * @param password
     */
    public static void regisrUser(final UserBean user, String name, String password, final Handler handlder){
        user.setUsername(name);
        user.setPassword(password);
        user.setOldpassword(password);
        user.setSecurityPhone(name);
        user.signUp(new SaveListener<UserBean>() {

            @Override
            public void done(UserBean userBean, BmobException e) {
                if (e==null){
                    PrivateManager.saveCurrentUserPrivateBean(userBean, handlder);     //注册成功  保存位置信息
                    Message msg = new Message();
                    msg.what = REGIST_SUCCESS;
                    msg.obj = userBean;
                    handlder.sendMessage(msg);
                }else {                                 //注册失败
                    Message msg = new Message();
                    msg.what = REGIST_FAIL;
                    handlder.sendMessage(msg);
                }
            }
        });
    }

    //--------------------登录相关-----------------
    /**
     * 登录
     * @param user
     * @param name
     * @param password
     */
    public static void loginUser(final Activity activity, UserBean user, String name, String password, final Handler handler){
        user.setUsername(name);
        user.setPassword(password);
        user.login(new SaveListener<UserBean>() {
            @Override
            public void done(UserBean userBean, BmobException e) {
                 if (e==null){
                     Message msg = new Message();
                     msg.what = LOGIN_SUCCESS;
                     msg.obj = userBean;
                     handler.sendMessage(msg);
                     //保存本地用户信息
                     UserSharedPreTool.saveUserToLocal(activity);
                     //查询安全用户
                     UserManager.getUser(userBean.getSecurityPhone(), handler);
                 }else {
                     Message msg = new Message();
                     msg.what = LOGIN_FAIL;
                     handler.sendMessage(msg);
                 }
            }
        });
    }

    //------------------------密码相关---------------------------
    /**
     * 重新设置密码
     * @param userId
     * @param oldpassword
     * @param newPassword
     * @param handler
     */
    public static void reResPassword(final String userId, String oldpassword, final String newPassword, final Handler handler){
        UserBean.updateCurrentUserPassword(oldpassword, newPassword, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                   updateOldPasword(userId, newPassword, handler);    //修改密码成功后需要更新oldpassword列
                }
            }
        });
    }

    /**
     * 更新旧密码 与新密码同步
     * @param name
     * @param newPasword
     * @param handler
     */
    public static void updateOldPasword(String name, final String newPasword, final Handler handler){
        UserBean user = new UserBean();
        user.setOldpassword(newPasword);
        user.update(name, new UpdateListener() {
            @Override
            public void done(BmobException e) {
               if (e==null){
                   Log.i("info", "修改密码成功");
                   Message msg = new Message();
                   msg.what = RESET_PASSWORD_SUCCESS;               //更新oldpasswor成功  更新密码成功
                   handler.sendMessage(msg);
               }else {
                   Log.i("info", "修改密码失败");
                   Message msg = new Message();
                   msg.what = RESET_PASSWORD_FSAIL;                 //更新oldpasswor失败  更新密码失败
                   handler.sendMessage(msg);
               }
            }
        });
    }
    /**
     * 更新当前用户的安全手机
     * @param userBean
     * @param securityUser    安全手机用户Userbean实例
     * @param newSecurity
     * @param hanlder
     */
    public static void updateSecurity(UserBean userBean, final UserBean securityUser, final String newSecurity, final Handler hanlder){
        userBean.setSecurityPhone(newSecurity);
        userBean.update(userBean.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    Message msg = new Message();
                    msg.what = UPDATE_SECURITY_SUCCESS;       //更新安全密码成功
                    msg.obj = securityUser;                      //将绑定成功的安全用户发送 用户获取安全用户的PrivateBean Id
                    hanlder.sendMessage(msg);
                }else {
                    Message msg = new Message();
                    msg.what = UPDATE_SECURITY_FAIL;       //更新安全密码失败
                    hanlder.sendMessage(msg);
                }
            }
        });

    }

    //---------------安全用户相关----------------
    /**
     * 获取安全手机用户
     * @param securityId
     */
    public static void getSecurityUser(final Activity activity, String securityId){
        BmobQuery<PrivateBean> query = new BmobQuery<>();
        query.addWhereEqualTo("user", securityId);
        query.findObjects(new FindListener<PrivateBean>() {
            @Override
            public void done(List<PrivateBean> list, BmobException e) {
               if (e==null){
                  //保存安全用户信息
                   if (list.size() > 0){
                       UserSharedPreTool.saveSecurityUserId(activity, list.get(0).getObjectId());
                   }
               }
            }
        });
    }


    public static void getSecurityUserAndPrivateBean(final Activity activity, final String security, final Handler handler){
        final UserBean userBean = BmobUser.getCurrentUser(UserBean.class);
        //先检查安全用户手机是否已经注册
        BmobQuery<UserBean> query = new BmobQuery<>();
        query.addWhereEqualTo("username", security);
        query.findObjects(new FindListener<UserBean>() {
            @Override
            public void done(List<UserBean> list, BmobException e) {
                if (e==null){
                    //更新本用户的安全手机
                    if (list.size() > 0){
                        UserManager.updateSecurity(userBean,list.get(0), security,handler);

                    }else {
                        TomastShow.lowTomast(activity, "该安全手机还没注册，请先注册 ");
                    }
                }else {
                    TomastShow.lowTomast(activity, "修改失败，请重试 ");
                }
            }
        });

    }

    //-----------查询用户相关----------------------

    public static void getUser(String user, final Handler handler){
        BmobQuery<UserBean> query = new BmobQuery<>();
        query.addWhereEqualTo("username", user);
        query.findObjects(new FindListener<UserBean>() {
            @Override
            public void done(List<UserBean> list, BmobException e) {
                if (e==null){
                    if (list.size() > 0){
                        Message msg = new Message();
                        msg.what = QUERY_USER_SUCCESS;     //查询成功
                        msg.obj = list.get(0);
                        handler.sendMessage(msg);
                    }
                }else {
                    Message msg = new Message();
                    msg.what = QUERY_USER_FAIL;             //查询失败
                    handler.sendMessage(msg);
                }
                if (list.size() ==0){
                    Message msg = new Message();
                    msg.what = QUERY_USER_NONE;     //查询成功 但是没有注册 该用用户
                    handler.sendMessage(msg);
                }
            }
        });
    }
}
