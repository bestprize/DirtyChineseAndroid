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
import com.fengxingshifang.dirtychineseandroid.utils.RefreshTokenUtils;
import com.fengxingshifang.dirtychineseandroid.utils.SpacesItemDecoration;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by git on 2017/12/6.
 */

public class HomeFragment extends BaseFragment {

    private static final String TAG = HomeFragment.class.getSimpleName();
    private String mUrl;
    private int nowPage;
    private XRecyclerView rv_list_main;
    private MyInfoListAdapter mInfoListAdapter;
    private InfoDao infoDao;
    private List<InfoListData.Info> listInfo;
    private InfoListData.Info info;
    private InfoListData mInfoListData;
    private String token;

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
        listInfo = new ArrayList<InfoListData.Info>();



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
            }
        });



        return view;
    }

    @Override
    public void initData() {
        nowPage = 0;
        getDataFromServer();
    }

    private void getDataFromServer() {
        mUrl = GlobalConstants.HOME_LIST_URL;
        mUrl = mUrl + "01/" + String.valueOf(nowPage) + "/" + GlobalConstants.numberPerPage;
        token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOi8vbG9jYWxob3N0L2RpcnR5Q2hpbmVzZS9wdWJsaWMvYXBpL2xvZ2luIiwiaWF0IjoxNTE0MTcwMDQ0LCJleHAiOjE1MTQxNzM2NDQsIm5iZiI6MTUxNDE3MDA0NCwianRpIjoiUDlGZkNyaUpSNDJ1WVdqayIsInN1YiI6MCwicHJ2IjoiODdlMGFmMWVmOWZkMTU4MTJmZGVjOTcxNTNhMTRlMGIwNDc1NDZhYSJ9.kIVT8EsfZpTV7oZNAmtGlGnRcZ0r2vskEz5-680UMSA";
//        RefreshTokenUtils refreshTokenUtils = new RefreshTokenUtils();
//        token = refreshTokenUtils.refreshToken(mContext);
        mUrl = mUrl + "?token=" + token;
        RequestParams params = new RequestParams(mUrl);
        params.addQueryStringParameter("wd","xUtils");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                processData(result, false);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
                Log.e("TAG", "xUtis3联网请求失败==" + ex.getMessage());


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
        JsonParser parser = new JsonParser();
        JsonArray jsonArrayResult = parser.parse(result).getAsJsonArray();

        for(JsonElement infoTmp:jsonArrayResult){
            info = gson.fromJson(infoTmp, InfoListData.Info.class);
            listInfo.add(info);
        }
        mInfoListAdapter.setmInfos(listInfo);
        mInfoListAdapter.notifyDataSetChanged();

    }


    /** 上拉加载和下拉刷新事件 **/
    private class MyLoadingListener implements XRecyclerView.LoadingListener{

        @Override
        public void onRefresh() {//下拉刷新
            rv_list_main.postDelayed(new Runnable() {
                @Override
                public void run() {
                    nowPage = 0;
                    listInfo = new ArrayList<InfoListData.Info>();
                    getDataFromServer();
                    rv_list_main.refreshComplete();
                }
            }, 1000);
        }

        @Override
        public void onLoadMore() {//上拉加载
            rv_list_main.postDelayed(new Runnable() {
                @Override
                public void run() {
                    nowPage ++;
                    getDataFromServer();
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


//class SpacesItemDecoration extends RecyclerView.ItemDecoration {
//    private int space;
//
//    public SpacesItemDecoration(int space) {
//        this.space = space;
//    }
//
//    @Override
//    public void getItemOffsets(Rect outRect, View view,
//                               RecyclerView parent, RecyclerView.State state) {
//        outRect.left = space;
//        outRect.right = space;
//        outRect.bottom = space;
//
//        // Add top margin only for the first item to avoid double space between items
//        if(parent.getChildPosition(view) == 0)
//            outRect.top = space;
//    }
//}