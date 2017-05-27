package com.inbin.watchu.beans;

import cn.bmob.v3.BmobUser;

/**
 * Created by InBin time: 2017/3/19.
 * Author: caizhenbin
 * Email: caizhenbins@163.com
 * PackName: com.inbin.watchu
 */

public class UserBean extends BmobUser {

    public UserBean(){

    }

    private String PicImg;
    private String DevInfornation;
    private String SecurityPhone;
    private String oldpassword;
    private String SimId;

    public void setSimId(String simId) {
        SimId = simId;
    }

    public String getSimId() {
        return SimId;
    }

    public void setOldpassword(String oldpassword) {
        this.oldpassword = oldpassword;
    }

    public String getOldpassword() {
        return oldpassword;
    }

    public void setSecurityPhone(String securityPhone) {
        SecurityPhone = securityPhone;
    }

    public String getSecurityPhone() {
        return SecurityPhone;
    }

    public void setPicImg(String picImg) {
        this.PicImg = picImg;
    }

    public void setDevInfornation(String devInfornation) {
        this.DevInfornation = devInfornation;
    }

    public String getPicImg() {
        return PicImg;
    }

    public String getDevInfornation() {
        return DevInfornation;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "PicImg='" + PicImg + '\'' +
                ", DevInfornation='" + DevInfornation + '\'' +
                ", SecurityPhone='" + SecurityPhone + '\'' +
                ", oldpassword='" + oldpassword + '\'' +
                ", SimId='" + SimId + '\'' +
                '}';
    }
}
