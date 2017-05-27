package com.inbin.watchu.activities;

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
import com.inbin.watchu.users.UserManager;
import com.inbin.watchu.utils.PhoneNumVertical;
import com.inbin.watchu.utils.TomastShow;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class UpdateSecurityActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mBack;
    private EditText mUser;
    private EditText mSecurity;
    private EditText mCode;
    private Button mQuery;
    private Button mSubmit;

    private Handler mHanlder = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case UserManager.UPDATE_SECURITY_SUCCESS:
                    TomastShow.lowTomast(UpdateSecurityActivity.this, "修改安全手机成功");
                    //获取安全用户实例
                    //保存安全用户的信息
                    UserBean securityUser = (UserBean) msg.obj;
                    UserManager.getSecurityUser(UpdateSecurityActivity.this, securityUser.getObjectId());
                    //保存本地用户信息
                    UserSharedPreTool.saveUserToLocal(UpdateSecurityActivity.this);
                    finish();
                    break;
                case UserManager.UPDATE_SECURITY_FAIL:
                    TomastShow.lowTomast(UpdateSecurityActivity.this, "修改安全手机失败");
                    break;
                default:break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_security);
        initView();
    }

    public void initView(){
        //返回
        mBack = (ImageView) findViewById(R.id.security_phone_back);
        mBack.setOnClickListener(this);
        //用户
        mUser = (EditText) findViewById(R.id.security_phone_user);
        //安全手机
        mSecurity = (EditText) findViewById(R.id.security_phone);
        //验证码
        mCode = (EditText) findViewById(R.id.security_phone_code);
        //发送验证码
        mQuery = (Button) findViewById(R.id.security_phone_query_code);
        mQuery.setOnClickListener(this);
        //提交修改
        mSubmit = (Button) findViewById(R.id.security_phone_submit);
        mSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.security_phone_back:              //返回
                finish();
                break;
            case R.id.security_phone_query_code:       //获取验证码
               String name =  mUser.getText().toString().trim();
                String username = BmobUser.getCurrentUser(UserBean.class).getUsername();
                if (!name.equals("") && PhoneNumVertical.isPhoneNum(name) && name.equals(username)){
                    BmobSMS.requestSMSCode(mUser.getText().toString().trim(), "手机验证码", new QueryListener<Integer>() {
                        @Override
                        public void done(Integer integer, BmobException e) {
                            if (e==null){
                                TomastShow.lowTomast(UpdateSecurityActivity.this, "验证码已发送");
                            }
                        }
                    });
                }else {
                    TomastShow.lowTomast(UpdateSecurityActivity.this, "请填写正确的手机号");
                }

                break;
            case R.id.security_phone_submit:
                String user = mUser.getText().toString();
                final String security = mSecurity.getText().toString();
                String code = mCode.getText().toString();
//                UserManager.getSecurityUserAndPrivateBean(UpdateSecurityActivity.this, security, mHanlder);

                if ((user.equals(BmobUser.getCurrentUser().getUsername())) && PhoneNumVertical.isPhoneNum(user)){
                    if ((!user.equals("")) && (!security.equals("")) && (!code.equals(""))){
                                //短信验证
                        BmobSMS.verifySmsCode(user, code, new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e==null){
                                    //修改用户安全手机
                                    UserManager.getSecurityUserAndPrivateBean(UpdateSecurityActivity.this, security, mHanlder);
                                }else {
                                    TomastShow.lowTomast(UpdateSecurityActivity.this, "验证码错误");
                                }
                            }
                        });

                    }else {
                        TomastShow.lowTomast(UpdateSecurityActivity.this, "请填写所需信息");
                    }
                }else {
                    TomastShow.lowTomast(UpdateSecurityActivity.this, "请填写正确用户的手机号或格式");
                }
                break;
            default: break;
        }
    }
}
