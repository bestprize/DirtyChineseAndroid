package com.fengxingshifang.dirtychineseandroid.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fengxingshifang.dirtychineseandroid.R;
import com.fengxingshifang.dirtychineseandroid.domain.InfoListData.Info;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by git on 2017/12/5.
 */

public class MyInfoListAdapter extends RecyclerView.Adapter<MyInfoListAdapter.ViewHolder>
        implements View.OnClickListener, View.OnLongClickListener {
    private Context mContext;
    private List<Info> mInfos;
    private OnRecyclerViewItemClickListener mOnItemClickListener ;
    private OnRecyclerViewItemLongClickListener mOnItemLongClickListener ;

    public MyInfoListAdapter() {
        mInfos = new ArrayList<>();
    }

    public void setmInfos(List<Info> Infos) {
        this.mInfos = Infos;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v,(Info)v.getTag());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (mOnItemLongClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemLongClickListener.onItemLongClick(v,(Info)v.getTag());
        }
        return true;
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view , Info Info);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnRecyclerViewItemLongClickListener {
        void onItemLongClick(View view , Info Info);
    }

    public void setOnItemLongClickListener(OnRecyclerViewItemLongClickListener listener) {
        this.mOnItemLongClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Log.i(TAG, "###onCreateViewHolder: ");
        //inflate(R.layout.list_item_record,parent,false) 如果不这么写，cardview不能适应宽度
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_info,parent,false);
        //将创建的View注册点击事件
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //Log.i(TAG, "###onBindViewHolder: ");
        final Info Info = mInfos.get(position);
        //将数据保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(Info);
        //Log.e("adapter", "###record="+record);
        holder.tv_list_title.setText(Info.getTitle());
        holder.tv_list_summary.setText(Info.getContent());
        holder.tv_list_time.setText(Info.getCreatetime());
    }

    @Override
    public int getItemCount() {
        //Log.i(TAG, "###getItemCount: ");
        if (mInfos != null && mInfos.size()>0){
            return mInfos.size();
        }
        return 0;
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_list_title;//笔记标题
        public TextView tv_list_summary;//笔记摘要
        public TextView tv_list_time;//创建时间
        public TextView tv_list_group;//笔记分类
        public CardView card_view_info;

        public ViewHolder(View view){
            super(view);
            card_view_info = (CardView) view.findViewById(R.id.card_view_info);
            tv_list_title = (TextView) view.findViewById(R.id.tv_list_title);
            tv_list_summary = (TextView) view.findViewById(R.id.tv_list_summary);
            tv_list_time = (TextView) view.findViewById(R.id.tv_list_time);
            tv_list_group = (TextView) view.findViewById(R.id.tv_list_group);
        }
    }
}
