package com.inbin.watchu.users;

import android.util.Log;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.inbin.watchu.R;

/**
 * Created by InBin time: 2017/3/29.
 * Author: caizhenbin
 * Email: caizhenbins@163.com
 * PackName: com.inbin.watchu.users
 */

public class OtherLocationManaget {

    //添加覆盖物
    public  void overlay(BaiduMap mBaiduMap, double latitude, double longtitude,BitmapDescriptor  bitmap){

        //定义Maker坐标点
        mBaiduMap.clear();
        LatLng point = new LatLng(latitude, longtitude);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);
        //定位过程变化
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(point);
        mBaiduMap.setMapStatus(msu);

    }

    /**
     * 添加覆盖物 安全用户的位置显示
     * @param mBaiduMap
     * @param latitude
     * @param longtitude
     */
    public static void cover(BaiduMap mBaiduMap, double latitude, double longtitude){
        //定义Maker坐标点
        mBaiduMap.clear();
        LatLng point = new LatLng(latitude, longtitude);
       //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.location_fill);
       //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        //在地图上添加Marker，并显示
          mBaiduMap.addOverlay(option);
        //定位过程变化
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(point);
        mBaiduMap.setMapStatus(msu);
    }

}
