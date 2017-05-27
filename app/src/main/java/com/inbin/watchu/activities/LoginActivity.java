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
import android.widget.TextView;

import com.inbin.watchu.MainActivity;
import com.inbin.watchu.R;
import com.inbin.watchu.beans.PrivateBean;
import com.inbin.watchu.beans.UserBean;
import com.inbin.watchu.sharedpreferences.UserSharedPreTool;
import com.inbin.watchu.users.PrivateManager;
import com.inbin.watchu.users.UserManager;
import com.inbin.watchu.utils.PhoneNumVertical;
import com.inbin.watchu.utils.TomastShow;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    //-----注册-------
    private TextView mRegist;
    private Intent mRegistIntent;

    //-----返回------
    private ImageView mBack;

    //-----登录------
    private EditText mUser;
    private EditText mPassword;
    private Button mSubmit;

    //-----遇到问题  修改密码-----
    private TextView mQuestion;
    private Intent mForgetIntent;

    //-----登录结果----------
    private Handler mHanlder = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case UserManager.LOGIN_SUCCESS:
                    TomastShow.lowTomast(LoginActivity.this, "登录成功,请重启本软件");
                    UserBean currentUser = BmobUser.getCurrentUser(UserBean.class);
                    //保存本地用户的PrivateBean
                    PrivateManager.getCurrentPrivateBean(LoginActivity.this, currentUser);
                    //保存本地用户信息
                    UserSharedPreTool.saveUserToLocal(LoginActivity.this);
                    //回到首页
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    //退出
                     finish();
                    break;
                case UserManager.LOGIN_FAIL:
                    TomastShow.lowTomast(LoginActivity.this, "登录失败");
                    break;
                case UserManager.QUERY_USER_SUCCESS:
                    //保存安全用户的PrivateBean Id
                    UserBean securityUser = (UserBean) msg.obj;
                    UserManager.getSecurityUser(LoginActivity.this, securityUser.getObjectId());
                    break;
                default:break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //修改密码
        mForgetIntent = new Intent(LoginActivity.this, ForgetActivity.class);
        //初始化注册相关
        initRegist();
        //登录相关初始化
        initView();
    }

    /**
     * 注册相关初始化
     */
    public void initRegist(){
        mRegist = (TextView) findViewById(R.id.login_regist);
        mRegistIntent = new Intent(LoginActivity.this, RegistActivity.class);
        //点击事件
        mRegist.setOnClickListener(this);
    }

    /**
     * 登录初始化相关
     */
    public void initView(){
        //返回
        mBack = (ImageView) findViewById(R.id.login_back);
        //用户
        mUser = (EditText) findViewById(R.id.login_user);
        mPassword = (EditText) findViewById(R.id.login_password);
        mSubmit = (Button) findViewById(R.id.login_submit);
        //问题相关
        mQuestion = (TextView) findViewById(R.id.login_question);

        //点击事件添加
        mBack.setOnClickListener(this);
        mSubmit.setOnClickListener(this);
        mQuestion.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_regist:      //注册
                startActivity(mRegistIntent);
                break;
            case R.id.login_back:        //返回
                finish();
                break;
            case R.id.login_submit:

                String name= mUser.getText().toString();
                String password = mPassword.getText().toString();
                if ((!name.equals("")) && (!password.equals(""))){
                    if (PhoneNumVertical.isPhoneNum(name)){
                        UserManager.loginUser(LoginActivity.this,new UserBean(),name, password, mHanlder);
                    }else {
                        TomastShow.lowTomast(LoginActivity.this, "填写11位手机号格式");
                    }

                }else {
                    TomastShow.lowTomast(LoginActivity.this, "请填写所需信息");
                }
                break;
            case R.id.login_question:
               startActivity(mForgetIntent);                              //遇到问题
                break;
        }
    }
}
