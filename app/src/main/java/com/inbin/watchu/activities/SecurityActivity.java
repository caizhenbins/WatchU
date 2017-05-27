package com.inbin.watchu.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.inbin.watchu.R;

public class SecurityActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mChangePassword;
    private Button mSecurityPhone;
    private ImageView mBack;
    private Intent mChangePassworIntent;
    private Intent mUpdateSecurityIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);
        //修改密码
        mChangePassworIntent = new Intent(SecurityActivity.this, ChangePasswordActivity.class);
        //修改安全手机号
        mUpdateSecurityIntent = new Intent(SecurityActivity.this, UpdateSecurityActivity.class);
        //初始化View
        initView();
    }

    public void initView(){
        //返回
        mBack = (ImageView) findViewById(R.id.security_back);
        mBack.setOnClickListener(this);
        //修改密码
        mChangePassword = (Button) findViewById(R.id.security_change_password);
        mChangePassword.setOnClickListener(this);
        //修改安全手机
        mSecurityPhone = (Button) findViewById(R.id.security_change_phone);
        mSecurityPhone.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.security_back:                 //返回
                finish();
                break;
            case R.id.security_change_password:      //修改密码
                startActivity(mChangePassworIntent);
                break;
            case R.id.security_change_phone:          //修改安全手机
                startActivity(mUpdateSecurityIntent);
                break;
        }
    }
}
