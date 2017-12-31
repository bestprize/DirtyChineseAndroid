package com.fengxingshifang.dirtychineseandroid;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fengxingshifang.dirtychineseandroid.adapter.MyInfoListAdapter;
import com.fengxingshifang.dirtychineseandroid.db.InfoDao;
import com.fengxingshifang.dirtychineseandroid.domain.DUser;
import com.fengxingshifang.dirtychineseandroid.domain.InfoListData;
import com.fengxingshifang.dirtychineseandroid.global.GlobalConstants;
import com.fengxingshifang.dirtychineseandroid.utils.PrefUtils;
import com.fengxingshifang.dirtychineseandroid.utils.RefreshTokenUtils;
import com.fengxingshifang.dirtychineseandroid.utils.SDCardUtil;
import com.fengxingshifang.dirtychineseandroid.utils.SpacesItemDecoration;
import com.fengxingshifang.dirtychineseandroid.utils.StringUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.sendtion.xrichtext.RichTextEditor;
import com.sendtion.xrichtext.RichTextView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by git on 2017/12/7.
 */

public class InfoActivity extends AppCompatActivity {

    private static final String TAG = InfoActivity.class.getSimpleName();
    private String mUrlInfo;
    private String mUrlListComment;
    private int nowPage;
    private XRecyclerView rv_list_comment;
    private MyInfoListAdapter mCommentListAdapter;
//    private InfoDao infoDao;
    private List<InfoListData.Info> listComment;
    private InfoListData.Info comment;
    private InfoListData mCommentListData;
    private String token;

    private TextView tv_info_title;//笔记标题
    private RichTextView tv_info_content;//笔记内容
    private TextView tv_info_time;//笔记创建时间
    private TextView tv_info_group;//选择笔记分类
    //private ScrollView scroll_view;
    private InfoListData.Info info;//笔记对象
    private String myInfoid;
    private String myTitle;
    private String myContent;
    private String myGroupName;
    private ProgressDialog loadingDialog;
    private Subscription subsLoading;
    private EditText info_publish_comment_area;
    private boolean isInfoAddOrComment;
    private String myPublisher;
    private DUser userInfo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 必须在setContentView之前调用
        setContentView(R.layout.activity_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_info);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //toolbar.setNavigationIcon(R.drawable.ic_dialog_info);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                finish();
            }
        });

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_info);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //点击评论区域动作
        info_publish_comment_area = (EditText) findViewById(R.id.info_publish_comment_area);
        info_publish_comment_area.setFocusable(false);
        Log.e("","isInfoAddOrComment-------------------------------33333333--------------------:"+Boolean.toString(isInfoAddOrComment));
        info_publish_comment_area.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PublishcommentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("info", info);
                bundle.putSerializable("isInfoAddOrComment", isInfoAddOrComment);
                intent.putExtra("data", bundle);
                startActivity(intent);
            }
        });



        loadingDialog = new ProgressDialog(this);
        loadingDialog.setMessage("数据加载中...");
        loadingDialog.setCanceledOnTouchOutside(false);

        tv_info_title = (TextView) findViewById(R.id.tv_info_title);//标题
        tv_info_title.setTextIsSelectable(true);
        tv_info_content = (RichTextView) findViewById(R.id.tv_info_content);//内容
        tv_info_time = (TextView) findViewById(R.id.tv_info_time);
        tv_info_group = (TextView) findViewById(R.id.tv_info_group);


        rv_list_comment = (XRecyclerView) findViewById(R.id.rv_list_comment);
        /****************** 设置XRecyclerView属性 **************************/
        rv_list_comment.addItemDecoration(new SpacesItemDecoration(0));//设置item间距
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);//竖向列表
        rv_list_comment.setLayoutManager(layoutManager);

        rv_list_comment.setLoadingMoreEnabled(true);//开启上拉加载
        rv_list_comment.setPullRefreshEnabled(false);//关闭下拉刷新
        rv_list_comment.setRefreshProgressStyle(ProgressStyle.SquareSpin);
        rv_list_comment.setLoadingMoreProgressStyle(ProgressStyle.BallScale);
        listComment = new ArrayList<InfoListData.Info>();



