package com.fengxingshifang.dirtychineseandroid.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.fengxingshifang.dirtychineseandroid.InfoActivity;
import com.fengxingshifang.dirtychineseandroid.R;
import com.fengxingshifang.dirtychineseandroid.adapter.MyInfoListAdapter;
import com.fengxingshifang.dirtychineseandroid.db.InfoDao;
import com.fengxingshifang.dirtychineseandroid.domain.InfoListData;
import com.fengxingshifang.dirtychineseandroid.global.GlobalConstants;
import com.fengxingshifang.dirtychineseandroid.utils.CacheUtils;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

/**
 * Created by git on 2017/12/6.
 */

public class HomeFragment extends BaseFragment {

    private static final String TAG = HomeFragment.class.getSimpleName();
    private String mUrl;
    private XRecyclerView rv_list_main;
    private MyInfoListAdapter mInfoListAdapter;
    private InfoDao infoDao;
    private List<InfoListData.Info> listInfo;
    private InfoListData mInfoListData;

    @Override
    public View initView() {
        Log.e(TAG,"home Fragment页面被初始化了...");
        View view = View.inflate(mContext, R.layout.fragment_home,null);
        rv_list_main = (XRecyclerView) view.findViewById(R.id.rv_list_main);
        /****************** 设置XRecyclerView属性 **************************/
        rv_list_main.addItemDecoration(new SpacesItemDecoration(0));//设置item间距
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);//竖向列表
        rv_list_main.setLayoutManager(layoutManager);

        rv_list_main.setLoadingMoreEnabled(true);//开启上拉加载
        rv_list_main.setPullRefreshEnabled(true);//开启下拉刷新
        rv_list_main.setRefreshProgressStyle(ProgressStyle.SquareSpin);
        rv_list_main.setLoadingMoreProgressStyle(ProgressStyle.BallScale);
        return view;
    }

    @Override
    public void initData() {
        getDataFromServer();
    }

    private void getDataFromServer() {
        mUrl = GlobalConstants.HOME_LIST_URL;
        mUrl = mUrl + "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOi8vbG9jYWxob3N0L2RpcnR5Q2hpbmVzZS9wdWJsaWMvYXBpL2xvZ2luIiwiaWF0IjoxNTEyNzIxNjI1LCJleHAiOjE1MTI3MjUyMjUsIm5iZiI6MTUxMjcyMTYyNSwianRpIjoiS2k5VFZmM3NQaGhsemFzTSIsInN1YiI6MTExMTExLCJwcnYiOiI4N2UwYWYxZWY5ZmQxNTgxMmZkZWM5NzE1M2ExNGUwYjA0NzU0NmFhIn0.RmXN1PUzl1GMuT0yLWhxQWSZOFQcNirnM3g-kpk8CDE";
        RequestParams params = new RequestParams(mUrl);
        params.addQueryStringParameter("wd","xUtils");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                processData(result, false);

//                CacheUtils.setCache(mUrl, result, mActivity);

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
        listInfo = mInfoListData.getInfos();

//        listInfo = infoDao.queryInfosAll();
//        Log.e("","-----------------------------"+listInfo.size());
//        mInfoListAdapter.setmInfos(listInfo);
//        mInfoListAdapter.notifyDataSetChanged();


//        String moreUrl = newsTabBean.data.more;
//        if (!TextUtils.isEmpty(moreUrl)) {
//            mMoreUrl = GlobalConstants.SERVER_URL + moreUrl;
//        } else {
//            mMoreUrl = null;
//        }

//        if (!isMore) {

        // 列表新闻
//            mNewsList = newsTabBean.data.news;


/****************** 设置XRecyclerView属性 **************************/

        mInfoListAdapter = new MyInfoListAdapter();
        mInfoListAdapter.setmInfos(listInfo);
        rv_list_main.setAdapter(mInfoListAdapter);

        rv_list_main.setLoadingListener(new HomeFragment.MyLoadingListener());
        mInfoListAdapter.setOnItemClickListener(new MyInfoListAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, InfoListData.Info info) {
                showToast(info.getTitle());
//
                Intent intent = new Intent(mContext, InfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("info", info);
                intent.putExtra("data", bundle);
                startActivity(intent);
            }
        });
        mInfoListAdapter.setOnItemLongClickListener(new MyInfoListAdapter.OnRecyclerViewItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, final InfoListData.Info info) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setTitle("提示");
//                builder.setMessage("确定删除笔记？");
//                builder.setCancelable(false);
//                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
////                        int ret = noteDao.deleteNote(note.getId());
////                        if (ret > 0){
////                            showToast("删除成功");
////                            refreshNoteList();
////                        }
//                    }
//                });
//                builder.setNegativeButton("取消", null);
//                builder.create().show();
            }
        });





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


    /** 上拉加载和下拉刷新事件 **/
    private class MyLoadingListener implements XRecyclerView.LoadingListener{

        @Override
        public void onRefresh() {//下拉刷新
            rv_list_main.postDelayed(new Runnable() {
                @Override
                public void run() {
                    rv_list_main.refreshComplete();
                }
            }, 1000);
        }

        @Override
        public void onLoadMore() {//上拉加载
            rv_list_main.postDelayed(new Runnable() {
                @Override
                public void run() {
                    rv_list_main.loadMoreComplete();
                }
            }, 1000);
        }
    }

    //刷新笔记列表
    private void refreshNoteList(){
//        infoList = InfoDao.queryNotesAll(groupId);
//        //Log.i(TAG, "###noteList: "+noteList);
//        mNoteListAdapter.setmNotes(noteList);
//        mNoteListAdapter.notifyDataSetChanged();
    }

    public void showToast(String text) {
        Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
    }

}


class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpacesItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;

        // Add top margin only for the first item to avoid double space between items
        if(parent.getChildPosition(view) == 0)
            outRect.top = space;
    }
}