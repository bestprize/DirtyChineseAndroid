package com.fengxingshifang.dirtychineseandroid.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fengxingshifang.dirtychineseandroid.R;
import com.fengxingshifang.dirtychineseandroid.base.BasePager;
import com.fengxingshifang.dirtychineseandroid.domain.InfoListData;
import com.fengxingshifang.dirtychineseandroid.global.GlobalConstants;
import com.fengxingshifang.dirtychineseandroid.utils.CacheUtils;
import com.fengxingshifang.dirtychineseandroid.utils.PrefUtils;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by bestprize on 2017/11/19.
 */

public class HomePager extends BasePager {

    private InfoListData mInfoListData;
    private String mUrl;
    private InfolistAdapter mInfolistAdapter;
    @ViewInject(R.id.lv_list)
    private ListView lvList;

    public HomePager(Activity activity) {
        super(activity);
//        mInfoListData = infoListData;

        mUrl = GlobalConstants.HOME_LIST_URL;
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.home_list, null);
        x.view().inject(this, view);
        return view;
    }



    @Override
    public void initData() {
        System.out.println("首页初始化啦...");

//        // 要给帧布局填充布局对象
//        TextView view = new TextView(mActivity);
//        view.setText("首页");
//        view.setTextColor(Color.RED);
//        view.setTextSize(22);
//        view.setGravity(Gravity.CENTER);
//
//        flContent.addView(view);

        // 修改页面标题
//        tvTitle.setText("智慧北京");

        // 隐藏菜单按钮
//        btnMenu.setVisibility(View.GONE);


        String cache = CacheUtils.getCache(mUrl, mActivity);
        if (!TextUtils.isEmpty(cache)) {
            processData(cache, false);
        }

        getDataFromServer();
    }

    private void getDataFromServer() {
        mUrl = GlobalConstants.HOME_LIST_URL;
        mUrl = mUrl + "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOi8vbG9jYWxob3N0L2RpcnR5Q2hpbmVzZS9wdWJsaWMvYXBpL2xvZ2luIiwiaWF0IjoxNTExODY4MjcwLCJleHAiOjE1MTE4NzE4NzAsIm5iZiI6MTUxMTg2ODI3MCwianRpIjoiRkNhVHBBVkRCQU5KbkJXdSIsInN1YiI6MTExMTExLCJwcnYiOiI4N2UwYWYxZWY5ZmQxNTgxMmZkZWM5NzE1M2ExNGUwYjA0NzU0NmFhIn0.YMcgOrP--tfFyeXYDADxHqVoFpoNOmAH2rX_7WICFqU";
        RequestParams params = new RequestParams(mUrl);
        params.addQueryStringParameter("wd","xUtils");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                processData(result, false);

                CacheUtils.setCache(mUrl, result, mActivity);

                // 收起下拉刷新控件
//                lvList.onRefreshComplete(true);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
                Log.e("TAG", "xUtis3联网请求失败==" + ex.getMessage());

                // 收起下拉刷新控件
//                lvList.onRefreshComplete(false);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.e("TAG", "onCancelled==" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                Log.e("TAG","onFinished==");
            }

        });
    }



    protected void processData(String result, boolean isMore) {
        Gson gson = new Gson();
        mInfoListData = gson.fromJson(result, InfoListData.class);

//        String moreUrl = newsTabBean.data.more;
//        if (!TextUtils.isEmpty(moreUrl)) {
//            mMoreUrl = GlobalConstants.SERVER_URL + moreUrl;
//        } else {
//            mMoreUrl = null;
//        }

//        if (!isMore) {

            // 列表新闻
//            mNewsList = newsTabBean.data.news;
            if (mInfoListData != null) {
                mInfolistAdapter = new InfolistAdapter();
                lvList.setAdapter(mInfolistAdapter);
            }

//            if (mHandler == null) {
//                mHandler = new Handler() {
//                    public void handleMessage(android.os.Message msg) {
//                        int currentItem = mViewPager.getCurrentItem();
//                        currentItem++;
//
//                        if (currentItem > mTopNews.size() - 1) {
//                            currentItem = 0;// 如果已经到了最后一个页面,跳到第一页
//                        }
//
//                        mViewPager.setCurrentItem(currentItem);
//
//                        mHandler.sendEmptyMessageDelayed(0, 3000);// 继续发送延时3秒的消息,形成内循环
//                    };
//                };
//
//                // 保证启动自动轮播逻辑只执行一次
//                mHandler.sendEmptyMessageDelayed(0, 3000);// 发送延时3秒的消息
//
//                mViewPager.setOnTouchListener(new OnTouchListener() {
//
//                    @Override
//                    public boolean onTouch(View v, MotionEvent event) {
//                        switch (event.getAction()) {
//                            case MotionEvent.ACTION_DOWN:
//                                System.out.println("ACTION_DOWN");
//                                // 停止广告自动轮播
//                                // 删除handler的所有消息
//                                mHandler.removeCallbacksAndMessages(null);
//                                // mHandler.post(new Runnable() {
//                                //
//                                // @Override
//                                // public void run() {
//                                // //在主线程运行
//                                // }
//                                // });
//                                break;
//                            case MotionEvent.ACTION_CANCEL:// 取消事件,
//                                // 当按下viewpager后,直接滑动listview,导致抬起事件无法响应,但会走此事件
//                                System.out.println("ACTION_CANCEL");
//                                // 启动广告
//                                mHandler.sendEmptyMessageDelayed(0, 3000);
//                                break;
//                            case MotionEvent.ACTION_UP:
//                                System.out.println("ACTION_UP");
//                                // 启动广告
//                                mHandler.sendEmptyMessageDelayed(0, 3000);
//                                break;
//
//                            default:
//                                break;
//                        }
//                        return false;
//                    }
//                });
//            }
//        } else {
//            // 加载更多数据
//            ArrayList<NewsData> moreNews = newsTabBean.data.news;
//            mNewsList.addAll(moreNews);// 将数据追加在原来的集合中
//            // 刷新listview
//            mNewsAdapter.notifyDataSetChanged();
//        }
    }


    class InfolistAdapter extends BaseAdapter {


        public InfolistAdapter() {
        }

        @Override
        public int getCount() {
            return mInfoListData.getInfos().size();
        }

        @Override
        public InfoListData.InfosBean getItem(int position) {
            return mInfoListData.getInfos().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.list_item_infos,
                        null);
                holder = new ViewHolder();
//                holder.ivIcon = (ImageView) convertView
//                        .findViewById(R.id.iv_icon);
                holder.tvTitle = (TextView) convertView
                        .findViewById(R.id.tv_title);
//                holder.tvDate = (TextView) convertView
//                        .findViewById(R.id.tv_date);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            InfoListData.InfosBean info = getItem(position);
            holder.tvTitle.setText(info.getTitle());
//            holder.tvDate.setText(news.pubdate);

            // 根据本地记录来标记已读未读
            String readIds = PrefUtils.getString(mActivity, "read_ids", "");
            if (readIds.contains(info.getInfoid() + "")) {
                holder.tvTitle.setTextColor(Color.GRAY);
            } else {
                holder.tvTitle.setTextColor(Color.BLACK);
            }

//            mBitmapUtils.display(holder.ivIcon, news.listimage);

            return convertView;
        }

    }

    static class ViewHolder {
//        public ImageView ivIcon;
        public TextView tvTitle;
//        public TextView tvDate;
    }


}