/****************** 设置XRecyclerView属性 **************************/

        mCommentListAdapter = new MyInfoListAdapter();
        mCommentListAdapter.setmInfos(listComment);
        rv_list_comment.setAdapter(mCommentListAdapter);


        rv_list_comment.setLoadingListener(new InfoActivity.MyLoadingListener());
        mCommentListAdapter.setOnItemClickListener(new MyInfoListAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, InfoListData.Info info) {

            }
        });
        mCommentListAdapter.setOnItemLongClickListener(new MyInfoListAdapter.OnRecyclerViewItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, final InfoListData.Info info) {
            }
        });


        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        info = (InfoListData.Info) bundle.getSerializable("info");
        //取出info的infoid
        myInfoid = info.getInfoid();
        getInfoDataFromServer();

        myTitle = info.getTitle();
        myContent = info.getContent();
        myPublisher = info.getPublisher();
        Log.e("","myPublisher-----------------------11111111111111----------------------------:"+myPublisher);
        tv_info_title.setText(myTitle);

        ;
        //下载图片至本地，并改写myContent中图片路径至myLocalContent
        List<String> textList = StringUtils.cutStringByImgTag(myContent);
        for (int i = 0; i < textList.size(); i++) {
            String text = textList.get(i);
            if (text.contains("<img") && text.contains("src=")) {
                String imagePath = StringUtils.getImgSrc(text);
                String serverImagePath = GlobalConstants.UPLOAD_PICS_URL + imagePath;
                String[] stringTmp = imagePath.split("/");
//                String imagePathTmp = stringTmp[1];
                String localImagePath = SDCardUtil.getPictureDirServer(stringTmp[1]) + File.separator + stringTmp[2];
//                String localImagePath = SDCardUtil.SDCardRoot + "DIRTYCHINESE" + File.separator + imagePath;
                Log.e("","serverImagePath-----------------------999999----------------------------:"+serverImagePath);
                Log.e("","localImagePath-----------------------999999----------------------------:"+localImagePath);
                downloadInfoPicFromServer(serverImagePath,localImagePath);
            } else {

            }
        }
//        downloadInfoPicFromServer();


        tv_info_content.post(new Runnable() {
            @Override
            public void run() {
                //showEditData(myContent);
                tv_info_content.clearAllLayout();
                showDataSync(myContent);
            }
        });
        tv_info_time.setText(info.getCreatetime());
