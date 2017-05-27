package com.inbin.watchu.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.inbin.watchu.R;
import com.inbin.watchu.listeners.ItemListener;

import java.util.List;

/**
 * Created by InBin time: 2017/3/17.
 * Author: caizhenbin
 * Email: caizhenbins@163.com
 * PackName: com.inbin.watchu.adapters
 */

public class UserOrderRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Integer> mLogonList;
    private List<String> mTitleList;
    private Context mContext;
    private View mItemView;
    private OrderViewHolder mViewHolder;
    //点击接口监听
    private ItemListener mItemListener;

    public UserOrderRecycleViewAdapter(Context context, List<Integer> logonList, List<String> title, ItemListener listener){
        this.mContext = context;
        this.mLogonList = logonList;
        this.mTitleList = title;
        this.mItemListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mItemView = LayoutInflater.from(mContext).inflate(R.layout.user_order_recycleview_item, parent, false);
        mViewHolder = new OrderViewHolder(mItemView);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        OrderViewHolder viewHoler = (OrderViewHolder) holder;
        viewHoler.setData(position);
        viewHoler.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTitleList.size() == 0 ? 0 : mTitleList.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder{

        private ImageView imvLogon;
        private TextView tvTitle;

        public OrderViewHolder(View itemView) {
            super(itemView);
            imvLogon = (ImageView) itemView.findViewById(R.id.user_order_item_logon);
            tvTitle = (TextView) itemView.findViewById(R.id.userr_order_item_name);
        }

        /**
         * 数据绑定
         * @param position
         */
        public void setData(int position){

            imvLogon.setImageResource(mLogonList.get(position));
            tvTitle.setText(mTitleList.get(position));

        }
    }
}
