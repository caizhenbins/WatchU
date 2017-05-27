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
import com.inbin.watchu.sharedpreferences.UserSharedPreTool;
import com.inbin.watchu.users.PrivateManager;
import com.inbin.watchu.users.UserManager;
import com.inbin.watchu.utils.PhoneNumVertical;
import com.inbin.watchu.utils.TomastShow;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class RegistActivity extends AppCompatActivity implements View.OnClickListener {

    //-----返回-------
    private ImageView mBack;

    //------注册------
    private EditText mUser;
    private EditText mPassword;
    private EditText mCode;
    private Button mSubmit;
    private Button mQueryCode;



    //-----提交本地用户PrivateBean表的信息
    private  Handler mHanlder = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case PrivateManager.SAVE_LOCALTION_SUCCESS:
                    //保存本用户的PrivateBean Id
                    UserSharedPreTool.saveLocalPrivateBeanId(RegistActivity.this, (String) msg.obj);
                    break;
                case UserManager.REGIST_SUCCESS:
                    TomastShow.lowTomast(RegistActivity.this, "注册成功");
                    //------------当前用户------------------
                    UserBean bean = (UserBean) msg.obj;
                    //保存用户详细信息在本地
                    UserSharedPreTool.saveUserToLocal(RegistActivity.this);
                    //退出登录
                    BmobUser.logOut();
                    //注册成功返回登录页面
                    Intent mLoginInten = new Intent(RegistActivity.this, LoginActivity.class);
                    startActivity(mLoginInten);
                    finish();
                    break;
                case UserManager.REGIST_FAIL:
                    TomastShow.lowTomast(RegistActivity.this, "注册失败");    //注册失败停留注册页面
                    break;
                default:break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        initView();
    }

    public void initView(){
        mBack = (ImageView) findViewById(R.id.regist_back);
        //注册
        mUser = (EditText) findViewById(R.id.regist_user);
        mPassword = (EditText) findViewById(R.id.regist_password);
        mCode = (EditText) findViewById(R.id.regist_code);
        mQueryCode = (Button) findViewById(R.id.regisr_query_code);
        mSubmit = (Button) findViewById(R.id.regist_submit);

        //返回事件监听
        mBack.setOnClickListener(this);
        //注册事件监听
        mQueryCode.setOnClickListener(this);
        mSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.regist_back:         //返回
                finish();
                break;
            case R.id.regisr_query_code:    //获取验证码
                String phone = mUser.getText().toString().trim();
                if (!phone.equals("") && PhoneNumVertical.isPhoneNum(phone)){
                    BmobSMS.requestSMSCode(mUser.getText().toString(), "手机验证码", new QueryListener<Integer>() {
                        @Override
                        public void done(Integer integer, BmobException e) {
                            if (e==null){
                                TomastShow.lowTomast(RegistActivity.this, "验证码发送成功");
                            }else {
                                TomastShow.lowTomast(RegistActivity.this, "验证码发送失败");
                            }
                        }
                    });
                }else {
                    TomastShow.lowTomast(RegistActivity.this, "请填写11位手机号格式");
                }
                break;
            case R.id.regist_submit:         //提交注册
                final String name = mUser.getText().toString();
                final String password = mPassword.getText().toString();
                String code = mCode.getText().toString().trim();
//                UserManager.regisrUser(new UserBean(),name, password , mHanlder);
                if ((!name.equals("")) && (!password.equals("")) && (!code.equals(""))){
                    if (PhoneNumVertical.isPhoneNum(name)){
                        //验证短信验证码
                        BmobSMS.verifySmsCode(name, code, new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e==null){
                                    UserManager.regisrUser(new UserBean(),name, password , mHanlder);     //验证码成功 注册
                                }else {
                                    TomastShow.lowTomast(RegistActivity.this, "验证码错误");
                                }
                            }
                        });
                    }else {
                        TomastShow.lowTomast(RegistActivity.this, "请填写11位手机号格式");
                    }

                }else {
                    TomastShow.lowTomast(RegistActivity.this, "请填写所需信息");
                }
                break;
        }
    }
}