//        setTitle("info详情");

        //获取comment数据并显示
        getCommentDataFromServer();

        getUserInfo(this);
        Log.e("","isInfoAddOrComment---------------------22222222222222------------------------------:"+Boolean.toString(isInfoAddOrComment));


    }


    private void getInfoDataFromServer() {
        mUrlInfo = GlobalConstants.INFO_VIEW_URL;
        mUrlInfo = mUrlInfo + myInfoid + "/" + "1" + "/" + "a2bdb0a0e7d611e78b31cf65557997fb/\"\"";
        token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOi8vbG9jYWxob3N0L2RpcnR5Q2hpbmVzZS9wdWJsaWMvYXBpL2xvZ2luIiwiaWF0IjoxNTE0NzI5NDQ2LCJleHAiOjE1MTQ3MzMwNDYsIm5iZiI6MTUxNDcyOTQ0NiwianRpIjoiWlNRT3IzU285MGdqWVJwQyIsInN1YiI6ImEyYmRiMGEwZTdkNjExZTc4YjMxY2Y2NTU1Nzk5N2ZiIiwicHJ2IjoiODdlMGFmMWVmOWZkMTU4MTJmZGVjOTcxNTNhMTRlMGIwNDc1NDZhYSJ9.Z6cBD5tQ1-jpXALR67hNk8zHW6pPyDyL37LxwNP812k";
//        RefreshTokenUtils refreshTokenUtils = new RefreshTokenUtils();
//        token = refreshTokenUtils.refreshToken(this);
        mUrlInfo = mUrlInfo + "?token=" + token;
        RequestParams params = new RequestParams(mUrlInfo);
        params.addQueryStringParameter("wd","xUtils");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                processInfoData(result, false);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
                Log.e("TAG", "xUtis3联网请求失败==" + ex.getMessage());


            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {
                Log.e("TAG", "onCancelled==" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                Log.e("TAG","onFinished==");
            }

        });
    }



    protected void processInfoData(String result, boolean isMore) {
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonArray jsonArrayResult = parser.parse(result).getAsJsonArray();

        for(JsonElement infoTmp:jsonArrayResult){
            info = gson.fromJson(infoTmp, InfoListData.Info.class);
        }

    }



    private void getCommentDataFromServer() {
        mUrlListComment = GlobalConstants.COMMENT_LIST_URL;
        mUrlListComment = mUrlListComment + myInfoid;
        token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOi8vbG9jYWxob3N0L2RpcnR5Q2hpbmVzZS9wdWJsaWMvYXBpL2xvZ2luIiwiaWF0IjoxNTE0NzI5NDQ2LCJleHAiOjE1MTQ3MzMwNDYsIm5iZiI6MTUxNDcyOTQ0NiwianRpIjoiWlNRT3IzU285MGdqWVJwQyIsInN1YiI6ImEyYmRiMGEwZTdkNjExZTc4YjMxY2Y2NTU1Nzk5N2ZiIiwicHJ2IjoiODdlMGFmMWVmOWZkMTU4MTJmZGVjOTcxNTNhMTRlMGIwNDc1NDZhYSJ9.Z6cBD5tQ1-jpXALR67hNk8zHW6pPyDyL37LxwNP812k";
//        RefreshTokenUtils refreshTokenUtils = new RefreshTokenUtils();
//        token = refreshTokenUtils.refreshToken(this);
        mUrlListComment = mUrlListComment + "?token=" + token;
        RequestParams params = new RequestParams(mUrlListComment);
        params.addQueryStringParameter("wd","xUtils");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                processCommentData(result, false);

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



    protected void processCommentData(String result, boolean isMore) {
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonArray jsonArrayResult = parser.parse(result).getAsJsonArray();

        for(JsonElement infoTmp:jsonArrayResult){
            comment = gson.fromJson(infoTmp, InfoListData.Info.class);
            listComment.add(comment);
        }
        mCommentListAdapter.setmInfos(listComment);
        mCommentListAdapter.notifyDataSetChanged();

    }

    private void downloadInfoPicFromServer(final String url, String path) {
//        progressDialog = new ProgressDialog(this);
        RequestParams requestParams = new RequestParams(url);
        requestParams.setSaveFilePath(path);
        x.http().get(requestParams, new Callback.ProgressCallback<File>() {
            @Override
            public void onWaiting() {
            }

            @Override
            public void onStarted() {
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
//                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//                progressDialog.setMessage("亲，努力下载中。。。");
//                progressDialog.show();
//                progressDialog.setMax((int) total);
//                progressDialog.setProgress((int) current);
            }

            @Override
            public void onSuccess(File result) {
                Toast.makeText(InfoActivity.this, "下载成功", Toast.LENGTH_SHORT).show();
//                progressDialog.dismiss();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
                Toast.makeText(InfoActivity.this, "下载失败，请检查网络和SD卡", Toast.LENGTH_SHORT).show();
//                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }



    /**
     * 异步方式显示数据
     * @param html
     */
    private void showDataSync(final String html){
        loadingDialog.show();

        subsLoading = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                showEditData(subscriber, html);
            }
        })
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())//生产事件在io
                .observeOn(AndroidSchedulers.mainThread())//消费事件在UI线程
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDialog.dismiss();
                        e.printStackTrace();
                        showToast("解析错误：图片不存在或已损坏");
                    }

                    @Override
                    public void onNext(String text) {
                        if (text.contains(SDCardUtil.getPictureDir())){
                            tv_info_content.addImageViewAtIndex(tv_info_content.getLastIndex(), text);
                        } else {
                            tv_info_content.addTextViewAtIndex(tv_info_content.getLastIndex(), text);
                        }
                    }
                });

    }

    /**SDCardUtil.SDCardRoot + "XRichText" + File.separator
     * 显示数据
     * @param html
     */
    private void showEditData(Subscriber<? super String> subscriber, String html) {
        try {
            List<String> textList = StringUtils.cutStringByImgTag(html);
            for (int i = 0; i < textList.size(); i++) {
                String text = textList.get(i);
                if (text.contains("<img") && text.contains("src=")) {
                    String imagePath = StringUtils.getImgSrc(text);
//                    if (new File(imagePath).exists()) {
                    String localImagePath = SDCardUtil.getPictureDirServer("") + imagePath;
                    Log.e("","localImagePath-----------------------88888881----------------------------:"+SDCardUtil.SDCardRoot + "DIRTYCHINESE" + imagePath);
                    if (new File(localImagePath).exists()) {
                        Log.e("","localImagePath-----------------------88888882----------------------------:"+SDCardUtil.SDCardRoot + "DIRTYCHINESE" + imagePath);
                        subscriber.onNext(localImagePath);
                    } else {
                        showToast("图片"+1+"已丢失，请重新插入！");
                    }
                } else {
                    subscriber.onNext(text);
                }
            }
            subscriber.onCompleted();
        } catch (Exception e){
            e.printStackTrace();
            subscriber.onError(e);
        }
    }

    /** 上拉加载和下拉刷新事件 **/
    private class MyLoadingListener implements XRecyclerView.LoadingListener{

        @Override
        public void onRefresh() {//下拉刷新
            rv_list_comment.postDelayed(new Runnable() {
                @Override
                public void run() {
//                    rv_list_comment.refreshComplete();
                }
            }, 1000);
        }

        @Override
        public void onLoadMore() {//上拉加载
            rv_list_comment.postDelayed(new Runnable() {
                @Override
                public void run() {
                    nowPage ++;
                    getCommentDataFromServer();
                    rv_list_comment.loadMoreComplete();
                }
            }, 1000);
        }
    }



    @Override
    protected void onResume() {
        super.onResume();

//        refreshNoteList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
//            case R.id.action_insert_image:
//                break;
//            case R.id.action_new_save:
//                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        showToast("111111111");

        finish();
    }


    private void getUserInfo(Context ctx) {
        //获取token
        String token = PrefUtils.getString(ctx, "token", null);
        String getUserUrl = GlobalConstants.GET_USER_URL;
        token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOi8vbG9jYWxob3N0L2RpcnR5Q2hpbmVzZS9wdWJsaWMvYXBpL2xvZ2luIiwiaWF0IjoxNTE0NzI5NDQ2LCJleHAiOjE1MTQ3MzMwNDYsIm5iZiI6MTUxNDcyOTQ0NiwianRpIjoiWlNRT3IzU285MGdqWVJwQyIsInN1YiI6ImEyYmRiMGEwZTdkNjExZTc4YjMxY2Y2NTU1Nzk5N2ZiIiwicHJ2IjoiODdlMGFmMWVmOWZkMTU4MTJmZGVjOTcxNTNhMTRlMGIwNDc1NDZhYSJ9.Z6cBD5tQ1-jpXALR67hNk8zHW6pPyDyL37LxwNP812k";
        getUserUrl = getUserUrl + "?token=" + token;

//        Log.e("TAG1111----------------", jsonStringInfo);
        RequestParams params = new RequestParams(getUserUrl);
        params.addQueryStringParameter("wd","xUtils");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson=new Gson();
                userInfo = gson.fromJson(result, DUser.class);
                Log.e("","myPublisher------------------------------000000000---------------------:"+myPublisher);
                Log.e("","userInfo.getUser().getUserid()-----------------------00000000000----------------------------:"+userInfo.getUser().getUserid());
                if(myPublisher.equals(userInfo.getUser().getUserid())){
                    isInfoAddOrComment = true;
                } else {
                    isInfoAddOrComment = false;
                }
                Log.e("TAG", "xUtis3联网请求success==");

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


    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
