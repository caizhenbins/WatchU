package com.inbin.watchu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.inbin.watchu.adapters.TabFragmentAdapter;
import com.inbin.watchu.beans.PrivateBean;
import com.inbin.watchu.beans.UserBean;
import com.inbin.watchu.fragments.LocationFragment;
import com.inbin.watchu.fragments.UserFragment;
import com.inbin.watchu.managers.DialogManager;
import com.inbin.watchu.services.LocationService;
import com.inbin.watchu.sharedpreferences.UserSharedPreTool;
import com.inbin.watchu.utils.TomastShow;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager mViewPager;
    private List<Fragment> mFragmentList;
    private List<String> mTitleList;
    private TabFragmentAdapter mFragmentAdapter;
    private TabLayout mTablayout;
    private TextView mToolBarTitle;
    private List<Integer> mImgIdList;
    private List<Integer> mImgFillId;
    private ImageView mTabImg;
    private TextView mTabTitle;
    private int mTextColorFill;
    private List<String> mUserList;
    private int mTextColor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        CheckPermission();

//        SIMMagager magager = new SIMMagager();
//        magager.saveSIM(MainActivity.this);
        mUserList = new ArrayList<>();
        //toolbar标题
        mToolBarTitle = (TextView) findViewById(R.id.main_title);

        mTextColorFill = getResources().getColor(R.color.colorTextFill);
        mTextColor = getResources().getColor(R.color.colorTabText);

        //数据初始化
        initTabData();
        initTabFragment();
        initImgId();
        initView();
//
    }

    @Override
    protected void onResume() {
        super.onResume();
        startUpLocationService();
    }

    @Override
    protected void onStart() {
        super.onStart();
        DialogManager.isSetupPassword(MainActivity.this);
    }

    /**
     * 初始化item view 控件
     */
    public void initView(){
        //初始化
        mViewPager = (ViewPager) findViewById(R.id.main_viewpager);
        mViewPager.setOffscreenPageLimit(3);
        mTablayout = (TabLayout) findViewById(R.id.main_tab);
        mFragmentAdapter = new TabFragmentAdapter(getSupportFragmentManager(), mTitleList, mFragmentList, this, mImgIdList, mImgFillId);
        //添加adapter
        mViewPager.setAdapter(mFragmentAdapter);
        mTablayout.setupWithViewPager(mViewPager);

        //产生联动
        mTablayout.setupWithViewPager(mViewPager);

        mTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

//                String title = (String) tab.getText();
               //当选项卡变化时，tab的标题也随着改变
                mToolBarTitle.setText(mFragmentAdapter.getTabTitle(tab.getPosition()));
//                 tab.setCustomView(mFragmentAdapter.getTabCustomView(tab.getPosition(), true));
                //导航栏图标
                mTabImg = (ImageView) tab.getCustomView().findViewById(R.id.nav_tab_imv);
                mTabImg.setImageResource(mImgFillId.get(tab.getPosition()));
                mTabTitle = (TextView) tab.getCustomView().findViewById(R.id.nav_tab_tv);
//                mTabTitle.setTextColor(getColor(R.color.colorBackGroundPrimary));
                mTabTitle.setTextColor(mTextColorFill);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                mTabImg = (ImageView) tab.getCustomView().findViewById(R.id.nav_tab_imv);
                mTabImg.setImageResource(mImgIdList.get(tab.getPosition()));

                mTabTitle = (TextView) tab.getCustomView().findViewById(R.id.nav_tab_tv);
                mTabTitle.setTextColor(mTextColor);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //填充自定义tab布局
        for (int i = 0; i < mTablayout.getTabCount(); i++){
            TabLayout.Tab tab = mTablayout.getTabAt(i);
            tab.setCustomView(mFragmentAdapter.getTabCustomView(i, false));
        }



    }

    /**
     * 初始化选项卡Tab的数据
     */
    public void initTabData(){
        if (mTitleList == null){
        mTitleList = new ArrayList<>();
        }
        mTitleList.add("位置");
        mTitleList.add("个人");
    }

    /**
     * 初始化选项卡Fragment
     */
    public void initTabFragment(){
        if (mFragmentList == null){
            mFragmentList = new ArrayList<>();
        }
        mFragmentList.add(new LocationFragment());       //位置
        mFragmentList.add(new UserFragment());           //用户
    }

    public void initImgId(){
        //未填充的icon
        if (mImgIdList == null){
            mImgIdList = new ArrayList<>();
        }
        mImgIdList.add(R.drawable.location);
        mImgIdList.add(R.drawable.user);
        //已填充的icon
        if (mImgFillId == null){
            mImgFillId = new ArrayList<>();
        }
        mImgFillId.add(R.drawable.location_fill);
        mImgFillId.add(R.drawable.user_fill);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void CheckPermission() {
//        if (!Settings.System.canWrite(MainActivity.this)) {
//            Uri selfPackageUri = Uri.parse("package:"
//                    + getPackageName());
//            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
//                    selfPackageUri);
//            startActivity(intent);
//        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (Settings.System.canWrite(MainActivity.this)){
//
//            }
//                // Do stuff here
//            }
//            else {
//                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
//                intent.setData(Uri.parse("package:" + getPackageName()));
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//            }
        }

    public void startUpLocationService(){
        if (BmobUser.getCurrentUser(UserBean.class) != null){
            ///启动后台服务 发送地理位置
            LocationService.activity = MainActivity.this;
            Intent serviceIntent = new Intent(MainActivity.this, LocationService.class);
            startService(serviceIntent);
        }
    }

    @Override
    public void onClick(View v) {

    }
}
