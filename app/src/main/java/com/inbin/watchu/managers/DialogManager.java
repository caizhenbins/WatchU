package com.inbin.watchu.managers;

import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.inbin.watchu.MainActivity;
import com.inbin.watchu.R;
import com.inbin.watchu.sharedpreferences.UserSharedPreTool;
import com.inbin.watchu.utils.TomastShow;

/**
 * Created by InBin time: 2017/4/14.
 * Author: caizhenbin
 * Email: caizhenbins@163.com
 * PackName: com.inbin.watchu.managers
 */

public class DialogManager {

    /**
     * 检查是否已经设置密码了
     * @return
     */
    public static void isSetupPassword( MainActivity activity){
        String password =  UserSharedPreTool.getLanuchPassword(activity);
        if (password == null){
           showPassworSteupDialog(activity);
        }else {
            showPassworDialog(activity);
        }
    }

    //输入密码对话框
    static AlertDialog passworDialog;
    static EditText mPasswordSure;
    static Button mCpnfirmPasswordSure;
    static Button mCancleSure;

    /**
     * 输入密码对话框
     */
    public static void showPassworDialog(final MainActivity activity){
        //创建对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final View passwordView = LayoutInflater.from(activity).inflate(R.layout.dialog_password, null);
        //子View
        mPasswordSure = (EditText) passwordView.findViewById(R.id.password_sure);
        mCpnfirmPasswordSure = (Button) passwordView.findViewById(R.id.password_comfirm_sure);
        //点击时间的添加
        mCpnfirmPasswordSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //确认安全密码

                String currentPassword = UserSharedPreTool.getLanuchPassword(activity);      //安全密码
                String inputPassword = mPasswordSure.getText().toString();                   //输入密码
                if (!inputPassword.equals("") && currentPassword != null){
                    if ((inputPassword.equals(currentPassword)) && (!inputPassword.equals(""))){
                        passworDialog.dismiss();
                    }else {
                        TomastShow.lowTomast(activity, "密码错误");
                    }
                }else {
                    TomastShow.lowTomast(activity, "请输入密码");
                }
            }
        });
        //自定义布局对话框 显示
        builder.setView(passwordView);
        builder.setCancelable(false);
        passworDialog = builder.show();
    }

    static AlertDialog steupDialog;
    static EditText mPassword;
    static EditText mPasswordComfig;
    static  Button mCpnfirmPassword;
    static Button mCancle;
    public static void showPassworSteupDialog(final MainActivity activity){
        //设置密码dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        //自定义布局
        View setupView = LayoutInflater.from(activity).inflate(R.layout.dialog_password_setup, null);
        builder.setView(setupView);
        mPassword = (EditText) setupView.findViewById(R.id.password);
        mPasswordComfig = (EditText) setupView.findViewById(R.id.comfig_password);
        mCpnfirmPassword = (Button) setupView.findViewById(R.id.password_comfirm);
        mCancle = (Button) setupView.findViewById(R.id.password_cancle);
        //点击事件
        mCpnfirmPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //保存设置的密码
                String password =  mPassword.getText().toString();
                String comfigPassword = mPasswordComfig.getText().toString();
                if (password.equals(comfigPassword) && (!password.equals("")) && (!comfigPassword.equals(""))){
                    if (comfigPassword.length() >= 4){
                        //填写成功 保存安全密码 dialog消失 显示首页
                        UserSharedPreTool.saveLancuhPassword(activity, comfigPassword);
                        steupDialog.dismiss();
                    }else {
                        TomastShow.lowTomast(activity, "密码至少4位");
                    }

                }else {
                    TomastShow.lowTomast(activity, "请输入密码");
                }
            }
        });
        mCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //对话框取消
                steupDialog.dismiss();
                activity.finish();
            }
        });
        builder.setView(setupView);
        builder.setCancelable(false);
        steupDialog = builder.show();
    }

}
