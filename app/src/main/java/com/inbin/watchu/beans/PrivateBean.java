package com.inbin.watchu.beans;

import cn.bmob.v3.BmobObject;

/**
 * Created by InBin time: 2017/3/23.
 * Author: caizhenbin
 * Email: caizhenbins@163.com
 * PackName: com.inbin.watchu.beans
 */

public class PrivateBean extends BmobObject {

    private UserBean user;
    private String longitude;
    private String latitude;
    private String location;
    private Boolean isClean;
    private Boolean isPhoto;
    private Boolean isWarnning;


    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }


    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setClean(Boolean clean) {
        isClean = clean;
    }

    public void setPhoto(Boolean photo) {
        isPhoto = photo;
    }

    public void setWarnning(Boolean warnning) {
        isWarnning = warnning;
    }



    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public Boolean getPhoto() {
        return isPhoto;
    }

    public Boolean getClean() {
        return isClean;
    }

    public Boolean getWarnning() {
        return isWarnning;
    }

    @Override
    public String toString() {
        return "PrivateBean{" +
                "user=" + user +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", location='" + location + '\'' +
                ", isClean=" + isClean +
                ", isPhoto=" + isPhoto +
                ", isWarnning=" + isWarnning +
                '}';
    }
}
