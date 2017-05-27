package com.inbin.watchu.users;

import android.os.AsyncTask;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.inbin.watchu.beans.PrivateBean;

/**
 * Created by InBin time: 2017/3/29.
 * Author: caizhenbin
 * Email: caizhenbins@163.com
 * PackName: com.inbin.watchu.users
 */
public class GetSecurityTask extends AsyncTask<PrivateBean, PrivateBean, Void> {
    private BaiduMap mBaiduMap;
    private BitmapDescriptor mMarker;
    private OtherLocationManaget locationManager;

    public GetSecurityTask(BaiduMap mBaiduMap, BitmapDescriptor mMarker, OtherLocationManaget locationManager) {
        this.mBaiduMap = mBaiduMap;
        this.mMarker = mMarker;
        this.locationManager = locationManager;
    }

    @Override
    protected Void doInBackground(PrivateBean... params) {

        if (params.length > 0) {
           publishProgress(params[0]);
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(PrivateBean... values) {
        super.onProgressUpdate(values);
        PrivateBean securityUser = values[0];
        double latitude = Double.parseDouble(securityUser.getLatitude());
        double longtitude = Double.parseDouble(securityUser.getLatitude());
        locationManager.overlay(mBaiduMap, longtitude, latitude, mMarker);
    }

}
