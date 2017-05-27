package com.inbin.watchu.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inbin.watchu.R;
import com.inbin.watchu.admin.DeviceAdminManager;
import com.inbin.watchu.beans.UserBean;
import com.inbin.watchu.services.LocationService;
import com.inbin.watchu.utils.TomastShow;

import cn.bmob.v3.BmobUser;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    //-----退出登录-----
    private Button mExit;
    //-----返回-----
    private ImageView mBack;
    //------z账户安全---------
    private RelativeLayout mUserSecurityLayout;
    private Intent mUserSecurityIntent;
    //设置管理器
    private RelativeLayout mAdminLayout;
    private TextView mAdminText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //账户安全
        mUserSecurityIntent = new Intent(SettingActivity.this, SecurityActivity.class);
        //初始化view
        initView();
    }

    public void initView() {
        //退出
        mExit = (Button) findViewById(R.id.setting_exit);
        mExit.setOnClickListener(this);
        //返回
        mBack = (ImageView) findViewById(R.id.setting_back);
        mBack.setOnClickListener(this);
        //账户安全
        mUserSecurityLayout = (RelativeLayout) findViewById(R.id.user_security);
        mUserSecurityLayout.setOnClickListener(this);
        //设置管理器
        mAdminLayout = (RelativeLayout) findViewById(R.id.setting_admin);
        mAdminLayout.setOnClickListener(this);
        mAdminText = (TextView) findViewById(R.id.setting_admin_txt);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_exit:
                if (BmobUser.getCurrentUser(UserBean.class) != null) {
                        BmobUser.logOut();                                        //有用户登录才操作退出
                    stopService(new Intent(SettingActivity.this, LocationService.class));    //停止服务 停止上传
                    TomastShow.lowTomast(SettingActivity.this, "已退出");
                }else {
                    TomastShow.lowTomast(SettingActivity.this, "无用户登录");
                }
                break;
            case R.id.setting_back:
                finish();          //返回
                break;
            case R.id.user_security:                                           //账户安全
                if (BmobUser.getCurrentUser(UserBean.class) != null) {
                   startActivity(mUserSecurityIntent);                                      //有用户登录才操作退出
                }else {
                    TomastShow.lowTomast(SettingActivity.this, "无用户登录");
                }
                break;
            case R.id.setting_admin:
                DeviceAdminManager.settingOrCloseAdmin(SettingActivity.this);
                onResume();
                break;
            default:
                break;
        }
        showAdminText();
    }

    public void showAdminText(){
        if (DeviceAdminManager.isAdmin(SettingActivity.this)){
            mAdminText.setText("已开启设备管理器");
        }else {
            mAdminText.setText("未开启设备管理器");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!DeviceAdminManager.isAdmin(SettingActivity.this)){
            TomastShow.lowTomast(SettingActivity.this, "您还未启动设备管理器，请开启");
        }
        showAdminText();
        ;
    }
}
