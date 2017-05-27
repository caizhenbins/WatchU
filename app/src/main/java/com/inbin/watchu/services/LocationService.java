package com.inbin.watchu.services;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.inbin.watchu.R;
import com.inbin.watchu.admin.DeviceAdminManager;
import com.inbin.watchu.admin.SmsObserver;
import com.inbin.watchu.beans.PrivateBean;
import com.inbin.watchu.beans.UserBean;
import com.inbin.watchu.managers.SmsManager;
import com.inbin.watchu.sharedpreferences.UserSharedPreTool;
import com.inbin.watchu.users.PrivateManager;
import com.inbin.watchu.utils.TomastShow;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

public class LocationService extends Service {

    //PrivateBean 本地用户对应的
    private PrivateBean mLocationPrivateBean;
    //本地PrivateBean Id
    private String mLocationPrivateBeanId;

    public static Activity activity;
    //短信相关
    private Handler mHanlder = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            TomastShow.lowTomast(activity, "接收到了短信了----------");
            switch (msg.what){
                case SmsManager.LOCK_NOM:
                    DeviceAdminManager.lockNow(activity);       //一键锁屏
                    break;
                case SmsManager.WARN_ING:
                    DeviceAdminManager.playWarnning(activity);   //播放警告
                    break;
                case SmsManager.WIPE_DATA:
                    DeviceAdminManager.wipeAllData(activity);    //抹除数据
                    break;
                default:break;
            }
        }
    };
    private SmsObserver smsObserver;
    private String mSecurityPhone;
    private List<String> mSecurityList;

    //定位客户端
    public LocationClient client;
    public LocationClientOption option;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mLocationPrivateBean = new PrivateBean();
        if (activity != null){
            mLocationPrivateBeanId = UserSharedPreTool.getLocalPrivateBeanId(activity);
            if (mSecurityList == null){
                mSecurityList = new ArrayList<>();
            }
            mSecurityPhone = UserSharedPreTool.getUserFormLocal(activity,mSecurityList).get(4);
        }
        //如果安全密码不为空则监听短信收发状态
        if (mSecurityPhone != null){
            smsObserver = new SmsObserver(mHanlder, getContentResolver(), mSecurityPhone);
            //注册短信监听]]
            getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, smsObserver);

        }
        initLocation();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
           initLocation();
        return super.onStartCommand(intent, flags, startId);
    }
    /**
     * 初始化百度地图
     */
    private void initLocation() {
        if (client == null && option == null){
            client = new LocationClient(getApplicationContext());
            option = new LocationClientOption();
        }
        //注册监听事件
        client.registerLocationListener(new MylocationListener());
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //坐标类型  经纬度
        option.setCoorType("bd09ll");
        //设置每隔10000ms获取一次坐标
        option.setScanSpan(10000);
        //设置是否需要地理位置信息 默认不需要
        option.setIsNeedAddress(true);
        //设置是否使用GPS 默认不需要
        option.setOpenGps(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setLocationNotify(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIsNeedLocationPoiList(true);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setIgnoreKillProcess(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集
        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        option.setEnableSimulateGps(false);
        client.setLocOption(option);
        client.start();
    }

    public class MylocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            //判断是否定位成功
            if (bdLocation != null && activity != null) {
                //根据用户的PrivateBean Id 来上传用户的位置信息
                    String longitude = String.valueOf(bdLocation.getLongitude());
                    String latitude = String.valueOf(bdLocation.getLatitude());
                    String address = bdLocation.getAddrStr();
                    //上传位置信息 经纬度 地理位置
                     if (mLocationPrivateBean == null){
                         mLocationPrivateBean = new PrivateBean();
                     }else if (mLocationPrivateBeanId == null){
                         mLocationPrivateBeanId = UserSharedPreTool.getLocalPrivateBeanId(activity);
                     }
                    PrivateManager.update(mLocationPrivateBean, mLocationPrivateBeanId, latitude, longitude, address);
                 //每次获取本地用户的PrivateBean信息
                if (BmobUser.getCurrentUser(UserBean.class) != null){
                    PrivateManager.getCurrentPrivateBean(activity, BmobUser.getCurrentUser(UserBean.class));
                }
            }
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        client.stop();
        if (smsObserver != null){
            getContentResolver().unregisterContentObserver(smsObserver);
        }
        if (client.isStarted()){
            client.stop();            //停止服务的时候 停止百度地图的本地数据上传
        }
    }
}
