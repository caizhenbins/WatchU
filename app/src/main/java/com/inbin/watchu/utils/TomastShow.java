package com.inbin.watchu.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by InBin time: 2017/3/20.
 * Author: caizhenbin
 * Email: caizhenbins@163.com
 * PackName: com.inbin.watchu.utils
 */

public class TomastShow extends Toast {
    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context The context to use.  Usually your {@link Application}
     *                or {@link Activity} object.
     */
    public TomastShow(Context context) {
        super(context);
    }

    public static void lowTomast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void LongTomast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }



}
