package com.inbin.watchu.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by InBin time: 2017/4/20.
 * Author: caizhenbin
 * Email: caizhenbins@163.com
 * PackName: com.inbin.watchu.utils
 */

public class PhoneNumVertical {

    static final String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
    /**
     * 手机号码正则 验证
     * @param num
     * @return
     */
    public static boolean isPhoneNum(String num){
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(num);
        return m.matches();
    }
}
