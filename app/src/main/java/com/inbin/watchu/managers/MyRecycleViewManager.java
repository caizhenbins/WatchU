package com.inbin.watchu.managers;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Created by InBin time: 2017/3/17.
 * Author: caizhenbin
 * Email: caizhenbins@163.com
 * PackName: com.inbin.watchu.managers
 */

public class MyRecycleViewManager extends LinearLayoutManager {

    private boolean isScrollable = false;
    public MyRecycleViewManager(Context context) {
        super(context);
    }


    public void setScrollEable(boolean isScrollable){
        this.isScrollable = isScrollable;
    }

    @Override
    public boolean canScrollVertically() {
        return isScrollable && super.canScrollVertically();
    }
}
