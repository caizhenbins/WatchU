package com.inbin.watchu.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inbin.watchu.R;

import com.inbin.watchu.activities.LoginActivity;
import com.inbin.watchu.activities.SettingActivity;
import com.inbin.watchu.adapters.UserOrderRecycleViewAdapter;

import com.inbin.watchu.beans.PrivateBean;
import com.inbin.watchu.beans.UserBean;
import com.inbin.watchu.listeners.ItemListener;
import com.inbin.watchu.managers.MyRecycleViewManager;
import com.inbin.watchu.managers.OrderDialogManager;
import com.inbin.watchu.managers.SmsManager;
import com.inbin.watchu.sharedpreferences.UserSharedPreTool;
import com.inbin.watchu.users.PrivateManager;
import com.inbin.watchu.utils.TomastShow;
import com.inbin.watchu.views.MyRecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * Created by InBin time: 2017/3/15.
 * Author: caizhenbin
 * Email: caizhenbins@163.com
 * PackName: com.inbin.watchu.fragments
 */

public class UserFragment extends Fragment implements View.OnClickListener, ItemListener {

    private List<Integer> mLogonList;
    private List<String> mTitleList;
    private UserOrderRecycleViewAdapter mOrderAdapter;

    private View mUserRootView;

    private RecyclerView mRecycleView;
    private MyRecycleViewManager mLayoutManager;

    //------登录注册相关-------
    private RelativeLayout mUserLayout;
    private TextView mUser;
    private Intent mLoginIntent;
    //-----设置相关--------
    private RelativeLayout mSettingLayout;
    private Intent mSettingIntent;
    //安全用户操作相关
    private PrivateBean mSecurityPrivateBean;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mUserRootView = inflater.inflate(R.layout.fragment_user, container, false);

        //登录
        mLoginIntent = new Intent(getActivity(), LoginActivity.class);
        //设置
        mSettingIntent = new Intent(getActivity(), SettingActivity.class);
        initRecycleViewData();
        initRecycleView(mUserRootView);
        //初始化view
        initView(mUserRootView);
        //判断是否需要显示用户
        showOrHideUser();

        return mUserRootView;
    }

    public void showOrHideUser() {
        if (checkCurrentUser()) {
            mUser.setText(BmobUser.getCurrentUser(UserBean.class).getUsername());
        } else {
            mUser.setText("请登录");
        }
    }

    private void initView(View root) {
        //用户
        mUser = (TextView) root.findViewById(R.id.user);
        mUserLayout = (RelativeLayout) root.findViewById(R.id.user_layout);
        mUserLayout.setOnClickListener(this);
        //设置
        mSettingLayout = (RelativeLayout) root.findViewById(R.id.user_setting_layout);
        mSettingLayout.setOnClickListener(this);
    }

    /**
     * 初始化Recyclew数据
     */
    public void initRecycleViewData() {
        if (mLogonList == null || mTitleList == null) {
            mLogonList = new ArrayList<>();
            mTitleList = new ArrayList<>();
            //        图标
            mLogonList.add(R.drawable.lock);
            mLogonList.add(R.drawable.order_waring);
            mLogonList.add(R.drawable.order_delete);

            //        标题
            mTitleList.add("一键锁屏");
            mTitleList.add("发送警告");
            mTitleList.add("抹除信息");
        }

    }

    public void initRecycleView(View rootView) {


        mLayoutManager = new MyRecycleViewManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.setScrollEable(false);
        mRecycleView = (RecyclerView) rootView.findViewById(R.id.user_security_recycleview);
        mOrderAdapter = new UserOrderRecycleViewAdapter(getContext(), mLogonList, mTitleList, this);

        mRecycleView.setLayoutManager(mLayoutManager);
        mRecycleView.setAdapter(mOrderAdapter);
        //添加分各线
        int dividerColor = getResources().getColor(R.color.colorDivider);
        mRecycleView.addItemDecoration(new MyRecycleViewDivider(getContext(), LinearLayoutManager.HORIZONTAL, 1, dividerColor));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_layout:
                if (!checkCurrentUser()) {
                    //当无用户登录的时候
                    startActivity(mLoginIntent);
                }
                break;
            case R.id.user_setting_layout:
                startActivity(mSettingIntent);
                break;
            default:
                break;
        }

    }

    /**
     * 检查是否有用户登录
     *
     * @return
     */
    public boolean checkCurrentUser() {
        UserBean currentUser = BmobUser.getCurrentUser(UserBean.class);
        if (currentUser != null) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public void onItemClick(int position) {

        if (BmobUser.getCurrentUser(UserBean.class) != null){
            if (mSecurityPrivateBean == null){
                mSecurityPrivateBean = new PrivateBean();
            }
            String id = UserSharedPreTool.getSEcurityUserId(getActivity());
            switch (position){
                case 0:
                    //一键锁屏
                    OrderDialogManager.dialogSenMeaasgeLockNow(getActivity(), mSecurityPrivateBean, id);      //一键锁屏
                    break;

                case 1:
                    //发送警告
                    OrderDialogManager.dialogSenMeaasgeWarn(getActivity(), mSecurityPrivateBean, id);        //发送警告
                    break;
                case 2:
                    //信息抹除
                    OrderDialogManager.dialogSenMeaasgeWipe(getActivity(), mSecurityPrivateBean, id);         //数据抹除
                    break;
                default:break;
            }
        }else {
            TomastShow.lowTomast(getActivity(), "请登录");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        showOrHideUser();
    }
}
