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
import android.widget.ImageView;

import com.inbin.watchu.R;
import com.inbin.watchu.beans.UserBean;
import com.inbin.watchu.users.UserManager;
import com.inbin.watchu.utils.PhoneNumVertical;
import com.inbin.watchu.utils.TomastShow;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class ForgetActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mUser;
    private EditText mPassword;
    private EditText mCode;
    private Button mQueryCode;
    private Button mSubmit;
    private ImageView mBack;

    private Handler mHanlder = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case UserManager.QUERY_USER_FAIL:
                    TomastShow.lowTomast(ForgetActivity.this, "查询失败");
                    break;
                case UserManager.QUERY_USER_SUCCESS:      //查询用户成功  存在
                    //修改密码
                    final UserBean current = (UserBean) msg.obj;
                    current.setUsername(mUser.getText().toString().trim());
                    current.setPassword(current.getOldpassword());
                    current.login(new SaveListener<UserBean>() {
                        @Override
                        public void done(UserBean object, BmobException e) {
                            if (e==null){
                                UserManager.reResPassword(current.getObjectId(), current.getOldpassword(), mPassword.getText().toString(), mHanlder);
                            }else {
                            }
                        }
                    });
                    break;
                case UserManager.RESET_PASSWORD_SUCCESS:
                    TomastShow.lowTomast(ForgetActivity.this, "找回密码成功");
                    BmobUser.logOut();      //退出登录
                    Intent loginIntent = new Intent(ForgetActivity.this, LoginActivity.class);       //跳转到登录
                    startActivity(loginIntent);
                    break;
                case UserManager.RESET_PASSWORD_FSAIL:
                    TomastShow.lowTomast(ForgetActivity.this, "找回密码失败");
                    break;
                case UserManager.QUERY_USER_NONE:
                    TomastShow.lowTomast(ForgetActivity.this, "该用户未注册，请先注册");
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        //初始化
        initView();
    }

    /**
     * 初始化
     */
    public void initView(){
        mUser = (EditText) findViewById(R.id.forget_user);
        mPassword = (EditText) findViewById(R.id.forget_new_password);
        mCode = (EditText) findViewById(R.id.forget_code);

        //验证码获取
        mQueryCode = (Button) findViewById(R.id.forget_query_code);
        mQueryCode.setOnClickListener(this);
        //提交修改
        mSubmit = (Button) findViewById(R.id.forget_submit);
        mSubmit.setOnClickListener(this);
        //返回
        mBack = (ImageView) findViewById(R.id.forget_back);
        mBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.forget_submit:                                      //提交修改密码请求
                final String user = mUser.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String code = mCode.getText().toString().trim();
                if ((!user.equals("")) && (!password.equals("")) && !(code.equals(""))){
                    //手机格式验证
                    if (PhoneNumVertical.isPhoneNum(user)){
                        //验证码验证
                        BmobSMS.verifySmsCode(user, code, new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e==null){
                                    //检查用户是否存在
                                    UserManager.getUser(user, mHanlder);
                                }else {
                                    TomastShow.lowTomast(ForgetActivity.this, "验证码错误");
                                }
                            }
                        });

                    }else {
                        TomastShow.lowTomast(ForgetActivity.this, "请填写11位手机号格式");
                    }

                }else {
                    TomastShow.lowTomast(ForgetActivity.this, "请填写所需信息");
                }

                break;
            case R.id.forget_query_code:                                 //获取验证码
                String name = mUser.getText().toString().trim();
                if (!name.equals("") && PhoneNumVertical.isPhoneNum(name)){
                    //请求短信验证码
                    BmobSMS.requestSMSCode(name, "手机验证码", new QueryListener<Integer>() {
                        @Override
                        public void done(Integer integer, BmobException e) {
                            if (e == null){
                                TomastShow.lowTomast(ForgetActivity.this, "验证码发送成功");
                            }else {
                                TomastShow.lowTomast(ForgetActivity.this, "验证码发送失败");
                            }
                        }
                    });
                }else {
                    TomastShow.lowTomast(ForgetActivity.this, "请填写11位手机号格式");
                }
                break;
            case R.id.forget_back:
                finish();
                break;
        }
    }
}
