package com.fengxingshifang.dirtychineseandroid;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.fengxingshifang.dirtychineseandroid.db.InfoDao;
import com.fengxingshifang.dirtychineseandroid.domain.DUser;
import com.fengxingshifang.dirtychineseandroid.domain.Info;
import com.fengxingshifang.dirtychineseandroid.domain.InfoListData;
import com.fengxingshifang.dirtychineseandroid.global.GlobalConstants;
import com.fengxingshifang.dirtychineseandroid.utils.CommonUtil;
import com.fengxingshifang.dirtychineseandroid.utils.ImageUtils;
import com.fengxingshifang.dirtychineseandroid.utils.PrefUtils;
import com.fengxingshifang.dirtychineseandroid.utils.SDCardUtil;
import com.fengxingshifang.dirtychineseandroid.utils.ScreenUtils;
import com.google.gson.Gson;
import com.sendtion.xrichtext.RichTextEditor;

import org.xutils.common.Callback;
import org.xutils.common.util.KeyValue;
import org.xutils.http.RequestParams;
import org.xutils.http.body.MultipartBody;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.iwf.photopicker.PhotoPicker;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by git on 2017/12/7.
 */

public class PublishcommentActivity extends AppCompatActivity {

//    private EditText et_comment_new_title;
    private RichTextEditor et_comment_new_content;
    private static final int cutTitleLength = 20;//截取的标题长度
    private int flag;//区分是新建笔记还是编辑笔记
    private ProgressDialog loadingDialog;
    private ProgressDialog insertDialog;
    private Subscription subsLoading;
    private Subscription subsInsert;
    private InfoDao infoDao;
    private InfoListData.Info parentinfo;
    private InfoListData.Info info;
    private String parentInfoid;
    private String parentTitle;
    private String parentPublisher;
    private String infoId;
    private String mUrl;
    private String token;
    private String jsonStringInfo;
    private String picUrl;
    private boolean isInfoAddOrComment;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 必须在setContentView之前调用
        setContentView(R.layout.activity_publishcomment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_comment_new);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //toolbar.setNavigationIcon(R.drawable.ic_dialog_info);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                dealwithExit();
            }
        });

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_comment_new);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dealwithExit();
            }
        });

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        parentinfo = (InfoListData.Info) bundle.getSerializable("info");
        isInfoAddOrComment = (boolean) bundle.getSerializable("isInfoAddOrComment");
        Log.e("","isInfoAddOrComment---------------------------------------------------:"+Boolean.toString(isInfoAddOrComment));
        parentInfoid = parentinfo.getInfoid();
        parentTitle = parentinfo.getTitle();


