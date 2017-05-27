package com.inbin.watchu.activities;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.inbin.watchu.R;
import com.inbin.watchu.beans.UserBean;
import com.inbin.watchu.sharedpreferences.UserSharedPreTool;
import com.inbin.watchu.users.UserManager;
import com.inbin.watchu.utils.PhoneNumVertical;
import com.inbin.watchu.utils.TomastShow;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {
    //-------修改密码--------
    private EditText mUser;
    private EditText mPassword;
    private EditText mCode;
    private Button mQueryCode;
    private Button mSubmit;

    //-------密码修改结果----------
    private Handler mHanlder = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UserManager.RESET_PASSWORD_SUCCESS:    //重设密码成功
                    TomastShow.lowTomast(ChangePasswordActivity.this, "修改密码成功");
                    UserSharedPreTool.saveUserToLocal(ChangePasswordActivity.this);
                    //退出登录
                    BmobUser.logOut();
                    //重新登录
                    Intent mLoginIntent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                    startActivity(mLoginIntent);
                    finish();
                    break;
                case UserManager.RESET_PASSWORD_FSAIL:
                    TomastShow.lowTomast(ChangePasswordActivity.this, "修改密码失败");
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        //初始化view
        initView();
    }


    private void initView() {
        //用户
        mUser = (EditText) findViewById(R.id.change_user);
        //密码
        mPassword = (EditText) findViewById(R.id.change_password);
        //验证码
        mCode = (EditText) findViewById(R.id.change_code);
        mQueryCode = (Button) findViewById(R.id.change_query_code);
        mQueryCode.setOnClickListener(this);
        //确认修改
        mSubmit = (Button) findViewById(R.id.change_submit);
        mSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_query_code:
                String phone = mUser.getText().toString().trim();
                String username = BmobUser.getCurrentUser(UserBean.class).getUsername();
                if (!phone.equals("") && PhoneNumVertical.isPhoneNum(phone) && phone.equals(username)){
                    //发送短信
                    BmobSMS.requestSMSCode(phone, "手机验证码", new QueryListener<Integer>() {
                        @Override
                        public void done(Integer integer, BmobException e) {
                            if (e==null){
                                TomastShow.lowTomast(ChangePasswordActivity.this, "验证码已发送");
                            }else {
                                TomastShow.lowTomast(ChangePasswordActivity.this, "验证码发送失败");
                            }
                        }
                    });
                }else {
                    TomastShow.lowTomast(ChangePasswordActivity.this, "请填写正确的用户名");
                }
                break;
            case R.id.change_submit:
                String name = mUser.getText().toString();              //用户手机
                final String newPassword = mPassword.getText().toString();   //新密码
                String code = mCode.getText().toString();              //密码

                final UserBean currentUser = BmobUser.getCurrentUser(UserBean.class);  //当前所登录的用户
                String currentName = currentUser.getUsername();
                //当前所登录的用户
                final String oldPassword = currentUser.getOldpassword();
                if (name.equals(currentName)) {
                    if ((!name.equals("")) && (!newPassword.equals("")) && (!code.equals(""))) {
                        //验证手机验证码
                        BmobSMS.verifySmsCode(name, code, new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e==null){
                                    UserManager.reResPassword(currentUser.getObjectId(), oldPassword, newPassword, mHanlder);
                                }else {
                                    TomastShow.lowTomast(ChangePasswordActivity.this, "验证码错误");
                                }
                            }
                        });
                    } else {
                        TomastShow.lowTomast(ChangePasswordActivity.this, "请填写所需信息");
                    }
                } else {
                    TomastShow.lowTomast(ChangePasswordActivity.this, "请填写正确的用户名");
                }

                break;
            default:
                break;
        }
    }
}
