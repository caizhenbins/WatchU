package com.inbin.watchu.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.inbin.watchu.R;

import java.util.List;

/**
 * Created by InBin time: 2017/3/15.
 * Author: caizhenbin
 * Email: caizhenbins@163.com
 * PackName: com.inbin.watchu.adapters
 */

public class TabFragmentAdapter extends FragmentPagerAdapter {

    private List<String> mTitleList;
    private List<Fragment> mFragmentList;
    private Context mContext;
    private List<Integer> imvId;
    private List<Integer> imvColorId;

    public TabFragmentAdapter(FragmentManager fm, List<String> titleList, List<Fragment> fragmentList, Context context, List<Integer> imvId, List<Integer> imvColorId) {
        super(fm);
        this.mTitleList = titleList;
        this.mFragmentList = fragmentList;
        this.mContext = context;
        this.imvId = imvId;
        this.imvColorId = imvColorId;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size() == 0 ? 0 : mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
//        return mTitleList.get(position);
        return null;
    }

    /**
     * 自定义选项卡布局
     * @param position
     * @return
     */
    public View getTabCustomView(int position, boolean isSelected){
        //布局
        View tabView = LayoutInflater.from(mContext).inflate(R.layout.nav_tab_bottom, null);
        //icon
        ImageView imv = (ImageView) tabView.findViewById(R.id.nav_tab_imv);
        //title
        TextView tvTitle = (TextView) tabView.findViewById(R.id.nav_tab_tv);
        if (!isSelected){
            imv.setImageResource(imvId.get(position));
            tvTitle.setText(mTitleList.get(position));
            tvTitle.setTextColor(Color.BLACK);
        }else {
//            imv.setImageResource(imvColorId.get(position));
//            tvTitle.setText(mTitleList.get(position));
//            tvTitle.setTextColor(Color.DKGRAY);
        }
        return tabView;
    }

    /**
     * 获取title
     * @param position
     * @return
     */
    public String getTabTitle(int position){
        return mTitleList.get(position);
    }
}