//        et_comment_new_title = (EditText) findViewById(R.id.et_comment_new_title);
        et_comment_new_content = (RichTextEditor) findViewById(R.id.et_comment_new_content);

        insertDialog = new ProgressDialog(this);
        insertDialog.setMessage("正在插入图片...");
        insertDialog.setCanceledOnTouchOutside(false);

        loadingDialog = new ProgressDialog(this);
        loadingDialog.setMessage("图片解析中...");
        loadingDialog.setCanceledOnTouchOutside(false);

        info = new InfoListData.Info();
        infoDao = new InfoDao(this);

        infoId = UUID.randomUUID().toString().replace("-","");


        verifyStoragePermissions(this);



    }



    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to
     * grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    //通过时
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    //拒绝是处理
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
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
        getMenuInflater().inflate(R.menu.menu_new, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_insert_image:
                callGallery();
                break;
            case R.id.action_new_save:
                saveNoteData(false);
                finish();
                break;
            case R.id.action_new_submit:
                saveNoteData(false);
                submitNoteData(false);
                deleteNoteData(false);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 保存数据,=0销毁当前界面，=1不销毁界面，为了防止在后台时保存笔记并销毁，应该只保存笔记
     */
    private void saveNoteData(boolean isBackground) {
//        String title = et_comment_new_title.getText().toString();
//        Log.i("","title---------------------------------------------------:"+title);
        String content = getEditData();

//        if (title.length() == 0 ){//如果标题为空，则截取内容为标题
//            if (content.length() > cutTitleLength){
//                title = content.substring(0,cutTitleLength);
//            } else if (content.length() > 0 && content.length() <= cutTitleLength){
//                title = content;
//            }
//        }
//        info.setTitle(title);
        info.setParentinfoid(parentInfoid);
        info.setParenttitle(parentTitle);
        info.setContent(content);
//        flag = 0;
//        if (flag == 0 ) {//新建笔记
//            if (title.length() == 0 && content.length() == 0) {
//                Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
//            } else {
                infoDao.insertInfo(info);
        //TODO
//                    //Log.i("", "noteId: "+noteId);
//                    //查询新建笔记id，防止重复插入
//                    note.setId((int) noteId);
//                    flag = 1;//插入以后只能是编辑
//                    if (!isBackground){
//                        Intent intent = new Intent();
//                        setResult(RESULT_OK, intent);
//                        finish();
//                    }
//            }
//        }else if (flag == 1) {//编辑笔记
//                if (!noteTitle.equals(myTitle) || !noteContent.equals(myContent)
//                        || !groupName.equals(myGroupName) || !noteTime.equals(myNoteTime)) {
//                    noteDao.updateNote(note);
//                }
//                    finish();if (!noteTitle.equals(myTitle) || !noteContent.equals(myContent)
//                        || !groupName.equals(myGroupName) || !noteTime.equals(myNoteTime)) {
//                    noteDao.updateNote(note);
//                }
//                    finish();
//        }
    }

    /**
     * 负责处理编辑数据提交等事宜，请自行实现
     */
    private String getEditData() {
        List<RichTextEditor.EditData> editList = et_comment_new_content.buildEditData();
        StringBuffer content = new StringBuffer();
        for (RichTextEditor.EditData itemData : editList) {
            if (itemData.inputStr != null) {
                content.append(itemData.inputStr);
                //Log.d("RichEditor", "commit inputStr=" + itemData.inputStr);
            } else if (itemData.imagePath != null) {
                content.append("<img src=\"").append(itemData.imagePath).append("\"/>");
                //Log.d("RichEditor", "commit imgePath=" + itemData.imagePath);
                //imageList.add(itemData.imagePath);
            }
        }
        return content.toString();
    }

    /**
     * 调用图库选择
     */
    private void callGallery(){
//        //调用系统图库
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");// 相片类型
//        startActivityForResult(intent, 1);

        //调用第三方图库选择
        PhotoPicker.builder()
                .setPhotoCount(5)//可选择图片数量
                .setShowCamera(true)//是否显示拍照按钮
                .setShowGif(true)//是否显示动态图
                .setPreviewEnabled(true)//是否可以预览
                .start(this, PhotoPicker.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                if (requestCode == 1){
                    //处理调用系统图库
                } else if (requestCode == PhotoPicker.REQUEST_CODE){
                    //异步方式插入图片
                    insertImagesSync(data);
                }
            }
        }
    }

    /**
     * 异步方式插入图片
     * @param data
     */
    private void insertImagesSync(final Intent data){
        insertDialog.show();

        subsInsert = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try{
                    et_comment_new_content.measure(0, 0);
                    int width = ScreenUtils.getScreenWidth(PublishcommentActivity.this);
                    int height = ScreenUtils.getScreenHeight(PublishcommentActivity.this);
                    ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                    //可以同时插入多张图片
                    for (String imagePath : photos) {
                        //Log.i("NewActivity", "###path=" + imagePath);
                        Bitmap bitmap = ImageUtils.getSmallBitmap(imagePath, width, height);//压缩图片
                        //bitmap = BitmapFactory.decodeFile(imagePath);
                        imagePath = SDCardUtil.saveToSdCard(bitmap);
                        //Log.i("NewActivity", "###imagePath="+imagePath);
                        subscriber.onNext(imagePath);
                    }
                    subscriber.onCompleted();
                }catch (Exception e){
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        })
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())//生产事件在io
                .observeOn(AndroidSchedulers.mainThread())//消费事件在UI线程
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        insertDialog.dismiss();
                        et_comment_new_content.addEditTextAtIndex(et_comment_new_content.getLastIndex(), " ");
                        showToast("图片插入成功");
                    }

                    @Override
                    public void onError(Throwable e) {
                        insertDialog.dismiss();
                        showToast("图片插入失败:"+e.getMessage());
                    }

                    @Override
                    public void onNext(String imagePath) {
                        et_comment_new_content.insertImage(imagePath, et_comment_new_content.getMeasuredWidth());
                    }
                });
    }


    //提交至服务器
    private void submitNoteData(boolean isBackground) {

        //图片上传
        postPicsToServer();





//        String title = et_comment_new_title.getText().toString();
//        Log.i("","title---------------------------------------------------:"+title);
        String content = getEditDataForSubmit();

        info.setInfoid(infoId);
//        info.setTitle(title);
        info.setContent(content);
        flag = 0;

        //-TODO
        //插入服务器数据库
        postDataToServer();




    }

    /**
     * 负责处理编辑数据提交等事宜，请自行实现
     */
    private String getEditDataForSubmit() {
        List<RichTextEditor.EditData> editList = et_comment_new_content.buildEditData();
        StringBuffer content = new StringBuffer();
        for (RichTextEditor.EditData itemData : editList) {
            if (itemData.inputStr != null) {
                content.append(itemData.inputStr);
                //Log.d("RichEditor", "commit inputStr=" + itemData.inputStr);
            } else if (itemData.imagePath != null) {
                //得到图片文件名
                String imageName = "";
                String[] imagePathSplit = itemData.imagePath.split("/");
                for(String imageNameTmp:imagePathSplit){
                    imageName = imageNameTmp;
                }
                content.append("<img src=\"").append("/").append(infoId).append("/").append(imageName).append("\"/>");
                //Log.d("RichEditor", "commit imgePath=" + itemData.imagePath);
                //imageList.add(itemData.imagePath);
            }
        }
        return content.toString();
    }



    private void postDataToServer() {
        if(isInfoAddOrComment){
            //add info
            mUrl = GlobalConstants.INFO_ADD_URL;
        }else{
            //comment
            mUrl = GlobalConstants.INFO_COMMENT_NEW_URL;
        }
        token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOi8vbG9jYWxob3N0L2RpcnR5Q2hpbmVzZS9wdWJsaWMvYXBpL2xvZ2luIiwiaWF0IjoxNTE0NjI0MDcxLCJleHAiOjE1MTQ2Mjc2NzEsIm5iZiI6MTUxNDYyNDA3MSwianRpIjoiMmdRQTdNOW4wTHhIalNvYiIsInN1YiI6ImEyYmRiMGEwZTdkNjExZTc4YjMxY2Y2NTU1Nzk5N2ZiIiwicHJ2IjoiODdlMGFmMWVmOWZkMTU4MTJmZGVjOTcxNTNhMTRlMGIwNDc1NDZhYSJ9.NzNRQ8LKihLT2XCG-hJYjhmBaijOXiAyZyR5cKpo9qQ";
//        RefreshTokenUtils refreshTokenUtils = new RefreshTokenUtils();
//        token = refreshTokenUtils.refreshToken(this);
        mUrl = mUrl + "?token=" + token;
        Gson gson=new Gson();
        jsonStringInfo = gson.toJson(info);
        Log.e("TAG1111----------------", jsonStringInfo);
        RequestParams params = new RequestParams(mUrl);
        params.setAsJsonContent(true);
        params.setBodyContent(jsonStringInfo);
        params.addQueryStringParameter("wd","xUtils");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                processData(result, false);
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


    /**
     * 负责处理编辑数据提交等事宜，请自行实现
     */
    private void postPicsToServer() {
        picUrl = GlobalConstants.PICS_UPLOAD_URL;
//        RefreshTokenUtils refreshTokenUtils = new RefreshTokenUtils();
//        token = refreshTokenUtils.refreshToken(this);
        token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOi8vbG9jYWxob3N0L2RpcnR5Q2hpbmVzZS9wdWJsaWMvYXBpL2xvZ2luIiwiaWF0IjoxNTE0NjI0MDcxLCJleHAiOjE1MTQ2Mjc2NzEsIm5iZiI6MTUxNDYyNDA3MSwianRpIjoiMmdRQTdNOW4wTHhIalNvYiIsInN1YiI6ImEyYmRiMGEwZTdkNjExZTc4YjMxY2Y2NTU1Nzk5N2ZiIiwicHJ2IjoiODdlMGFmMWVmOWZkMTU4MTJmZGVjOTcxNTNhMTRlMGIwNDc1NDZhYSJ9.NzNRQ8LKihLT2XCG-hJYjhmBaijOXiAyZyR5cKpo9qQ";
        picUrl = picUrl + "?token=" + token;
        RequestParams params = new RequestParams(picUrl);
        List<KeyValue> list = new ArrayList<KeyValue>();
        List<RichTextEditor.EditData> editList = et_comment_new_content.buildEditData();
        StringBuffer content = new StringBuffer();
        int keyValueInt = 0;
        for (RichTextEditor.EditData itemData : editList) {
            if (itemData.inputStr != null) {
//                content.append(itemData.inputStr);
                //Log.d("RichEditor", "commit inputStr=" + itemData.inputStr);
            } else if (itemData.imagePath != null) {
                //得到图片文件名
                String imageName = "";
                String[] imagePathSplit = itemData.imagePath.split("/");
                for(String imageNameTmp:imagePathSplit){
                    imageName = imageNameTmp;
                }
                File file = new File(itemData.imagePath);
                list.add(new KeyValue("file" + String.valueOf(keyValueInt),file));
                keyValueInt ++;
//                content.append("<img src=\"").append("/").append(infoId).append("/").append(imageName).append("\"/>");
                //Log.d("RichEditor", "commit imgePath=" + itemData.imagePath);
                //imageList.add(itemData.imagePath);
            }
        }
        list.add(new KeyValue("filenumber" , String.valueOf(keyValueInt)));
        list.add(new KeyValue("infoid" , infoId));
        MultipartBody body=new MultipartBody(list,"UTF-8");
        params.setRequestBody(body);
        params.setMultipart(true);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                processData(result, false);
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


    private void deleteNoteData(boolean isBackground) {

        infoDao.deleteInfo(info.getInfoid());


    }





    @Override
    protected void onStop() {
        super.onStop();
        //如果APP处于后台，或者手机锁屏，则启用密码锁
        if (CommonUtil.isAppOnBackground(getApplicationContext()) ||
                CommonUtil.isLockScreeen(getApplicationContext())){
            saveNoteData(true);//处于后台时保存数据
        }
    }

    /**
     * 退出处理
     */
    private void dealwithExit(){
//        String noteTitle = et_new_title.getText().toString();
//        String noteContent = getEditData();
//        String groupName = tv_new_group.getText().toString();
//        String noteTime = tv_new_time.getText().toString();
//        if (flag == 0) {//新建笔记
//            if (noteTitle.length() > 0 || noteContent.length() > 0) {
//                saveNoteData(false);
//            }
//        }else if (flag == 1) {//编辑笔记
//            if (!noteTitle.equals(myTitle) || !noteContent.equals(myContent)
//                    || !groupName.equals(myGroupName) || !noteTime.equals(myNoteTime)) {
//                saveNoteData(false);
//            }
//        }
        finish();
    }

    @Override
    public void onBackPressed() {
        dealwithExit();
    }

    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }




}
