package com.inbin.watchu.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.inbin.watchu.R;
import com.inbin.watchu.beans.PrivateBean;
import com.inbin.watchu.beans.UserBean;
import com.inbin.watchu.sharedpreferences.UserSharedPreTool;
import com.inbin.watchu.users.GetSecurityTask;
import com.inbin.watchu.users.OtherLocationManaget;
import com.inbin.watchu.users.PrivateManager;
import com.inbin.watchu.utils.TomastShow;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;


/**
 * Created by InBin time: 2017/3/15.
 * Author: caizhenbin
 * Email: caizhenbins@163.com
 * PackName: com.inbin.watchu.fragments
 */

public class LocationFragment extends Fragment {

    private View mLocationRootView;
    //--------百度地图相关--------
    private MapView mapView = null;
    public BaiduMap mBaiduMap;
    public LocationClient mLocationClient;
    public BDLocationListener myLocationListener;
    private  boolean isFirstIn = true;
    private MapStatus.Builder builder;
    private GetSecurityTask mTask;
    private BitmapDescriptor mMark;

    private List<String> list;

    //地图模式
    private MyLocationConfiguration.LocationMode mLocationMode;

    //--------PrivateBean位置数据更新相关  本地用户-------

    private PrivateBean mPrivateBean;
    private String mPrivateId;

    //--------PrivateBean位置数据更新相关 绑定安全手机用户-------
    private String otherId;
    private OtherLocationManaget mOtherLocationManaget;
    private Handler mHanlder = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
              case PrivateManager.GET_PRIVATEBEAN_SUCCESS:
                  //与绑定用户的地图显示
                  PrivateBean other = (PrivateBean) msg.obj;
                  double latitude = Double.parseDouble(other.getLatitude());       //经度
                  double longtitude = Double.parseDouble(other.getLongitude());     //纬度
                  OtherLocationManaget.cover(mBaiduMap, latitude, longtitude);
                break;
              case PrivateManager.GET_PRIVATEBEAN_FAIL:
                  //获取id失败
                  TomastShow.lowTomast(getActivity(), "获取绑定安全用户位置失败，请重启本应用或确定保持良好的网络环境");
                  break;
              default:break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mLocationRootView = inflater.inflate(R.layout.fragment_location, container, false);

        //初始化PrivateBean Id
        mPrivateBean = new PrivateBean();
        otherId = UserSharedPreTool.getSEcurityUserId(getActivity());
        //初始化百度地图
        initMap(mLocationRootView);
        //对方定位
//        PrivateManager.getSecurityPrivateBean(otherId, mHanlder);
        list = new ArrayList<>();

        return mLocationRootView;
    }


    @Override
    public void onStart() {

        //开启定位功能
        mBaiduMap.setMyLocationEnabled(true);
        if (!mLocationClient.isStarted()) {
            mLocationClient.start();    //开始定位
        }

        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();    //实现百度地图的生命周期管理
        otherId = UserSharedPreTool.getSEcurityUserId(getActivity());
        list = UserSharedPreTool.getUserFormLocal(getActivity(), new ArrayList<String>());
        for (int i = 0; i < list.size(); i++){
            Log.i("info", "本地用户信息:" + list.get(i));
        }
        //获取本地的PrivateBean信息
        Log.i("info", "本地的PrivateBean Id:" + UserSharedPreTool.getLocalPrivateBeanId(getActivity()));
        Log.i("info","安全用户的PrivateBean Id:" + UserSharedPreTool.getSEcurityUserId(getActivity()));
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();     //实现百度地图的生命周期管理
    }

    @Override
    public void onStop() {

        //停止定位
        mBaiduMap.setMyLocationEnabled(false);
        mLocationClient.stop();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();   //实现百度地图的生命周期管理
    }



    /**
     * 初始化定位设置
     */
    public void initLocation(){
        LocationClientOption option = new LocationClientOption();
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

        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(new MyLocationListener());

    }

    public void initMap(View rootView){
        mapView = (MapView) rootView.findViewById(R.id.baidu_map);
        mBaiduMap = mapView.getMap();
        //开启定位功能
        mBaiduMap.setMyLocationEnabled(true);
        mLocationClient = new LocationClient(getContext().getApplicationContext());
        //覆盖物初始化
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
        mMark = BitmapDescriptorFactory.fromResource(R.drawable.location_fill);
        mBaiduMap.setMapStatus(msu);
        //初始化定位设置
        initLocation();
        //覆盖物
        builder = new MapStatus.Builder();
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
             //个人位置信息
            //获取实时经纬度信息
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(bdLocation.getRadius())
                    .latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude()).build();
            //设置位置信息
            mBaiduMap.setMyLocationData(locData);
            LatLng ll = new LatLng(bdLocation.getLatitude(),
                    bdLocation.getLongitude());
            //是否第一次进入
            if (isFirstIn){
                isFirstIn = false;
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
            if (otherId != null && mHanlder != null && BmobUser.getCurrentUser(UserBean.class) != null){
//                Log.i("info", "执行了百度地图");
                  PrivateManager.getSecurityPrivateBean(otherId, mHanlder);
            }
            }
        }
}
